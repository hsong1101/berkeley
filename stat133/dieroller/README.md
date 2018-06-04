README
================
Hanmaro Song
April 23, 2018

Overview
--------

`"dieroller"` is a minimal [R](http://www.r-project.org/) package that provides functions to simulate rolling a die.

-   `die()` creates a die object (of class `"die"`)
-   `roll()` rolls a die object, producing a `"roll"` object.
-   `plot()` method for a `"roll"` object to plot frequencies of sides of a die.
-   `summary()` method for a `"roll"` object.

Motivation
----------

This package has been developed to illustrate some of the concepts behind the creation of an R package.

Installation
------------

Install the development version from GitHub via the package `"devtools"`:

``` r
# development version from GitHub:
#install.packages("devtools") 


# install "dieroller"
devtools::install_github("stat133-sp18/hw-stat133-hsong1101/tree/master/dieroller", build_vignettes = TRUE)
```

Usage
-----

``` r
library(dieroller)

# default die1
die1 = die()
die
## function(sides = c(1:6), prob = rep(1/6, 6)) {
## 
##   check_sides(sides)
##   check_prob(prob)
## 
##   res = list(sides = sides, prob = prob)
## 
##   class(res) = 'die'
##   return(res)
## }
## <environment: namespace:dieroller>

# default die2
die2 = die(sides = c('a', 'b', 'c', 'd', 'e', 'f'), prob = c(0.1, 0.2, 0.3, 0.2, 0.1, 0.1))
die2
## object "die"
## 
##   side prob
## 1    a  0.1
## 2    b  0.2
## 3    c  0.3
## 4    d  0.2
## 5    e  0.1
## 6    f  0.1

# default roll
roll1 = roll(die1)
roll2 = roll(die2, times = 10)

# default roll print
roll1
## object "roll"
## 
## $rolls
## [1] 2
roll2
## object "roll"
## 
## $rolls
##  [1] "e" "b" "e" "e" "a" "d" "b" "b" "f" "e"

# summary of roll
summary(roll2)
## summary "roll"
## 
##   side count prop
## 1    a     1  0.1
## 2    b     3  0.3
## 3    c     0  0.0
## 4    d     1  0.1
## 5    e     4  0.4
## 6    f     1  0.1

# barplot of roll
plot(roll2)
```

![](README-unnamed-chunk-1-1.png)

``` r

# addition of rolls
roll2 = roll2 + 100
summary(roll2)
## summary "roll"
## 
##   side count       prop
## 1    a    13 0.11818182
## 2    b    28 0.25454545
## 3    c    29 0.26363636
## 4    d    19 0.17272727
## 5    e    13 0.11818182
## 6    f     8 0.07272727
```
