package com.test.log
import com.ggstar.util.ip.IpHelper
/**
  * Created by hadoop on 18-2-7.
  */
object ipUtils {
  def getCity(ip:String) = {
    IpHelper.findRegionByIp(ip)
  }

  def main(args: Array[String]): Unit = {
    println(getCity("58.30.15.255"))
  }
}
