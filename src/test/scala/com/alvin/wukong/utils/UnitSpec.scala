package com.alvin.wukong.utils


import org.scalatest._
import org.scalatest.mock.MockitoSugar
import org.scalatest.prop.PropertyChecks

abstract class UnitSpec extends FlatSpec with ShouldMatchers with MockitoSugar with BeforeAndAfterAll
        with PropertyChecks with OptionValues with Inside with Inspectors
