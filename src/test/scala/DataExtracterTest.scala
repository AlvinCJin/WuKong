import com.alvin.wukong.configs.IOConfig
import com.alvin.wukong.extractors.DataExtracter
import com.alvin.wukong.utils.{Parquet, UnitSpec}
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.mockito.Mockito._
import com.typesafe.config._


class DataExtracterTest extends UnitSpec{

  val sqlContext = mock[SQLContext](RETURNS_DEEP_STUBS)
  val path = "input/table"
  val format = Parquet

  val userCF = ConfigFactory.parseString(
    """
      |data_input: {
      |  table_format: "parquet"
      |  table_name: "table"
      |}
    """.stripMargin
  )

  val fakeDF = mock[DataFrame]

  val dataExtracter = new DataExtracter(sqlContext)

  "A data extracter" should "call read()" in {

    val ioCF = IOConfig("input","output")

    when(dataExtracter.read(path, Parquet)).thenReturn(fakeDF)

    dataExtracter.extract(ioCF)(userCF) should be theSameInstanceAs(fakeDF)

  }


}
