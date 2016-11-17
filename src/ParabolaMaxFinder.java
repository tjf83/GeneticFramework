import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Trevor on 11/8/16.
 */
public class ParabolaMaxFinder implements GeneticFramework {

    private int maxGenerations;
    private float heatScore;
    private int carryingCapacity;
    private Population<Float> population;


    /* Runs the simulation with the established parameters */
    public static void main(String args[] ) {

        ParabolaMaxFinder maxFinder = new ParabolaMaxFinder(100, 100, 100);
        for (int i = 0; i < maxFinder.getMaxGenerations(); i++) {
            maxFinder.breedPopulation();
            System.out.print(maxFinder.findMaxFitness());

        }
        System.out.print(maxFinder.findMaxFitness());
    }

    /* Takes in an existing population and generates a new generation */
    public void breedPopulation() {
        setPopulation(generatePopulation(findMaxFitness()));
    }

    /* Takes in a population mean and range and returns a new population
     * Helper function for breedPopulation */
    private Population<Float> generatePopulation(Float mean) {

        Random random = new Random();
        ArrayList<Float> members = new ArrayList<>();

        for (int i = 0; i < getCarryingCapacity(); i++) {
            Float randomNumber = ((random.nextFloat() - (float) .5) * getHeatScore()) + mean;
            members.add(randomNumber);
        }

        heatCooldown();

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
    public ArrayList<Float> evaluateFitness(Population population) {
        ArrayList<Float> fitnessScore = new ArrayList<>();

        for (int i = 0; i < getCarryingCapacity(); i++) {
            float value = getPopulation().getMembers().get(i);

            /* This is where the parabola is definedv*/
            float score = -1 * value * value + 1000;
            fitnessScore.add(score);
        }
        return fitnessScore;
    }

    /* Function for reducing heat, allowing genetic algorithm to keep closing in on correct value */
    public void heatCooldown() {
        setHeatScore((float) (getHeatScore() * .9));
    }


    /* Constructor for ParabolaMaxFinder */
    public ParabolaMaxFinder(int maxGenerations, float heatScore, int carryingCapacity) {
        setMaxGenerations(maxGenerations);
        setHeatScore(heatScore);
        setCarryingCapacity(carryingCapacity);

        setPopulation(generatePopulation((float) 0));
    }


    /*Getter and Setter methods for ParabolaMaxFinder*/
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

    private Population<Float> getPopulation() {
        return this.population;
    }

    private void setPopulation(Population<Float> population) {
        this.population = population;
    }
}

