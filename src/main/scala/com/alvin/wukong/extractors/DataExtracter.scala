package com.alvin.wukong.extractors

import com.alvin.wukong.configs.IOConfig
import com.alvin.wukong.utils.Format
import org.apache.spark.sql.{DataFrame, SQLContext}
import com.typesafe.config.Config

class DataExtracter(val sqlContext: SQLContext) {

  def extract(ioConfig: IOConfig)(implicit userConfig: Config): DataFrame = {

    val inputPath = ioConfig.inputPath
    val inputConfig = userConfig.getConfig("data_input")
    val table = inputConfig.getString("table_name")
    val format = inputConfig.getString("table_format")

    read(inputPath+'/'+table, Format(format))
  }

  def read(path: String, format: Format): DataFrame = {
    sqlContext
      .read
      .format(format.literal)
      .load(path)
  }
}
