package ir.h4n.aprService.models
import cats.Applicative
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class ErrorResponse(errors: Seq[ErrorItem])

object ErrorResponse {

  import io.circe.generic.auto._

  implicit def outputEntityEncoder[F[_]: Applicative]: EntityEncoder[F, ErrorResponse] =
    jsonEncoderOf[F, ErrorResponse]
}

case class ErrorItem(errorCode: ErrorCodes.Type, errorMessage: String)

object ErrorItem {

  import io.circe.generic.auto._

  implicit def outputEntityEncoder[F[_]: Applicative]: EntityEncoder[F, ErrorItem] =
    jsonEncoderOf[F, ErrorItem]
}

object ErrorCodes extends Enumeration {
  type Type = Value

  val IncorrectFee:       Type = Value(4001)
  val IncorrectPrincipal: Type = Value(4002)
  val IncorrectSchedule:  Type = Value(4003)
  val IncorrectInterest:  Type = Value(4004)

  implicit val errorCodesEncoder: Encoder[ErrorCodes.Type] = Encoder.enumEncoder(ErrorCodes)

}
