package main.java.algorithm;

import java.util.Arrays;

public class Individual {
    private final int[] genes;
    private double length;
    private float crossingProbability;
    private float mutationProbability;

    public Individual(int[] genes) {
        this.genes = genes;
    }

    public int[] getGenes() {
        return genes;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public float getCrossingProbability() {
        return crossingProbability;
    }

    public void setCrossingProbability(float crossingProbability) {
        this.crossingProbability = crossingProbability;
    }

    public float getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(float mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(genes).forEach(gene -> sb.append(gene).append(", "));

        return sb.toString();
    }
}
