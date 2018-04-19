# factorOperations.py
# -------------------
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


from bayesNet import Factor
import operator as op
import util

def joinFactorsByVariableWithCallTracking(callTrackingList=None):


    def joinFactorsByVariable(factors, joinVariable):
        """
        Input factors is a list of factors.
        Input joinVariable is the variable to join on.

        This function performs a check that the variable that is being joined on 
        appears as an unconditioned variable in only one of the input factors.

        Then, it calls your joinFactors on all of the factors in factors that 
        contain that variable.

        Returns a tuple of 
        (factors not joined, resulting factor from joinFactors)
        """

        if not (callTrackingList is None):
            callTrackingList.append(('join', joinVariable))

        currentFactorsToJoin =    [factor for factor in factors if joinVariable in factor.variablesSet()]
        currentFactorsNotToJoin = [factor for factor in factors if joinVariable not in factor.variablesSet()]

        # typecheck portion
        numVariableOnLeft = len([factor for factor in currentFactorsToJoin if joinVariable in factor.unconditionedVariables()])
        if numVariableOnLeft > 1:
            print "Factor failed joinFactorsByVariable typecheck: ", factor
            raise ValueError, ("The joinBy variable can only appear in one factor as an \nunconditioned variable. \n" +  
                               "joinVariable: " + str(joinVariable) + "\n" +
                               ", ".join(map(str, [factor.unconditionedVariables() for factor in currentFactorsToJoin])))
        
        joinedFactor = joinFactors(currentFactorsToJoin)
        return currentFactorsNotToJoin, joinedFactor

    return joinFactorsByVariable

joinFactorsByVariable = joinFactorsByVariableWithCallTracking()


def joinFactors(factors):
    """
    Question 3: Your join implementation 

    Input factors is a list of factors.  
    
    You should calculate the set of unconditioned variables and conditioned 
    variables for the join of those factors.

    Return a new factor that has those variables and whose probability entries 
    are product of the corresponding rows of the input factors.

    You may assume that the variableDomainsDict for all the input 
    factors are the same, since they come from the same BayesNet.

    joinFactors will only allow unconditionedVariables to appear in 
    one input factor (so their join is well defined).

    Hint: Factor methods that take an assignmentDict as input 
    (such as getProbability and setProbability) can handle 
    assignmentDicts that assign more variables than are in that factor.

    Useful functions:
    Factor.getAllPossibleAssignmentDicts
    Factor.getProbability
    Factor.setProbability
    Factor.unconditionedVariables
    Factor.conditionedVariables
    Factor.variableDomainsDict
    """

    # typecheck portion
    unconditioned = [set(factor.unconditionedVariables()) for factor in factors]
    if len(factors) > 1:
        intersect = reduce(lambda x, y: x & y, unconditioned)
        if len(intersect) > 0:
            print "Factor failed joinFactors typecheck: ", factor
            raise ValueError, ("unconditionedVariables can only appear in one factor. \n"
                    + "unconditionedVariables: " + str(intersect) + 
                    "\nappear in more than one input factor.\n" + 
                    "Input factors: \n" +
                    "\n".join(map(str, factors)))

    conditioned = [factor.conditionedVariables() for factor in factors]

    uncond = set([])
    cond = set([])
    assignments = {}

    for factor in factors:
        uncond.update(factor.unconditionedVariables())
        cond.update(factor.conditionedVariables())
        assignments.update(factor.variableDomainsDict())
        # remove vars in unconditioned
        cond.difference_update(uncond)

    result = Factor(uncond, cond, assignments)
    # initialize prob to 1 to multiply with other non-zero probabilities
    for assignment in result.getAllPossibleAssignmentDicts():
        result.setProbability(assignment, 1)

    for factor in factors:
        for res_assignment in result.getAllPossibleAssignmentDicts():
            for fac_assignment in factor.getAllPossibleAssignmentDicts():
                # check if factor's conditions are in the result factor
                if all(item in res_assignment.items() for item in fac_assignment.items()):
                    result.setProbability(res_assignment, result.getProbability(res_assignment)*factor.getProbability(fac_assignment))


    return result

def eliminateWithCallTracking(callTrackingList=None):

    def eliminate(factor, eliminationVariable):
        """
        Question 4: Your eliminate implementation 

        Input factor is a single factor.
        Input eliminationVariable is the variable to eliminate from factor.
        eliminationVariable must be an unconditioned variable in factor.
        
        You should calculate the set of unconditioned variables and conditioned 
        variables for the factor obtained by eliminating the variable
        eliminationVariable.

        Return a new factor where all of the rows mentioning
        eliminationVariable are summed with rows that match
        assignments on the other variables.

        Useful functions:
        Factor.getAllPossibleAssignmentDicts
        Factor.getProbability
        Factor.setProbability
        Factor.unconditionedVariables
        Factor.conditionedVariables
        Factor.variableDomainsDict
        """
        # autograder tracking -- don't remove
        if not (callTrackingList is None):
            callTrackingList.append(('eliminate', eliminationVariable))

        # typecheck portion
        if eliminationVariable not in factor.unconditionedVariables():
            print "Factor failed eliminate typecheck: ", factor
            raise ValueError, ("Elimination variable is not an unconditioned variable " \
                            + "in this factor\n" + 
                            "eliminationVariable: " + str(eliminationVariable) + \
                            "\nunconditionedVariables:" + str(factor.unconditionedVariables()))
        
        if len(factor.unconditionedVariables()) == 1:
            print "Factor failed eliminate typecheck: ", factor
            raise ValueError, ("Factor has only one unconditioned variable, so you " \
                    + "can't eliminate \nthat variable.\n" + \
                    "eliminationVariable:" + str(eliminationVariable) + "\n" +\
                    "unconditionedVariables: " + str(factor.unconditionedVariables()))

        uncond = factor.unconditionedVariables()
        cond = factor.conditionedVariables()
        assignments = factor.variableDomainsDict()

        if eliminationVariable in uncond:
            uncond.remove(eliminationVariable)
        elif eliminationVariable in cond:
            cond.remove(eliminationVariable)

        result = Factor(uncond, cond, assignments)

        for res_assignment in result.getAllPossibleAssignmentDicts():
            for fac_assignment in factor.getAllPossibleAssignmentDicts():
                # check if all the variables are same except the elimination var.
                if sum([item in fac_assignment.items() for item in res_assignment.items()]) == len(fac_assignment)-1:
                    result.setProbability(res_assignment, result.getProbability(res_assignment) + factor.getProbability(fac_assignment))

        return result

    return eliminate

