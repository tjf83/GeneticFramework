import java.util.ArrayList;


/**
 * Created by Trevor on 11/8/16.
 */
public interface GeneticFramework {


    ArrayList<Float> evaluateFitness(Population population);

    void breedPopulation();

    void heatCooldown();
}
