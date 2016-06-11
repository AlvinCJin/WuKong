package com.alvin.wukong.cli

import com.alvin.wukong.utils.InvalidConfigurationException


case class CliArgs( configFile: Option[String] = None,
                    env: String = null,
                    jobName: String = null
                    )

trait CliArgParser{

  import scopt.OptionParser

  def parseCli(
                appClassPath: String,
                appHeader: String
                ) = new OptionParser[CliArgs](appClassPath) {
    head(appHeader)
    opt[String]('c', "configFile")
      .action { (x, c) => c.copy(configFile = Some(x)) }
      .text("user specified configuration file")
    opt[String]('e', "env").required()
      .action { (x, c) => c.copy(env = x) }
      .text("choose deploy environment")
    opt[String]('n', "jobName")
      .action { (x, c) => c.copy(jobName = x) }
      .text("name of the job")
    checkConfig { c => c match {
      case c if c.jobName != null || c.configFile != None => success
      case _ => failure("Either 'jobName' or 'configFile' have to be defined.")
    }

    }
    note(
      """
        |spark-submit --class com.alvin.wukong.apps.ETLApp
        |--master spark://ip:7077
        |--total-executor-cores 12 --executor-cores 2 --executor-memory 3g --driver-memory 4g
        |--driver-java-options "-XX:MaxPermSize=2048m
        |-Dconfig.resource=/dev.conf "
        |/path/wukong-assembly-0.1.jar
        |--configFile /path/job.conf
        |--env dev
      """.stripMargin
    )
  }

  def getCliArgs(args: Array[String], cliDefaultParams: CliArgs = CliArgs()) =
    parseCli("com.alvin.wukong.apps.<Class>", "apps").parse(args, cliDefaultParams) match {
      case Some(c) => c
      case None => throw InvalidConfigurationException("Invalid CLI args.")
    }
}
