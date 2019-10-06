package ir.h4n.aprService

import java.time.temporal.ChronoUnit

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import ir.h4n.aprService.models.Input
import ir.h4n.aprService.models.Schedule

trait IRRService[F[_]] {
  def calculateIRRRate(input: Input): F[Double]
}

class IRRServiceImpl[F[_]: Sync: Applicative]() extends IRRService[F] with BaseService[F] {

  def calculateIRRRate(input: Input): F[Double] = {

    def calculate(items: Seq[DoubleToLongTuple], i: Double): Double =
      items.map { case (months, amount) => amount / Math.pow(i + 1, months) }.sum

    def calculateDerivative(items: Seq[DoubleToLongTuple], i: Double): Double =
      items.map { case (months, amount) => amount * (-months) * Math.pow(i + 1, (-months) - 1) }.sum

    val sortedSchedules: Seq[Schedule] = input.schedule.sortBy(_.date)

    val scheduleFlow: Seq[DoubleToLongTuple] =
      sortedSchedules.map(
        i =>
          (ChronoUnit.MONTHS.between(sortedSchedules.head.date.minusMonths(1), i.date).toDouble,
           i.principal + i.interestFee)
      )

    val distanceToStartDay = 0d

    val flow: Seq[DoubleToLongTuple] = Seq(distanceToStartDay -> -input.principal,
                                           distanceToStartDay -> input.upfrontFee.value,
                                           distanceToStartDay -> input.upfrontCreditlineFee.value) ++ scheduleFlow

    val temp: Double =
      calculateByRapson(
        x => calculate(flow, x),
        xper => calculateDerivative(flow, xper),
        distanceToStartDay
      )

    (Math.floor(temp * Math.pow(10, 10)) / Math.pow(10, 10)).pure[F]
  }

}
