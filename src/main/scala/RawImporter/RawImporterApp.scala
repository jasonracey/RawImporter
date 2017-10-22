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

  RawImporter.importRawFiles(parentDir)

  println("Done.")
}