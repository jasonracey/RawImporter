package RawImporter

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.channels.FileChannel

object FileUtil {
  def copyFile(source: File, dest: File): Unit = {
    var inputChannel: FileChannel = null
    var outputChannel: FileChannel = null
    try {
      inputChannel = new FileInputStream(source).getChannel
      dest.getCanonicalFile.getParentFile.mkdirs
      dest.createNewFile
      outputChannel = new FileOutputStream(dest).getChannel
      outputChannel.transferFrom(inputChannel, 0, inputChannel.size)
    } catch {
      // close channels, but stop if there's an exception
      case e: Exception => throw e
    } finally {
      if (inputChannel != null) inputChannel.close()
      if (outputChannel != null) outputChannel.close()
    }
  }

  def getChildDirectories(dir: File): List[File] = {
    dir.listFiles.filter{ _.isDirectory }.toList
  }

  def getFilesOfType(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter{ _.isFile }.toList.filter{ file =>
      extensions.exists{ file.getName.endsWith(_) }
    }
  }
}