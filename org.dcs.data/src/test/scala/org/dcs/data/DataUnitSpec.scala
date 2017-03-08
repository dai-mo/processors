package org.dcs.data

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar

/**
  * Created by cmathew on 24.10.16.
  */

trait BaseDataUnitSpec extends Matchers
  with OptionValues
  with Inside
  with Inspectors
  with MockitoSugar
  with ScalaFutures


abstract class DataUnitSpec extends FlatSpec
  with BaseDataUnitSpec
  with BeforeAndAfterAll

abstract class AsyncDataUnitSpec extends AsyncFlatSpec
  with BaseDataUnitSpec
  with BeforeAndAfterAll


