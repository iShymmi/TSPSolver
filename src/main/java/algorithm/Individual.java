package main.java.algorithm;

import java.util.Arrays;
import java.util.Random;

public class Individual {
    private final int[] genes;
    private double length;
    private double reverseLength;
    private double lowerPickBound;
    private double upperPickBound;

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
            firstGene = random.nextInt(genes.length - 2) + 1;
            secondGene = random.nextInt(genes.length - 2) + 1;
        }while (firstGene == secondGene);

        int temp = genes[firstGene];
        genes[firstGene] = genes[secondGene];
        genes[secondGene] = temp;

        return copy();
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

        int crossPoint = random.nextInt(genes.length - 1) + 2;

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

    public Individual copy(){
        int genesLength = this.getGenes().length;
        int[] newGenes = new int[genesLength];

        System.arraycopy(this.getGenes(), 0, newGenes, 0, genesLength);

        Individual newIndividual = new Individual(newGenes);
        newIndividual.setLength(this.length);

        return newIndividual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Individual that = (Individual) o;

        return Arrays.equals(genes, that.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    public int[] getGenes() {
        return genes;
    }

    public double getLength() {
        return length;
    }

    public double getReverseLength() {
        return reverseLength;
    }

    public double getLowerPickBound() {
        return lowerPickBound;
    }

    public double getUpperPickBound() {
        return upperPickBound;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setReverseLength(double reverseLength) {
        this.reverseLength = reverseLength;
    }

    public void setPickBounds(double lowerBound, double upperBound){
        lowerPickBound = lowerBound;
        upperPickBound = upperBound;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(genes).forEach(gene -> sb.append(gene).append(", "));

        return sb.toString() + "length =  " +length + ", reverse length = " +reverseLength
                +", Lower bound = " +lowerPickBound + ", upper bound = " +upperPickBound;
    }
}
