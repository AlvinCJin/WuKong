package com.alvin.wukong.utils

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


object Utils {

  lazy val conf = new SparkConf(false)
    .setMaster("local[*]")
    .setAppName("Spark Tests")
    .set("spark.default.parallelism", "1")
    .set("spark.cores.max", "1")

  lazy val sc = SparkContext.getOrCreate(conf)
  lazy val sqlContext = new SQLContext(sc)

}
