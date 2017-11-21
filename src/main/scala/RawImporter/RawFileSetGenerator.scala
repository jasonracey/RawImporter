package RawImporter

import java.io.File

object RawFileSetGenerator {
  def getRawFileSet(dirAndTifNames: Seq[(File, List[String])]): Set[File] = {
    val allRawFiles: Seq[File] = dirAndTifNames.flatMap{ case (dir: File, tifNames: List[String]) =>
      tifNames.flatMap{ tifName: String =>
        val rawFileNumberStrings: Seq[String] = getRawFileNumberStrings(tifName)
        getRawFiles(dir, rawFileNumberStrings)
      }
    }
    allRawFiles.toSet
  }

  private def getRawFileNumberStrings(tifName: String): Seq[String] = {
    val delimitedParts: String = tifName.replace(".tif", "")
    val partArray: Array[String] = delimitedParts.split("_")
    partArray.filter{ RegexUtil.numericPattern.findFirstIn(_).nonEmpty }
  }

  private def getRawFiles(dir: File, abbreviatedRawFileNumberStrings: Seq[String]): Seq[File] = {
    val firstStringOption: Option[String] = abbreviatedRawFileNumberStrings.headOption

    // this supports generating a set of non-sequential raw files for tifs like "IMG_0122_4_6.tif"
    val fullRawFileNumberStrings: Seq[String] = abbreviatedRawFileNumberStrings.flatMap{ currentString: String =>
      firstStringOption.map{ firstString: String =>
        val charsToPrepend: String = firstString.substring(0, firstString.length - currentString.length)
        charsToPrepend + currentString
      }
    }

    fullRawFileNumberStrings.map{ rawFileNumberString: String =>
      new File(s"$dir/cr2/IMG_$rawFileNumberString.CR2")
    }
  }
}
