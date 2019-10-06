package ir.h4n.aprService.models
import cats.effect.IO
import io.circe
import ir.h4n.aprService.APRServiceImpl
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class APRServiceTest extends FlatSpec with Matchers {

  import io.circe.generic.auto._
  import io.circe.parser._

  val aprService = new APRServiceImpl[IO]

  it should "return apr = 48.4" in {
    val jsonString =
      """{
        |	"principal": 51020400,
        |	"upfrontFee": {
        |		"value": 1020400
        |	},
        |	"upfrontCreditlineFee": {
        |		"value": 0
        |	},
        |	"schedule": [
        |		{
        |			"id": 1,
        |			"date": "2016-10-20",
        |			"principal": 3595000,
        |			"interestFee": 1530600
        |		},
        |		{
        |			"id": 2,
        |			"date": "2016-11-21",
        |			"principal": 3702800,
        |			"interestFee": 1422800
        |		},
        |		{
        |			"id": 3,
        |			"date": "2016-12-20",
        |			"principal": 3813900,
        |			"interestFee": 1311700
        |		},
        |		{
        |			"id": 4,
        |			"date": "2017-01-20",
        |			"principal": 3928300,
        |			"interestFee": 1197300
        |		},
        |		{
        |			"id": 5,
        |			"date": "2017-02-20",
        |			"principal": 4046200,
        |			"interestFee": 1079400
        |		},
        |		{
        |			"id": 6,
        |			"date": "2017-03-20",
        |			"principal": 4167600,
        |			"interestFee": 958000
        |		},
        |		{
        |			"id": 7,
        |			"date": "2017-04-20",
        |			"principal": 4292600,
        |			"interestFee": 833000
        |		},
        |		{
        |			"id": 8,
        |			"date": "2017-05-22",
        |			"principal": 4421400,
        |			"interestFee": 704200
        |		},
        |		{
        |			"id": 9,
        |			"date": "2017-06-20",
        |			"principal": 4554000,
        |			"interestFee": 571600
        |		},
        |		{
        |			"id": 10,
        |			"date": "2017-07-20",
        |			"principal": 4690600,
        |			"interestFee": 435000
        |		},
        |		{
        |			"id": 11,
        |			"date": "2017-08-21",
        |			"principal": 4831400,
        |			"interestFee": 294200
        |		},
        |		{
        |			"id": 12,
        |			"date": "2017-09-20",
        |			"principal": 4976600,
        |			"interestFee": 149300
        |		}
        |	]
        |}""".stripMargin

    val eitherInput: Either[circe.Error, Input] = parse(jsonString).flatMap(_.as[Input])

    assert(eitherInput.isRight)

    eitherInput match {
      case Left(e) =>
        e.printStackTrace()
        fail()

      case Right(in) =>
        val result = aprService.calculateAPRRate(in)
        assert(result.unsafeRunSync() === 48.4d)
    }

  }
}
