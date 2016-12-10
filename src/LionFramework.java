import java.util.ArrayList;

/**
 * Created by sahityamantravadi on 12/7/16.
 */
public class LionFramework {

    private int maxGenerations;
    private float heatScore;
    private int carryingCapacity;
    private Population<Lion> population;
    private Environment environment;

    /* Runs the simulation with the established parameters */
    public static void main(String args[]) {

        //Input values for fur colors
        ArrayList furColorModifiers = new ArrayList<Float>();
        furColorModifiers.add(.85); //Red
        furColorModifiers.add(.8); // Black
        furColorModifiers.add(.95); // Brown
        furColorModifiers.add(1);  //Tawny

        //Input values for environmental evolutionary goals
        Float bestWeight = (float) 420;
        Float bestSpeed = (float) 50;
        Float bestStrength = (float) 650;
        Float bestInt = (float) 100;
        Environment environment = new Environment(bestWeight, bestSpeed, bestStrength, bestInt, furColorModifiers);

        //Input Values for Simulation Parameters
        int maxGenerations = 20;
        float heatScore = (float) 50;
        int carryingCapacity = 100;

        LionFramework lionFramework = new LionFramework(maxGenerations, heatScore, carryingCapacity, environment);

    }

    /* Takes in an existing population and generates a new generation */
    public void breedPopulation() {
        setPopulation(generatePopulation(findMaxFitness()));
        heatCooldown();
    }

    /* Takes in a population mean and range and returns a new population
     * Helper function for breedPopulation */
    private Population<Lion> generatePopulation(Environment environment) {

        ArrayList<Lion> members = new ArrayList<>();

        for (int i = 0; i < getCarryingCapacity(); i++) {
            members.add(environment.generateNewLion());
        }

        return new Population<>(members);
    }

    /* Returns the individual with the highest fitness */
    private Float findMaxFitness() {

        ArrayList<Float> fitnessScore = evaluateFitness(getPopulation());

        float maxFitness = fitnessScore.get(0);
        float value = getPopulation().getMembers().get(0);

        for (int i = 1; i < getCarryingCapacity(); i++) {
            if (fitnessScore.get(i) > maxFitness) {
                maxFitness = fitnessScore.get(i);
                value = getPopulation().getMembers().get(i);
            }
        }

        return value;
    }

    /* Fitness evaluation function. Returns an arraylist with all fitness scores.
       Contains logic determining parabola being analyzed
     * Helper function for findMaxFitness */
    public ArrayList<Float> evaluateFitness() {
        ArrayList<Float> fitnessScore = new ArrayList<>();

        for (int i = 0; i < getCarryingCapacity(); i++) {
            float score = getEnvironment().evaluateFitness(getPopulation().getMembers().get(i));
            fitnessScore.add(score);
        }
        return fitnessScore;
    }

    /* Function for reducing heat, allowing genetic algorithm to keep closing in on correct value */
    public void heatCooldown() {
        setHeatScore((float) (getHeatScore() * .9));
    }


    /* Constructor for LionFramework */
    public LionFramework(int maxGenerations, float heatScore, int carryingCapacity, Environment environment) {
        setMaxGenerations(maxGenerations);
        setHeatScore(heatScore);
        setCarryingCapacity(carryingCapacity);
        setEnvironment(environment);
        setPopulation(generatePopulation(environment));
    }


    /*Getter and Setter methods for ParabolaMaxFinder*/

    private void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private Environment getEnvironment() {
        return environment;
    }

    private int getMaxGenerations() {
        return this.maxGenerations;
    }

    private void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
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
