#!/bin/bash
# Cat & Mouse Framework
# CS9E - Assignment 4.2
#
# Framework by Jeremy Huddleston <jeremyhu@cs.berkeley.edu>
# $LastChangedDate: 2007-10-11 15:49:54 -0700 (Thu, 11 Oct 2007) $
# $Id: catmouse-fw.sh 88 2007-10-11 22:49:54Z selfpace $

# Source the file containing your calculator functions:
. calc.sh

# Additional math functions:

# angle_between <A> <B> <C>
# Returns true (exit code 0) if angle B is between angles A and C and false otherwise
function angle_between {
	local A=$1
	local B=$2
	local C=$3
	
	result=$(bashcalc "c($B - $A) > c($C - $A) && c($C - $B) > c($C - $A)" == 1 )
	return $result
	# ADD CODE HERE FOR PART 1
}
### Simulation Functions ###
# Variables for the state
RUNNING=0
GIVEUP=1
CAUGHT=2
# this is just to test
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
	

	if $(float_lte 1 $(bashcalc "$cat_radius * $(cosine "$cat_angle - $mouse_angle")")); then
        return 0
    else
        return 1
    fi

	# ADD CODE HERE FOR PART 1
}

function caught {
        local A=$1
        local B=$2
        local C=$3
        local cat_radius=$4
        result=$(angle_between $A $B $C && $cat_radius == 1)
        return $result
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

	if $(does_cat_see_mouse $old_cat_angle $old_cat_radius $old_mouse_angle) ; then
                # Move the cat in if it's not at the statue and it can see the mouse
                new_cat_radius=$(echo "scale=2; $new_cat_radius - 1" | bc -l)
                if $(float_lt "$new_cat_radius" 1) ; then
                        new_cat_radius=1
                fi
        else
                # Move the cat around if it's at the statue or it can't see the mouse
                # Check if the cat caught the mouse
                new_cat_angle=$(echo "scale=2; $(angle_reduce $(bashcalc "$old_cat_angle + $(bashcalc "1.25 / $old_cat_radius")"))" | bc -l)
                if $(float_eq $new_cat_radius 1) && $(angle_between $old_cat_angle $old_mouse_angle $new_cat_angle) ; then
                        echo $CAUGHT $step $new_cat_angle $new_cat_radius $new_mouse_angle $max_steps
                        return $CAUGHT
                fi
        fi

        # Now move the mouse if it wasn't caught
        if : ; then
                # Move the mouse
                new_mouse_angle=$(echo "scale=2; $(angle_reduce $(bashcalc "$old_mouse_angle + 1"))" | bc -l)
                # Give up if we're at the last step and haven't caught the mouse
                if [ $step -eq $max_steps ] ; then
                        echo $GIVEUP $step $new_cat_angle $new_cat_radius $new_mouse_angle $max_steps
                        return $GIVEUP
                fi
        fi

        CATANGLE=$new_cat_angle
        CATRADIUS=$new_cat_radius
        MOUSEANGLE=$new_mouse_angle

        echo ${state} ${step} ${new_cat_angle} ${new_cat_radius} ${new_mouse_angle} ${max_steps}
        return ${state}


}

if [[ ${#} != 4 ]] ; then
        echo "$0: usage" >&2
        echo "$0 <cat angle> <cat radius> <mouse angle> <max steps>" >&2
        exit 1
elif [[ $2 < 1 || $4 < 1 ]] ; then
        echo "invalid input"
        exit 1

fi

# ADD CODE HERE FOR PART 3

CURSTATE=$RUNNING
CURSTEP=1

CATANGLE="$1"
CATRADIUS="$2"
MOUSEANGLE="$3"

while next_step "$CURSTATE" "$CURSTEP" "$CATANGLE" "$CATRADIUS" "$MOUSEANGLE" "$4" ; do
        let "CURSTEP += 1"
done

