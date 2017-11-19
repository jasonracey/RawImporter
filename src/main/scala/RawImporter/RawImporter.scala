package RawImporter

import java.io.File

object RawImporter {
  def importRawFiles(parentDir: File): Unit = {
    val allRawFiles: Set[File] = getRawFileSet(parentDir)

    allRawFiles.toArray.sortBy{ _.getName }.foreach{ src: File =>
      val dst: File = new File(src.getPath.replace("/Volumes/photos-a", "/Users/jasonracey/Files"))
      if (!dst.exists) {
        FileUtil.copyFile(src, dst)
        println(s"Copied $src")
      }
    }
  }

  private def getRawFileSet(parentDir: File): Set[File] = {
    val allChildDirs: List[File] = FileUtil.getChildDirectories(parentDir)

    val childDirsWithRawFiles: List[File] = allChildDirs.filter{ dir: File =>
      RegexUtil.photoDirPattern.findFirstIn(dir.getPath).nonEmpty
    }

    val dirAndTifNames: Seq[(File, List[String])] = childDirsWithRawFiles.map{ dir: File =>
      dir -> FileUtil.getFilesOfType(dir, List("tif")).map{ _.getName }
    }

    val allRawFiles: Seq[File] = dirAndTifNames.flatMap{ case (dir: File, tifNames: List[String]) =>
      tifNames.flatMap{ tifName: String =>
        val rawFileNumberStrings: Seq[String] = tifName
          .replace(".tif", "")
          .split("_")
          .filter{ RegexUtil.numericPattern.findFirstIn(_).nonEmpty }

        val rawFilesOption: Option[Seq[File]] = rawFileNumberStrings.headOption.map{ rawFileNumberString: String =>
          val baseRawFileNumber: Int = rawFileNumberString.toInt
          val rawFileNumbers: Seq[Int] = baseRawFileNumber to baseRawFileNumber + rawFileNumberStrings.length

          val rawFilesForCurrentTif: Seq[File] = rawFileNumbers.map{ currentRawFileNumber: Int =>
            val withLeadingZeroes: String = "%04d".format(currentRawFileNumber)
            new File(s"$dir/cr2/IMG_$withLeadingZeroes.CR2")
          }

          rawFilesForCurrentTif.filter{ _.exists }
        }

        rawFilesOption.getOrElse(Seq.empty)
      }
    }

    allRawFiles.toSet
  }
}
