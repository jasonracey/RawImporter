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

  val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(allChildDirs)

  val existsInSource: Set[File] = rawFileSet.filter{ _.exists }

  // sorting so that overall progress can be estimated from console output
  val sortedExists = existsInSource.toArray.sortBy{ _.getName }

  sortedExists.foreach{ src: File =>
    val dst: File = new File(src.getPath.replace("/Volumes/photos-a", "/Users/jasonracey/Files"))
    if (!dst.exists) {
      FileUtil.copyFile(src, dst)
      println(s"Copied $src")
    }
  }

  println("Done.")
}