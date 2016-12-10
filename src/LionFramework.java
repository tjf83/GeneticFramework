import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * Created by sahityamantravadi on 12/7/16.
 */
public class LionFramework {

    private float heatScore;
    private int carryingCapacity;
    private int breedingPoolSize;
    private Population<Lion> population;
    private Environment environment;

    /* Runs the simulation with the established parameters */
    public static void main(String args[]) {

        //Input values for fur colors

        ArrayList furColorModifiers = new ArrayList<Float>();
        furColorModifiers.add((float) .9970); //Red
        furColorModifiers.add((float) .9975);  // Black
        furColorModifiers.add((float) .998); // Brown
        furColorModifiers.add((float) 1);   //Tawny

        //Input values for environmental evolutionary goals
        Float bestWeight = (float) 420;
        Float bestSpeed = (float) 50;
        Float bestStrength = (float) 650;
        Float bestInt = (float) 100;
        Environment environment = new Environment(bestWeight, bestSpeed, bestStrength, bestInt, furColorModifiers);

        //Input Values for Simulation Parameters
        //Heat score should start at 1 (Generally, see breed function in environment)
        int maxGenerations = 20;
        float heatScore = (float) 1;
        int carryingCapacity = 100;
        int breedingPopulation = 20;

        LionFramework lionFramework = new LionFramework(heatScore, carryingCapacity,
                breedingPopulation, environment);

        //Run the simulation (Repeatedly breed population and reduce heat)
        for (int i = 0; i < maxGenerations; i++) {
            lionFramework.breedPopulation();
            lionFramework.heatCooldown();
        }

        Population<Lion> finalPopulation = lionFramework.getPopulation();

        for (int i = 0; i < 10; i++) {
            lionFramework.printLion(finalPopulation.getMembers().get(i));
        }

    }

    //Prints a lion to the terminal
    public void printLion(Lion lion) {

        int weight = (int) Math.floor((double) lion.weight);
        int speed = (int) Math.floor((double) lion.speed);
        int strength = (int) Math.floor((double) lion.strength);
        int intelligence = (int) Math.floor((double) lion.intelligence);

        String genderString = "";

        switch (lion.gender) {
            case MALE:
                genderString = "Male"; break;
            case FEMALE:
                genderString = "Female"; break;
        }

        String furColorString = "";

        switch (lion.furcolor) {
            case BLACK:
                furColorString = "Black"; break;
            case BROWN:
                furColorString = "Brown"; break;
            case RED:
                furColorString = "Red"; break;
            case TAWNY:
                furColorString = "Tawny"; break;
        }

        String stats = String.format("\n Time to talk about another lion! This one is %s with the following stats;" +
                        "\n Weight: %d" +
                        "\n Running Top Speed: %d " +
                        "\n Peak Bite Force: %d " +
                        "\n Sociability: %d " +
                        "\n Fur Color: %s \n",
                genderString, weight, speed, strength, intelligence, furColorString);

        System.out.print(stats);
    }

    /* Takes in an existing population and generates a new generation */
    public void breedPopulation() {

        //Step 1. Evaluate all Fitness, choose N best lions
       Population<Lion> lionBreedingPopulation = generateBreedingPopulation();

        //Step 2. Breed lions back to carrying capacity
        Population<Lion> newPopulation = restoreToCarryingCapacity(lionBreedingPopulation);

        //Set population as new population
        setPopulation(newPopulation);
    }

    /* Creates the breeding population by selecting all max fitness individuals */
    private Population<Lion> generateBreedingPopulation() {

        //Step 1. Evaluate all Fitness, choose N best lions
        ArrayList<Float> fitnessScores = evaluateFitness();

        Population<Lion> lionsTryingToMate = getPopulation();
        Population<Lion> lionBreedingPool = new Population<>();
        while (lionBreedingPool.getMembers().size() < getBreedingPoolSize()) {

            //Track max fitness individual and where it is
            float maxFitness = fitnessScores.get(0);
            int index = 0;

            for (int j = 0; j < fitnessScores.size(); j++) {
                if (fitnessScores.get(j) > maxFitness) {
                    maxFitness = fitnessScores.get(j);
                    index = j;
                }
            }

            //Add individual to breeding pool
            Lion nextFittestLion = lionsTryingToMate.getMembers().get(index);
            lionBreedingPool.getMembers().add(nextFittestLion);

            //Remove from lists for future iterations
            lionsTryingToMate.getMembers().remove(index);
            fitnessScores.remove(index);
        }

        return lionBreedingPool;
    }

