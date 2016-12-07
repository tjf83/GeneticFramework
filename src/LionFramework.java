import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sahityamantravadi on 12/7/16.
 */
public class LionFramework {

    private int maxGenerations;
    private float heatScore;
    private int carryingCapacity;
    private Population<Float> population;
    Parabola parabola;

    /* Runs the simulation with the established parameters */
    public static void main(String args[]) {

        Parabola parabola = new Parabola(4, 11, 3);

        LionFramework vertexFinder = new LionFramework(100, 100, 1000, parabola);
        for (int i = 0; i < vertexFinder.getMaxGenerations(); i++) {
            vertexFinder.breedPopulation();
        }
        System.out.print(vertexFinder.findMaxFitness());
        System.out.print("\n");
        System.out.print(parabola.evaluateFitness(vertexFinder.findMaxFitness()));
    }

    /* Takes in an existing population and generates a new generation */
    public void breedPopulation() {
        setPopulation(generatePopulation(findMaxFitness()));
        heatCooldown();
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
            float score = getParabola().evaluateFitness(getPopulation().getMembers().get(i));
            fitnessScore.add(score);
        }
        return fitnessScore;
    }

    /* Function for reducing heat, allowing genetic algorithm to keep closing in on correct value */
    public void heatCooldown() {
        setHeatScore((float) (getHeatScore() * .9));
    }


    /* Constructor for ParabolaMaxFinder */
    public LionFramework(int maxGenerations, float heatScore, int carryingCapacity, Parabola parabola) {
        setMaxGenerations(maxGenerations);
        setHeatScore(heatScore);
        setCarryingCapacity(carryingCapacity);

        setParabola(parabola);

        setPopulation(generatePopulation((float) 0));
    }


    /*Getter and Setter methods for ParabolaMaxFinder*/

    private Parabola getParabola() {
        return this.parabola;
    }

    private void setParabola(Parabola parabola) {
        this.parabola = parabola;
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

    private Population<Float> getPopulation() {
        return this.population;
    }

    private void setPopulation(Population<Float> population) {
        this.population = population;
    }

}
