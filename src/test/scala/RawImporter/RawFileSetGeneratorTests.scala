package RawImporter

import java.io.File
import org.scalatest.FunSuite

final class RawFileSetGeneratorTests  extends FunSuite {
  test("can generate empty set from empty tif names list"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List()
      Seq((dir, tifNames))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 0)
  }

  test("can generate correct raw file objects for one tif name"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List("IMG_0120_1_2_3_4.tif")
      Seq((dir, tifNames))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 5)

    val paths: Array[String] = rawFileSet.toArray.map{ _.getPath }

    assert(paths.contains("a/b/cr2/IMG_0120.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0121.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0122.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0123.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0124.CR2"))
  }

  test("can generate correct raw file objects for two tif names"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List("IMG_0120_1_2_3_4.tif", "IMG_0436_37_38_39_40.tif")
      Seq((dir, tifNames))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 10)

    val paths: Array[String] = rawFileSet.toArray.map{ _.getPath }

    assert(paths.contains("a/b/cr2/IMG_0120.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0121.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0122.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0123.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0124.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0436.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0437.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0438.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0439.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0440.CR2"))
  }

  test("can generate correct raw file objects for tifs merged from the same raw files"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List("IMG_0120_1_2_3_4.tif", "IMG_0120_1_2_3_4_edited.tif")
      Seq((dir, tifNames))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 5)

    val paths: Array[String] = rawFileSet.toArray.map{ _.getPath }

    assert(paths.contains("a/b/cr2/IMG_0120.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0121.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0122.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0123.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0124.CR2"))
  }

  test("can generate correct raw file objects for multiple directories"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir1: File = new File("a/b")
      val dir2: File = new File("c/d")
      val tifNames1: List[String] = List("IMG_0446_47_48_49_50_Soft3_edited_skylight.tif", "IMG_0801_2_3_4_5_edited_crop.tif")
      val tifNames2: List[String] = List("IMG_0001_2_3_4_5_Realistic_edited.tif", "IMG_0396_397_398_399_400_Realistic_edited.tif")
      Seq((dir1, tifNames1), (dir2, tifNames2))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 20)

    val paths: Array[String] = rawFileSet.toArray.map{ _.getPath }

    assert(paths.contains("a/b/cr2/IMG_0446.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0447.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0448.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0449.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0450.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0801.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0802.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0803.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0804.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0805.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0001.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0002.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0003.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0004.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0005.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0396.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0397.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0398.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0399.CR2"))
    assert(paths.contains("c/d/cr2/IMG_0400.CR2"))
  }

  test("can generate correct raw file objects from tifs merged from non-sequential raw files"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List("IMG_0122_4_6.tif", "IMG_0129_31_33.tif")
      Seq((dir, tifNames))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 6)

    val paths: Array[String] = rawFileSet.toArray.map{ _.getPath }

    assert(paths.contains("a/b/cr2/IMG_0122.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0124.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0126.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0129.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0131.CR2"))
    assert(paths.contains("a/b/cr2/IMG_0133.CR2"))
  }

  test("generator ignores Lightroom HDR tifs"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List("IMG_0446-HDR.tif", "IMG_0446-HDR_edited.tif")
      Seq((dir, tifNames))
    }

    val rawFileSet: Set[File] = RawFileSetGenerator.getRawFileSet(dirAndTifNames)

    assert(rawFileSet != null)
    assert(rawFileSet.size === 0)
  }
}