# load the source code of the functions to be tested
source("functions.R")

# context with one test that groups expectations
context("Test for range value") 

test_that("range works as expected", {
  x <- c(1, 2, 3, 4, 5)
  y <- c(1, 2, 3, 4, NA)
  z <- c(TRUE, FALSE, TRUE)
  w <- letters[1:5]

  
  expect_equal(stat_range(x), 4)
  expect_length(stat_range(x), 1)
  expect_type(stat_range(x), 'double')

  expect_length(stat_range(y), 1)
  expect_equal(stat_range(y), NA_real_)
  
  expect_length(stat_range(z), 1)
  expect_type(stat_range(z), 'integer')
  expect_equal(stat_range(z), 1L)
  
  expect_error(stat_range(w), 'non-numeric argument to binary operator')
  
  
})

