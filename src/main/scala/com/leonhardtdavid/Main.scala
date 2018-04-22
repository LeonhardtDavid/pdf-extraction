package com.leonhardtdavid

import java.io.File
import java.nio.file.FileSystems

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import akka.stream.alpakka.file.DirectoryChange
import akka.stream.alpakka.file.scaladsl.DirectoryChangesSource
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Main {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val logger = Logging(this.system, "application")

  System.setProperty("java.awt.headless", "true")
  System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider")

  def main(args: Array[String]): Unit = {
    val directory = args(0)
    val maybeRegex = args.lift(1).map(_.r)

    val fs = FileSystems.getDefault
    val changes = DirectoryChangesSource(fs.getPath(directory), pollInterval = 1.second, maxBufferSize = 1000)

    changes.runForeach {
      case (path, change) if path.toString.endsWith(".pdf") && change == DirectoryChange.Creation =>
        try {
          val pdf = PDDocument.load(new File(path.toString))
          val stripper = new PDFTextStripper
          stripper.setAddMoreFormatting(true)
          val text = stripper.getText(pdf)

          val extractedText = maybeRegex match {
            case Some(regex) =>
              regex.findFirstIn(text) match {
                case Some(regex(t)) => t
                case _              => "NONE"
              }
            case _ =>
              text
          }

          logger.debug("Extracted text:\n{}", extractedText)
        } catch {
          case e: Throwable => logger.error(e, "Error extracting text")
        }

      case (path, change) =>
        logger.debug("Obviate => Path: {}, Change: {}", path, change)
    }
  }

}
