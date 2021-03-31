package main.java.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TSPSolver {

    private int populationSize;
    private int generationAmount;
    private int startIndex = -1;
    private boolean isInit;
    private double crossingPickProbability;
    private double mutationPickProbability;
    private Random random;

    private double[][] distances;
    private Individual[] population;
    private Individual[] descendants;

    public TSPSolver() { }

    public TSPSolver(double[][] distances, int populationSize, int generationAmount, int startIndex) {
        isDataValid(distances);

        this.distances = distances;
        this.populationSize = populationSize;
        this.generationAmount = generationAmount;
        this.startIndex = startIndex;
    }

    public void run(){
        if(!isInit()){
            init();
        }

        ArrayList<Individual[]> selectedGroup = selectBreedAndMutationGroups();

        Individual[] breedGroup = selectedGroup.get(0);
        Individual[] mutationGroup = selectedGroup.get(1);

        descendants = new Individual[(breedGroup.length / 2) + mutationGroup.length];

        breed(breedGroup,descendants);
        mutate(mutationGroup,descendants,breedGroup.length / 2);
    }

    /**
     * Select breed and mutation group based on pick ratio
     * @return ArrayList(0) - breed group, ArraList(1) - mutation group
     */
    private ArrayList<Individual[]> selectBreedAndMutationGroups(){
        List<Individual> breedGroup = new ArrayList<>((int)(populationSize * crossingPickProbability));
        List<Individual> mutationGroup = new ArrayList<>((int)(populationSize * mutationPickProbability));
        ArrayList<Individual[]> selectedGorups = new ArrayList<>(2);

        for(Individual each: population){
            if(random.nextDouble() < crossingPickProbability){
                breedGroup.add(each);
            }

            if(random.nextDouble() < mutationPickProbability){
                mutationGroup.add(each);
            }
        }

        if(breedGroup.size()  % 2 != 0){
            int[] genesOfRandomIndividual = population[random.nextInt(population.length)].getGenes();
            Individual copiedIndividual = new Individual(genesOfRandomIndividual);
            breedGroup.add(copiedIndividual);
        }

        selectedGorups.add(breedGroup.toArray(Individual[]::new));
        selectedGorups.add(mutationGroup.toArray(Individual[]::new));

        return selectedGorups;
    }

    private void selection(){}

    private void countLength(){}

    /**
     * Pick and breed every 2 random Individuals and adds to descendatns array
     * @param breedGroup array with Individual to breed
     * @param descendants descendants array
     */
    private void breed(Individual[] breedGroup,Individual[] descendants){
        int breedSize = breedGroup.length;
        int firstIndividualIndex;
        int secondIndividualIndex;

        for(int i = 0; breedSize != 0; i++){
            do{
                firstIndividualIndex = random.nextInt(breedSize);
                secondIndividualIndex = random.nextInt(breedSize);
            }while (firstIndividualIndex == secondIndividualIndex);

            descendants[i] = breedGroup[firstIndividualIndex]
                                        .breed(breedGroup[secondIndividualIndex]);

            swap(breedGroup,breedSize - 1, firstIndividualIndex);
            swap(breedGroup, breedSize - 2, secondIndividualIndex);

            breedSize -= 2;
        }
    }

    /**
     * Mutate every Individual and adds it to descendats array
     * @param mutationGroup array with Individual to mutate
     * @param descendants descendants array
     * @param index next avaiable index of descendats array
     */
    private void mutate(Individual[] mutationGroup,Individual[] descendants,int index){
        for (Individual individual : mutationGroup) {
            descendants[index] = individual.mutate();
            index++;
        }
    }

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

        if(startIndex < 0 || startIndex > distances.length - 1){
            throw new IllegalArgumentException("Start index is out of bounds");
        }

        isInit = true;
        random = new Random();
        population = new Individual[populationSize];
    }

    private void swap(Individual[] array, int firstIndex, int secondIndex){
        Individual temp;

        temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;
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

    public boolean isInit(){
        return isInit;
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

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setCrossingPickProbability(double crossingPickProbability) {
        this.crossingPickProbability = crossingPickProbability;
    }

    public void setMutationPickProbability(double mutationPickProbability) {
        this.mutationPickProbability = mutationPickProbability;
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

        /*int[] points1 = {1,3,2,1};
        int[] points2 = {1,3,2,1};

        Individual individual1 = new Individual(points1);
        Individual individual2 = new Individual(points2);

        System.out.println(individual1.toString());
        System.out.println(individual2.toString());

        Individual individual3 = individual1.mutate();

        System.out.println(individual3.toString());*/

        TSPSolver tspSolver = new TSPSolver();
        tspSolver.setDistances(distances);
        tspSolver.setStartIndex(5);
        tspSolver.setGenerationAmount(100);
        tspSolver.setPopulationSize(10);
        tspSolver.setCrossingPickProbability(1);
        tspSolver.setMutationPickProbability(1);
        tspSolver.init();
        tspSolver.generatePopulation();

        tspSolver.run();

        Arrays.stream(tspSolver.descendants).forEach(System.out::println);
    }
}
