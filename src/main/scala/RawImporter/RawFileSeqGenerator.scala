package RawImporter

import java.io.File

trait RawFileSeqGenerator {
  def getRawFileSeq(rawFileDirAndTifNames: Seq[(File, List[String])]): Seq[File] = {
    val allRawFiles: Seq[File] = rawFileDirAndTifNames.flatMap{ case (rawFileDir: File, tifNames: List[String]) =>
      tifNames.flatMap{ tifName: String =>
        val rawFileNumberStrings: Seq[String] = getRawFileNumberStrings(tifName)
        getRawFileInstances(rawFileDir, rawFileNumberStrings)
      }
    }

    allRawFiles.distinct
  }

  protected def getDelimitedRawFileNumbers(tifName: String): String

  protected def getRawFileInstance(rawFileDir: File, rawFileNumberString: String): File

  private def getRawFileNumberStrings(tifName: String): Seq[String] = {
    val delimitedRawFileNumbers: String = getDelimitedRawFileNumbers(tifName)
    val rawFileNumbers: Array[String] = delimitedRawFileNumbers.split("_")
    rawFileNumbers.filter{ RegexUtil.numericPattern.findFirstIn(_).nonEmpty }
  }

  private def getRawFileInstances(dir: File, abbreviatedRawFileNumberStrings: Seq[String]): Seq[File] = {
    val firstStringOption: Option[String] = abbreviatedRawFileNumberStrings.headOption

    // this supports generating a set of non-sequential raw files for tifs like "IMG_0122_4_6.tif"
    val fullRawFileNumberStrings: Seq[String] = abbreviatedRawFileNumberStrings.flatMap{ currentString: String =>
      firstStringOption.map{ firstString: String =>
        val charsToPrepend: String = firstString.substring(0, firstString.length - currentString.length)
        charsToPrepend + currentString
      }
    }

    fullRawFileNumberStrings.map{ rawFileNumberString: String =>
      getRawFileInstance(dir, rawFileNumberString)
    }
  }
}

object CanonRawFileSeqGenerator extends RawFileSeqGenerator {
  protected def getDelimitedRawFileNumbers(tifName: String): String = {
    tifName.replace(".tif", "")
  }

  protected def getRawFileInstance(dir: File, rawFileNumberString: String): File = {
    new File(s"$dir/cr2/IMG_$rawFileNumberString.CR2")
  }
}

object SonyRawFileSeqGenerator extends RawFileSeqGenerator {
  protected def getDelimitedRawFileNumbers(tifName: String): String = {
    RegexUtil.sonyLeadingTextPattern.replaceFirstIn(tifName, "").replace(".tif", "")
  }

  protected def getRawFileInstance(dir: File, rawFileNumberString: String): File = {
    val targetStringLength: Int = 5
    val paddedFileNumberString: String = "0"*(targetStringLength - rawFileNumberString.length) + rawFileNumberString
    new File(s"$dir/raw/A73$paddedFileNumberString.ARW")
  }
}
