import java.util.*;

/**
 * Implements methods to solve problems involving Markov Chain.
 *
 * @author Daniel Bielech
 */
class Markov {

    /**
     * Possible direction of movement on the 2D grid.
     */
    public static final List<Direction> DIRECTIONS = List.of(Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH);
    /**
     * Numbers of iterations for the algorithm loop.
     */
    public static final double ITERATIONS = 10000000.0;

    /**
     * Starting from given state, simulate a random walk of n steps.
     * Save the result to a 2D array where each cell consists of the number of times it has been visited.
     *
     * @param grid          The 2D space where the movement of the random walker happen.
     * @param nOfSteps      Given number of steps.
     * @param startingState The state from which the movement will begin.
     * @return Array representing the movement distribution after x trials.
     */
    public static int[][] metropolisAlgorithm(int[][] grid, int nOfSteps, State startingState) {
        int[][] gridCopy = grid.clone();
        int i = 0;
        int j = 0;
        State currentState = startingState;
        while (i < ITERATIONS) {
            while (j < nOfSteps) {
                State newState = getAdjacentRandomState(currentState);
                double acceptanceProbability =
                        getAcceptanceProbability(
                                calculateSSP(newState, gridCopy.length),
                                calculateSSP(currentState, gridCopy.length));
                double r = Math.random();
                if (r < acceptanceProbability) {
                    currentState = newState;
                }
                j++;
            }
            gridCopy[currentState.getX()][currentState.getY()] += 1;
            currentState = startingState;
            j = 0;
            i++;
        }
        return gridCopy;
    }

    /**
     * Get the sum of all transitions.
     *
     * @param steps Number of steps in each state for CTMC.
     * @return The sum.
     */
    private static double getSumOfTransitions(int[] steps) {
        int sum = 0;
        for (int r : steps) {
            sum += r;
        }
        return sum;
    }

    /**
     * Calculate propensity (sum of rates for state transitions from given state).
     *
     * @param state Given state.
     * @param rates List of rates for CTMC.
     * @return Propensity.
     */
    private static double getPropensity(int state, double[] rates) {
        return rates[state * 2 - 1] + rates[state * 2 - 2];
    }

    /**
     * Calculate the steady state probability for given state, assuming it is equal for all states.
     *
     * @param state    Given state.
     * @param gridSize The size of the grid (length of one row, assuming the grid is a square).
     * @return Steady state probability.
     */
    private static double calculateSSP(State state, int gridSize) {
        if ((state.getY()) >= gridSize
                || (state.getY()) < 0
                || (state.getX()) >= gridSize
                || (state.getX()) < 0) {
            return 0.0;
        } else {
            return 1.0 / gridSize * gridSize;
        }
    }

    /**
     * Get the steady state probability for given state from the list of steady state probabilities.
     *
     * @param state    Given state.
     * @param gridSize The size of the grid (length of one row, assuming the grid is a square).
     * @param ssProbs  Steady state probabilities.
     * @return Steady state probability.
     */
    private static double getBiasSSP(State state, int gridSize, double[] ssProbs) {
        if ((state.getY()) >= gridSize
                || (state.getY()) < 0
                || (state.getX()) >= gridSize
                || (state.getX()) < 0) {
            return 0.0;
        } else {
            return ssProbs[state.getIndex() - 1];
        }
    }

    /**
     * Get a random state within a given grid.
     *
     * @param gridSize The size of the grid (length of one row, assuming the grid is a square).
     * @return Random state.
     */
    private static State getRandomState(int gridSize) {
        return new State(new Random().nextInt(gridSize), new Random().nextInt(gridSize));
    }

    /**
     * Get a random state adjacent to the given state.
     *
     * @param currentState Given state.
     * @return Random adjacent state.
     */
    private static State getAdjacentRandomState(State currentState) {
        Direction movementDirection = DIRECTIONS.get(new Random().nextInt(DIRECTIONS.size()));

        return getAdjacentState(movementDirection, currentState);
    }

    /**
     * Calculate acceptance probability of going from state A to state B.
     *
     * @param sspB State B.
     * @param sspA State A.
     * @return Acceptance probability.
     */
    private static double getAcceptanceProbability(double sspB, double sspA) {
        return Math.min(1, sspB / sspA);
    }

