package com.test.log

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hadoop on 18-2-7.
  */
object SparkFormat {
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf()
    sparkconf.setAppName("format")
    sparkconf.setMaster("local[2]")
    val sc = new SparkContext(sparkconf)
    val access = sc.textFile("file:///home/data/access_10000.log")
    //access.take(10).foreach(println)
    access.map(line => {
      val splits = line.split(" ")
        val ip = splits(0)
        val time = splits(3) + " " + splits(4)
        val url = splits(11).replaceAll("\"","")
        val traffic = splits(9)
        DataUtils.parse(time)+"\t"+url+"\t"+traffic+"\t"+ip

    }).take(10).foreach(println)
  }
}
