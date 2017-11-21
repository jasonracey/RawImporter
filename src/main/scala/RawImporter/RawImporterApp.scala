package RawImporter

import java.io.File

import scala.util.Try

object RawImporterApp extends App {
  if (args.length != 1) {
    println("Please specify a year.")
    System.exit(1)
  }

  val yearOption: Option[Int] = Try(args(0).toInt).toOption

  if (yearOption.isEmpty) {
    println("Year must be an int.")
    System.exit(1)
  }

  val parentDir: File = new File(s"/Volumes/photos-a/Photographs/${yearOption.get}")

  if (!parentDir.exists) {
    println(s"Directory not found: $parentDir")
    System.exit(1)
  }

  val allChildDirs: List[File] = FileUtil.getChildDirectories(parentDir)

  val dirsThatContainRawFiles: List[File] = allChildDirs.filter{ dir: File =>
    RegexUtil.photoDirPattern.findFirstIn(dir.getPath).nonEmpty
  }

  val dirAndTifNames: Seq[(File, List[String])] = dirsThatContainRawFiles.map{ dir: File =>
    dir -> FileUtil.getFilesOfType(dir, List("tif")).map{ _.getName }
  }

  val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

  val existsInSource: Set[File] = rawFileSet.filter{ _.exists }

  // sorting so that overall progress can be estimated from console output
  val sortedSrcExists = existsInSource.toArray.sortBy{ _.getPath }

  sortedSrcExists.foreach{ src: File =>
    val dst: File = new File(src.getPath.replace("/Volumes/photos-a", "/Users/jasonracey/Files"))
    if (!dst.exists) {
      FileUtil.copyFile(src, dst)
      println(s"Wrote $dst")
    }
  }

  println("Done.")
}