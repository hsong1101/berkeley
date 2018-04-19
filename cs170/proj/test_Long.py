from copy import copy, deepcopy
import numpy as np
import sys
import heapq
import itertools
import solver_template

from copy import copy, deepcopy

from test import matrix, matrix100, matrix200

def findMinTour(startingKingdom, fullWalk, cqList, matrix, maxtrix_size,kingdomNames):
	"""
	Return the min cost to "visit" all vertices of the full Walk 
	and conquer all kingdoms in conquered list
	cqList = [1,4]
	fullwalk = [1,2,]
	"""
	isSurrender = []

	for i in range(len(fullWalk)):
		isSurrender.append(False)
	#print(isSurrender)
	# define a path 
	returned_path = []
	temp_path = []
	conquered = []
	lastKingdom = 0
	for k in range(len(fullWalk)):
		v = fullWalk[k]
		if v in cqList:
			if v not in conquered:
				if temp_path != []:
					conquered.append(v)
					isSurrender[k] = True
					returned_path.extend(temp_path)
					returned_path.append(v)
					#print(returned_path)
					temp_path = []
				else:
					conquered.append(v)
					#print(conquered)
					isSurrender[k] = True
					returned_path.append(v)
					temp_path = []
			else:
				isSurrender[k] = True
				temp_path.append(v)
				#print(temp_path)
		else:
			isSurrender[k] = True
			temp_path.append(v)
			#print(temp_path)
	#print(isSurrender)

	for i in range(len(kingdomNames)):
		if kingdomNames[i] == returned_path[-1]:
			lastKingdom = i
			break
	#print("The last kingdom: ", lastKingdom)
	print(lastKingdom)
	g = Graph(maxtrix_size)

	dijkstraPath = g.dijkstra(matrix, kingdomNames, lastKingdom, startingKingdom)

	#print("Dijkstra from the last kingdom back to the starting one: ", dijkstraPath)
	returned_path += dijkstraPath
	print(dijkstraPath)
	return returned_path

# Python program for Dijkstra's single source shortest
# path algorithm. The program is for adjacency matrix
# representation of the graph
 
from collections import defaultdict
 
#Class to represent a graph
class Graph:
	def __init__(self,vertices):
		self.V = vertices
		self.graph = [[0 for column in range(vertices)] for row in range(vertices)]
	def minDistance(self,dist,queue):
		minimum = float("inf")
		min_index = -1
		for i in range(len(dist)):
			if dist[i] < minimum and i in queue:
				minimum = dist[i]
				min_index = i
		return min_index

	def printPath(self,parent,j):
		path = []
		if parent[j] == -1:
			path.append(j)
			#print(j)
			return
		self.printPath(parent, parent[j])
		path.append(j)
		#print(j)
		#print(path)
		return path
	def printSolution(self,dist,parent,dst):
		src = 0
		print("Vertex Distance from Source Path")
		lst_paths = {}
		for i in range(1, len(dist)):
			print(src,i, dist[i])
			self.printPath(parent,i)
		#print(lst_paths[dst])
		#return lst_paths[dst]
	def returnPath(self,parent,j):
		path = []
		#print(parent)
		if parent[j] == -1:
			#path += [j]
			return path
		path.append(j)
		return self.returnPath(parent,parent[j])
		

	def dijkstra(self, graph,kingdomNames,src,dst):
		row = len(graph)
		col = len(graph[0])
		dist = [float("Inf")] * row
		parent = [-1] * row
		dist[src] = 0
		queue = []
		paths = []
		#print("Starting kingdom: ", src)
		#print("Ending kingdom: ", dst)
		for i in range(row):
			queue.append(i)
		#Find shortest path for all vertice
		while queue:
		# Pick the minimum dist vertex from the set of vertices
		# still in queue
			u = self.minDistance(dist,queue)
		# remove min element     
			queue.remove(u)
			for i in range(col):
				if graph[u][i] != 'x' and i in queue:
					if dist[u] + graph[u][i] < dist[i]:
						dist[i] = dist[u] + graph[u][i]
						parent[i] = u
		# print the constructed distance array
		#paths = self.printSolution(dist,parent,dst)
		#print(path)
		#return path

		#return self.returnPath(src,dst)
		#print(parent[dst])
		#print(dst)
		while parent[dst] != -1:
			paths.append(kingdomNames[dst])
			dst = parent[dst]

		paths.reverse()
		return paths

