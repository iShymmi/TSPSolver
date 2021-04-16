package algorithm;

import java.util.ArrayList;
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

        long start = System.currentTimeMillis();
        long elapsedTime = 0;

        while(elapsedTime < stopCondition){
            ArrayList<Individual[]> selectedGroup = selectBreedAndMutationGroups();

            Individual[] breedGroup = selectedGroup.get(0);
            Individual[] mutationGroup = selectedGroup.get(1);

            descendants = new Individual[breedGroup.length + mutationGroup.length];

            breed(breedGroup,descendants);

            mutate(mutationGroup,descendants,breedGroup.length);

            for(Individual each : descendants){
                setLength(each);
            }

            selection();

            long end = System.currentTimeMillis();
            elapsedTime = end - start;
        }
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

        stream(population).forEach(this::setLength);
        pickBestIndividual();
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
            Individual randomIndividual = population[random.nextInt(population.length)];
            breedGroup.add(randomIndividual.copy());
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

        // Transforming length
        for(Individual each: descendants){
            transformedLength = each.getReverseLength() + worstIndividualLength + 0.01;
            each.setReverseLength(transformedLength);
            sumOfLengths += transformedLength;
        }

        // Setting pick bounds
        for(Individual each: descendants){
            ratio = each.getReverseLength() / sumOfLengths;
            each.setPickBounds(bound, bound += ratio);
        }

        // Picking random individual in bounds
        for(int i = 0; i < populationSize; i++){
            double randomPick = random.nextDouble();

            newPopulation[i] = getIndividualByBound(randomPick).copy();
        }

        if(pocketTrigger){
            newPopulation[populationSize - 1] = bestIndividual.copy();
        }

        population = newPopulation;
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

            if(descendants[i].getLength() <= descendants[bestIndividualIndex].getLength()){
                bestIndividualIndex = i;
            }

            if(descendants[i].getLength() > descendants[worstIndividualIndex].getLength()){
                worstIndividualIndex = i;
            }

            descendants[i].setReverseLength(descendants[i].getLength() * (-1));
        }

        // best individual need to store the best individual from every run
        if(descendants[bestIndividualIndex].getLength() < bestIndividual.getLength()){
            bestIndividual = descendants[bestIndividualIndex].copy();
        }

        // have to store worst individual length from current run
        worstIndividualLength = descendants[worstIndividualIndex].getLength();
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
                }
                return each;
            }
        }

        return descendants[descendants.length - 1];
    }

    private void setLength(Individual individual){
        double length = 0.0;
        int[] genes = individual.getGenes();

        for(int i = 0; i < genes.length - 1; i++){
            int firstPoint = genes[i];
            int secondPoint = genes[i + 1];
            length += distances[firstPoint][secondPoint];
        }

        individual.setLength(length);
    }

    public void pickBestIndividual(){
        if(bestIndividual == null){
            bestIndividual = population[0].copy();
        }

        for (Individual individual : population) {
            if (individual.getLength() < bestIndividual.getLength()) {
                bestIndividual = individual.copy();
            }
        }
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

        for(int i = 0; breedSize != 0; i += 2){
            do{
                firstIndividualIndex = random.nextInt(breedSize);
                secondIndividualIndex = random.nextInt(breedSize);
            }while (firstIndividualIndex == secondIndividualIndex);

            descendants[i] = breedGroup[firstIndividualIndex]
                                        .breed(breedGroup[secondIndividualIndex]);
            descendants[i + 1] = breedGroup[secondIndividualIndex]
                    .breed(breedGroup[firstIndividualIndex]);

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
}
