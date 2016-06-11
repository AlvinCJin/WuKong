package com.alvin.wukong

package object utils {

  case class InvalidConfigurationException(val msg: String) extends RuntimeException(msg)
  case class UnsupportedFormatException(val msg: String) extends RuntimeException(msg)
  case class UnsupportedOpsException(val msg: String) extends RuntimeException(msg)
}
