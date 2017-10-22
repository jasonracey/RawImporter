package RawImporter

import org.scalatest.FunSuite

final class RegexUtilTests extends FunSuite {
  test("photoDirPattern matches full-day directories that contain originals") {
    val path: String = "/Volumes/photos-a/Photographs/2017/20170531_CanyonlandsNationalPark_DruidArch"
    val result: Option[String] = RegexUtil.photoDirPattern.findFirstIn(path)
    assert(result.nonEmpty)
    assert(result.get === path)
  }

  test("photoDirPattern matches partial-day directories that contain originals") {
    val path: String = "/Volumes/photos-a/Photographs/2017/20170601a_CanyonlandsNationalPark_FalseKiva"
    val result: Option[String] = RegexUtil.photoDirPattern.findFirstIn(path)
    assert(result.nonEmpty)
    assert(result.get === path)
  }

  test("photoDirPattern does not match directories that do not contain originals") {
    val path: String = "/Volumes/photos-a/Photographs/2017/201703_artEASTRefesh"
    val result: Option[String] = RegexUtil.photoDirPattern.findFirstIn(path)
    assert(result.isEmpty)
  }

  test("numericPattern matches strings with only numeric characters") {
    val s: String = "0120"
    val result: Option[String] = RegexUtil.numericPattern.findFirstIn(s)
    assert(result.nonEmpty)
    assert(result.get === s)
  }

  test("numericPattern does not match strings with leading non numeric characters") {
    val s: String = "a1"
    val result: Option[String] = RegexUtil.numericPattern.findFirstIn(s)
    assert(result.isEmpty)
  }

  test("numericPattern does not match strings with following non numeric characters") {
    val s: String = "1a"
    val result: Option[String] = RegexUtil.numericPattern.findFirstIn(s)
    assert(result.isEmpty)
  }

  test("numericPattern does not match strings with internal non numeric characters") {
    val s: String = "1a1"
    val result: Option[String] = RegexUtil.numericPattern.findFirstIn(s)
    assert(result.isEmpty)
  }

  test("numericPattern does not match strings with numeric sign characters") {
    val s: String = "-1"
    val result: Option[String] = RegexUtil.numericPattern.findFirstIn(s)
    assert(result.isEmpty)
  }
}