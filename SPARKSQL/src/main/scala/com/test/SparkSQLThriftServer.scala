package com.test

import java.sql.DriverManager

/**
  * Created by hadoop on 18-2-5.
  */
object SparkSQLThriftServer {
  def main(args: Array[String]): Unit = {
    Class.forName("org.apache.hive.jdbc.HiveDriver")
    val conn = DriverManager.getConnection("jdbc:hive2://localhost:10000","hadoop","")
    val pstmt = conn.prepareStatement("select * from test")
    val rs = pstmt.executeQuery()
    while(rs.next()){
      println("name:  "+rs.getString("name")+"id:  "+rs.getInt("id"))
    }
    rs.close()
    pstmt.close()
    conn.close()
  }
}
