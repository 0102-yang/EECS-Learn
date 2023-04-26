# search.py
# ---------
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
"""
In search.py, you will implement generic search algorithms which are called by
Pacman agents (in searchAgents.py).
"""

import util


class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        util.raiseNotDefined()

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        util.raiseNotDefined()

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        util.raiseNotDefined()

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        util.raiseNotDefined()


def tinyMazeSearch(problem):
    """
    Returns a sequence of moves that solves tinyMaze.  For any other maze, the
    sequence of moves will be incorrect, so only use this for tinyMaze.
    """
    from game import Directions
    s = Directions.SOUTH
    w = Directions.WEST
    return [s, s, w, s, w, w, s, w]


def depthFirstSearch(problem: SearchProblem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print("Start:", problem.getStartState())
    print("Is the start a goal?", problem.isGoalState(problem.getStartState()))
    print("Start's successors:", problem.getSuccessors(problem.getStartState()))
    """
    actions = util.Stack()
    visited = set()

    def depth_first_search(state) -> bool:
        """ 
        Execute depth-first search algorithm. 
        """
        if state in visited:
            return False
        visited.add(state)

        if problem.isGoalState(state):
            return True

        for next_state, action, _ in problem.getSuccessors(state):
            actions.push(action)
            if depth_first_search(next_state):
                return True
            actions.pop()

        return False

    depth_first_search(problem.getStartState())
    return actions.list


def breadthFirstSearch(problem: SearchProblem):
    """Search the shallowest nodes in the search tree first."""
    # Execute breadth first search algorithm.
    start_state = problem.getStartState()
    paths = {start_state: []}
    search_queue = util.Queue()
    search_queue.push(start_state)

    while not search_queue.isEmpty():
        state = search_queue.pop()

        if problem.isGoalState(state):
            return paths[state]

        for next_state, action, _ in problem.getSuccessors(state):
            if next_state not in paths:
                search_queue.push(next_state)
                paths[next_state] = paths[state].copy() + [action]

    return []


def uniformCostSearch(problem: SearchProblem):
    """Search the node of least total cost first."""
    return aStarSearch(problem)


def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0


def aStarSearch(problem: SearchProblem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    paths = {}
    search_priority_queue = util.PriorityQueue()
    visited = set()

    start_state = problem.getStartState()
    start_state_action_list = []
    visited.add(start_state)
    paths[start_state] = start_state_action_list
    search_priority_queue.push(
        start_state,
        problem.getCostOfActions(start_state_action_list) +
        heuristic(start_state, problem))

    while not search_priority_queue.isEmpty():
        state = search_priority_queue.pop()

        if problem.isGoalState(state):
            return paths[state]

        for next_state, action, _ in problem.getSuccessors(state):
            if next_state not in visited:
                next_state_action_list = paths[state].copy() + [action]
                paths[next_state] = next_state_action_list

                search_priority_queue.push(
                    next_state,
                    problem.getCostOfActions(next_state_action_list) +
                    heuristic(next_state, problem))
                if not problem.isGoalState(next_state):
                    visited.add(next_state)

    return []


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
