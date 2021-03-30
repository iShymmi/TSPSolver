package main.java.algorithm;

import java.util.Arrays;
import java.util.Random;

public class Individual {
    private final int[] genes;
    private double length;
    private float crossingProbability;
    private float mutationProbability;

    public Individual(int[] genes) {
        this.genes = genes;
    }

    /**
        * Mutates given route
        * pattern example:
            * random point from 1 to points size - 1  eg. 1 , 3
            * before mutation: 1|>2<|0|>4<|5|1
            * after mutation:  1|>4<|0|>2<|5|1
     */
    public Individual mutate(){
        Random random = new Random();
        int firstGene;
        int secondGene;

        do{
            System.out.println(firstGene = random.nextInt(genes.length - 2) + 1);
            System.out.println(secondGene = random.nextInt(genes.length - 2) + 1);
        }while (firstGene == secondGene);

        int temp = genes[firstGene];
        genes[firstGene] = genes[secondGene];
        genes[secondGene] = temp;

        return this;
    }

    /**
        * Breed two individuals :
        * pattern example:
        *   cross point: 1
        *   genes of individual :  1 2 << 3 4 5
        *   genes of crossing individual:  4 1 5 3 2
        *   fill new individual in order -> genes from this individual till cross point
        *   rest of missing genes in same order as in crossing individual
        *   new individual -> 1 2 >> 4 5 3
    */
    public Individual breed(Individual individualToBreed){
        Random random = new Random();
        int[] breedGenes = individualToBreed.getGenes();
        int[] newGenes = new int[genes.length];
        boolean[] tempArray = new boolean[genes.length];

        int crossPoint = random.nextInt(genes.length - 2) + 2;

        int i;
        for(i = 0; i < crossPoint; i++){
            newGenes[i] = genes[i];
            tempArray[genes[i]] = true;
        }

        for(int j = 1; i < genes.length - 1; j++){
            if(!tempArray[breedGenes[j]]){
                newGenes[i] = breedGenes[j];
                i++;
            }
        }
        newGenes[genes.length - 1] = genes[0];
        return new Individual(newGenes);
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
