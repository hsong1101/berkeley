hw03
================
Hanmaro Song
March 15, 2018

Q 2.1

    cat nba* | cut -d ',' -f 2 | sort | uniq | grep -v "team" > team-names.txt | head -5 team*
    "ATL"
    "BOS"
    "BRK"
    "CHI"
    "CHO"

Q 2.2

    cat nba* | cut -d ',' -f 3 | sort | uniq | grep -v "position" > position-names.txt | head -5 pos*
    "C"
    "PF"
    "PG"
    "SF"
    "SG"

Q 2.3

    cat nba* | cut -d ',' -f 7 | grep -v "experience" | sort | uniq -c | sort -r > experience-count.txt | head -5 exp*
    80 0
    52 1
    46 2
    36 3
    35 4

Q 2.4

    cat nba* | grep -E "LAC|team" > LAC.csv && cat LAC*
    "player","team","position","height","weight","age","experience","salary"
    "Alan Anderson","LAC","SF",78,220,34,7,1315448
    "Austin Rivers","LAC","SG",76,200,24,4,1.1e+07
    "Blake Griffin","LAC","PF",82,251,27,6,20140838
    "Brandon Bass","LAC","PF",80,250,31,11,1551659
    "Brice Johnson","LAC","PF",82,230,22,0,1273920
    "Chris Paul","LAC","PG",72,175,31,11,22868828
    "DeAndre Jordan","LAC","C",83,265,28,8,21165675
    "Diamond Stone","LAC","C",83,255,19,0,543471
    "J.J. Redick","LAC","SG",76,190,32,10,7377500
    "Jamal Crawford","LAC","SG",77,200,36,16,13253012
    "Luc Mbah a Moute","LAC","SF",80,230,30,8,2203000
    "Marreese Speights","LAC","C",82,255,29,8,1403611
    "Paul Pierce","LAC","SF",79,235,39,18,3500000
    "Raymond Felton","LAC","PG",73,205,32,11,1551659
    "Wesley Johnson","LAC","SF",79,215,29,6,5628000

Q 2.5

    cat nba* | grep "LAL" | cut -d ',' -f 6 | sort | uniq -c
    2 19
    1 20
    2 22
    3 24
    2 25
    2 30
    2 31
    1 37

Q 2.6

    cat nba* | grep "CLE" | wc -l
    15

Q 2.7

    cat nba* | grep -E "GSW|player" | cut -d ',' -f 1,4,5 > gsw-height-weight.csv | cat gsw*
    "player","height","weight"
    "Andre Iguodala",78,215
    "Damian Jones",84,245
    "David West",81,250
    "Draymond Green",79,230
    "Ian Clark",75,175
    "James Michael McAdoo",81,230
    "JaVale McGee",84,270
    "Kevin Durant",81,240
    "Kevon Looney",81,220
    "Klay Thompson",79,215
    "Matt Barnes",79,226
    "Patrick McCaw",79,185
    "Shaun Livingston",79,192
    "Stephen Curry",75,190
    "Zaza Pachulia",83,270

Q 2.8

    cat nba* | cut -d ',' -f 1,8 | sort -t ',' -k2 -nr | head -10 > top10-salaries.csv | echo -e "\"player\",\"salary\"\n$(cat top10-salaries.csv)" > top10-salaries.csv && cat Ftop*
    "player","salary"
    "LeBron James",30963450
    "Russell Westbrook",26540100
    "Mike Conley",26540100
    "Kevin Durant",26540100
    "James Harden",26540100
    "DeMar DeRozan",26540100
    "Al Horford",26540100
    "Carmelo Anthony",24559380
    "Damian Lillard",24328425
    "Dwyane Wade",23200000

``` r
source("../code/binomial-functions.R")
library(ggplot2)
```

``` r
# test all helper functions
is_positive(0.01)
```

    ## [1] TRUE

``` r
is_positive(2)
```

    ## [1] TRUE

``` r
is_positive(-2)
```

    ## [1] FALSE

``` r
is_positive(0)
```

    ## [1] FALSE

``` r
is_nonnegative(0)
```

    ## [1] TRUE

``` r
is_nonnegative(2)
```

    ## [1] TRUE

``` r
is_nonnegative(-0.00001)
```

    ## [1] FALSE

``` r
is_nonnegative(-2)
```

    ## [1] FALSE

``` r
is_positive_integer(2)
```

    ## [1] TRUE

``` r
is_positive_integer(2L)
```

    ## [1] TRUE

``` r
is_positive_integer(0)
```

    ## [1] FALSE

``` r
is_positive_integer(-2)
```

    ## [1] FALSE

``` r
is_nonneg_integer(0)
```

    ## [1] TRUE

``` r
is_nonneg_integer(1)
```

    ## [1] TRUE

``` r
is_nonneg_integer(-1)
```

    ## [1] FALSE

``` r
is_nonneg_integer(-2.5)
```

    ## [1] FALSE

``` r
is_probability(0)
```

    ## [1] TRUE

``` r
is_probability(0.5)
```

    ## [1] TRUE

``` r
is_probability(1)
```

    ## [1] TRUE

``` r
is_probability(-1)
```

    ## [1] FALSE

``` r
is_probability(1.000001)
```

    ## [1] FALSE

``` r
bin_factorial(0)
```

    ## [1] 1

``` r
bin_factorial(5)
```

    ## [1] 120

``` r
bin_combination(5, 2)
```

    ## [1] 10

``` r
bin_combination(4, 4)
```

    ## [1] 1

``` r
bin_probability(5, 2, 0.5)
```

    ## [1] 0.3125

``` r
bin_distribution(5, 0.5)
```

    ##   success probability
    ## 1       0     0.03125
    ## 2       1     0.15625
    ## 3       2     0.31250
    ## 4       3     0.31250
    ## 5       4     0.15625
    ## 6       5     0.03125

``` r
# rolling a fair dice 10 times and get 3 six-dotted side.
bin_probability(10, 3, 1/6)
```

    ## [1] 0.1550454

``` r
# plot for the distribution of sixes with 10 rolls and probability of 0.25

dat = bin_distribution(10, 0.25)
dat
```

    ##    success  probability
    ## 1        0 5.631351e-02
    ## 2        1 1.877117e-01
    ## 3        2 2.815676e-01
    ## 4        3 2.502823e-01
    ## 5        4 1.459980e-01
    ## 6        5 5.839920e-02
    ## 7        6 1.622200e-02
    ## 8        7 3.089905e-03
    ## 9        8 3.862381e-04
    ## 10       9 2.861023e-05
    ## 11      10 9.536743e-07

``` r
plt = ggplot(dat, aes(x=success, y=probability)) + geom_point()
plt
```

![](../images/plot_sixes_distribution-1.png)

``` r
# 3 head in 5 tosses of a biased coin with p = .35

prob = 0

for (i in 4:5) {
  prob = prob + bin_probability(5, i, 0.35)
}

# or
# for (i in 1:3) {
#  prob = prob + bin_probability(5, i, 0.35)
# }
# 1-prob

prob
```

    ## [1] 0.0540225

``` r
# bin_distribution with .35% of heads out of 15 tosses
dist = bin_distribution(15, .35)
dist
```

    ##    success  probability
    ## 1        0 1.562069e-03
    ## 2        1 1.261672e-02
    ## 3        2 4.755531e-02
    ## 4        3 1.109624e-01
    ## 5        4 1.792469e-01
    ## 6        5 2.123387e-01
    ## 7        6 1.905604e-01
    ## 8        7 1.319264e-01
    ## 9        8 7.103729e-02
    ## 10       9 2.975066e-02
    ## 11      10 9.611752e-03
    ## 12      11 2.352527e-03
    ## 13      12 4.222484e-04
    ## 14      13 5.246873e-05
    ## 15      14 4.036056e-06
    ## 16      15 1.448841e-07
