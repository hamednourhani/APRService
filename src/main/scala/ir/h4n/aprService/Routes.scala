package ir.h4n.aprService

import cats.effect.Sync
import cats.implicits._
import ir.h4n.aprService.models.Input
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class Routes[F[_]: Sync](controller: Controller[F]) {

  def routes: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "calculate" =>
        for {
          input  <- req.as[Input]
          output <- controller.calc(input)
          res <- output.fold(
                  e => BadRequest(e),
                  res => Ok(res)
                )
        } yield res
    }
  }
}
