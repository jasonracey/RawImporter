package RawImporter

import java.io.{File, FileWriter}

import org.scalatest.{BeforeAndAfterAll, FunSuite}

final class FileUtilTests extends FunSuite with BeforeAndAfterAll {
  test("FileUtil can copy a file to another location") {
    val src: File = new File(srcPath)
    val dst: File = new File(dstPath)

    assert(src.exists())
    assert(!dst.exists())

    FileUtil.copyFile(src, dst)

    assert(dst.exists())
  }

  test("FileUtil can get child directories of a directory") {
    val currentDir: File = new File(parent)
    val result: List[File] = FileUtil.getChildDirectories(currentDir)

    assert(result.nonEmpty)
    assert(result.size === 3)

    val paths = result.map{ _.getPath }

    assert(paths.contains(child1))
    assert(paths.contains(child2))
    assert(paths.contains(child3))
  }

  test("FileUtil can get a list of files by a single extension") {
    val result: List[File] = FileUtil.getFilesOfType(new File("."), List("xxx"))

    assert(result.nonEmpty)
    assert(result.size === 3)

    val dirNames = result.map{ _.getName }

    assert(dirNames.contains(xxx_a))
    assert(dirNames.contains(xxx_b))
    assert(dirNames.contains(xxx_c))
  }

  test("FileUtil can get a list of files by multiple extension") {
    val result: List[File] = FileUtil.getFilesOfType(new File("."), List("xxx", "yyy"))

    assert(result.nonEmpty)
    assert(result.size === 4)

    val dirNames = result.map{ _.getName }

    assert(dirNames.contains(xxx_a))
    assert(dirNames.contains(xxx_b))
    assert(dirNames.contains(xxx_c))
    assert(dirNames.contains(yyy_a))
  }

  val srcPath: String = "src.txt"
  val dstPath: String = "dst/dst.txt" // put in a sub dir to test dir created also

  val parent: String = "parent"
  val child1: String = s"$parent/child1"
  val child2: String = s"$parent/child2"
  val child3: String = s"$parent/child3"

  val xxx_a: String = s"a.xxx"
  val xxx_b: String = s"b.xxx"
  val xxx_c: String = s"c.xxx"
  val yyy_a: String = s"a.yyy"
  val zzz_a: String = s"a.zzz"

  override def beforeAll(): Unit = {
    createTempDirs()
    createTempFiles()
  }

  private def createTempDirs(): Unit = {
    for {
      path <- Set(parent, child1, child2, child3)
    } {
      val file: File = new File(path)
      file.mkdir()
    }
  }

  private def createTempFiles(): Unit = {
    for {
      path <- Set(srcPath, xxx_a, xxx_b, xxx_c, yyy_a, zzz_a)
    } {
      val writer: FileWriter = new FileWriter(path)
      try {
        writer.write("")
      }
      finally {
        writer.close()
      }
    }
  }

  override def afterAll(): Unit = {
    deleteTempDirs()
    deleteTempFiles()
  }

  def deleteTempDirs(): Unit = {
    for {
      path <- Set(child1, child2, child3, parent)
    } {
      val file: File = new File(path)
      if (file.exists()) file.delete()
    }
  }

  def deleteTempFiles(): Unit = {
    for {
      path <- Seq(srcPath, dstPath, xxx_a, xxx_b, xxx_c, yyy_a, zzz_a)
    } {
      val f: File = new File(path)
      if (f.exists()) f.delete()
    }
  }
}