    /* Generates a randomized lion population using the environment's generation function */
    private Population<Lion> generateInitialPopulation() {

        ArrayList<Lion> members = new ArrayList<>();
        for (int i = 0; i < getCarryingCapacity(); i++) {
            members.add(getEnvironment().generateNewLion());
        }

        return new Population<>(members);
    }

    /* Takes in a breeeding population and randomly mates lions until carrying capacity */
    private Population<Lion> restoreToCarryingCapacity(Population<Lion> breedingPopulation) {

        Random rn = new Random();
        Population<Lion> nextGeneration = new Population<>();

        //Randomly choose two lions, make them breed and add to the pool
        for (int i = 0; i < carryingCapacity; i++) {
            int maleLionIndex = (int) Math.floor(rn.nextDouble() * breedingPopulation.getMembers().size());
            Lion maleLion = breedingPopulation.getMembers().get(maleLionIndex);

            while (maleLion.gender != Lion.Gender.MALE) {
                maleLionIndex = (int) Math.floor(rn.nextDouble() * breedingPopulation.getMembers().size());
                maleLion = breedingPopulation.getMembers().get(maleLionIndex);

            }

            int femaleLionIndex = (int) Math.floor(rn.nextDouble() * breedingPopulation.getMembers().size());
            Lion femaleLion = breedingPopulation.getMembers().get(femaleLionIndex);

            //No lion self love (Can be used to control Gender as well)
            while ((maleLion.gender == femaleLion.gender)){

                femaleLionIndex = (int) Math.floor(rn.nextDouble() * breedingPopulation.getMembers().size());
                femaleLion = breedingPopulation.getMembers().get(femaleLionIndex);
            }

            Lion babyLion = getEnvironment().breedLions(maleLion, femaleLion, getHeatScore());

            nextGeneration.getMembers().add(babyLion);
        }

        return nextGeneration;
    }

    /* Fitness evaluation function. Returns an arraylist with all fitness scores. */
    public ArrayList<Float> evaluateFitness() {
        ArrayList<Float> fitnessScore = new ArrayList<>();

        for (int i = 0; i < getCarryingCapacity(); i++) {

            Lion lion = getPopulation().getMembers().get(i);
            float fitness = getEnvironment().evaluateFitness(lion);
            fitnessScore.add(fitness);
        }

        return fitnessScore;
    }

    /* Function for reducing heat, allowing genetic algorithm to keep closing in on correct value */
    public void heatCooldown() {
        setHeatScore((float) (getHeatScore() * .9));
    }


    /* Constructor for LionFramework */
    public LionFramework(float heatScore, int carryingCapacity,
                         int breedingPopulation, Environment environment) {

        setHeatScore(heatScore);
        setCarryingCapacity(carryingCapacity);
        setBreedingPoolSize(breedingPopulation);
        setEnvironment(environment);

        //Generates the Initial Population for the simulation
        setPopulation(generateInitialPopulation());
    }


    private void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private Environment getEnvironment() {
        return environment;
    }

    private float getHeatScore() {
        return this.heatScore;
    }

    private void setHeatScore(float heatScore) {
        this.heatScore = heatScore;
    }

    private int getCarryingCapacity() {
        return this.carryingCapacity;
    }

    private void setBreedingPoolSize(int breedingPoolSize) {
        this.breedingPoolSize = breedingPoolSize;
    }

    private int getBreedingPoolSize() {
        return this.breedingPoolSize;
    }

    private void setCarryingCapacity(int carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }

    private Population<Lion> getPopulation() {
        return this.population;
    }

    private void setPopulation(Population<Lion> population) {
        this.population = population;
    }

}
