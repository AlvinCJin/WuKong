import scala.util.Properties

object Versions {

  val crossScala = Seq("2.11.6", "2.10.4")

  /* Leverages optional Spark 'scala-2.11' profile optionally set by the user via -Dscala-2.11=true if enabled */
  lazy val scalaVersion = sys.props.get("scala-2.11") match {
    case Some(is) if is.nonEmpty && is.toBoolean => crossScala.head
    case crossBuildFor                           => crossScala.last
  }

  /* For `scalaBinaryVersion.value outside an sbt task. */
  lazy val scalaBinary = scalaVersion.dropRight(2)

  val SparkVersion              = "1.5.1"
  val NScalaVersion             = "2.2.0"
  val CassandraVersion          = "2.1.5"
  val CassandraDriverVersion    = "2.1.5"
  val CassandraConnectorVersion = "1.4.0"
  val Guava           = "14.0.1"
  val JDK             = "1.7"
  val KafkaVersion    = "0.8.2.0"
  val ScalaMock       = "3.2"
  val ScalaTest       = "2.2.4"
  val Json4sVersion   = "3.2.10"
  val Slf4j           = "1.6.1"
  val HadoopVersion    = "2.6.0"
  val ScoptVersion     = "3.3.0"
  val SparkAvro        = "2.0.1"
  val AvroMapRed       = "1.7.7"
  val AvroVersion      = "1.7.7"


  val hint = (binary: String) => if (binary == "2.10") "[To build against Scala 2.11 use '-Dscala-2.11=true']" else ""

  val status = (versionInReapply: String, binaryInReapply: String) =>
    println(s"""
               |  Scala: $versionInReapply ${hint(binaryInReapply)}
        |  Scala Binary: $binaryInReapply
        |  Java: target=$JDK user=${Properties.javaVersion}
        """.stripMargin)
}
