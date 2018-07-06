package RawImporter

import java.io.File

import org.scalatest.FunSuite

final class SonyRawFileSeqGeneratorTests  extends FunSuite {
  test("can generate correct raw file objects for one tif name"){
    val dirAndTifNames: Seq[(File, List[String])] = {
      val dir: File = new File("a/b")
      val tifNames: List[String] = List("A7300120_1_2_3_4.tif")
      Seq((dir, tifNames))
    }

    val rawFileSeq: Seq[File] = SonyRawFileSeqGenerator.getRawFileSeq(dirAndTifNames)

    rawFileSeq.foreach(println)

    assert(rawFileSeq != null)
    assert(rawFileSeq.size === 5)

    val paths: Array[String] = rawFileSeq.toArray.map{ _.getPath }

    assert(paths.contains("a/b/raw/A7300120.ARW"))
    assert(paths.contains("a/b/raw/A7300121.ARW"))
    assert(paths.contains("a/b/raw/A7300122.ARW"))
    assert(paths.contains("a/b/raw/A7300123.ARW"))
    assert(paths.contains("a/b/raw/A7300124.ARW"))
  }
}