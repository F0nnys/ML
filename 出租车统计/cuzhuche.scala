import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.clustering.KMeans
object cuzhuche {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[2]")
      .appName("cuzhuche").getOrCreate()
    val fieldSchema = StructType(Array(
      StructField("TID", StringType, true),
      StructField("Lat", DoubleType, true),
      StructField("Lon", DoubleType, true),
      StructField("Time", StringType, true)
    ))
    val taxiDF=spark.read.format("csv").option("header","false")
      .schema(fieldSchema).load("/sparkml/cuzhuche/taxi.csv")

    val columns = Array("Lat","Lon")
    val va = new VectorAssembler().setInputCols(columns)
      .setOutputCol("features")
    val taxiDF2 = va.transform(taxiDF)
    taxiDF2.cache()
    val traintestratio = Array(0.8,0.2)
    val Array(traindata,testdata) = taxiDF2.randomSplit(traintestratio,4484)
    val km = new KMeans().setK(10).setFeaturesCol("features").setPredictionCol("prediction")
        .fit(taxiDF2)
    //val kmresult = km.clusterCenters
    //val kmRDD1 = spark.sparkContext.parallelize(kmresult)
    //val kmRDD2 = kmRDD1.map(k=>(k(1),k(0)))
    //kmRDD2.saveAsTextFile("/sparkml/cuzhuche/kmResult")
    val prediction = km.transform(testdata)
    prediction.createTempView("haha")
    val tmpQuery = spark.sql("select features,prediction from haha")
    tmpQuery.show()
    //val tmpQuery = spark.sql("select substring(Time,0,2) as hour,prediction from haha")
    //val predictCount = tmpQuery.groupBy("hour","prediction").agg(count("prediction").alias("count")).orderBy(desc("count"))
    //predictCount.show()
    //predictCount.write.csv("/sparkml/cuzhuche/predictCount.csv")
    //val busyZones = prediction.groupBy("prediction").count()
    //busyZones.show()
    //busyZones.write.csv("/sparkml/cuzhuche/busyZones.csv")

  }
}
