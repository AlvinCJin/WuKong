import sbt._
import sbt.Keys._

object Build extends sbt.Build {

  lazy val wukong = Project("WuKong", file("."))
    .settings(basicSettings: _*)
    .settings(libraryDependencies ++= Dependencies.basic)

  lazy val basicSettings = Seq(
    organization := "com.alvin.wukong",
    version := "0.1",
    scalaVersion := "2.10.4",
    scalacOptions ++= Seq("-unchecked", "-deprecation"),
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"))

}

object Dependencies {
  import Versions._

  implicit class Exclude(module: ModuleID) {
    def guavaExclude: ModuleID =
      module exclude("com.google.guava", "guava")

    def logbackExclude: ModuleID = module
      .exclude("ch.qos.logback", "logback-classic")
      .exclude("ch.qos.logback", "logback-core")

    def commonsLoggingExclude: ModuleID =
      module.exclude("commons-logging", "commons-logging")

    def kafkaExclusions: ModuleID = module
      .exclude("org.slf4j", "slf4j-simple")
      .exclude("com.sun.jmx", "jmxri")
      .exclude("com.sun.jdmk", "jmxtools")
      .exclude("net.sf.jopt-simple", "jopt-simple")
  }

  object Compile {

    val guava               = "com.google.guava"        % "guava"                  % Guava

    object Time {
      val nscala               = "com.github.nscala-time"  %% "nscala-time"           % NScalaVersion
    }

    object Scopt {
      val scopt               = "com.github.scopt"  %% "scopt"           % ScoptVersion
    }

    object Spark {
      val sparkCore            = "org.apache.spark" %% "spark-core"            % SparkVersion %  "provided"
      val sparkML             = "org.apache.spark" %% "spark-mllib"            % SparkVersion commonsLoggingExclude
      val sparkSql           = List("org.apache.spark" %% "spark-sql"       % SparkVersion exclude("com.twitter", "parquet-hadoop-bundle") commonsLoggingExclude,
        // Bump up dependency versions to avoid conflicts
        "com.esotericsoftware.kryo" % "kryo" % "2.24.0",
        "commons-configuration" % "commons-configuration" % "1.10" commonsLoggingExclude)
    }

    object Json {
      val json4sNative         = "org.json4s"       %% "json4s-native"                  % Json4sVersion
      val json4sJackson        = "org.json4s"       %% "json4s-jackson"                 % Json4sVersion
      val json4sExt            = "org.json4s"       %% "json4s-ext"                     % Json4sVersion
    }

    object Test {
      val scalaMock         = "org.scalamock"           %% "scalamock-scalatest-support"  % ScalaMock % "test"
      val scalaTest         = "org.scalatest"           %% "scalatest"                    % ScalaTest % "test"
      val mockito           = "org.mockito"             % "mockito-all"                   % "1.10.19" % "test"
      val scalacheck        = "org.scalacheck"          %% "scalacheck"                   % "1.12.5"  % "test"
    }


    object Avro {
      val sparkAvro            = "com.databricks" %% "spark-avro"              % SparkAvro
      val avroMapRed           = "org.apache.avro" % "avro-mapred"             % AvroMapRed classifier "hadoop2" exclude("org.mortbay.jetty", "servlet-api")
    }

    object Cassandra {
      val cassandraSparkConnector    = "com.datastax.spark"     % "spark-cassandra-connector_2.10"   % CassandraConnectorVersion exclude("com.codahale.metrics", "metrics-core")
      val cassandraDriver       = "com.datastax.cassandra"  % "cassandra-driver-core"       % CassandraDriverVersion     exclude("com.codahale.metrics", "metrics-core") //guavaExclude
      val cassandraClient       = "org.apache.cassandra"    % "cassandra-clientutil"        % CassandraVersion           //guavaExclude
      val cassandraAll          = "org.apache.cassandra"    % "cassandra-all"               % CassandraVersion           logbackExclude
    }


    object MacWire {
      val macwireMacros = "com.softwaremill.macwire" %% "macros" % "1.0.7" % "provided"
      val macwireUtil = "com.softwaremill.macwire" %% "runtime" % "1.0.7"
    }

  }

  import Compile._

  val time = Seq(Time.nscala)

  val scopt = Seq(Scopt.scopt)

  val test = Seq(Test.scalaTest, Test.scalacheck, Test.mockito)

  val spark = Spark.sparkCore :: Spark.sparkML :: Spark.sparkSql ::: Nil

  val json = Seq(Json.json4sNative, Json.json4sJackson, Json.json4sExt)

  val avro = Seq(Avro.sparkAvro, Avro.avroMapRed)

  val macwire = Seq(MacWire.macwireMacros, MacWire.macwireUtil)

  val basic: Seq[sbt.ModuleID] = spark ++ json ++ scopt ++ test ++ macwire ++ avro

  val all = basic

}