eliminate = eliminateWithCallTracking()


def normalize(factor):
    """
    Question 5: Your normalize implementation 

    Input factor is a single factor.

    The set of conditioned variables for the normalized factor consists 
    of the input factor's conditioned variables as well as any of the 
    input factor's unconditioned variables with exactly one entry in their 
    domain.  Since there is only one entry in that variable's domain, we 
    can either assume it was assigned as evidence to have only one variable 
    in its domain, or it only had one entry in its domain to begin with.
    This blurs the distinction between evidence assignments and variables 
    with single value domains, but that is alright since we have to assign 
    variables that only have one value in their domain to that single value.

    Return a new factor where the sum of the all the probabilities in the table is 1.
    This should be a new factor, not a modification of this factor in place.

    If the sum of probabilities in the input factor is 0,
    you should return None.

    This is intended to be used at the end of a probabilistic inference query.
    Because of this, all variables that have more than one element in their 
    domain are assumed to be unconditioned.
    There are more general implementations of normalize, but we will only 
    implement this version.

    Useful functions:
    Factor.getAllPossibleAssignmentDicts
    Factor.getProbability
    Factor.setProbability
    Factor.unconditionedVariables
    Factor.conditionedVariables
    Factor.variableDomainsDict
    """

    # typecheck portion
    variableDomainsDict = factor.variableDomainsDict()
    for conditionedVariable in factor.conditionedVariables():
        if len(variableDomainsDict[conditionedVariable]) > 1:
            print "Factor failed normalize typecheck: ", factor
            raise ValueError, ("The factor to be normalized must have only one " + \
                            "assignment of the \n" + "conditional variables, " + \
                            "so that total probability will sum to 1\n" + 
                            str(factor))

    total = 0

    if len(variableDomainsDict.keys()) == 1:
        result = Factor(factor.unconditionedVariables(), factor.conditionedVariables(), factor.variableDomainsDict())
        
        for fac_assignment in factor.getAllPossibleAssignmentDicts():
            total += factor.getProbability(fac_assignment)
            for res_assignment in result.getAllPossibleAssignmentDicts():
                if fac_assignment == res_assignment:
                    result.setProbability(res_assignment, factor.getProbability(fac_assignment))


        for res_assignment in result.getAllPossibleAssignmentDicts():
            result.setProbability(res_assignment, result.getProbability(res_assignment)/total)

        return result if total != 0 else None

    else:
        one_entry = None
        for var in variableDomainsDict.items():
            if len(var[1]) == 1:
                one_entry = var

        # 2. check for var that has one value all over the extracted table
        # 3. uncondition = any var with diff value. condition = evidence + any var with only one value
        # 4. Get the sum and norm.

        uncond = factor.unconditionedVariables()
        if one_entry != None:
            uncond.difference_update(set([one_entry[0]]))

        result = Factor(factor.unconditionedVariables(), factor.conditionedVariables(), factor.variableDomainsDict())
        
        for fac_assignment in factor.getAllPossibleAssignmentDicts():
            total += factor.getProbability(fac_assignment)
            for res_assignment in result.getAllPossibleAssignmentDicts():
                if fac_assignment == res_assignment:
                    result.setProbability(res_assignment, factor.getProbability(fac_assignment))

        for res_assignment in result.getAllPossibleAssignmentDicts():
            result.setProbability(res_assignment, result.getProbability(res_assignment)/total)

        # check if any variable has only one entry value. If so, it is conditioned variable along with evidence
        comp = result.getAllPossibleAssignmentDicts()[0].items()

        var = result.variableDomainsDict()

        diff = {}

        for x in comp:
            diff[x[0]] = False

        # check if any variable changes its value. => is unconditioned variables.
        for res_assignment in result.getAllPossibleAssignmentDicts()[1:]:
            for x, y in zip(comp, res_assignment.items()):
                if x[1] != y[1]:
                    # print 'im different' + y[0]
                    diff[x[0]] = True

        # vars in cond list are the vars that are conditioned.
        cond = [x for x in diff if diff[x] == False]

        uncond = [x for x in uncond if x not in cond]

        result2 = Factor(uncond, cond, factor.variableDomainsDict())

        for r2_assignment, r_assignment in zip(result2.getAllPossibleAssignmentDicts(), result.getAllPossibleAssignmentDicts()):
            if r2_assignment == r_assignment:
                result2.setProbability(r2_assignment, result.getProbability(r_assignment))

        return result2 if total != 0 else None
