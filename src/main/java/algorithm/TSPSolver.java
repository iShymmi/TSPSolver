package main.java.algorithm;

import java.util.Arrays;
import java.util.Random;

public class TSPSolver {

    private int populationSize;
    private int generationAmount;
    private int startIndex = -1;

    private double[][] distances;
    private Individual[] population;

    public TSPSolver(double[][] distances, int populationSize, int generationAmount, int startIndex) {
        isDataValid(distances);

        this.distances = distances;
        this.populationSize = populationSize;
        this.generationAmount = generationAmount;
        this.startIndex = startIndex;
    }

    public TSPSolver() {
    }

    private void selection(){}

    private void countLength(){}

    /**
        Generates individuals with a random arrangement of genes
     */
    private void generatePopulation(){

        for(int i = 0; i < population.length; i++){
            population[i] = new Individual(shuffleGenes());
        }
    }

    /**
        Shuffle genes
        First and last gene indicates the starting point

        return int[] matrix with genes
     */
    private int[] shuffleGenes(){
        int genesAmount = distances.length;

        int [] genesIndexes = new int[genesAmount + 1];
        genesIndexes[0] = genesIndexes[genesAmount] = startIndex;

        Random random = new Random();

        int index = 0;
        for(int i = 1; i < genesIndexes.length - 1; i++){
            genesIndexes[i] = (i == startIndex + 1) ? ++index : index;
            index++;
        }

        for(int i = 1; i < genesIndexes.length -1; i++){
            int randomIndex = random.nextInt((genesIndexes.length - 2)) + 1;
            int temp = genesIndexes[randomIndex];
            genesIndexes[randomIndex] = genesIndexes[i];
            genesIndexes[i] = temp;
        }

        return genesIndexes;
    }

    private void drawCorssAndMutationProbability(){}

    /**
        Checks if any of the required variables is empty
        If it pass validation it inits the population matrix with value of populationSize

        throws NullPointerException() if any of argument isn't setted
     */

    private void init(){
        if(distances.length == 0 || populationSize == 0 || generationAmount == 0 || startIndex == -1){
            throw new NullPointerException("Invalid one of parameters:"
                    + "\nDistances: " +((distances.length == 0) ? null : distances.length)
                    + "\nPopulation size: "+((populationSize == 0) ? null : populationSize)
                    + "\nGeneration abount: "+((generationAmount == 0) ? null : generationAmount)
                    + "\nStart index: "+((startIndex == -1) ? null : startIndex));
        }

        population = new Individual[populationSize];
    }

    /**
        Checks if provided distances matix has size [n][n]
        It doesn't check if distance[n][m] == distance[m][n] because in real case it can be different

        throws new IllegalArgumentExpception() if its invalid
     */
    private void isDataValid(double[][] data){
        if(data.length < 3){
            throw new IllegalArgumentException("Required minimum 3 points");
        }
        for (double[] distance : data) {
            if (data.length != distance.length) {
                throw new IllegalArgumentException("Incorrect data size");
            }
        }
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public double[][] getDistances() {
        return distances;
    }

    public int getGenerationAmount() {
        return generationAmount;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setGenerationAmount(int generationAmount) {
        this.generationAmount = generationAmount;
    }

    public void setDistances(double[][] distances) {
        isDataValid(distances);

        this.distances = distances;
    }

    //Todo validation if startIndex in range (0,distances.length)
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public static void main(String... args){
        double[][] distances = new double[][]{
                {1.0, 2.0, 3.0, 44, 22, 33},
                {1.0, 2.0, 3.0, 44, 22, 33},
                {1.0, 2.0, 3.0, 44, 22, 33},
                {1.0, 2.0, 3.0, 44, 22, 33},
                {1.0, 2.0, 3.0, 44, 22, 33},
                {1.0, 2.0, 3.0, 44, 22, 33}
        };

        int[] points1 = {1,3,2,1};
        int[] points2 = {1,3,2,1};

        Individual individual1 = new Individual(points1);
        Individual individual2 = new Individual(points2);

        System.out.println(individual1.toString());
        System.out.println(individual2.toString());

        Individual individual3 = individual1.mutate();

        System.out.println(individual3.toString());

        TSPSolver tspSolver = new TSPSolver();
        tspSolver.setDistances(distances);
        tspSolver.setStartIndex(5);
        tspSolver.setGenerationAmount(100);
        tspSolver.setPopulationSize(100);
        tspSolver.init();
        tspSolver.generatePopulation();

        Arrays.stream(tspSolver.population).forEach(System.out::println);
    }
}