package com.faw.hq.dmp.spark.imp.qmapp.inter

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.faw.hq.dmp.spark.imp.qmapp.bean.Result
import com.faw.hq.dmp.spark.imp.qmapp.contanst.KafkaParams
import com.faw.hq.dmp.spark.imp.qmapp.realize.APKafkaConsumer.logger
import com.faw.hq.dmp.spark.imp.qmapp.util._
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import redis.clients.jedis.Pipeline

/**
  * @ program: app
  * @ description :将kafka连接，kafka offset维护，写到hdfs，以及OneId的封装
  * @ author: ZhangXiuYun
  * @ create: 2020-01-13 17:25
  **/
object Implkafka  {

  //实现连接kafka方法
  def getKafkaStream(topic: String, ssc: StreamingContext) = {
    val kafkaStream = KafkaRedisUtils.createDirectStream(ssc,KafkaParams.kafkaParams1,KafkaParams.module, KafkaParams.groupid, KafkaParams.topics)
    kafkaStream
  }
  //实现redis维护offset方法
  def getOffset(module: String, groupid: String,rdd:InputDStream[ConsumerRecord[String, String]]
               ): Unit = {
    //开始处理批次消息---将offset交予redis维护
    rdd.foreachRDD(rdd => {
      //获取当前批次的RDD的偏移量
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      // 处理从获取 kafka 中的数据
      if (!rdd.isEmpty()) {
        // 获取 redis 连接
        val jedisClient = JedisPoolUtils.getPool.getResource
        //开启事务
        val pipeline: Pipeline = jedisClient.pipelined()
        pipeline.multi()
        try {
          //更新offset到Redis中
          offsetRanges.foreach({ offsetRange =>
            /*  logger.info("==========> partition : " + offsetRange.partition + " fromOffset:  " + offsetRange.fromOffset
              + " untilOffset: " + offsetRange.untilOffset)*/

            // Kafka 的 Offsets 以 module:groupId:topic 为 key 的 hash 结构存入 Redis 中
            val key = s"${KafkaParams.module}:${KafkaParams.groupid}:${offsetRange.topic}"
            pipeline.hset(key, offsetRange.partition.toString, offsetRange.untilOffset.toString)
          })
          //提交事务
          pipeline.exec()
          //关闭pipeline
          pipeline.sync()
        } catch {
          case e: Exception => {
            /*logger.error("数据处理异常", e)*/
            pipeline.discard()
          }
        } finally {
          //关闭连接
          pipeline.close()
          jedisClient.close()
        }
      }
    })
  }
  //将数据写到hdfs
  def toHDFS(line: RDD[String]): Unit = {
    val rdd1 = line.map((_, " "))
    RDD.rddToPairRDDFunctions(rdd1).partitionBy(new HashPartitioner(1))
      .saveAsHadoopFile(KafkaParams.hdfs_url + "dt=" + KafkaParams.dateString, classOf[String], classOf[String], classOf[AppendTextOutputFormat])
  }

  //封装OneId处理-----根据需求，改变strings(2), strings(11)，即可
  def oneIdTo(onidDestream: DStream[String]): Unit ={
    onidDestream.foreachRDD(rdd => {
      rdd.foreach(rdd => {
        val strings = rdd.split(";")
        try{
          //strings(2), strings(11)，根据OneId规则需求，要的字段
          val oneXyIdUnity = GetMethods.getOneIdUtil(strings(2), strings(11))
          val onQmeIdUnity = GetMethods.getOneIdUtil(strings(2), strings(13))
          val url = KafkaParams.oneId
          val str: String = JSON.toJSONString(oneXyIdUnity, SerializerFeature.PrettyFormat)
          val str1: String = JSON.toJSONString(onQmeIdUnity, SerializerFeature.PrettyFormat)
          val result = HttpClientExtendUtil.doPostRequestBody(url, str)
          val result1 = HttpClientExtendUtil.doPostRequestBody(url, str1)
          val json: Result = JSON.parseObject(result, classOf[Result])
          println(json.getCode)
        }
        catch{
          case ex:Exception =>/* logger.error("捕获异常")*/ println("捕获异常")
        }
      })
    })
  }
}

