package com.alvin.wukong.configs


import com.typesafe.config.Config

case class IOConfig(inputPath: String, outputPath: String)

object IOConfig {

  def apply(config: Config): IOConfig ={

    val ioConfig = config.getConfig("hdfs")
    val inputPath = ioConfig.getString("input_path")
    val outputPath = ioConfig.getString("output_path")

    IOConfig(inputPath, outputPath)
  }

}
