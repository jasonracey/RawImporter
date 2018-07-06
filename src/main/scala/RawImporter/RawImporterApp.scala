package RawImporter

import java.io.File

import scala.util.Try

object RawImporterApp extends App {
  if (args.length == 0) throw new IllegalArgumentException("Please specify a year.")

  val year: Int = Try(args(0).toInt).toOption.getOrElse(throw new IllegalArgumentException("Year must be an int."))

  val rawFilesParentDir: File = new File(s"/Volumes/photos-a/Photographs/$year")

  if (!rawFilesParentDir.exists) throw new IllegalArgumentException(s"Directory not found: $rawFilesParentDir")

  val allChildDirs: List[File] = FileUtil.getChildDirectories(rawFilesParentDir)

  val dirsThatContainRawFiles: List[File] = allChildDirs.filter{ childDir: File =>
    RegexUtil.photoDirPattern.findFirstIn(childDir.getPath).nonEmpty
  }

  val rawFileDirAndTifNames: Seq[(File, List[String])] = dirsThatContainRawFiles.map{ rawFileDir: File =>
    rawFileDir -> FileUtil.getFilesOfType(rawFileDir, List("tif")).map{ _.getName }
  }

  val rawFileSeqGenerator: RawFileSeqGenerator = if (year >= 2018) SonyRawFileSeqGenerator else CanonRawFileSeqGenerator

  val rawFileSeq: Seq[File] = rawFileSeqGenerator.getRawFileSeq(rawFileDirAndTifNames)

  // sorting so that overall progress can be estimated from console output
  rawFileSeq.filter{ _.exists }.sortBy{ _.getPath }.foreach{ src: File =>
    val dst: File = new File(src.getPath.replace("/Volumes/photos-a", "/Users/jasonracey/Files"))
    if (!dst.exists) {
      FileUtil.copyFile(src, dst)
      println(s"Wrote $dst")
    }
  }

  println("Done.")
}