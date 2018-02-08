package com.test
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by hadoop on 18-2-5.
  */
object DataFrame {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
    sparkConf.setMaster("local[2]")
    sparkConf.setAppName("test")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new HiveContext(sc)
    import sqlContext.implicits._
    //val people = sc.textFile("file:///home/infos.txt")
    //val infoDF = people.map(_.split(",")).map(line => Info(line(0).toInt
    //,line(1),line(2).toInt)).toDF()
    //infoDF.show()
    //infoDF.filter("substr(name,0,1) = 'l'").show()
    //infoDF.sort("age").show()
    val jdbcDF = sqlContext.read.format("jdbc").option("url","jdbc:mysql://localhost:3306").option("dbtable","hive.TBLS").option(
      "user","root").option("password","123456").option("driver","com.mysql.jdbc.Driver").load()
    jdbcDF.show()
  }

  case class Info(id:Int,name:String,age:Int)
}
