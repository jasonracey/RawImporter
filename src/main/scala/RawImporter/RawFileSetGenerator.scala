package RawImporter

import java.io.File

object RawFileSetGenerator {
  def getRawFileSet(allChildDirs: List[File]): Set[File] = {
    val dirsThatContainRawFiles: List[File] = allChildDirs.filter{ dir: File =>
      RegexUtil.photoDirPattern.findFirstIn(dir.getPath).nonEmpty
    }

    val dirAndTifNames: Seq[(File, List[String])] = dirsThatContainRawFiles.map{ dir: File =>
      dir -> FileUtil.getFilesOfType(dir, List("tif")).map{ _.getName }
    }

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
    val numericParts: Seq[String] = partArray.filter{ RegexUtil.numericPattern.findFirstIn(_).nonEmpty }
    numericParts
  }

  // todo: refactor to handle case of non-sequential raw files
  private def getRawFiles(dir: File, rawFileNumberStrings: Seq[String]): Seq[File] = {
    val rawFilesOption: Option[Seq[File]] = rawFileNumberStrings.headOption.map{ rawFileNumberString: String =>
      val startFileNumber: Int = rawFileNumberString.toInt
      val endFileNumber: Int = startFileNumber + rawFileNumberStrings.length
      (startFileNumber to endFileNumber).map{ getRawFile(dir, _) }
    }
    rawFilesOption.getOrElse(Seq.empty)
  }

  private def getRawFile(baseDir: File, rawFileNumber: Int): File = {
    val withLeadingZeroes: String = "%04d".format(rawFileNumber)
    val rawFile: File = new File(s"$baseDir/cr2/IMG_$withLeadingZeroes.CR2")
    rawFile
  }
}
