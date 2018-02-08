package com.test.log



import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.hive.HiveContext

import scala.collection.mutable.ListBuffer

/**
  * Created by hadoop on 18-2-7.
  */
object TopNJob {
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf()
    sparkconf.setAppName("format")
    sparkconf.setMaster("local[2]")
    val sc = new SparkContext(sparkconf)
    val sqlcontext = new HiveContext(sc)
    sqlcontext.setConf("spark.sql.sources.partitionColumnTypeInference.enabled","false")
    val accessDF = sqlcontext.read.format("parquet").load("/home/clean")
    //accessDF.printSchema()
    //accessDF.show(false)
    val day = "20161110"
    StatDAO.deleteTables(day)
    videoAccessTopNStat(sqlcontext,accessDF,day)
    cityAccessTopNStat(sqlcontext,accessDF,day)
    videoTrafficTopNStat(sqlcontext,accessDF,day)
  }
  //traffic
  def videoTrafficTopNStat(spark: HiveContext, frame: DataFrame,day:String) = {
    import spark.implicits._
    val trafficAccessTopN = frame.filter($"day" === day && $"cmsType" === "video")
      .groupBy("day","cmsId").agg(sum("traffic").as("traffics"))
      .orderBy($"traffics".desc)
    val trafficAccess = trafficAccessTopN.select(
      trafficAccessTopN("day"),
      trafficAccessTopN("cmsId"),
      trafficAccessTopN("traffics")
    )
    try{
      trafficAccess.foreachPartition(partitionOfRecords => {
        val list = new ListBuffer[TrafficVideoAccessStat]
        partitionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val Traffics = info.getAs[Long]("traffics")
          list.append(TrafficVideoAccessStat(day,cmsId,Traffics))
        })
        StatDAO.insertTrafficAccessTopN(list)
      })
    }catch{
      case e:Exception => e.printStackTrace()
    }
  }

  //city
  def cityAccessTopNStat(spark: HiveContext, frame: DataFrame,day:String) = {
      import spark.implicits._
      val cityAccessTopN = frame.filter($"day" === day && $"cmsType" === "video").groupBy("day","city","cmsId").agg(count("cmsId").as("times"))
          .orderBy($"times".desc)
      val cityAccess = cityAccessTopN.select(
        cityAccessTopN("day"),
        cityAccessTopN("city"),
        cityAccessTopN("cmsId"),
        cityAccessTopN("times"),
        row_number().over(Window.partitionBy(cityAccessTopN("city")).orderBy($"times".desc))
          .as("times_rank")
      ).filter("times_rank <= 3")
      try{
        cityAccess.foreachPartition(partitionOfRecords => {
            val list = new ListBuffer[DayCityVideoAccessStat]
            partitionOfRecords.foreach(info => {
              val day = info.getAs[String]("day")
              val city = info.getAs[String]("city")
              val cmsId = info.getAs[Long]("cmsId")
              val times = info.getAs[Long]("times")
              val times_rank = info.getAs[Int]("times_rank")
              list.append(DayCityVideoAccessStat(day,city,cmsId,times,times_rank))
            })
            StatDAO.insertCityAccessTopN(list)
            })
          }catch{
           case e:Exception => e.printStackTrace()
          }
  }
  //all
  def videoAccessTopNStat(spark:SQLContext, frame: DataFrame,day:String) = {
    import spark.implicits._
    val videoAccess = frame.filter($"day" === day && $"cmsType" === "video").groupBy("day","cmsId").agg(count("cmsId").as("times"))
        .orderBy($"times".desc)
//    frame.registerTempTable("access_log")
//    val videoAccess = spark.sql("select day,cmsId,count(22) as times from access_log " +
//    "where day='20161110' and cmsType='video' " +
//    "group by day,cmsId order by times desc")
//    videoAccess.show(false)
    try{
      videoAccess.foreachPartition(partitionOfRecords => {
        val list = new ListBuffer[DayVideoAccessStat]
        partitionOfRecords.foreach(info => {
          val day = info.getAs[String]("day")
          val cmsId = info.getAs[Long]("cmsId")
          val times = info.getAs[Long]("times")
          list.append(DayVideoAccessStat(day,cmsId,times))
        })
        StatDAO.insertVideoAccessTopN(list)
      })
    }catch{
     case e:Exception => e.printStackTrace()
    }

  }
}
