package com.alvin.wukong.transformers

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, ArrayType, LongType, StructType}

import scala.collection.mutable


class CollectFunc[T](colType: DataType) extends UserDefinedAggregateFunction {

  // an aggregation function can take multiple arguments in general. but
  // this one just takes one
  def inputSchema: StructType =
    new StructType().add("inputCol", colType)
  // the aggregation buffer can also have multiple values in general but
  // this one just has one
  def bufferSchema: StructType =
    new StructType().add("outputCol", ArrayType(colType))
  // returns just an array
  def dataType: DataType = ArrayType(colType)
  // always gets the same result
  def deterministic: Boolean = true

  // each partial array is initialized to empty
  def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer.update(0, new mutable.ArrayBuffer[T])
  }

  // add an individual item to each buffer
  def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    val list = buffer.getSeq[T](0)
    if (!input.isNullAt(0)) {
      val sales = input.getAs[T](0)
      buffer.update(0, list:+sales)

    }
  }

  // buffers are merged by merging the single arrays
  def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {

    buffer1.update(0, buffer1.getSeq[T](0) ++ buffer2.getSeq[T](0))
  }

  // the aggregation buffer just has one array: so return it
  def evaluate(buffer: Row): Any = {
    buffer.getSeq[T](0)
  }
}