package com.test.log

import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hadoop on 18-2-7.
  */
object SparkcleanYARN {

  def filterLog(line :String) = {
    val splits = line.split("\t")
    if (splits(1).contains("code") || splits(1).contains("article") || splits(1).contains("video")) {
      if (splits.length == 4) {
        true
      } else {
        false
      }
    } else {
      false
    }
  }
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf()
    val sc = new SparkContext(sparkconf)
    val Array(input,output) = args
    val accessRDD = sc.textFile(input).filter(line => this.filterLog(line))
    val sqlcontext = new SQLContext(sc)
    val accessDF = sqlcontext.createDataFrame(accessRDD.map(x => AccessConverUtil.parseLog(x)),AccessConverUtil.struct)
    //accessDF.printSchema()
    //accessDF.show(false)
    val filterDF = accessDF.filter(!accessDF.col("url").contains("null"))
    filterDF.coalesce(1).write.partitionBy("day").format("parquet").mode(SaveMode.Overwrite).save(output)
  }
}
