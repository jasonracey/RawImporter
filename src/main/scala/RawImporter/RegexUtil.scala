package RawImporter

import scala.util.matching.Regex

object RegexUtil {
  // no match: /Volumes/photos-a/Photographs/2017/201703_artEASTRefesh
  //    match: /Volumes/photos-a/Photographs/2017/20170531_CanyonlandsNationalPark_DruidArch
  //    match: /Volumes/photos-a/Photographs/2017/20170601a_CanyonlandsNationalPark_FalseKiva
  val photoDirPattern: Regex = "^\\/Volumes\\/photos-a\\/Photographs\\/\\d{4}\\/\\d{7,}\\w+$".r

  // no match: Edited2
  //    match: 0120
  val numericPattern: Regex = "^\\d+$".r
}
