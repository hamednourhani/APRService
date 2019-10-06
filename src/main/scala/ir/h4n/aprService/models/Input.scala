package ir.h4n.aprService.models
import java.time.LocalDate

import cats.Applicative
import cats.data.Validated
import cats.effect.Sync
import cats.implicits._
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonOf
import org.http4s.circe.jsonEncoderOf

final case class Input(
    principal:            Long,
    upfrontFee:           Fee,
    upfrontCreditlineFee: Fee,
    schedule:             Seq[Schedule]
)

object Input {

  import io.circe.generic.auto._

  implicit def inputEntityDecoder[F[_]: Sync]: EntityDecoder[F, Input] =
    jsonOf[F, Input]

  implicit def outputEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Output] =
    jsonEncoderOf[F, Output]

  implicit class InputOps(val i: Input) extends AnyVal {
    def validate: Validated[ErrorResponse, Input] = Input.validate(i)
  }

  def validate(input: Input): Validated[ErrorResponse, Input] = {
    val validateFee = Validated.condNel(
      input.upfrontFee.value >= 0,
      (),
      ErrorItem(ErrorCodes.IncorrectFee, s"incorrect upfrontFee:${input.upfrontFee.value}")
    )

    val validatePrincipal = Validated.condNel(
      input.principal >= 0,
      (),
      ErrorItem(ErrorCodes.IncorrectPrincipal, s"incorrect principal:${input.principal}")
    )

    val validateScheduleSize = Validated.condNel(
      input.schedule.nonEmpty,
      (),
      ErrorItem(ErrorCodes.IncorrectSchedule, s"incorrect schedule:${input.schedule.mkString(",")}")
    )

    val validateInterests = Validated.condNel(
      input.schedule.forall(_.interestFee >= 0),
      (),
      ErrorItem(ErrorCodes.IncorrectInterest, s"incorrect interest:${input.schedule.map(_.interestFee).mkString(",")}")
    )

    val validateSchedulePrincipals = Validated.condNel(
      input.schedule.forall(_.principal >= 0),
      (),
      ErrorItem(ErrorCodes.IncorrectInterest, s"incorrect principal:${input.schedule.map(_.principal).mkString(",")}")
    )

    validateFee
      .combine(validatePrincipal)
      .combine(validateScheduleSize)
      .combine(validateInterests)
      .combine(validateSchedulePrincipals)
      .leftMap(_.toList)
      .bimap(ErrorResponse.apply, _ => input)

  }
}

final case class Fee(value: Long)

final case class Schedule(id: Int, date: LocalDate, principal: Long, interestFee: Long)
