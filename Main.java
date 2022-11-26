//This is the brief for the Stochastic Assessment
///
//_______________________________________________________
//
//         Edit this file at your own peril.
//
//         I recommend only changing numbers.
//
//         Do not add anything. During marking I will use a different version of Stochastic.java
//
//         Any code you add here will ne lost and ignored.
//
//______________________________________________________


class Main {
    public static void main(String[] args) {

        long startT = System.currentTimeMillis();


// A random walk in discrete time.
// Steady state probabilities of all states are equal.

        int numStates = 9;
        int s1 = 1;
        int s2 = 2;

        double A1 = Markov.getTransitionProbability(s1, s2, numStates);


// The estimated probability that the walker is in state s2 at time step TS when it started in state s1.

        numStates = 9; // fixed for this question

        s1 = 1;
        s2 = 2;
        int TS = 3;

        double A2 = Markov.getEstimatedProbability(s1, s2, numStates, TS);


// A random walk in discrete time.
// Steady state probabilities of all states are GIVEN.

        double[] ssprob = {0.1, 0.1, 0.1, 0.2, 0.1, 0.2, 0.05, 0.05, 0.1};
        s1 = 1;
        s2 = 2;

        double A3 = Markov.getBiasTransitionProbability(s1, s2, ssprob);


// A random walk in continuous time.

        double[] rates = {10.0, 20.0, 10.0, 1.0, 1.0, 1.0};
        double A4 = Markov.getContinuousTransitionProbability(s1, s2, rates);


// Estimated probability that at continuous time TSC the walker chain is in state s2 when it started in state s1.

        double TSC = 0.07;

        s1 = 1;
        s2 = 3;

        double A5 = Markov.getContinuousEstimatedProbability(s1, s2, rates, TSC);

        // Results

        System.out.println("Your answers to the questions were: " + " \n A1  " + A1 + "\n A2  " + A2 + "\n A3  " + A3 + "\n A4  " + A4 + "\n A5  " + A5);


        long endT = System.currentTimeMillis();
        System.out.println("Total execution time was: " + ((endT - startT) / 1000.0) + " seconds");

    }
}