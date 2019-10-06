package ir.h4n.aprService.models

import cats.Applicative
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

final case class Output(apr: Double, irr: Double)

object Output {

  import io.circe.generic.auto._

  implicit def outputEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Output] =
    jsonEncoderOf[F, Output]
}

object SampleOutput {
  val output: String = """
                         |{
                         |  "apr": 48.3,
                         |  "irr": 0.0334008783
                         |}""".stripMargin

  //{
  //	"apr": 4.836206291054793,
  //	"irr": 0.03340084
  //}
}
