#!/bin/bash
# Cat & Mouse Framework
# CS9E - Assignment 4.2
#
# Framework by Jeremy Huddleston <jeremyhu@cs.berkeley.edu>
# $LastChangedDate: 2007-10-11 15:49:54 -0700 (Thu, 11 Oct 2007) $
# $Id: catmouse-fw.sh 88 2007-10-11 22:49:54Z selfpace $

# Source the file containing your calculator functions:
. bashcalc-fw.sh

# Additional math functions:

# angle_between <A> <B> <C>
# Returns true (exit code 0) if angle B is between angles A and C and false otherwise
function angle_between {
	local A=$1
	local B=$2
	local C=$3

#	a=$(angle_reduce "$A")
#	b=$(angle_reduce "$B")
#	c=$(angle_reduce "$C")

	BtoA=$(bashcalc "$B - $A")
	CtoB=$(bashcalc "$C - $B")
	CtoA=$(bashcalc "$C - $A")
	

	if [[ $(cosine "$BtoA") > $(cosine "$CtoA") && $(cosine "$CtoB") > $(cosine "$CtoA") ]]
	then
		echo hooray
	fi
	return 0
	
	# ADD CODE HERE FOR PART 1
}

### Simulation Functions ###
# Variables for the state
RUNNING=0
GIVEUP=1
CAUGHT=2
return 0
# does_cat_see_mouse <cat angle> <cat radius> <mouse angle>
#
# Returns true (exit code 0) if the cat can see the mouse, false otherwise.
#
# The cat sees the mouse if
# (cat radius) * cos (cat angle - mouse angle)
# is at least 1.0.
function does_cat_see_mouse {
	local cat_angle=$1
	local cat_radius=$2
	local mouse_angle=$3

	# ADD CODE HERE FOR PART 1
}

# next_step <current state> <current step #> <cat angle> <cat radius> <mouse angle> <max steps>
# returns string output similar to the input, but for the next step:
# <state at next step> <next step #> <cat angle> <cat radius> <mouse angle> <max steps>
#
# exit code of this function (return value) should be the state at the next step.  This allows for easy
# integration into a while loop.
function next_step {
	local state=$1
	local -i step=$2
	local old_cat_angle=$3
	local old_cat_radius=$4
	local old_mouse_angle=$5
	local -i max_steps=$6

	local new_cat_angle=${old_cat_angle}
	local new_cat_radius=${old_cat_radius}
	local new_mouse_angle=${old_mouse_angle}

	# First, make sure we are still running
	if (( ${state} != ${RUNNING} )) ; then
		echo ${state} ${step} ${old_cat_angle} ${old_cat_radius} ${old_mouse_angle} ${max_steps}
		return ${state}
	fi

	# ADD CODE HERE FOR PART 2

	# Move the cat first
	if _______ ; then
		# Move the cat in if it's not at the statue and it can see the mouse
	else
		# Move the cat around if it's at the statue or it can't see the mouse
		# Check if the cat caught the mouse
	fi

	# Now move the mouse if it wasn't caught
	if ______ ; then
		# Move the mouse

		# Give up if we're at the last step and haven't caught the mouse
		if _______ ; then
		fi
	fi

	echo ${state} ${step} ${new_cat_angle} ${new_cat_radius} ${new_mouse_angle} ${max_steps}
	return ${state}
}

### Main Script ###

if [[ ${#} != 4 ]] ; then
	echo "$0: usage" >&2
	echo "$0 <cat angle> <cat radius> <mouse angle> <max steps>" >&2
	exit 1
fi

# ADD CODE HERE FOR PART 3
