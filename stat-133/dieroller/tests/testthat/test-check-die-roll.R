context("Check die and roll arguments")


# test cases auxilary functions

test_that("check_sides with ok vectors", {

  expect_true(check_sides(c(1:6)))
  expect_true(check_sides(c('a', 'b', 'c', 'd', 'e', 'f')))
  expect_true(check_sides(c(1, 2, 3, 'f', 'e', 'P')))
})


test_that("check_sides fails with invalid lengths", {

  expect_error(check_sides(c('one', 'two', 'three')))
  expect_error(check_sides(c('one')))
  expect_error(check_sides(1:5))
  expect_error(check_sides(1))
})

test_that("check_time with ok value", {

  expect_true(check_time(1))
  expect_true(check_time(2))
  expect_true(check_time(1000))
  expect_true(check_time(2187))
})

test_that("check_time with invalid value", {

  expect_error(check_time(0.1))
  expect_error(check_time(-2))
  expect_error(check_time(3.9))
  expect_error(check_time(0.0001))
})


test_that("check_prob works with ok vectors", {

  expect_true(check_prob(c(0.1, 0.2, 0.1, 0.2, 0.1, 0.3)))
  expect_true(check_prob(c(0, 0, 0, 0, 0, 1)))
  expect_true(check_prob(c(0.5, 0.1, 0.1, 0.1, 0.1, 0.1)))
})


test_that("check_prob fails with invalid lengths", {

  expect_error(check_prob(1:5))
  expect_error(check_prob(1))
})


test_that("check_prob fails with invalid numbers", {

  expect_error(check_prob(0.333, 0.666))
  expect_error(check_prob(-0.5, 0.5))
  expect_error(check_prob(0.5, -0.5))
  expect_error(check_prob(0.5, NA))
})



# test cases of die and roll object

test_that("check die object with invalid parameter", {
  expect_error(die(sides = c(1, 2, 3)))
  expect_error(die(sides = c('a', 'b', 'c', 'd', 'e')))
  expect_error(die(prob = c(0.1, 0.2, 0.3, 0.4, 0.5)))
  expect_error(die(prob = c(0.1, 0.5, 0.4)))
})

test_that("check die object with valid parameter", {
  expect_equal(class(die()), 'die')
  expect_equal(class(die(sides = c(5:10))), 'die')
  expect_equal(class(die(prob = c(0.1, 0.1, 0.1, 0.1, 0.1, 0.5))), 'die')
})

test_that("check roll object with invalid parameter", {
  d = die()
  expect_error(roll(d, -1))
  expect_error(roll(d, 0))
  expect_error(roll(1, 1))
  expect_error(roll('d', 3))
  expect_error(roll(roll(d)))
})

test_that("check roll object with valid parameter", {
  d = die()
  expect_equal(class(roll(d)), 'roll')
  expect_equal(class(roll(d, times = 2)), 'roll')
  expect_equal(class(roll(d, times = 111)), 'roll')
})
