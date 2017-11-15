#!/bin/bash
# Bash Calculator Framework
# CS9E - Assignment 4.1

## Floating Point Math Functions ##

# bashcalc <expression>

function bashcalc {
	echo $1 | bc -l	# ADD CODE HERE FOR PART 2
}

function sine {
	bashcalc "s($1)"
}

function cosine {
	bashcalc "c($1)"
}

function angle_reduce {
	pi=$(bashcalc "8*a(1)")

	div=$(bashcalc "scale = 0; $1 / $pi")

	result=$(bashcalc "$1 - $div*$pi")

	if [ $(float_lte $result 0) -eq 1 ]
	then
		result=$(bashcalc "$result + $pi")

	fi

	return $result
}

function float_lt {
	result=$(bashcalc "$1 < $2")
	return $result
}
function float_eq {
	result=$(bashcalc "$1 == $2")
	return $result
}

function float_lte {
	result=$(bashcalc "$1<=$2")
	return $result
}
