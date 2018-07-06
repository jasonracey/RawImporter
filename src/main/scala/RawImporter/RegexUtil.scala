package RawImporter

import scala.util.matching.Regex

object RegexUtil {
  // no match: Edited2
  //    match: 0120
  val numericPattern: Regex = "^\\d+$".r

  // no match: /Volumes/photos-a/Photographs/2017/201703_artEASTRefesh
  //    match: /Volumes/photos-a/Photographs/2017/20170531_CanyonlandsNationalPark_DruidArch
  //    match: /Volumes/photos-a/Photographs/2017/20170601a_CanyonlandsNationalPark_FalseKiva
  val photoDirPattern: Regex = "^\\/Volumes\\/photos-a\\/Photographs\\/\\d{4}\\/\\d{7,}\\w+$".r

  //    match: A730000
  //    match: A73000
  //    match: A7300
  //    match: A730
  //    match: A73
  val sonyLeadingTextPattern: Regex = "^A730*".r
}