def unsurrenededNeighbors(matrix, conquered, conqueredList):
	neigh = []
	for i in range(0, len(matrix)):
		if i not in conquered:
			neighi = []
			for j in range(0, len(matrix[i])):
				if matrix[i][j] != 'x' and j != i and j not in conqueredList:
					neighi.append(j)
			neigh.append((neighi, i))
	return neigh

def maxNeighbor(neighbors):
	result = ([],0)
	for x in neighbors:
		if len(x[0]) > len(result[0]):
			result = x
	return result

def setCoverGreedy(matrix):
	result = []
	cqred = set()
	while (len(cqred) != len(matrix)):
		maxNeigh = maxNeighbor(unsurrenededNeighbors(matrix, result, cqred))
		cqred.add(maxNeigh[1])
		cqred.update(maxNeigh[0])
		result.append(maxNeigh[1])
	return result

conquered50 = setCoverGreedy(matrix)
names = [i for i in range(0, len(matrix))]
fullWalk50 = [0, 2, 3, 5, 3, 2, 6, 7, 1, 7, 11, 12, 17, 16, 15, 13, 8, 9, 8, 13, 15, 14, 10, 14, 15, 16, 17, 12, 35, 34, 33, 32, 30, 32, 31, 28, 31, 29, 27, 18, 27, 25, 21, 25, 22, 25, 27, 26, 19, 26, 20, 26, 23, 26, 24, 26, 27, 29, 31, 32, 33, 34, 35, 47, 41, 39, 38, 42, 49, 48, 49, 42, 38, 39, 41, 46, 43, 37, 43, 45, 40, 45, 44, 36, 44, 45, 43, 46, 41, 47, 35, 12, 11, 7, 6, 2, 0, 4, 0]
sol50 = findMinTour(0, fullWalk50, conquered50, matrix, len(matrix), names)

f = open('50.out', 'w')

for x in sol50:
	f.write('a'+str(x+1)+' ')
f.write('\n')
for x in conquered50:
	f.write('a'+str(x+1)+' ')
f.write('\n')
f.close()

conquered100 = setCoverGreedy(matrix100)
names = [i for i in range(0, len(matrix100))]
fullWalk100 = [0, 2, 3, 5, 3, 2, 6, 7, 1, 97, 91, 86, 91, 87, 88, 90, 95, 92, 94, 89, 94, 93, 85, 93, 94, 92, 95, 90, 96, 84, 61, 66, 65, 64, 62, 57, 58, 57, 62, 64, 63, 59, 60, 54, 60, 56, 50, 56, 55, 51, 49, 53, 49, 51, 55, 52, 55, 56, 60, 59, 63, 64, 65, 66, 61, 84, 83, 82, 81, 79, 81, 80, 77, 80, 78, 76, 67, 76, 74, 68, 74, 69, 74, 70, 74, 71, 74, 76, 75, 72, 75, 73, 75, 76, 78, 80, 81, 82, 83, 84, 96, 90, 88, 87, 91, 97, 1, 7, 11, 12, 17, 16, 15, 13, 8, 9, 8, 13, 15, 14, 10, 14, 15, 16, 17, 12, 35, 34, 33, 32, 30, 32, 31, 28, 27, 18, 27, 25, 21, 25, 22, 25, 27, 26, 19, 26, 20, 26, 23, 26, 24, 26, 27, 28, 31, 29, 31, 32, 33, 34, 35, 47, 41, 39, 38, 42, 48, 42, 38, 39, 41, 46, 43, 37, 43, 45, 40, 45, 44, 36, 44, 45, 43, 46, 41, 47, 35, 12, 11, 7, 6, 2, 0, 4, 0]
sol100 = findMinTour(0, fullWalk100, conquered100, matrix100, len(matrix100), names)

