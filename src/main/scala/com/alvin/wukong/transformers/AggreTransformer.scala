package com.alvin.wukong.transformers

import com.alvin.wukong.utils.UnsupportedOpsException
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.{ParamMap, Param}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._


class AggreTransformer (override val uid: String) extends Transformer{

  def this() = this(Identifiable.randomUID("collectListTF"))

  val evaCol = new Param[String](this, "evaColumn", "The selected column.")
  val keyCol = new Param[String](this, "keyColumn", "The groupBy column")
  val opName = new Param[String](this, "aggregationMethod", "The aggregation function")

  def setEvaColumn(value: String): this.type = set(evaCol, value)

  def setKeyColumn(value: String): this.type = set(keyCol, value)

  def setEvaMethod(value: String): this.type = set(opName, value)

  override def transform(dataset: DataFrame): DataFrame = {

    val opCol = $(opName).toLowerCase
      match {
        case "max" => max($(evaCol))
        case "list" => {
            val colType = dataset.schema($(evaCol)).dataType
            val collectlist = new CollectFunc(colType)
            collectlist(dataset($(evaCol)))
        }
        case _ => throw UnsupportedOpsException("Unsupported Ops.")
      }

    dataset
      .groupBy($(keyCol))
      .agg(opCol)
      .withColumnRenamed(opCol.toString, $(opName)+'_'+$(evaCol))

  }

  override def transformSchema(schema: StructType): StructType = {


    StructType(Seq(
      StructField($(keyCol), schema($(keyCol)).dataType, true),
      StructField($(opName)+'_'+$(evaCol), ArrayType(schema($(evaCol)).dataType), true)
    ))
  }

  override def copy(extra: ParamMap): AggreTransformer = defaultCopy(extra)
}