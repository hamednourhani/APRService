package ir.h4n.aprService

import java.time.temporal.ChronoUnit

import scala.util.Try

import cats.Applicative
import cats.effect.Sync
import ir.h4n.aprService.models.Input
import ir.h4n.aprService.models.Schedule

trait APRService[F[_]] {
  def calculateAPRRate(input: Input): F[Double]
}

class APRServiceImpl[F[_]: Sync: Applicative]() extends APRService[F] with BaseService[F] {

  def calculateAPRRate(input: Input): F[Double] = {

    def calculate(items: Seq[LongTuple], i: Double): Double =
      items.map { case (days, amount) => amount / Math.pow(i + 1, days / Constants.DAYS_OF_YEAR) }.sum

    def calculateDerivative(items: Seq[LongTuple], i: Double): Double =
      items.map {
        case (days, amount) =>
          amount * (-days / Constants.DAYS_OF_YEAR) * Math.pow(i + 1, -days / Constants.DAYS_OF_YEAR - 1)
      }.sum

    val sortedSchedules: Seq[Schedule] =
      input.schedule
        .sortBy(_.date)

    val dayToPayBack: Seq[LongTuple] = input.schedule.map(
      i => (ChronoUnit.DAYS.between(sortedSchedules.head.date.minusMonths(1), i.date), i.principal + i.interestFee)
    )

    val inFlow  = Seq((0L, input.principal))
    val outFlow = Seq(0L -> input.upfrontCreditlineFee.value, 0L -> input.upfrontFee.value) ++ dayToPayBack

    val numberOfStartDay = 1d

    val temp = calculateByRapson(x => calculate(inFlow, x) - calculate(outFlow, x),
                                 xper => calculateDerivative(inFlow, xper) - calculateDerivative(outFlow, xper),
                                 numberOfStartDay) * 100

    Sync[F].fromEither(
      Try(BigDecimal(Math.floor(temp * 10000) / 10000).setScale(1, BigDecimal.RoundingMode.HALF_UP)).toEither
        .map(_.toDouble)
    )
  }

}
