#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/1/19 13:36
# @Author  : Aries
# @Site    : 
# @File    : visualize.py
# @Software: PyCharm
from pandas_highcharts.display import display_charts
from pandas_highcharts.core import serialize
import pandas as pd
import urllib.request
import json
data = pd.read_csv("G://gongxiang//busyZones.csv",header=None,names=("area","counts"))
pre = pd.read_csv("G://gongxiang//kmresult2.csv",header=None,names=("lon","lat"))
pre["area"] = pre.index
df = pd.merge(data,pre,on="area",how="left")
def foundloc(lon,lat):
    url="http://api.map.baidu.com/geocoder/v2/?location=%f,%f&output=json&ak=Yrf32LVk9DEHOYchZAQlG9nFICRTst5V"%(lat,lon)
    req = urllib.request.urlopen(url)
    res = req.read().decode("utf-8")
    temp = json.loads(res)
    return temp["result"]["formatted_address"]
lon = list(df["lon"])
lat = list(df["lat"])
situation = zip(lon,lat)
situa = []
for i in situation:
    situa.append(foundloc(i[0],i[1])[6:-1])
df["name"] = situa
df1 = df[["counts","name"]]
df1 = df1.set_index("name")
display_charts(df1,title="区域打车次数")