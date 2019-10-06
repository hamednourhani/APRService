package ir.h4n.aprService.models
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import io.circe
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class InputTest extends FlatSpec with Matchers {

  import io.circe.generic.auto._
  import io.circe.parser._

  it should "not parse input model" in {

    val input: String = """
                          |{
                          |  "principal": 51020400,
                          |  "upfrontFee": {
                          |    "value": 1020400
                          |  },
                          |  "upfrontCreditlineFee": {
                          |    "value": 0
                          |  },
                          |  "schedule": [
                          |    {
                          |      "id": 1,
                          |      "date": "2016-10-20",
                          |      "interestFee": 1530600
                          |    },
                          |    {
                          |      "id": 2,
                          |      "date": "2016-11-21",
                          |      "principal": 3702800,
                          |      "interestFee": 1422800
                          |    }
                          |  ]
                          |}""".stripMargin

    val json: Either[circe.Error, Input] = parse(input).flatMap(_.as[Input])

    assert(json.isLeft)

  }

  it should "parse input model" in {

    val input: String = """
                          |{
                          |  "principal": 51020400,
                          |  "upfrontFee": {
                          |    "value": 1020400
                          |  },
                          |  "upfrontCreditlineFee": {
                          |    "value": 0
                          |  },
                          |  "schedule": [
                          |    {
                          |      "id": 1,
                          |      "date": "2016-10-20",
                          |      "principal": 3595000,
                          |      "interestFee": 1530600
                          |    },
                          |    {
                          |      "id": 2,
                          |      "date": "2016-11-21",
                          |      "principal": 3702800,
                          |      "interestFee": 1422800
                          |    }
                          |  ]
                          |}""".stripMargin

    val json: Either[circe.Error, Input] = parse(input).flatMap(_.as[Input])

    assert(json.isRight)
    assert(json.map(_.principal).contains(51020400))

  }

  it should s"fail on input validation with errorCode:${ErrorCodes.IncorrectFee}" in {
    val input  = Fixtures.input.copy(upfrontFee = Fee(-1L))
    val result = input.validate
    assert(result.isInvalid)
    result match {
      case Valid(_)   => fail()
      case Invalid(e) => assert(e.errors.exists(_.errorCode === ErrorCodes.IncorrectFee))
    }
  }

  it should s"fail on input validation with errorCode:${ErrorCodes.IncorrectPrincipal}" in {
    val input  = Fixtures.input.copy(principal = -1L)
    val result = input.validate
    assert(result.isInvalid)
    result match {
      case Valid(_)   => fail()
      case Invalid(e) => assert(e.errors.exists(_.errorCode === ErrorCodes.IncorrectPrincipal))
    }
  }

  it should s"fail on input validation with errorCode:${ErrorCodes.IncorrectSchedule}" in {
    val input  = Fixtures.input.copy(schedule = Seq.empty)
    val result = input.validate
    assert(result.isInvalid)
    result match {
      case Valid(_)   => fail()
      case Invalid(e) => assert(e.errors.exists(_.errorCode === ErrorCodes.IncorrectSchedule))
    }
  }

  it should s"fail on input validation with errorCode:${ErrorCodes.IncorrectInterest}" in {
    val input  = Fixtures.input.copy(schedule = Seq(Fixtures.schedule1.copy(interestFee = -1L)))
    val result = input.validate
    assert(result.isInvalid)
    result match {
      case Valid(_)   => fail()
      case Invalid(e) => assert(e.errors.exists(_.errorCode === ErrorCodes.IncorrectInterest))
    }
  }

}
