class Link:
    """A linked list.

    >>> s = Link(1, Link(2, Link(3)))
    >>> s.first
    1
    >>> s.rest
    Link(2, Link(3))
    """
    empty = ()

    def __init__(self, first, rest=empty):
        assert rest is Link.empty or isinstance(rest, Link)
        self.first = first
        self.rest = rest

    def __repr__(self):
        if self.rest is Link.empty:
            return 'Link({})'.format(self.first)
        else:
            return 'Link({}, {})'.format(self.first, repr(self.rest))

def digits(n):
    """Return the digits of n as a linked list.

    >>> digits(0) is Link.empty
    True
    >>> digits(543)
    Link(5, Link(4, Link(3)))
    """
    s = Link.empty

    while n > 0:
        n, last = n // 10, n % 10

        if not s:
            s = Link(last)
        else:
            s = Link(last, s)


    return s

class Fib():
    """A Fibonacci number.

    >>> start = Fib()
    >>> start
    0
    >>> start.next()
    1
    >>> start.next().next()
    1
    >>> start.next().next().next()
    2
    >>> start.next().next().next().next()
    3
    >>> start.next().next().next().next().next()
    5
    >>> start.next().next().next().next().next().next()
    8
    """

    def __init__(self, value=0, prev=0, n=0):
        self.value = value
        self.prev = prev
        self.n = n

    def next(self):

        if self.n == 0:
            self.value = 1
        elif self.n == 1:
            self.prev = 0



        return Fib(self.value+self.prev, self.value, self.n+1)
        

    def __repr__(self):
        return str(self.value)

class VendingMachine:
    """A vending machine that vends some product for some price.

    >>> v = VendingMachine('candy', 10)
    >>> v.vend()
    'Machine is out of stock.'
    >>> v.deposit(15)
    'Machine is out of stock. Here is your $15.'
    >>> v.restock(2)
    'Current candy stock: 2'
    >>> v.vend()
    'You must deposit $10 more.'
    >>> v.deposit(7)
    'Current balance: $7'
    >>> v.vend()
    'You must deposit $3 more.'
    >>> v.deposit(5)
    'Current balance: $12'
    >>> v.vend()
    'Here is your candy and $2 change.'
    >>> v.deposit(10)
    'Current balance: $10'
    >>> v.vend()
    'Here is your candy.'
    >>> v.deposit(15)
    'Machine is out of stock. Here is your $15.'

    >>> w = VendingMachine('soda', 2)
    >>> w.restock(3)
    'Current soda stock: 3'
    >>> w.restock(3)
    'Current soda stock: 6'
    >>> w.deposit(2)
    'Current balance: $2'
    >>> w.vend()
    'Here is your soda.'
    """
    def __init__(self, name, price, stock=0, balance=0):
        self.name = name
        self.price = price
        self.stock = stock
        self.balance = balance

    def vend(self):
        if self.stock <= 0:
            return "Machine is out of stock."
        else:
            if self.balance < self.price:
                return "You must deposit ${} more.".format(self.price - self.balance)
            else:
                if self.price == self.balance:
                    self.balance = 0
                    self.stock -= 1
                    return "Here is your {}.".format(self.name)
                else:
                    temp = self.balance
                    self.balance = 0
                    self.stock -= 1
                    return "Here is your {} and ${} change.".format(self.name, temp - self.price)

    def deposit(self, money):
        if self.stock <= 0:
            return "Machine is out of stock. Here is your ${}.".format(money)
        else:
            self.balance += money
            return "Current balance: ${}".format(self.balance)

    def restock(self, stock):
        self.stock += stock
        return "Current {} stock: {}".format(self.name, self.stock)


class MissManners:
    """A container class that only forwards messages that say please.

    >>> v = VendingMachine('teaspoon', 10)
    >>> v.restock(2)
    'Current teaspoon stock: 2'

    >>> m = MissManners(v)
    >>> m.ask('vend')
    'You must learn to say please first.'
    >>> m.ask('please vend')
    'You must deposit $10 more.'
    >>> m.ask('please deposit', 20)
    'Current balance: $20'
    >>> m.ask('now will you vend?')
    'You must learn to say please first.'
    >>> m.ask('please hand over a teaspoon')
    'Thanks for asking, but I know not how to hand over a teaspoon.'
    >>> m.ask('please vend')
    'Here is your teaspoon and $10 change.'

    >>> double_fussy = MissManners(m) # Composed MissManners objects
    >>> double_fussy.ask('deposit', 10)
    'You must learn to say please first.'
    >>> double_fussy.ask('please deposit', 10)
    'Thanks for asking, but I know not how to deposit.'
    >>> double_fussy.ask('please please deposit', 10)
    'Thanks for asking, but I know not how to please deposit.'
    >>> double_fussy.ask('please ask', 'please deposit', 10)
    'Current balance: $10'
    """
    def __init__(self, obj):
        self.obj = obj

    def ask(self, message, *args):
        magic_word = 'please '
        if not message.startswith(magic_word):
            return 'You must learn to say please first.'

        command = message[7:]

        if hasattr(self.obj, command):
            if not args:
                return getattr(self.obj, command)()
            if len(args) == 1:
                return getattr(self.obj, command)(args[0])
            return getattr(self.obj, command)(args[0], args[1])
        else:
            return "Thanks for asking, but I know not how to {}.".format(command)
