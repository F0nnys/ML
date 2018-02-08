package com.test.log

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import org.apache.commons.lang3.time.FastDateFormat

/**
  * Created by hadoop on 18-2-6.
  */
object DataUtils {
  val YYYYMMDDHHMM_TIME_FORMAT = FastDateFormat.getInstance("dd/MMM/yyy:HH:mm:ss Z",Locale.ENGLISH)
  val TARGET_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  def parse(time:String) = {
    TARGET_FORMAT.format(new Date(getTime(time)))
  }
  def getTime(time:String) = {
    try {
      YYYYMMDDHHMM_TIME_FORMAT.parse(time.substring(time.indexOf("[") + 1, time.lastIndexOf("]"))).getTime
    }catch {
      case e:Exception => {
        0l
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(parse("[06/Dec/2014:07:29:30 +0800]"))
  }
}
