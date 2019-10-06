package ir.h4n.aprService
import java.time.LocalDate

object Constants {
  val DAYS_OF_YEAR = 365.25d
}

trait BaseService[F[_]] {

  type DoubleFunc = Double => Double

  type LongTuple = (Long, Long)

  type DoubleToLongTuple = (Double, Long)

  implicit val dateOrdering: Ordering[LocalDate] = new Ordering[LocalDate] {
    override def compare(x: LocalDate, y: LocalDate): Int = x.compareTo(y)
  }

  def calculateByRapson(func: DoubleFunc, derOfFunc: DoubleFunc, start: Double): Double = {
    var res = start
    var y   = 0d
    do {
      y = func(res) / derOfFunc(res)
      res -= y
    } while (Math.abs(y) > Math.exp(-12))
    res
  }
}
