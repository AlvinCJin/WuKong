package com.alvin.wukong.configs


import com.typesafe.config.Config

case class SysConfig(ioConfig: IOConfig)

object SysConfig {

  def apply(config: Config):SysConfig = {

    val ioConfig = IOConfig(config)

    SysConfig(ioConfig)

  }

}
