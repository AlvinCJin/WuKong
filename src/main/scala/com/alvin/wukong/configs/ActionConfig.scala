package com.alvin.wukong.configs


import com.typesafe.config.Config

case class ActionConfig(keyCol: String, evalCol: String, evalMethod: String)

object ActionConfig {

  def apply(actionConfig: Config): ActionConfig ={

    val keyCol = actionConfig.getString("key_column")
    val evalCol = actionConfig.getString("eval_column")
    val evalMethod = actionConfig.getString("eval_method")

    ActionConfig(keyCol, evalCol, evalMethod)
  }

}