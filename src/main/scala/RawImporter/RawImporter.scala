package RawImporter

import java.io.File

object RawImporter {
  def importRawFiles(parentDir: File): Unit = {
    val childDirs: List[File] = FileUtil.getChildDirectories(parentDir)

    val photoDirs: List[File] = childDirs.filter{ dir: File =>
      RegexUtil.photoDirPattern.findFirstIn(dir.getPath).nonEmpty
    }

    photoDirs.foreach{ dir: File =>
      val rawFileSet: scala.collection.mutable.Set[File] = scala.collection.mutable.Set.empty

      val tifFileNames: List[String] = FileUtil.getFilesOfType(dir, List("tif")).map{ _.getName }

      tifFileNames.foreach{ name: String =>

        val rawFileNumberStrings: Seq[String] = name
          .replace(".tif", "")
          .split ("_")
          .filter { RegexUtil.numericPattern.findFirstIn(_).nonEmpty }

        val firstRawFileNumber = rawFileNumberStrings.head.toInt

        var addedToSetCount: Int = 0
        while (addedToSetCount < rawFileNumberStrings.length) {
          val currentRawFileNumber: Int = firstRawFileNumber + addedToSetCount
          val withLeadingZeroes: String = "%04d".format(currentRawFileNumber)
          val rawFile: File = new File(s"$dir/cr2/IMG_$withLeadingZeroes.CR2")
          if (rawFile.exists) rawFileSet += rawFile
          addedToSetCount += 1
        }
      }

      rawFileSet.toArray.sortBy{ _.getName }.foreach{ f: File =>
        val src: File = new File(f.getPath)
        val dst: File = new File(f.getPath.replace("/Volumes/photos-a", "/Users/jasonracey/Files"))
        if (!dst.exists) {
          FileUtil.copyFile(src, dst)
          println(s"Copied $src")
        }
      }
    }
  }
}
