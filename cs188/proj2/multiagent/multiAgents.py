# multiAgents.py
# --------------
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
  
from __future__ import division
from util import manhattanDistance
from game import Directions

import random, util
from util import *

from game import Agent

class ReflexAgent(Agent):
    """
      A reflex agent chooses an action at each choice point by examining
      its alternatives via a state evaluation function.

      The code below is provided as a guide.  You are welcome to change
      it in any way you see fit, so long as you don't touch our method
      headers.
    """


    def getAction(self, gameState):

        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {North, South, West, East, Stop}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()
        # print legalMoves
        # print self.evaluationFunction(gameState, legalMoves[0])

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        currPos = currentGameState.getPacmanPosition()
        currFood = currentGameState.getFood().asList()
        newPos = successorGameState.getPacmanPosition() #tuple
        newFood = successorGameState.getFood() # 2 Dim array of boolean
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]
        walls = successorGameState.getWalls().asList()
        ghostPos = successorGameState.getGhostPositions()[0]
        foodList = newFood.asList()
        # grid is 5 X 10 where the border is wall.
        # successorGameState = Layout of the current state
        # newPos = current position
        # newGhostStates[0] = position and facing direction
        closest = closestFood(newPos, foodList)
        farthest = farthestFood(newPos, foodList)

        if ghostAdjacent(ghostPos, newPos):
          return 0

        if newPos in currFood:
          return 50
        else:
          return 1/closest

def ghostAdjacent(ghostPos, newPos):
    gx, gy = ghostPos
    x, y = newPos

    if ghostPos == newPos or x == gx and y+1 == gy or x == gx and y-1 == gy or x+1 == gx and y == gy or x-1 == gx and y == gy:
      return True
    else:
      return False

def farthestFood(pos, foodList):
    temp = 0
    for x in foodList:
      if manhattanDistance(pos, x) > temp:
        temp = manhattanDistance(pos, x)

    return temp


def closestFood(pos, foodList):
    temp = 999999
    for x in foodList:
      if manhattanDistance(pos, x) < temp:
        temp = manhattanDistance(pos, x)
    
    return temp

