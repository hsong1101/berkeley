def hailstone(n):

    length = 1

    print(int(n))

    while n != 1:

        if n % 2 == 0:
            n /= 2
            print(int(n))
            length += 1

        else:
            n = n * 3 + 1
            print(int(n))
            length += 1

    return length

    """Print the hailstone sequence starting at n and return its
    length.

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

print(hailstone(50))