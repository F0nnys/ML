package com.test.log

import java.sql.{Connection, PreparedStatement}

import scala.collection.mutable.ListBuffer

/**
  * Created by hadoop on 18-2-7.
  */
object StatDAO {
  def insertVideoAccessTopN(list:ListBuffer[DayVideoAccessStat]):Unit = {
    var connection:Connection = null
    var pstmt:PreparedStatement = null
    try{
      connection = MysqlUtils.getConnection()
      connection.setAutoCommit(false)
      val sql = "insert into day_video_access_topn_stat(day,cms_id,times) values (?,?,?)"
      pstmt = connection.prepareStatement(sql)
      for(ele <-list){
        pstmt.setString(1,ele.day)
        pstmt.setLong(2,ele.cmsId)
        pstmt.setLong(3,ele.times)
        pstmt.addBatch()
      }
      pstmt.executeBatch()
      connection.commit()
    }catch{
      case e:Exception => e.printStackTrace()
    }finally {
      MysqlUtils.release(connection,pstmt)
    }
  }
  def insertCityAccessTopN(list:ListBuffer[DayCityVideoAccessStat]):Unit = {
    var connection:Connection = null
    var pstmt:PreparedStatement = null
    try{
      connection = MysqlUtils.getConnection()
      connection.setAutoCommit(false)
      val sql = "insert into day_video_city_access_topn_stat(day,city,cms_id,times,times_rank) values (?,?,?,?,?)"
      pstmt = connection.prepareStatement(sql)
      for(ele <-list){
        pstmt.setString(1,ele.day)
        pstmt.setString(2,ele.city)
        pstmt.setLong(3,ele.cmsId)
        pstmt.setLong(4,ele.times)
        pstmt.setInt(5,ele.timesrank)
        pstmt.addBatch()
      }
      pstmt.executeBatch()
      connection.commit()
    }catch{
      case e:Exception => e.printStackTrace()
    }finally {
      MysqlUtils.release(connection,pstmt)
    }
  }

  def insertTrafficAccessTopN(list:ListBuffer[TrafficVideoAccessStat]):Unit = {
    var connection:Connection = null
    var pstmt:PreparedStatement = null
    try{
      connection = MysqlUtils.getConnection()
      connection.setAutoCommit(false)
      val sql = "insert into day_video_traffics_access_topn_stat(day,cmsId,traffics) values (?,?,?)"
      pstmt = connection.prepareStatement(sql)
      for(ele <-list){
        pstmt.setString(1,ele.day)
        pstmt.setLong(2,ele.cmsId)
        pstmt.setLong(3,ele.Traffics)
        pstmt.addBatch()
      }
      pstmt.executeBatch()
      connection.commit()
    }catch{
      case e:Exception => e.printStackTrace()
    }finally {
      MysqlUtils.release(connection,pstmt)
    }
  }
  def deleteTables(day:String):Unit = {
    val tables = Array("day_video_access_topn_stat","day_video_city_access_topn_stat","day_video_traffics_access_topn_stat")
    var connection:Connection = null
    var pstmt:PreparedStatement = null
    try{
      connection = MysqlUtils.getConnection()
      for(table <- tables){
        val deletesql = s"delete from $table where day = ?"
        pstmt = connection.prepareStatement(deletesql)
        pstmt.setString(1,day)
        pstmt.executeUpdate()
      }
    }catch {
      case e:Exception => e.printStackTrace()
    }finally {
      MysqlUtils.release(connection,pstmt)
    }
  }
}
