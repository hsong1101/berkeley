# title: Functions for Binomial Distribution and its Probability
# description: Helper functions are created to make Binomial functions
# input: None
# output: Binomial functions

#` @ title: is_integer
#` @ description: check whether given param is integer or not
#` @ param: x
#` @ return logical, True if integer, False otherwise
is_integer = function(x) {
  
  x %% 1 == 0
}

#` @ title: is_positive
#` @ description: check whether given param is positive
#` @ param: x
#` @ return logical, True if positive, False otherwise
is_positive = function(x) {
  x > 0
}

#` @ title: is_nonnegative
#` @ description: check whether given param is non-negative
#` @ param: x
#` @ return logical, True if non-negative, False otherwise
is_nonnegative = function(x) {
  x >= 0
}

#` @ title: is_positive_integer
#` @ description: check whether given param is positive integer or not
#` @ param: x
#` @ return logical, True if positive integer, False otherwise
is_positive_integer = function(x) {
  is_integer(x) & is_positive(x)
}

#` @ title: is_nonneg_integer
#` @ description: check whether given param is non-negative integer or not
#` @ param: x
#` @ return logical, True if non-negative integer, False otherwise
is_nonneg_integer = function(x) {
  is_integer(x) & is_nonnegative(x)
}

#` @ title: is_probability
#` @ description: check whether given param is between 0 and 1
#` @ param: x
#` @ return logical, True if probability, False otherwise
is_probability = function(x) {
  0 <= x & x <= 1
}

#` @ title: bin_factorial
#` @ description: the product of 1 to x (factorial of x)
#` @ param: x
#` @ return the product of 1 to x
bin_factorial = function(x) {
  
  if (x == 0) {
    1
  } else {
    temp = 1
    
    for(i in 1:x) {
      temp = temp * i
    }
    temp
  }
}

#` @ title: bin_combination
#` @ description: function works as n chooses k. Computes the combination of given n out of k
#` @ param: n, k
#` @ return the number of combination
bin_combination = function(n, k) {
  bin_factorial(n)/(bin_factorial(k) * bin_factorial(n-k))
}

#` @ title: bin_probability
#` @ description: computes the probability of n happening out of k with probability p
#` @ param: n, k, p
#` @ return probability of event(s) happening
bin_probability = function(n, k, p) {
  if(!is_nonneg_integer(n) | !is_nonneg_integer(k)) {
    stop("Input value error. Put nonnegative integers")
  } else if(!is_probability(p)) {
    stop("Input value error. Put proper probability. 0 <= x <= 1")
  } else {
    bin_combination(n, k) * p^k * (1 - p)^(n-k)
  }
}

#` @ title: bin_distribution
#` @ description: creates a data frame that contains distribution of probability from 1 to n
#` @ param: n, p
#` @ return data frame of trials and its probability
bin_distribution = function(n, p) {
  success = c(0:n)
  probability = rep(0, n+1)
  #` loop n times (n trials)
  for(i in success) {
    probability[i+1] = bin_probability(n, i, p)
  }
  data.frame(success, probability)
}




















