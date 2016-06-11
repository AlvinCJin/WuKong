package com.alvin.wukong.utils


sealed abstract class Format(val name: String, val literal: String)

case object Avro extends Format("avro", "com.databricks.spark.avro")
case object Parquet extends Format("parquet", "parquet")

object Format {

  def apply(name: String): Format = {

    name match {
      case Avro.name => Avro
      case Parquet.name => Parquet
      case _ => throw new UnsupportedOperationException(s"$name is not a supported format")
    }
  }

}
