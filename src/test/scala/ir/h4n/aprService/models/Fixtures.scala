package ir.h4n.aprService.models
import java.time.LocalDate

object Fixtures {

  val schedule1 =
    Schedule(
      id = 1,
      LocalDate.parse("2016-10-20"),
      principal   = 100L,
      interestFee = 10L
    )

  val schedule2 =
    Schedule(
      id = 2,
      LocalDate.parse("2016-11-21"),
      principal   = 100L,
      interestFee = 10L
    )

  val input =
    Input(
      principal            = 1000L,
      upfrontFee           = Fee(0L),
      upfrontCreditlineFee = Fee(0L),
      schedule             = Seq(schedule1, schedule2)
    )
}
