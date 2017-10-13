from math import pi

def pi_fraction(gap):
    """Print the fraction within gap of pi that has the smallest denominator.

    >>> pi_fraction(0.01)
    22 / 7 = 3.142857142857143
    >>> pi_fraction(1)
    3 / 1 = 3.0
    >>> pi_fraction(1/8)
    13 / 4 = 3.25
    >>> pi_fraction(1e-6)
    355 / 113 = 3.1415929203539825

    """
    numerator, denominator = 3, 1
    "*** YOUR CODE HERE ***"
    print(numerator, '/', denominator, '=', numerator/denominator)