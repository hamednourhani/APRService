package ir.h4n.aprService.models
import cats.effect.IO
import ir.h4n.aprService.APRService
import ir.h4n.aprService.APRServiceImpl
import ir.h4n.aprService.Controller
import ir.h4n.aprService.ControllerImpl
import ir.h4n.aprService.IRRService
import ir.h4n.aprService.IRRServiceImpl
import ir.h4n.aprService.Routes
import org.specs2.matcher.MatchResult

class RoutesTest extends org.specs2.mutable.Specification {

  import org.http4s._
  import org.http4s.implicits._

  "return notFound" >> {
    "return 404" >> {
      uriReturns404()
    }
    "return response" >> {
      uriReturnsOutput()
    }
    "return errorResponse" >> {
      uriReturnsErrorResponse()
    }
  }

  val aprService: APRService[IO] = new APRServiceImpl[IO]
  val irrService: IRRService[IO] = new IRRServiceImpl[IO]
  val controller: Controller[IO] = new ControllerImpl[IO](aprService, irrService)
  val routes:     Routes[IO]     = new Routes[IO](controller)

  private[this] def uriReturns404(): MatchResult[Status] = {
    val response: IO[Response[IO]] =
      routes.routes.orNotFound.run(Request(method = Method.GET, uri = Uri.uri("/calculate")))
    response.unsafeRunSync().status must beEqualTo(Status.NotFound)
  }

  private[this] def uriReturnsErrorResponse(): MatchResult[Status] = {

    val jsonInput: String = """{
                              |	"principal": 51020400,
                              |	"upfrontFee": {
                              |		"value": 1020400
                              |	},
                              |	"upfrontCreditlineFee": {
                              |		"value": 0
                              |	},
                              |	"schedule": []
                              |}""".stripMargin

    val request = Request[IO](method = Method.POST, uri = Uri.uri("/calculate")).withEntity(jsonInput)

    val response: IO[Response[IO]] =
      routes.routes.orNotFound
        .run(request)

    response.unsafeRunSync().status must beEqualTo(Status.BadRequest)
  }

  private[this] def uriReturnsOutput(): MatchResult[Status] = {

    val jsonInput: String = """{
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

    val request = Request[IO](method = Method.POST, uri = Uri.uri("/calculate")).withEntity(jsonInput)

    val response: IO[Response[IO]] =
      routes.routes.orNotFound.run(request)

    response.unsafeRunSync().status must beEqualTo(Status.Ok)
  }

}
