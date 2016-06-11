package com.alvin.wukong.loaders

import com.alvin.wukong.configs.IOConfig
import com.alvin.wukong.utils.Format
import org.apache.spark.sql.{DataFrame, SaveMode}
import com.typesafe.config.Config


class DataLoader {

  def load(df: DataFrame, ioConfig: IOConfig)(implicit userConfig: Config) = {

    val outputPath = ioConfig.outputPath

    val outputConfig = userConfig.getConfig("data_output")
    val table = outputConfig.getString("table_name")
    val format = outputConfig.getString("table_format")

    write(df, Format(format), outputPath+'/'+table)

  }


  def write(df: DataFrame, format: Format, path: String, options: Map[String, String] = Map()) = {

    df.write
      .format(format.literal)
      .options(options)
      .mode(SaveMode.Overwrite)
      .save(path)

  }

}
