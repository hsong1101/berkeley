def ab_plus_c(a, b, c):
    """Computes a * b + c.

    >>> ab_plus_c(2, 4, 3)  # 2 * 4 + 3
    11
    >>> ab_plus_c(0, 3, 2)  # 0 * 3 + 2
    2
    >>> ab_plus_c(3, 0, 2)  # 3 * 0 + 2
    2
    """
    if a == 0 or b == 0:
        return c
    elif a == 1:
        return b + c   
    else:
        return b + ab_plus_c(a-1, b, c)

def gcd(a, b):
    """Returns the greatest common divisor of a and b.
    Should be implemented using recursion.

    >>> gcd(34, 19)
    1
    >>> gcd(39, 91)
    13
    >>> gcd(20, 30)
    10
    >>> gcd(40, 40)
    40
    """
    c,d = max(a,b), min(a,b)

    def fn(c, d):

        if c % d == 0:
            return d
        elif c == 1:
            return 1
        else:
            return fn(d, c%d)


    return fn(c,d)

def hailstone(n):
    """Print out the hailstone sequence starting at n, and return the
    number of elements in the sequence.

    >>> a = hailstone(10)
    10
    5
    16
    8
    4
    2
    1
    >>> a
    7
    """

    def fn(n, count):

        if n == 1:
            print(n)
            return count + 1
        else:
            if n % 2 == 0:
                print(n)
                return fn(n//2, count+1)
            else:
                print(n)
                return fn(n*3+1, count+1)

    return fn(n, 0)
