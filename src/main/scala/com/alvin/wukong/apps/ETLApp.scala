package com.alvin.wukong.apps

import com.alvin.wukong.cli.CliArgs
import com.alvin.wukong.configs.{ActionConfig, SysConfig}
import com.alvin.wukong.extractors.DataExtracter
import com.alvin.wukong.loaders.DataLoader
import com.alvin.wukong.transformers.AggreTransformer
import com.typesafe.config._
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.{DataFrame, SQLContext}
import scala.collection.JavaConversions._
import com.softwaremill.macwire._



trait JobModule {

  val sysCF: SysConfig
  val sqlContext: SQLContext
  lazy val dataExtractor = wire[DataExtracter]
  lazy val dataLoader = wire[DataLoader]

}

object ETLApp extends WukongApp {

  def main(args: Array[String]): Unit = {

    val cliArgs: CliArgs = getCliArgs(args)

    val jobName    = cliArgs.jobName
    val envConfig = ConfigFactory.load(cliArgs.env)
    val systemCF = SysConfig(envConfig)

    var appName = getClass.getSimpleName

    implicit val userCF = cliArgs.configFile match {
      case Some(configFile) => {
        appName += "-" + configFile
        ConfigFactory.load(ConfigFactory.parseFile(new java.io.File(configFile)))
      }
      case _ => {
        appName += "-" + jobName
        ConfigFactory.load(jobName)
      }
    }

    val spark = new SparkSettings()
    spark.sparkConf.setAppName(appName)

    val module = new JobModule {
      override lazy val sysCF = systemCF
      override lazy val sqlContext = spark.sqlContext
    }


    val inputDF = module.dataExtractor.extract(systemCF.ioConfig)

    val outputDF = chainTransformers(inputDF)

    module.dataLoader.load(outputDF, systemCF.ioConfig)


  }


  def chainTransformers(dataset: DataFrame)(implicit config: Config): DataFrame = {


    val actionCFs = config
      .getConfigList("groupby_actions")
      .map(ActionConfig(_))
      .toList

    val collectCF = actionCFs(0)

    val collectListTF = new AggreTransformer()
      .setEvaColumn(collectCF.evalCol)
      .setKeyColumn(collectCF.keyCol)
      .setEvaMethod(collectCF.evalMethod)

    val pipeline = new Pipeline()
      .setStages(Array(collectListTF))

    val newDataset = pipeline
      .fit(dataset)
      .transform(dataset)

    newDataset
  }

}
