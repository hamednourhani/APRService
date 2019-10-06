package ir.h4n.aprService

import cats.effect.ConcurrentEffect
import cats.effect.ContextShift
import cats.effect.Timer
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

class Server[F[_]: ConcurrentEffect: Timer: ContextShift](routes: Routes[F]) {

  def stream: Stream[F, Nothing] = {

    val httpApp = routes.routes.orNotFound

    val finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

    BlazeServerBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(finalHttpApp)
      .serve

  }.drain
}
