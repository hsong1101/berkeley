## Generators

def make_generators_generator(g):
    """Generates all the "sub"-generators of the generator returned by
    the generator function g.

    >>> def ints_to(n):
    ...     for i in range(1, n + 1):
    ...          yield i
    ...
    >>> def ints_to_5():
    ...     for item in ints_to(5):
    ...         yield item
    ...
    >>> for gen in make_generators_generator(ints_to_5):
    ...     print("Next Generator:")
    ...     for item in gen:
    ...         print(item)
    ...
    Next Generator:
    1
    Next Generator:
    1
    2
    Next Generator:
    1
    2
    3
    Next Generator:
    1
    2
    3
    4
    Next Generator:
    1
    2
    3
    4
    5
    """
    for x in g:
        yield x

def permutations(lst1):
    """Generates all permutations of sequence LST. Each permutation is a
    list of the elements in LST in a different order.

    The order of the permutations does not matter.

    >>> sorted(permutations([1, 2, 3]))
    [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]
    >>> type(permutations([1, 2, 3]))
    <class 'generator'>
    >>> sorted(permutations((10, 20, 30)))
    [[10, 20, 30], [10, 30, 20], [20, 10, 30], [20, 30, 10], [30, 10, 20], [30, 20, 10]]
    >>> sorted(permutations("ab"))
    [['a', 'b'], ['b', 'a']]
    """

    lst1 = list(lst1)

    def permutation(lst):

        if len(lst) == 0:
            return []
     
        # If there is only one element in lst then, only
        # one permuatation is possible
        if len(lst) == 1:
            return [lst]
     
        # Find the permutations for lst if there are
        # more than 1 characters
     
        l = [] # empty list that will store current permutation
     
        # Iterate the input(lst) and calculate the permutation
        for i in range(len(lst)):
           m = lst[i]
     
           # Extract lst[i] or m from the list.  remLst is
           # remaining list
           remLst = lst[:i] + lst[i+1:]
     
           # Generating all permutations where m is first
           # element
           for p in permutations(remLst):
               l.append([m] + p)

        return l

    for x in permutation(lst1):
        yield x
