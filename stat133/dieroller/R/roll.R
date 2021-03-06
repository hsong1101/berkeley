#' @title roll
#' @description create a roll object
#' @param a die object, the numeric value to roll the die
#' @return roll object
#' @export
#' @examples
#' # default
#' die1 = die()
#'
#' roll1 = roll(die1, times = 10)
#' roll2 = roll(die1)
#'
#' summary(roll1)
#' plot(roll1)
#' roll1
roll = function(die, times = 1) {

  if (class(die) != "die") {
    stop("\nroll() requires an object 'die'")
  }

  check_time(times)
  set.seed(123)
  r = sample(die$sides, size = times, replace = TRUE, prob = die$prob)

  res = list(rolls = r, sides = die$sides, total = times, prob = die$prob )
  class(res) = 'roll'
  return(res)

}


#' @title check_time
#' @description check if given times (number of rolls) are valid
#' @param the number of rolls
#' @return boolean
check_time = function(times) {

  if (!is.numeric(times) | times %% 1 != 0 | times < 1) {
    stop("\n'times' must be a positive integer")
  }
  return(TRUE)
}

#' @export
print.roll = function(x) {
  cat('object "roll"\n\n')

  print(list(rolls = x$rolls))

  invisible(x)
}

#' @export
summary.roll = function(x) {

  count = table(x$rolls)
  prob = prop.table(count)

  freqs = data.frame(count, prob)
  freqs = freqs[, c(1,2,4)]
  colnames(freqs) = c('side', 'count', 'prob')

  res = list(freqs = freqs)
  class(res) = 'summary.roll'
  return(res)

}


#' @export
print.summary.roll = function(x) {

  cat('summary "roll"\n\n')
  print(x$freqs)
  invisible(x)

}

#' @export
plot.roll = function(x, times = length(x$rolls)) {
  if (times > length(x$rolls)) {
    stop("Times should be less than the total size of the rolls")
  }
  barplot(table(x$rolls[1:times]) / times, xlab = "sides of dice", ylab = "relative frequencies")
  title(sprintf("Frequencies in a series of %s die rolls", times))
}


#' @export
"[.roll" <- function(x, i) {
  x$rolls[i]
}


#' @export
"[<-.roll" <- function(x, i, value) {
  if (!(value %in% x$sides)) {
    stop("\nreplacing value must be in the sides of dice")
  }

  if (i > x$total) {
    stop("\nindex out of bounds")
  }

  temp = rep(0, length(x$rolls))
  temp = x$rolls
  temp[i] = value
  x$rolls = temp
  class(x) = 'roll'
  return(x)
}

#' @export
"+.roll" = function(roll, incr) {
  if (incr <= 0 | !is.numeric(incr)) {
    stop("\ninvalid increament (must be positive integer)")
  }

  sides = roll$sides
  total = roll$total
  prob = roll$prob
  rolls = roll$rolls

  set.seed(123)
  d = die(sides = sides, prob = prob)
  r = roll(d, total + incr)

  temp = r$rolls

  for(i in 1:length(rolls)) {
    temp[i] = rolls[i]
  }

  r$rolls = temp

  class(r) = 'roll'

  return(r)
}
