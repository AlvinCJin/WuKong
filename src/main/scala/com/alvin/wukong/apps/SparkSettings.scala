package com.alvin.wukong.apps


import org.apache.hadoop.fs.FileSystem
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}


class SparkSettings {

  lazy val sparkConf = new SparkConf()

  lazy val sc = SparkContext.getOrCreate(sparkConf)

  lazy val sqlContext: SQLContext = new SQLContext(sc)

  lazy val fileSystem = FileSystem.get(sc.hadoopConfiguration)

}
