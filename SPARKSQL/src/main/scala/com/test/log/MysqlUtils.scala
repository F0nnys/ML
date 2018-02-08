package com.test.log

import java.sql.{Connection, DriverManager, PreparedStatement}

/**
  * Created by hadoop on 18-2-7.
  */
object MysqlUtils {
  def getConnection() = {
    DriverManager.getConnection("jdbc:mysql://localhost:3306/weblog_project?user=root&password=123456")
  }
  def release(connection:Connection,psmtm:PreparedStatement):Unit ={
    try{
      if(psmtm !=null){
        psmtm.close()
      }
    }catch{
      case e:Exception => e.printStackTrace()
    }finally {
      if (connection != null){
        connection.close()
      }
    }
  }
  def main(args: Array[String]): Unit = {
    println(getConnection())
  }
}
