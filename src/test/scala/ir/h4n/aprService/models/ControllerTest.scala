package ir.h4n.aprService.models
import cats.effect.IO
import ir.h4n.aprService.APRService
import ir.h4n.aprService.ControllerImpl
import ir.h4n.aprService.IRRService
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfter
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ControllerTest extends FlatSpec with Matchers with BeforeAndAfter with MockFactory {

  val irrService: IRRService[IO] = stub[IRRService[IO]]
  val aprService: APRService[IO] = stub[APRService[IO]]

  val controller = new ControllerImpl[IO](aprService, irrService)

  it should "should validate input and calc output" in {
    val input = Fixtures.input
    val apr   = 48.4d
    val irr   = 0.034

    (aprService.calculateAPRRate _)
      .when(input)
      .returns(IO(apr))

    (irrService.calculateIRRRate _)
      .when(input)
      .returns(IO(irr))

    val result: IO[Either[ErrorResponse, Output]] = controller.calc(input)

    result.unsafeRunSync() match {
      case Left(e) =>
        println(e)
        fail()

      case Right(v) =>
        assert(v === Output(apr, irr))

    }
  }
}
