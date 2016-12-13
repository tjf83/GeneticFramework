import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sahityamantravadi on 12/7/16.
 */
public class LionFramework {

    private float heatScore;
    private int carryingCapacity;
    private int breedingPoolSize;

    private int maxGenerations;
    private Population<Lion> population;
    private Environment environment;

    /* Runs the simulation with the established parameters */
    public static void main(String args[]) {

        /*
         *A couple of ideas to possibly try...
         * 1. Make a male with suboptimal fur color w/ perfect fitness. See effect on fur color distribution
         *      ->Done in generateInitialPopulation*
         * 2. Implement Prides to show different effect of males on spread of sub-optimal colors
         *      ->The thinking here being if 1 male mates w/ 5 females, his genes will go farther
         *      ->Done in breeding step
         * 3. Possibly add a second fur-color-like trait. See how that affects it
         *      -> Could end up being "too much data"
         * 4.  ???
         */


        //Generates environment. Manipulate parameters inside function
        Environment environment = setupEnvironment();

        //Input Values for Simulation Parameters
        LionFramework lionFramework = setupLionFramework(environment);

        String csvString = "";

        //Run the simulation (Repeatedly breed population and reduce heat)
        for (int i = 0; i < lionFramework.getMaxGenerations(); i++) {
            lionFramework.breedPopulation();

            //Write as CSV object
            String newPopCsv = lionFramework.writePopulationAsCsv(lionFramework.getPopulation(), i);
            csvString += newPopCsv;

            lionFramework.heatCooldown();
        }

        String fileName = "file.txt";

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(csvString);
            out.close();
        }
        catch (IOException e) {
        }


        Population<Lion> finalPopulation = lionFramework.getPopulation();
        for (int i = 0; i < 10; i++) {
            lionFramework.printLion(finalPopulation.getMembers().get(i));
        }

    }

    //Static helper setup function for Environment
    public static Environment setupEnvironment() {

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
        return new Environment(bestWeight, bestSpeed, bestStrength, bestInt, furColorModifiers);
    }

    //Static helper setup function for LionFramework
    public  static LionFramework setupLionFramework(Environment environment) {

        //Heat score should start at 1 (Generally, see breed function in environment)
        int maxGenerations = 20;
        float heatScore = (float) 1;
        int carryingCapacity = 100;
        int breedingPopulation = 20;

        return new LionFramework(maxGenerations, heatScore, carryingCapacity, breedingPopulation, environment);
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

    //Writes a Lion as a CSV object
    public String writePopulationAsCsv(Population<Lion> population, int generation) {

        String nextCsv = "";
        for (int i = 0; i < population.getMembers().size(); i++) {
            nextCsv += writeLionAsCsv(population.getMembers().get(i), generation);
        }
        return nextCsv;
    }

    public String writeLionAsCsv(Lion lion, int generation) {

        //Order is Weight, Speed, Strength, Intelligence, Gender, FurColor, Generation

        int weight = (int) lion.weight;
        int speed = (int) lion.speed;
        int strength = (int) lion.strength;
        int intelligence = (int) lion.intelligence;

        String weightStr = String.valueOf(weight);
        String speedStr = String.valueOf(speed);
        String strengthStr = String.valueOf(strength);
        String intelligenceStr = String.valueOf(intelligence);
        String generationStr = String.valueOf(generation);

        String gender;
        String furColor;

        switch(lion.gender) {
            case MALE:
                gender = "Male";
                break;
            case FEMALE:
                gender = "Female";
                break;
            default:
                gender = "Whatever";
        }

        switch(lion.furcolor) {
            case TAWNY:
                furColor = "Tawny";
                break;
            case BLACK:
                furColor = "Black";
                break;
            case BROWN:
                furColor = "Brown";
                break;
            case RED:
                furColor = "Red";
                break;
            default:
                furColor = "Whatever";
        }

        String lionString = weightStr + "\t" + speedStr + "\t" + strengthStr + "\t" + intelligenceStr + "\t" +
                gender + "\t" +furColor + "\t" + generationStr + "\n";


        return lionString;
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

        //Perfect fitness male lion
        Lion perfectLion = new Lion(420, 50, 650, 100, Lion.FurColor.TAWNY, Lion.Gender.MALE);

        members.add(perfectLion);

        while (members.size() < getCarryingCapacity()) {
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
    public LionFramework(int maxGenerations, float heatScore, int carryingCapacity,
                         int breedingPopulation, Environment environment) {

        setHeatScore(heatScore);
        setCarryingCapacity(carryingCapacity);
        setBreedingPoolSize(breedingPopulation);
        setMaxGenerations(maxGenerations);
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


    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }


    private Population<Lion> getPopulation() {
        return this.population;
    }

    private void setPopulation(Population<Lion> population) {
        this.population = population;
    }

}
