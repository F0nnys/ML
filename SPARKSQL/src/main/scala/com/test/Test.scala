package com.test

import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by hadoop on 18-1-28.
  */
object Test {

  def main(args: Array[String]): Unit = {
      val sparkConf = new SparkConf().setAppName("test").setMaster("local[2]")
      val sc = new SparkContext(sparkConf)
      val sqlContext = new HiveContext(sc)

      sqlContext.sql("select * from test").show()

    //sqlContext.sql("select count(*) from test.testtable ").show()
  }

}
