package com.test.log

import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Row
/**
  * Created by hadoop on 18-2-7.
  */
object AccessConverUtil {
  val struct = StructType(
    Array(
      StructField("url",StringType),
      StructField("cmsType",StringType),
      StructField("cmsId",LongType),
      StructField("traffic",LongType),
      StructField("ip",StringType),
      StructField("city",StringType),
      StructField("time",StringType),
      StructField("day",StringType)
    )
  )
  def parseLog(log:String) = {
    try{
      val splits = log.split("\t")
      val url = splits(1)
      val traffic = splits(2).toLong
      val ip = splits(3)

      val domain = "http://www.imooc.com/"
      val cms = url.substring(url.indexOf(domain)+domain.length)
      val cmsTypeId = cms.split("/")
      var cmsType = ""
      var cmsId = 0l
      if(cmsTypeId.length > 1){
        cmsType = cmsTypeId(0)
        cmsId = cmsTypeId(1).toLong
      }
      val city = ipUtils.getCity(ip)
      val time = splits(0)
      val day = time.substring(0,10).replaceAll("-","")
      Row(url,cmsType,cmsId,traffic,ip,city,time,day)
    }catch {
      case e:Exception => Row("null","null",0l,0l,"null","null","null","null")
    }

  }
}