def scoreEvaluationFunction(currentGameState):
    """
      This default evaluation function just returns the score of the state.
      The score is the same one displayed in the Pacman GUI.

      This evaluation function is meant for use with adversarial search agents
      (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
      This class provides some common elements to all of your
      multi-agent searchers.  Any methods defined here will be available
      to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

      You *do not* need to make any changes here, but you can if you want to
      add functionality to all your adversarial search agents.  Please do not
      remove anything, however.

      Note: this is an abstract class: one that should not be instantiated.  It's
      only partially specified, and designed to be extended.  Agent (game.py)
      is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
      Your minimax agent (question 2)
    """

    def getAction(self, gs):
        """
          Returns the minimax action from the current gameState using self.depth
          and self.evaluationFunction.

          Here are some method calls that might be useful when implementing minimax.

          gameState.getLegalActions(agentIndex):
            Returns a list of legal actions for an agent
            agentIndex=0 means Pacman, ghosts are >= 1

          gameState.generateSuccessor(agentIndex, action):
            Returns the successor game state after an agent takes an action

          gameState.getNumAgents():
            Returns the total number of agents in the game

          gameState.isWin():
            Returns whether or not the game state is a winning state

          gameState.isLose():
            Returns whether or not the game state is a losing state
        """
        # print gameState
        # eval fn called at leaf node or win or lose state
        agents = gs.getNumAgents()
        e = self.evaluationFunction
        m = self.depth * agents
        d = self.depth
        

        def player(c, a):
          return c % a

        def returnMax(lst):
          temp = lst[0]
          for x in lst[1:]:
            if x[0] > temp[0]:
              temp = x
          return temp

        def returnMin(lst):
          temp = lst[0]
          for x in lst[1:]:
            if x[0] < temp[0]:
              temp = x
          return temp

        def returnValue(state, curr, m):
          # at every recursion, curr + 1
          p = player(curr, agents)
          values = []
          if curr == m:
            # Arrived at the bottom of the expanded tree. Need to return e(children)
            return [e(state)]
          else:
            successors = []
            for action in state.getLegalActions(p):
              successors.append([state.generateSuccessor(p, action), action])
            if len(successors) == 0:
              # if no successors with curr < d -> given d is bigger than d of tree
              # print e(state)
              return [e(state)]
            else:
              for successor in successors:
                values.append([returnValue(successor[0], curr + 1, m), successor[1]])
              # print values
              if p:
                return returnMin(values)
              else:
                return returnMax(values)


        return returnValue(gs, 0, m)[1]


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
      Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gs):
        """
          Returns the minimax action using self.depth and self.evaluationFunction
        """
        agents = gs.getNumAgents()
        e = self.evaluationFunction
        m = self.depth * agents

        def player(c, a):
          if c < 1:
            return 0
          return c % a 

        # print gs.getLegalActions(8)

        def returnValue(state, level, alpha, beta):
          # at every recursion, level + 1
          p = player(level, agents)
          # print state.isWin(), state.isLose()

          if level == m:
            return [e(state)]

          elif state.isWin() or state.isLose():
            return [e(state)]

          else:

            v = 0
            action = 0
            # pacman's turn
            if not p:
              # print 'pacman'
              v = -999999
              for a in state.getLegalActions(p):

                temp = returnValue(state.generateSuccessor(p, a), level + 1, alpha, beta)
                # print 'pacman temp', temp
                if v < temp[0]:
                  v = temp[0]
                  action = a

                if alpha < v:
                  alpha = v
                # print 'pacman v, alpha, beta', v, alpha, beta
                if alpha > beta:
                  break
            # ghosts' turn
            else:
              # print 'ghost'
              v = 999999

              for a in state.getLegalActions(p):

                temp = returnValue(state.generateSuccessor(p, a), level + 1, alpha, beta)
                # print 'ghost temp', temp
                if v > temp[0]:
                  v = temp[0]
                  action = a

                if beta > v:
                  beta = v

                # print 'ghost v, alpha, beta', v, alpha, beta
                if alpha > beta:
                  break

            return [v, action]


        temp = returnValue(gs, 0, -999999, 999999)
        # print temp
        return temp[1]

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """
    def getAction(self, gs):
        """
          Returns the expectimax action using self.depth and self.evaluationFunction

          All ghosts should be modeled as choosing uniformly at random from their
          legal moves.
        """
        agents = gs.getNumAgents()
        e = self.evaluationFunction
        m = self.depth * agents
        d = self.depth
        

        def player(c, a):
          return c % a

        def returnMax(lst):
          temp = lst[0]
          for x in lst[1:]:
            if x[0] > temp[0]:
              temp = x
          return temp

        def returnMin(lst):
          temp = lst[0]
          for x in lst[1:]:
            if x[0] < temp[0]:
              temp = x
          return temp

        def returnMean(lst):
          length = len(lst)
          temp = 0
          for x in lst:
            while type(x) is list:
              x = x[0]
            temp += x

          # print temp, length
          return temp / length
          # return [temp/len(lst)]

        def returnValue(state, curr, m):
          # at every recursion, curr + 1
          p = player(curr, agents)
          values = []
          # bottom level
          if curr == m:
            return e(state)
          else:
            successors = []
            for action in state.getLegalActions(p):
              successors.append([state.generateSuccessor(p, action), action])
            if len(successors) == 0:
              # if no children exist
              return e(state)
            else:
              for successor in successors:
                values.append([returnValue(successor[0], curr + 1, m), successor[1]])

              if p:
                return returnMean(values)
              else:
                return returnMax(values)


        return returnValue(gs, 0, m)[1]

def betterEvaluationFunction(currentGameState):
    """
      Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
      evaluation function (question 5).

      DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction

