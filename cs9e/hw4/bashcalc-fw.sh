#!/bin/bash
# Bash Calculator Framework
# CS9E - Assignment 4.1

## Floating Point Math Functions ##

# bashcalc <expression>

function bashcalc {
	echo $1 | bc -l	# ADD CODE HERE FOR PART 2
}

function sine {
	echo "s($1)" | bc -l
}

function cosine {
	echo "c($1)" | bc -l	# ADD CODE HERE FOR PART 3
}

function angle_reduce {
	rem=$(echo "$1 % 1" | bc)
	result=$(echo "8*a($rem)" | bc -l)
	echo $result
}

function float_lt {
	result=$(echo "$1 < $2" | bc -l)
	return $result
}
function float_eq {
	result=$(echo "$1==$2" | bc -l)
	return $result
}

function float_lte {
	result=$(echo "$1<=$2" | bc -l)
	return $result
}
