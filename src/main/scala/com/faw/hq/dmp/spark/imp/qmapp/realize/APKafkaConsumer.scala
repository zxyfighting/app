package com.faw.hq.dmp.spark.imp.qmapp.realize

import com.faw.hq.dmp.spark.imp.qmapp.contanst.KafkaParams
import com.faw.hq.dmp.spark.imp.qmapp.inter.{ImplJDBC, ImplPUV, Implkafka}
import com.faw.hq.dmp.spark.imp.qmapp.util._
import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

/**
  * auth:张秀云
  * purpose:实现手机app数据采集(新意，启明)
  */
object APKafkaConsumer {
  private val logger: Logger = Logger.getLogger(this.getClass)
  def main(args: Array[String]): Unit = {
    while (true) {
      //配置spark上下文环境对象
      val processInterval = 59
      val conf = new SparkConf().setAppName("app").setMaster("local[*]")
      val sc = new SparkContext(conf)
      //设置采集器每十秒批次拉取数据
      val sparkstreaming = new StreamingContext(sc, Seconds(processInterval))

      //利用checkpoint作为容错处理
      sparkstreaming.sparkContext.setCheckpointDir(KafkaParams.chkDir)

      // 初始化 Redis 连接池
      JedisPoolUtils.makePool(RedisConfig("prod.dbaas.private", 16359, 30000, "realtime123", 1000, 100, 50))
      val kafkaStream = KafkaRedisUtils.createDirectStream(sparkstreaming, KafkaParams.kafkaParams1, KafkaParams.module, KafkaParams.groupid, KafkaParams.topics)

      //将offset交给redis维护
        Implkafka.getOffset(KafkaParams.module,KafkaParams.groupid,kafkaStream)

      //将一条条json数据解析
      val resultDestream: DStream[String] = kafkaStream.map(_.value()).map(rdd => {
        GetMethods.getApp(rdd)
      })

      /**
        * OneId处理
        *
        * */
      //若dataType为"visit"(新意),则调用oneid接口，将相应的数据送oneid平台
      val onidDestream: DStream[String] = resultDestream.filter(rdd => {
        val strings = rdd.split(";")
        strings(0).equals("visit")
      })
      //判断条件后，将数据发送到OneId平台
       Implkafka.oneIdTo(onidDestream)

      //将数据追加到hdfs上封装
      resultDestream.foreachRDD(rdd => {
        Implkafka.toHDFS(rdd)
      })

      /**
        * 计算实时更新的pv
        *
        * */
     // 1.过滤出pageUrl为空的字段
      val pvResult: DStream[(String, Long)] = resultDestream.map(rdd => {
        val strings = rdd.split(";")
        (strings(14), 1L)
      }).filter(rdd => {
        !rdd._1.equals("\\N")
      })

      // 2.将处理完的数据累加即可---有状态算子：注意：由于存到hdfs，为了清除数据，次日重新统计，要在次日凌晨重启
      // 2.1 将数据转换为（date，1L）
      val value1: DStream[(String, Long)] = pvResult.map(rdd => (KafkaParams.dateString, rdd._2))
      // 2.2 将数据累加
      val pvResultCounts: DStream[(String, Long)] = ImplPUV.pvUpdate(value1)

      //将数据导入到mysql中，通过分区建立一次连接对象
      val sql = "replace into dmp_behavior_app_pv (app_pv_dt,app_pv_amounts,create_time,update_time)  values(?,?,?,now())"
      //封装sql，只需改变sql语句即可
      ImplJDBC.puvCounts(sql,pvResultCounts)

      //uv是要去重
      //获取过期时间为一天
      val timeout = KafkaParams.config.getString("redis.expireTime")
      //去重--根据今天访问过的用户清单进行过滤
      // 1.改变数据类型
      val mapRDD: DStream[UserVisit] = resultDestream.
        filter(rdd=>{
          val strings = rdd.split(";")
          !strings(2).equals("\\N")
        }).
        map(rdd => {
        val strings = rdd.split(";")
        UserVisit(strings(2), KafkaParams.dateString)
      })
      mapRDD.cache()
      // 2.进行批次间去重---利用redis
      val filterRDD: DStream[UserVisit] = ImplPUV.filterRedis(mapRDD,sparkstreaming)

      // 3.本批次去重---groupBykey
      val distinctDstream: DStream[UserVisit] = ImplPUV.groupByUnion(filterRDD)

      // 4.把所有今天访问过的用户保存起来
      ImplPUV.distinctRedis(distinctDstream)

      // 5.将计算结果累加
      val updaDestream: DStream[(String, Long)] = distinctDstream.map(rdd => (rdd.dateToday, 1L))
      val uvDstream: DStream[(String, Long)] = ImplPUV.getUsers(updaDestream)

      // 将数据保存在mysql数据库
      val sql1 = "replace into  dmp_behavior_app_uv (app_uv_dt,app_uv_amounts,create_time,update_time) values(?,?,?,now())"
      ImplJDBC.puvCounts(sql1,uvDstream)

      //开启采集器
      sparkstreaming.start()
      sparkstreaming.awaitTerminationOrTimeout(GetMethods.getSecondsNextEarlyMorning())
      sparkstreaming.stop(false, true)

    }
  }
  case class UserVisit(uninid: String, dateToday: String)

}