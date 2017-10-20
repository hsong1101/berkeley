#!/bin/bash
# Bash Calculator Framework
# CS9E - Assignment 4.1

## Floating Point Math Functions ##

# bashcalc <expression>
# This function simply passes in the given expressions to 'bc -l' and prints the result

function bashcalc {
	echo $1 | bc -l	# ADD CODE HERE FOR PART 2
}

# Remove this line when you start part 3
return 0

# sine <expression>
# This function prints the cosine of the given expression
function sine {
# ADD CODE HERE FOR PART 3
}

# cosine <expression>
# This function prints the cosine of the given expression
function cosine {
	# ADD CODE HERE FOR PART 3
}

# angle_reduce <angle>
# Prints the angle given expressed as a value between 0 and 2pi
function angle_reduce {
	# ADD CODE HERE FOR PART 3
}

# float_{lt,lte,eq} <expr 1> <expr 2>
# These functions returns true (exit code 0) if the first value is less than the second (lt),
# less than or equal to the second (lte), or equal to the second (eq).
# Note: We can't just use BASH's builtin [[ ... < ... ]] operator because that is
#       for integer math.
function float_lt {
	# ADD CODE HERE FOR PART 3
}
function float_eq {
# ADD CODE HERE FOR PART 3
}

function float_lte {
#ADD CODE HERE FOR PART 3
}
