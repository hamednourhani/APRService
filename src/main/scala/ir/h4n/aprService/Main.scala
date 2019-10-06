package ir.h4n.aprService

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.implicits._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val aprService: APRService[IO] = new APRServiceImpl[IO]
    val irrService: IRRService[IO] = new IRRServiceImpl[IO]
    val controller: Controller[IO] = new ControllerImpl[IO](aprService, irrService)
    val routes:     Routes[IO]     = new Routes[IO](controller)
    val server:     Server[IO]     = new Server[IO](routes)

    server.stream.compile.drain.as(ExitCode.Success)
  }
}