    /**
     * Get proposal probability to transition to a random state.
     *
     * @return Transition probability.
     */
    private static double getProposeProbability() {
        return 1.0 / DIRECTIONS.size();
    }

    /**
     * Get state, adjacent to the current state, in a given direction.
     *
     * @param direction    Given direction.
     * @param currentState Current state.
     * @return Adjacent state.
     */
    private static State getAdjacentState(Direction direction, State currentState) {
        switch (direction) {
            case NORTH:
                return new State(currentState.getX() - 1, currentState.getY());
            case EAST:
                return new State(currentState.getX(), currentState.getY() + 1);
            case WEST:
                return new State(currentState.getX(), currentState.getY() - 1);
            case SOUTH:
                return new State(currentState.getX() + 1, currentState.getY());
            default:
                throw new RuntimeException("Cannot go in any direction. Terminating!");
        }
    }

    /**
     * Get given state direction in relation to the current state.
     *
     * @param currentState Current state.
     * @param newState     Given state.
     * @return Direction.
     */
    private static Direction getDirectionOfMovement(State currentState, State newState) {
        if (Math.abs(newState.getX() - currentState.getX()) == 1) {
            if (newState.getX() - currentState.getX() == 1) {
                return Direction.SOUTH;
            } else {
                return Direction.NORTH;
            }
        }
        if (Math.abs(newState.getY() - currentState.getY()) == 1) {
            if (newState.getY() - currentState.getY() == 1) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }
        }
        throw new RuntimeException("Could not detect the direction of the movement");
    }

    /**
     * Tells whether given states are adjacent.
     *
     * @param s1 State one.
     * @param s2 State two.
     * @return True if states are adjacent, false otherwise.
     */
    private static boolean statesAreAdjacent(State s1, State s2) {
        for (Direction d : DIRECTIONS) {
            if (s2.equals(getAdjacentState(d, s1))) return true;
        }
        return false;
    }

    /**
     * Calculate the transition probability of the random walker getting from state s1 to state s2.
     * Assume the steady state probabilities are equal.
     *
     * @param s1 State one.
     * @param s2 State two.
     * @param n  Number of states.
     * @return Transition probability.
     */
    public static double getTransitionProbability(int s1, int s2, int n) {
        int gridSize = (int) Math.sqrt(n); // gridSize * gridSize = nOfStates
        State currentState = new State(String.valueOf(s1), gridSize);
        State newtState = new State(String.valueOf(s2), gridSize);

        if (!statesAreAdjacent(currentState, newtState) && s1 != s2) return 0.0;

        var largestTransitionProbability = 0.0;
        var totalTransitionProbability = 0.0;
        for (Direction d : DIRECTIONS) {
            var transitionProbability = getAcceptanceProbability(
                    calculateSSP(getAdjacentState(d, currentState), gridSize),
                    calculateSSP(currentState, gridSize)) * getProposeProbability();
            if (transitionProbability > largestTransitionProbability)
                largestTransitionProbability = transitionProbability;
            totalTransitionProbability += transitionProbability;
        }
        var selfTransitionProbability = 1.0 - totalTransitionProbability;
        if (s1 == s2) {
            return selfTransitionProbability;
        } else {
            return largestTransitionProbability;
        }
    }

    /**
     * Calculate estimated probability that the walker is in state s2 at time step TS when it started in state s1
     *
     * @param s1        Starting state.
     * @param s2        Ending state.
     * @param numStates Number of states.
     * @param TS        Number of transitions.
     * @return Probability of being in state two after given number of transitions.
     */
    public static double getEstimatedProbability(int s1, int s2, int numStates, double TS) {
        int gridSize = (int) Math.sqrt(numStates);
        State startingState = new State(String.valueOf(s1), gridSize);
        State desiredState = new State(String.valueOf(s2), gridSize);

        int[][] markovChain = new int[gridSize][gridSize];
        int[][] result = metropolisAlgorithm(markovChain, (int) TS, startingState);

        return result[desiredState.getX()][desiredState.getY()] / ITERATIONS;

    }

    /**
     * Calculate the transition probability of the random walker getting from state s1 to state s2.
     * Take into consideration given steady state probabilities.
     *
     * @param s1     State one.
     * @param s2     State two.
     * @param ssprob Steady state probabilities.
     * @return Transition probability.
     */
    public static double getBiasTransitionProbability(int s1, int s2, double[] ssprob) {
        int gridSize = 3; // gridSize * gridSize = nOfStates
        State currentState = new State(String.valueOf(s1), gridSize);
        State newtState = new State(String.valueOf(s2), gridSize);

        if (!statesAreAdjacent(currentState, newtState) && s1 != s2) return 0.0;

        var totalTransitionProbability = 0.0;
        Map<Direction, Double> transitionProbabilities = new HashMap<>();
        for (Direction d : DIRECTIONS) {
            var transitionProbability = getAcceptanceProbability(
                    getBiasSSP(getAdjacentState(d, currentState), gridSize, ssprob),
                    getBiasSSP(currentState, gridSize, ssprob)) * getProposeProbability();
            totalTransitionProbability += transitionProbability;
            transitionProbabilities.put(d, transitionProbability);
        }
        var selfTransitionProbability = 1.0 - totalTransitionProbability;
        if (s1 == s2) {
            return selfTransitionProbability;
        } else {
            return transitionProbabilities.get(getDirectionOfMovement(currentState, newtState));
        }
    }

    /**
     * Calculate transition probability from state s1 to state s2 in Continuous Time Markov Chain.
     *
     * @param s1    State one.
     * @param s2    State two.
     * @param rates List of transition rates.
     * @return Transition probability.
     */
    public static double getContinuousTransitionProbability(int s1, int s2, double[] rates) {
        if (s1 == 1 && s2 == 2) {
            return rates[0] / getPropensity(s1, rates);
        } else if (s1 == 1 && s2 == 3) {
            return rates[1] / getPropensity(s1, rates);
        } else if (s1 == 2 && s2 == 1) {
            return rates[2] / getPropensity(s1, rates);
        } else if (s1 == 2 && s2 == 3) {
            return rates[3] / getPropensity(s1, rates);
        } else if (s1 == 3 && s2 == 1) {
            return rates[4] / getPropensity(s1, rates);
        } else if (s1 == 3 && s2 == 2) {
            return rates[5] / getPropensity(s1, rates);
        }
        // States are the same. Cannot perform self-transition in CTMC.
        return 0.0;
    }

    /**
     * Calculate probability that at time TSC the Markov Chain is in state s2 when it was started in state s1.
     *
     * @param s1    State one.
     * @param s2    State two.
     * @param rates List of transition rates.
     * @param TSC   Given timestamp.
     * @return Probability of Markov Chain being in s2 at time TSC.
     */
    public static double getContinuousEstimatedProbability(int s1, int s2, double[] rates, double TSC) {
        double time = 0;
        int currentState = s1;
        int[] markovChain = new int[4];

        int i = 0;
        while (i < ITERATIONS) {
            while (time < TSC) {
                double propensity = getPropensity(currentState, rates);
                double r = Math.random();
                double waitingTime = -(1 / propensity) * Math.log(r);
                time += waitingTime;
                int newState = getRandomAdjacentStateUsingTowerSampling(currentState, rates);
                // Make the transition
                currentState = newState;
            }
            markovChain[currentState] += 1;
            currentState = s1;
            time = 0;
            i++;
        }
        return markovChain[s2] / getSumOfTransitions(markovChain);
    }

    /**
     * Get a random state, adjacent to the current state, based on the transition probabilities.
     *
     * @param currentState Current state.
     * @param rates        List of transition rates.
     * @return Random adjacent state.
     */
    private static int getRandomAdjacentStateUsingTowerSampling(int currentState, double[] rates) {
        List<Integer> adjacentStates = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            if (i != currentState) {
                adjacentStates.add(i);
            }
        }
        double transProb1 = getContinuousTransitionProbability(currentState, adjacentStates.get(0), rates);

        double random = Math.random();

        return random < transProb1 ? adjacentStates.get(0) : adjacentStates.get(1);
    }
}