f = open('100.out', 'w')

for x in sol100:
	f.write('a'+str(x+1)+' ')
f.write('\n')
for x in conquered100:
	f.write('a'+str(x+1)+' ')
f.write('\n')
f.close()

conquered200 = setCoverGreedy(matrix200)
names = [i for i in range(0, len(matrix200))] 
fullWalk200 = [0, 2, 3, 5, 3, 2, 6, 7, 1, 97, 91, 86, 91, 87, 88, 90, 95, 92, 94, 89, 94, 93, 85, 93, 94, 92, 95, 90, 96, 84, 61, 66, 65, 64, 62, 57, 58, 57, 62, 64, 63, 59, 60, 54, 60, 56, 50, 56, 55, 51, 49, 53, 49, 51, 55, 52, 55, 56, 60, 59, 63, 64, 65, 66, 61, 84, 83, 82, 81, 79, 81, 80, 77, 80, 78, 76, 67, 76, 74, 70, 74, 71, 74, 76, 75, 68, 75, 69, 75, 72, 75, 73, 75, 76, 78, 80, 81, 82, 83, 84, 96, 90, 88, 87, 91, 97, 1, 7, 11, 12, 17, 16, 15, 13, 8, 9, 8, 13, 15, 14, 10, 14, 15, 16, 17, 12, 35, 34, 33, 32, 30, 32, 31, 28, 31, 29, 27, 18, 27, 25, 21, 25, 22, 25, 27, 26, 19, 26, 20, 26, 23, 26, 24, 26, 27, 29, 31, 32, 33, 34, 35, 47, 41, 39, 38, 42, 48, 42, 38, 39, 41, 46, 43, 37, 43, 45, 40, 45, 44, 36, 44, 45, 43, 46, 41, 47, 35, 12, 11, 7, 6, 2, 0, 4, 0, 195, 99, 100, 98, 102, 98, 100, 101, 103, 101, 100, 104, 105, 109, 110, 115, 114, 113, 111, 106, 107, 106, 111, 113, 112, 108, 112, 113, 114, 115, 110, 133, 132, 131, 130, 128, 130, 129, 126, 129, 127, 125, 116, 125, 123, 117, 123, 118, 123, 119, 123, 120, 123, 125, 124, 121, 124, 122, 124, 125, 127, 129, 130, 131, 132, 133, 145, 139, 137, 136, 140, 146, 140, 136, 137, 139, 144, 141, 135, 141, 143, 138, 143, 142, 134, 142, 143, 141, 144, 139, 145, 133, 110, 109, 105, 104, 100, 99, 195, 189, 184, 189, 185, 186, 188, 193, 190, 192, 187, 192, 191, 183, 191, 192, 190, 193, 188, 194, 182, 159, 164, 163, 162, 160, 155, 156, 155, 160, 162, 161, 157, 158, 152, 158, 154, 148, 154, 153, 149, 147, 151, 147, 149, 153, 150, 153, 154, 158, 157, 161, 162, 163, 164, 159, 182, 181, 180, 179, 177, 179, 178, 175, 178, 176, 174, 165, 174, 172, 166, 172, 167, 172, 168, 172, 169, 172, 174, 173, 170, 173, 171, 173, 174, 176, 178, 179, 180, 181, 182, 194, 188, 186, 185, 189, 195, 0]
sol200 = findMinTour(0, fullWalk200, conquered200, matrix200, len(matrix200), names)

f = open('200.out', 'w')

for x in sol200:
	f.write('a'+str(x+1)+' ')
f.write('\n')
for x in conquered200:
	f.write('a'+str(x+1)+' ')
f.write('\n')
f.close()




