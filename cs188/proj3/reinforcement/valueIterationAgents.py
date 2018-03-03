# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


# valueIterationAgents.py
# -----------------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


import mdp, util

from learningAgents import ValueEstimationAgent
import collections

class ValueIterationAgent(ValueEstimationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A ValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100):
        """
          Your value iteration agent should take an mdp on
          construction, run the indicated number of iterations
          and then act according to the resulting policy.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state, action, nextState)
              mdp.isTerminal(state)
        """
        self.mdp = mdp
        self.discount = discount
        self.iterations = iterations
        self.values = util.Counter() # A Counter is a dict with default 0
        self.runValueIteration()

    def runValueIteration(self):
        # Write value iteration code here

        # Setting the 0th iteration to 0
        states = self.mdp.getStates()[1:]

        for _ in range(self.iterations):
            values = util.Counter()
            for state in states:
                bestAction = self.computeActionFromValues(state)
                values[state] = self.computeQValueFromValues(state, bestAction)

            self.values = values

            

    def getValue(self, state):
        """
          Return the value of the state (computed in __init__).
        """
        return self.values[state]


    def computeQValueFromValues(self, state, action):
        """
          Compute the Q-value of action in state from the
          value function stored in self.values.
        """
        # [((0, 1), 1.0), ((0, 0), 0.0), ((0, 2), 0.0)] = T
        # t[0] = state, t[1] = transition probability to t[0]
        T = self.mdp.getTransitionStatesAndProbs(state, action)

        return sum([t[1] * (self.mdp.getReward(state, action, t[0]) + self.discount * self.values[t[0]]) for t in T])


    def computeActionFromValues(self, state):
        """
          The policy is the best action in the given state
          according to the values currently stored in self.values.

          You may break ties any way you see fit.  Note that if
          there are no legal actions, which is the case at the
          terminal state, you should return None.
        """
        # should return an action that returns the maximum value from Qvalue
        actions = self.mdp.getPossibleActions(state)
        temp = -999999
        action = ''
        for a in actions:
          if self.computeQValueFromValues(state, a) > temp:
            temp = self.computeQValueFromValues(state, a)
            action = a

        return action

    def getPolicy(self, state):
        return self.computeActionFromValues(state)

    def getAction(self, state):
        "Returns the policy at the state (no exploration)."
        return self.computeActionFromValues(state)

    def getQValue(self, state, action):
        return self.computeQValueFromValues(state, action)

class AsynchronousValueIterationAgent(ValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        An AsynchronousValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs cyclic value iteration
        for a given number of iterations using the supplied
        discount factor.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 1000):
        """
          Your cyclic value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy. Each iteration
          updates the value of only one state, which cycles through
          the states list. If the chosen state is terminal, nothing
          happens in that iteration.

          Some useful mdp methods you will use:
              mdp.getStates()
              mdp.getPossibleActions(state)
              mdp.getTransitionStatesAndProbs(state, action)
              mdp.getReward(state)
              mdp.isTerminal(state)
        """
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        states = self.mdp.getStates()

        for i in range(self.iterations):
            state = states[i % len(states)]
            
            if self.mdp.isTerminal(state):
              continue

            bestAction = self.computeActionFromValues(state)
            self.values[state] = self.computeQValueFromValues(state, bestAction) 

class PrioritizedSweepingValueIterationAgent(AsynchronousValueIterationAgent):
    """
        * Please read learningAgents.py before reading this.*

        A PrioritizedSweepingValueIterationAgent takes a Markov decision process
        (see mdp.py) on initialization and runs prioritized sweeping value iteration
        for a given number of iterations using the supplied parameters.
    """
    def __init__(self, mdp, discount = 0.9, iterations = 100, theta = 1e-5):
        """
          Your prioritized sweeping value iteration agent should take an mdp on
          construction, run the indicated number of iterations,
          and then act according to the resulting policy.
        """
        self.theta = theta
        ValueIterationAgent.__init__(self, mdp, discount, iterations)

    def runValueIteration(self):
        # Compute predecessors of all states.
        predecessors = {}
        for state in self.mdp.getStates()[1:]:
          predecessors[state] = []

        for state in self.mdp.getStates()[1:]:
          for action in self.mdp.getPossibleActions(state):
            for nextState in self.mdp.getTransitionStatesAndProbs(state, action):

              if self.mdp.isTerminal(nextState[0]):
                continue
              if nextState[1] > 0:
                predecessors[nextState[0]].append(state)

        queue = util.PriorityQueue()

        diff = 0
        for state in self.mdp.getStates()[1:]:
          diff = abs(self.values[state] - self.computeQValueFromValues(state, self.computeActionFromValues(state)))
          queue.push(state, -diff)

        for i in range(self.iterations):
          if not queue.isEmpty():
            state = queue.pop()

            if self.mdp.isTerminal(state):
              continue

            bestAction = self.computeActionFromValues(state)
            self.values[state] = self.computeQValueFromValues(state, bestAction) 

            # Update s's value (if it is not a terminal state) in self.values.

            for predecessor in predecessors[state]:
              for action in self.mdp.getPossibleActions(predecessor):
                for nextState in self.mdp.getTransitionStatesAndProbs(predecessor, action):
                  if self.mdp.isTerminal(nextState):
                    continue
                  diff = 0
                  if nextState[1] > 0:
                    diff = abs(self.values[predecessor] - self.computeQValueFromValues(predecessor, self.computeActionFromValues(predecessor)))
                  if diff > self.theta:
                    queue.update(predecessor, -diff)