package main.java.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.stream;

public class TSPSolver {

    private double[][] distances;
    private int populationSize;
    private int startIndex;
    private int stopCondition;
    private double crossingPickProbability;
    private double mutationPickProbability;

    private Random random;
    private Individual[] population;
    private Individual[] descendants;
    private Individual bestIndividual;
    private double worstIndividualLength;
    private boolean pocketTrigger;
    private boolean isInit;

    public TSPSolver() { }

    public TSPSolver(double[][] distances, int startIndex, int populationSize, int stopCondition,
                     double crossingPickProbability, double mutationPickProbability) {
        isDataValid(distances);

        this.distances = distances;
        this.populationSize = populationSize;
        this.stopCondition = stopCondition;
        this.startIndex = startIndex;
        this.crossingPickProbability = crossingPickProbability;
        this.mutationPickProbability = mutationPickProbability;
    }

    public void run(){
        if(!isInit()){
            init();
        }

        for(int i = 0; i < stopCondition; i++){
            ArrayList<Individual[]> selectedGroup = selectBreedAndMutationGroups();

            Individual[] breedGroup = selectedGroup.get(0);
            Individual[] mutationGroup = selectedGroup.get(1);

            descendants = new Individual[(breedGroup.length / 2) + mutationGroup.length];

            breed(breedGroup,descendants);
            mutate(mutationGroup,descendants,breedGroup.length / 2);

            for(Individual each : descendants){
                setLength(each);
            }

            selection();

        }
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

    /**
     * Selects new population from descendants
     */
    private void selection(){
        double sumOfLengths = 0.0;
        double transformedLength;
        double bound = 0.0;
        double ratio;
        pocketTrigger = true;

        Individual[] newPopulation = new Individual[populationSize];
        prepareForSelection();

        for(Individual each: descendants){
            transformedLength = each.getReverseLength() + worstIndividualLength + 0.01;
            each.setReverseLength(transformedLength);
            sumOfLengths += transformedLength;
        }

        for(Individual each: descendants){
            ratio = each.getReverseLength() / sumOfLengths;
            each.setPickBounds(bound, bound += ratio);
        }

        for(int i = 0; i < populationSize; i++){
            double randomPick = random.nextDouble();

            newPopulation[i] = getIndividualByBound(randomPick);
        }
        System.out.println(pocketTrigger);
        if(pocketTrigger){
            System.out.println("Trigger on");
            newPopulation[populationSize - 1] = bestIndividual.copy();
        }

        System.out.println("###########NEW POPULATION###############");
        stream(newPopulation).forEach(System.out::println);

        population = newPopulation;
    }

    /**
     * Return copy of Individual which roulettePick value will be its between lower and upper bound
     * @param roulettePick random double from 0 to 1
     * @return copy of picked Individual
     */
    private Individual getIndividualByBound(double roulettePick){
        for(Individual each: descendants){
            if(roulettePick >= each.getLowerPickBound() && roulettePick < each.getUpperPickBound()){
                if(each.equals(bestIndividual)){
                    pocketTrigger = false;
                    System.out.println("EQUALS");
                }
                return each.copy();
            }
        }

        return descendants[descendants.length - 1].copy();
    }

    /**
     * Prepares variables before selection process
     * #1 Reverse length for each Individual from descendants
     * #2 Picks best individual (The one with shortest length) from all runs
     * #3 Picks worst individual length (The longest one) from current run
     */
    private void prepareForSelection(){
        int bestIndividualIndex = 0;
        int worstIndividualIndex = 0;

        for(int i = 0; i < descendants.length; i++){
            descendants[i].setReverseLength(descendants[i].getLength() * (-1));

            if(descendants[i].getLength() < descendants[bestIndividualIndex].getLength()){
                bestIndividualIndex = i;
            }

            if(descendants[i].getLength() > descendants[worstIndividualIndex].getLength()){
                worstIndividualIndex = i;
            }
        }

        // best individual need to store the best individual from every run
        if(bestIndividual == null){
            bestIndividual = descendants[bestIndividualIndex].copy();
        }else if(bestIndividual.getLength() > descendants[bestIndividualIndex].getLength()){
                bestIndividual = descendants[bestIndividualIndex].copy();
        }

        // have to store worst individual length from current run
        worstIndividualLength = descendants[worstIndividualIndex].getLength();
    }

    private void setLength(Individual individual){
        double length = 0.0;
        int[] genes = individual.getGenes();

        for(int i = 0; i < genes.length - 1; i++){
            length += distances[genes[i]][genes[i+1]];
        }

        individual.setLength(length);
    }

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
        if(distances.length == 0 || populationSize == 0 || startIndex == -1){
            throw new NullPointerException("Invalid one of parameters:"
                    + "\nDistances: " +((distances.length == 0) ? null : distances.length)
                    + "\nPopulation size: "+((populationSize == 0) ? null : populationSize)
                    + "\nStart index: "+((startIndex == -1) ? null : startIndex));
        }

        if(startIndex < 0 || startIndex > distances.length - 1){
            throw new IllegalArgumentException("Start index is out of bounds");
        }

        isInit = true;
        random = new Random();
        population = new Individual[populationSize];
        generatePopulation();
    }

    private void defaultSetup(){
        stopCondition = 1;
        mutationPickProbability = 0.5;
        crossingPickProbability = 0.5;
        startIndex = -1;
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

    public Individual getBestIndividual(){
        return bestIndividual;
    }

    public double[][] getDistances() {
        return distances;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public void setStopCondition(int stopCondition) {
        this.stopCondition = stopCondition;
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
        /*double[][] distances = new double[][]{
                {0 , 16, 47, 72, 77, 79},
                {16, 0 , 37, 57, 65, 66},
                {47, 37, 0 , 40, 30, 35},
                {72, 57, 40, 0 , 31, 23},
                {77, 65, 30, 31, 0 , 10},
                {79, 66, 35, 23, 10, 0 }
        }

        Individual individual1 = new Individual(points1);
        Individual individual2 = new Individual(points2);

        System.out.println(individual1.toString());
        System.out.println(individual2.toString());

        Individual individual3 = individual1.mutate();

        System.out.println(individual3.toString());*//*

        TSPSolver tspSolver = new TSPSolver(distances,0,10,1,0.5,0.5);

        tspSolver.run();

        Arrays.stream(tspSolver.descendants).forEach(System.out::println);

        System.out.println("\n############### BEST ##############");
        System.out.println(tspSolver.bestIndividual);*/

        //*int[] points1 = {1,3,2,1};
        int[] points = {1,3,2,1};

        Individual individual1 = new Individual(points);
        System.out.println(individual1);
        Individual individual2 = individual1.copy();
        System.out.println(individual2);
        individual1.mutate();

        System.out.println("\n"+ individual1);
        System.out.println(individual2);
    }
}
