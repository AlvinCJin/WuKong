package com.alvin.wukong.apps

import com.alvin.wukong.cli.CliArgParser
import com.typesafe.config.Config
import org.apache.spark.sql.DataFrame

trait WukongApp extends Serializable with CliArgParser{
  def chainTransformers(dataset: DataFrame)(implicit config: Config): DataFrame
}
