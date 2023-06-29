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

from util import manhattanDistance
from game import Directions
import random, util

from game import Agent
from pacman import GameState


class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """

    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [
            self.evaluationFunction(gameState, action) for action in legalMoves
        ]
        bestScore = max(scores)
        bestIndices = [
            index for index in range(len(scores)) if scores[index] == bestScore
        ]
        chosenIndex = random.choice(
            bestIndices)  # Pick randomly among the best

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (new_food) and Pacman position after moving (new_pos).
        new_scared_times holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        food_list = currentGameState.getFood().asList()

        successor_game_state = currentGameState.generatePacmanSuccessor(action)
        new_pacman_position = successor_game_state.getPacmanPosition()

        new_ghost_states = [
            (ghost_state.getPosition(), ghost_state.scaredTimer)
            for ghost_state in successor_game_state.getGhostStates()
        ]

        if not food_list:
            return 999999

        # Evaluate ghost position
        ghost_penalty = 0
        closest_ghost_state = min(
            new_ghost_states,
            key=lambda g: manhattanDistance(new_pacman_position, g[0]))
        closest_ghost_distance = manhattanDistance(closest_ghost_state[0],
                                                   new_pacman_position)
        if closest_ghost_distance <= 1 and closest_ghost_state[1] == 0:
            ghost_penalty = 3

        # Evaluate eating food
        food_reward = 0
        if new_pacman_position in food_list:
            food_reward = 2

        # Evaluate food distance
        closest_food_position = min(
            food_list, key=lambda f: manhattanDistance(new_pacman_position, f))
        closest_food_distance = manhattanDistance(new_pacman_position,
                                                  closest_food_position)
        food_distance_reward = 1 / (closest_food_distance + 1)

        # Stop action penalty
        stop_penalty = 0.5 if action == Directions.STOP else 0

        return food_reward + food_distance_reward - ghost_penalty - stop_penalty


def scoreEvaluationFunction(currentGameState: GameState):
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

    def __init__(self, evalFn='scoreEvaluationFunction', depth='2'):
        self.index = 0  # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)


class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
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

        def minimax(state: GameState, depth, agent_index):
            '''
            Minimax algorithm implementation. 
            '''
            if depth == 0 or state.isWin() or state.isLose():
                return None, self.evaluationFunction(state)

            if agent_index == 0:
                # Pacman
                best_action, max_score = max(
                    ((action,
                      minimax(state.generateSuccessor(agent_index, action),
                              depth, agent_index + 1)[1])
                     for action in state.getLegalActions(agent_index)),
                    key=lambda x: x[1])
                return best_action, max_score
            else:
                # Ghosts
                agent_num = gameState.getNumAgents()
                best_action, min_score = min(
                    ((action,
                      minimax(
                          state.generateSuccessor(agent_index, action),
                          depth if agent_index + 1 != agent_num else depth - 1,
                          (agent_index + 1) % agent_num)[1])
                     for action in state.getLegalActions(agent_index)),
                    key=lambda x: x[1])
                return best_action, min_score

        action, _ = minimax(gameState, self.depth, agent_index=0)
        return action


class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """

        def alpha_beta_minimax(state: GameState, depth, agent_index, alpha,
                               beta):
            '''
            Alpha-beta minimax algorithm implementation.
            '''
            if depth == 0 or state.isWin() or state.isLose():
                return None, self.evaluationFunction(state)

            if agent_index == 0:
                # Pacman
                max_score = float('-inf')
                best_action = None
                for action in state.getLegalActions(agent_index):
                    _, score = alpha_beta_minimax(
                        state.generateSuccessor(agent_index, action), depth,
                        agent_index + 1, alpha, beta)

                    if score > max_score:
                        max_score = score
                        best_action = action
                    if max_score > beta:
                        return None, max_score

                    alpha = max(alpha, max_score)

                return best_action, max_score
            else:
                # Ghosts
                agent_num = state.getNumAgents()
                next_depth = depth if agent_index + 1 != agent_num else depth - 1
                next_agent_index = (agent_index + 1) % agent_num

                min_score = float('inf')
                best_action = None
                for action in state.getLegalActions(agent_index):
                    _, score = alpha_beta_minimax(
                        state.generateSuccessor(agent_index, action),
                        next_depth, next_agent_index, alpha, beta)

                    if score < min_score:
                        min_score = score
                        best_action = action
                    if min_score < alpha:
                        return None, min_score

                    beta = min(beta, min_score)

                return best_action, min_score

        best_action, _ = alpha_beta_minimax(gameState,
                                            self.depth,
                                            agent_index=0,
                                            alpha=float('-inf'),
                                            beta=float('inf'))
        return best_action


class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """

        def expectimax(state: GameState, depth, agent_index):
            '''
            Expectimax algorithm implementation.
            '''
            if depth == 0 or state.isWin() or state.isLose():
                return None, self.evaluationFunction(state)

            if agent_index == 0:
                # Pacman
                best_action, max_score = max(
                    ((action,
                      expectimax(state.generateSuccessor(agent_index, action),
                                 depth, agent_index + 1)[1])
                     for action in state.getLegalActions(agent_index)),
                    key=lambda x: x[1])
                return best_action, max_score
            else:
                # Ghosts
                agent_num = state.getNumAgents()
                next_depth = depth if agent_index + 1 != agent_num else depth - 1
                next_agent_index = (agent_index + 1) % agent_num

                expected_score = 0
                num_actions = len(state.getLegalActions(agent_index))
                probability = 1.0 / num_actions

                for action in state.getLegalActions(agent_index):
                    _, score = expectimax(
                        state.generateSuccessor(agent_index, action),
                        next_depth, next_agent_index)
                    expected_score += probability * score
                return None, expected_score

        action, _ = expectimax(gameState, self.depth, agent_index=0)
        return action


def betterEvaluationFunction(current_game_state: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION:
    By change evaluation function, we can change the behavior of the pacman.
    1. It will get more penalty score when pacman is too close to the ghost. This will allow pacman to avoid being catched by the ghost.
    2. Evaluate ghost scared time. This will allow pacman to chase weak ghost.
    3. Evalueate distance between closest food and pacman. This will allow pacman to get food.
    """
    food_list = current_game_state.getFood().asList()
    evaluation_score = current_game_state.getScore()
    pacman = current_game_state.getPacmanPosition()
    ghost = [(ghost_state.getPosition(), ghost_state.scaredTimer)
             for ghost_state in current_game_state.getGhostStates()]

    # Evaluate closest ghost
    closest_ghost = min(ghost, key=lambda g: manhattanDistance(pacman, g[0]))
    closest_ghost_distance = manhattanDistance(closest_ghost[0], pacman)
    ghost_factor = -15.0
    evaluation_score += ghost_factor / (1 + closest_ghost_distance)

    # Evalueate ghost scared time
    evaluation_score += sum(g[1] for g in ghost)

    # Evaluate closet food
    if food_list:
        closest_food = min(food_list,
                           key=lambda f: manhattanDistance(pacman, f))
        closest_food_distance = manhattanDistance(closest_food, pacman)
        food_factor = 10.0
        evaluation_score += food_factor / (1 + closest_food_distance)

    return evaluation_score


# Abbreviation
better = betterEvaluationFunction
