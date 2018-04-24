import os
import sys
sys.path.append('..')
sys.path.append('../..')
import argparse
import utils
from utils import *

"""
======================================================================
  Complete the following function.
======================================================================
"""


def solve(list_of_kingdom_names, starting_kingdom, adjacency_matrix, params=[]):
    """
    Write your algorithm here.
    Input:
        list_of_kingdom_names: An list of kingdom names such that node i of the graph corresponds to name index i in the list
        starting_kingdom: The name of the starting kingdom for the walk
        adjacency_matrix: The adjacency matrix from the input file

    Output:
        Return 2 things. The first is a list of kingdoms representing the walk, and the second is the set of kingdoms that are conquered
    """

    #A = adjacency matrix, u = vertex u, v = vertex v
    def weight(A, u, v):
        return A[u][v]

    #A = adjacency matrix, u = vertex u
    def adjacent(A, u):
        L = []
        for x in range(len(A)):
            if A[u][x] > 0 and x != u and A[u][x] != 'x':
                L.insert(0,x)
        return L

    #Q = min queue
    def extractMin(Q):
        q = Q[0]
        Q.remove(Q[0])
        return q

    #Q = min queue, V = vertex list
    def decreaseKey(Q, K):
        for i in range(len(Q)):
            for j in range(len(Q)):
                if K[Q[i]] < K[Q[j]]:
                    s = Q[i]
                    Q[i] = Q[j]
                    Q[j] = s

    #V = vertex list, A = adjacency list, r = root
    def prim(V, A, r):
        u = 0
        v = 0

        # initialize and set each value of the array P (pi) to none
        # pi holds the parent of u, so P(v)=u means u is the parent of v
        P=[None]*len(V)

        # initialize and set each value of the array K (key) to some large number (simulate infinity)
        K = [999999]*len(V)

        # initialize the min queue and fill it with all vertices in V
        Q=[0]*len(V)
        for u in range(len(Q)):
            Q[u] = V[u]

        # set the key of the root to 0
        K[r] = 0
        decreaseKey(Q, K)    # maintain the min queue

        # loop while the min queue is not empty
        while len(Q) > 0:
            u = extractMin(Q)    # pop the first vertex off the min queue

            # loop through the vertices adjacent to u
            Adj = adjacent(A, u)
            for v in Adj:
                w = weight(A, u, v)    # get the weight of the edge uv

                # proceed if v is in Q and the weight of uv is less than v's key
                if Q.count(v)>0 and w < K[v]:
                    # set v's parent to u
                    P[v] = u
                    # v's key to the weight of uv
                    K[v] = w
                    decreaseKey(Q, K)    # maintain the min queue
        return P


    # graph is a list of kingdoms that previous i is the parent of j where j = i + 1    
    graph = prim(adjacency_matrix, list_of_kingdom_names, starting_kingdom)

    # key = parent, value = children
    g = {}

    for x in range(len(list_of_kingdom_names)):
        g[x] = []

    for x in range(len(graph)):
        for y in range(len(graph)):
            if x == graph[y]:
                g[x].append(y)  


    def path(k):
        if not g[k]:
            return [k]

        lst = [k]

        for child in g[k]:
            lst += path(child) + [k]
            # print(lst)

        return lst


    full_path = path(starting_kingdom)

    # print(full_path)



    # return closed_walk, conquered_kingdoms


"""
======================================================================
   No need to change any code below this line
======================================================================
"""


def solve_from_file(input_file, output_directory, params=[]):
    print('Processing', input_file)
    
    input_data = utils.read_file(input_file)
    number_of_kingdoms, list_of_kingdom_names, starting_kingdom, adjacency_matrix = data_parser(input_data)
    closed_walk, conquered_kingdoms = solve(list_of_kingdom_names, starting_kingdom, adjacency_matrix, params=params)

    basename, filename = os.path.split(input_file)
    output_filename = utils.input_to_output(filename)
    output_file = f'{output_directory}/{output_filename}'
    if not os.path.exists(output_directory):
        os.makedirs(output_directory)
    utils.write_data_to_file(output_file, closed_walk, ' ')
    utils.write_to_file(output_file, '\n', append=True)
    utils.write_data_to_file(output_file, conquered_kingdoms, ' ', append=True)


def solve_all(input_directory, output_directory, params=[]):
    input_files = utils.get_files_with_extension(input_directory, 'in')

    for input_file in input_files:
        solve_from_file(input_file, output_directory, params=params)


if __name__=="__main__":
    parser = argparse.ArgumentParser(description='Parsing arguments')
    parser.add_argument('--all', action='store_true', help='If specified, the solver is run on all files in the input directory. Else, it is run on just the given input file')
    parser.add_argument('input', type=str, help='The path to the input file or directory')
    parser.add_argument('output_directory', type=str, nargs='?', default='.', help='The path to the directory where the output should be written')
    parser.add_argument('params', nargs=argparse.REMAINDER, help='Extra arguments passed in')
    args = parser.parse_args()
    output_directory = args.output_directory
    if args.all:
        input_directory = args.input
        solve_all(input_directory, output_directory, params=args.params)
    else:
        input_file = args.input
        solve_from_file(input_file, output_directory, params=args.params)