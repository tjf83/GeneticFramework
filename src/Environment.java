import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Trevor on 12/10/16.
 */
public class Environment {

    float weightCenter;       //average - 420 lbs
    float speedCenter;        //top - 50 mph
    float strengthCenter;     //jaw strength - 650 psi
    float intelligenceCenter; //average - 100
    ArrayList<Float> furColorModifiers;
    Lion.Gender gender;

    public Environment (float weightCenter, float speedCenter, float strengthCenter,
                        float intelligenceCenter, ArrayList<Float> furColorModifiers) {

        setWeightCenter(weightCenter);
        setSpeedCenter(speedCenter);
        setStrengthCenter(strengthCenter);
        setIntelligenceCenter(intelligenceCenter);
        setFurColorModifiers(furColorModifiers);
    }

    public float scoreValues(float value, float center) {
        return (1 - ((value / center) - 1) * ((value / center) - 1));
    }

    //If the case doesn't match anything, the lion dies. (fitness = 0)
    public float furColorFitnessMap(Lion.FurColor c){
        switch (c) {
            case RED:
                return getFurColorModifiers().get(0);
            case BLACK:
                return getFurColorModifiers().get(1);
            case BROWN:
                return getFurColorModifiers().get(2);
            case TAWNY:
                return getFurColorModifiers().get(3);
            default:
                return (float) 0;
        }
    }

    //Evaluate a single lion's fitness
    public float evaluateFitness(Lion lion) {
        float result;

        float weightScore = scoreValues(lion.weight, getWeightCenter());
        float speedScore = scoreValues(lion.speed, getSpeedCenter());
        float strengthScore = scoreValues(lion.strength, getStrengthCenter());
        float intelligenceScore = scoreValues(lion.intelligence, getIntelligenceCenter());
        float furScore = furColorFitnessMap(lion.furcolor);

        result = weightScore * speedScore * strengthScore * intelligenceScore * furScore;

        return result;
    }

    public Lion breedLions(Lion male, Lion female, float heatScore) {


        float weight = average(male.weight, female.weight) + heatModifier(getWeightCenter(), heatScore);
        float speed = average(male.speed, female.speed) + heatModifier(getSpeedCenter(), heatScore);
        float strength = average(male.strength, female.strength) + heatModifier(getStrengthCenter(), heatScore);
        float intel = average(male.intelligence, female.intelligence) + heatModifier(getStrengthCenter(), heatScore);

        Lion.FurColor furColor = randomFurColor(male.furcolor, female.furcolor);
        Lion.Gender gender = randomGender();

        return new Lion(weight, speed, strength, intel, furColor, gender);

    }

    public float heatModifier(float center, float heatScore) {
        Random rn = new Random();

        float ratio = (rn.nextFloat() * 2) - 1;

        //Chooses degree to which heat affects modification
        float modification = center * ratio * heatScore / 2;

        return modification;


    }

    public Lion generateNewLion() {
        Random rn = new Random();

        float weight = (2 * rn.nextFloat()) * getWeightCenter();
        float speed = (2* rn.nextFloat()) * getSpeedCenter();
        float strength = (2* rn.nextFloat()) * getStrengthCenter();
        float intelligence = (2 * rn.nextFloat()) * getIntelligenceCenter();
        Lion.FurColor furColor;
        Lion.Gender gender;
        float furFloat = 4*rn.nextFloat();

        if (furFloat < 1) {
            furColor =  Lion.FurColor.BLACK;
        }
        else if (furFloat < 2) {
            furColor = Lion.FurColor.BROWN;
        }
        else if (furFloat < 3) {
            furColor = Lion.FurColor.RED;
        }
        else {
            furColor = Lion.FurColor.TAWNY;
        }

        gender = randomGender();

        return new Lion(weight, speed, strength, intelligence, furColor, gender);
    }

    Lion.FurColor randomFurColor(Lion.FurColor a, Lion.FurColor b) {

        Random rn = new Random();

        Float guess = 2 * rn.nextFloat();

        if (guess < 1) {
            return a;
        }
        else {
            return b;
        }
    }

    Lion.Gender randomGender() {

        Random rn = new Random();

        Float guess = 2 * rn.nextFloat();

        if (guess < 1) {
            return Lion.Gender.FEMALE;
        }
        else {
            return Lion.Gender.MALE;
        }
    }

    //Helper functions
    public float average(float a, float b) {
        return (a + b) / 2;
    }
    public float getWeightCenter() {
        return weightCenter;
    }
    public void setWeightCenter(float weightCenter) {
        this.weightCenter = weightCenter;
    }
    public float getSpeedCenter() {
        return speedCenter;
    }
    public void setSpeedCenter(float speedCenter) {
        this.speedCenter = speedCenter;
    }
    public float getStrengthCenter() {
        return strengthCenter;
    }
    public void setStrengthCenter(float strengthCenter) {
        this.strengthCenter = strengthCenter;
    }
    public ArrayList<Float> getFurColorModifiers() {
        return furColorModifiers;
    }
    public void setFurColorModifiers(ArrayList<Float> furColorModifiers) {
        this.furColorModifiers = furColorModifiers;
    }
    public Lion.Gender getGender() {
        return gender;
    }
    public void setGender(Lion.Gender gender) {
        this.gender = gender;
    }
    public float getIntelligenceCenter() {

        return intelligenceCenter;
    }
    public void setIntelligenceCenter(float intelligenceCenter) {
        this.intelligenceCenter = intelligenceCenter;
    }
}
