package ir.h4n.aprService
import cats.Applicative
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import cats.effect.Sync
import cats.implicits._
import ir.h4n.aprService.models.ErrorResponse
import ir.h4n.aprService.models.Input
import ir.h4n.aprService.models.Output

trait Controller[F[_]] {
  def calc(input: Input): F[Either[ErrorResponse, Output]]
}

class ControllerImpl[F[_]: Sync: Applicative](aprService: APRService[F], irrService: IRRService[F])
    extends Controller[F] {

  override def calc(input: Input): F[Either[ErrorResponse, Output]] =
    input.validate match {

      case Invalid(e) =>
        Either.left[ErrorResponse, Output](e).pure[F]

      case Valid(validatedInput) =>
        for {
          apr <- aprService.calculateAPRRate(validatedInput)
          irr <- irrService.calculateIRRRate(validatedInput)
        } yield {
          Either.right[ErrorResponse, Output](Output(apr = apr, irr = irr))
        }

    }

}
