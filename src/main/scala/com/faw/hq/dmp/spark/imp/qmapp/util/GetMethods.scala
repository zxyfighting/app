package com.faw.hq.dmp.spark.imp.qmapp.util

import java.util.{Calendar, Date}

import com.fasterxml.jackson.databind.ObjectMapper
import com.faw.hq.dmp.spark.imp.qmapp.bean.{AppInfo, OneIdUnity}

/**
  * author:张秀云
  * purpose：存放方法
  */
object GetMethods {

  /**
  * json解析方法
  *
  */
  def getApp(json:String):String={
    val str = json.replace("\\\"", "\"").replace("\"{","{").replace("}\"","}")
      val objectMapper = new ObjectMapper
      val appInfo = objectMapper.readValue(str, classOf[AppInfo])
      appInfo.jsonToLine()
  }


  def getOneIdUtil(deviceId:String,phoneNo:String): OneIdUnity ={
    val oneIdUnity = new OneIdUnity
    oneIdUnity.setOneId(null)
    oneIdUnity.setIdCard(null)
    oneIdUnity.setDriverNo(null);
    oneIdUnity.setaId(null);
    oneIdUnity.setMobile(phoneNo);
    oneIdUnity.setCookieId(null);
    oneIdUnity.setDeviceId(deviceId);
    oneIdUnity.setUnionId(null);
    oneIdUnity.setOpenId(null);
    oneIdUnity.setVin(null);
    oneIdUnity.setFaceId(null);
    oneIdUnity.setSource(null);
    oneIdUnity.setCreateTime(null);
    oneIdUnity.setUpdateTime(null)
    oneIdUnity
  }
  def  getSecondsNextEarlyMorning() ={
    var cal = Calendar.getInstance()
    cal.add(Calendar.DAY_OF_YEAR, 1)
    // 改成这样就好了
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)
    (cal.getTimeInMillis() - System.currentTimeMillis())
  }


  def main(args: Array[String]): Unit = {
    val json00="\"{\\\"data\\\":{\\\"visitData\\\":{\\\"pageLabel\\\":\\\"111\\\",\\\"pageParam\\\":\\\"S001\\\",\\\"pageType\\\":\\\"series\\\",\\\"pageUrl\\\":\\\"/view/001\\\",\\\"refData\\\":\\\"222\\\"}},\\\"dataType\\\":\\\"visit\\\",\\\"reportTime\\\":\\\"2019-11-25 13:12:47\\\"}\""

    val str1="\"{\\\"data\\\":{\\\"visitData\\\":{\\\"pageLabel\\\":\\\"111\\\",\\\"pageParam\\\":\\\"S001\\\",\\\"pageType\\\":\\\"series\\\",\\\"pageUrl\\\":\\\"/view/001\\\",\\\"refData\\\":\\\"222\\\"}},\\\"dataType\\\":\\\"visit\\\",\\\"reportTime\\\":\\\"2019-11-25 13:12:47\\\"}\""
    val str2="{\"reportTime\":\"2019-12-24 10:59:33\",\"data\":{\"userInfo\":{\"phoneNo\":\"18602399446\"},\"deviceInfo\":{\"deviceId\":\"21F941C7-B750-4EAB-8461-AC114581A50D\"},\"visitData\":{\"pageUrl\":\"https:\\/\\/hqapp.hqxs.faw.cn\\/api\\/front\\/zx\\/118061.jhtml\",\"pageLabel\":\"\",\"pageParam\":\"\",\"refData\":\" \",\"pageType\":\"news\"}},\"dataType\":\"visit\"}"
    val str3="{\"reportTime\":\"2019-12-24 10:59:33\",\"data\":{\"userInfo\":{\"phoneNo\":\"18602399446\"},\"deviceInfo\":{\"deviceId\":\"21F941C7-B750-4EAB-8461-AC114581A50D\"},\"visitData\":{\"pageUrl\":\"https:\\/\\/hqapp.hqxs.faw.cn\\/api\\/front\\/zx\\/118061.jhtml\",\"pageLabel\":\"\",\"pageParam\":\"\",\"refData\":\" \",\"pageType\":\"news\"}},\"dataType\":\"visit\"}"
    val str4="\"{\\\"data\\\":{\\\"deviceInfo\\\":{\\\"deviceId\\\":\\\"866936036143137\\\"},\\\"visitData\\\":{\\\"pageLabel\\\":\\\"\\\",\\\"pageParam\\\":\\\"\\\",\\\"pageType\\\":\\\"news\\\",\\\"pageUrl\\\":\\\"https://hqapp.hqxs.faw.cn/api/front/zx/143708.jhtml\\\",\\\"refData\\\":\\\"\\\"}},\\\"dataType\\\":\\\"visit\\\",\\\"reportTime\\\":\\\"2019-12-24 10:57:54\\\"}\""
    val str5="\"{\\\"data\\\":{\\\"deviceInfo\\\":{\\\"deviceId\\\":\\\"867309040398908\\\"},\\\"visitData\\\":{\\\"pageLabel\\\":\\\"\\\",\\\"pageParam\\\":\\\"\\\",\\\"pageType\\\":\\\"news\\\",\\\"pageUrl\\\":\\\"https://hqapp.hqxs.faw.cn/api/front/zx/146557.jhtml\\\",\\\"refData\\\":\\\"\\\"}},\\\"dataType\\\":\\\"visit\\\",\\\"reportTime\\\":\\\"2019-12-26 15:14:34\\\"}\""
    val str = str5.replace("\\\"", "\"").replace("\"{","{").replace("}\"","}")
    println(str)
    val objectMapper = new ObjectMapper
    val appInfo = objectMapper.readValue(str, classOf[AppInfo])
    System.out.println(appInfo.jsonToLine())
  }


}

