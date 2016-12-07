/**
 * Created by sahityamantravadi on 12/7/16.
 */
import java.lang.Math.*;

public class Lion {

    public enum FurColor {
        RED, BROWN, BLACK, TAWNY
    }

    public enum Gender {
        FEMALE, MALE
    }

    float weight;       //average - 420 lbs
    float speed;        //top - 50 mph
    float strength;     //jaw strength - 650 psi
    float intelligence; //average - 100
    FurColor furcolor;
    Gender gender;


    public Lion (float wt, float sp, float str, float in, FurColor fc, Gender g) {
        weight = wt;
        speed = sp;
        strength = str;
        intelligence = in;
        furcolor = fc;
        gender = g;
    }

    public float furColorFitnessMap(FurColor c){
        switch (c) {
            case RED:
                return (float) 0.8;
                break;
            case BLACK:
                return (float) 0.85;
                break;
            case BROWN:
                return (float) 0.95;
                break;
            case TAWNY:
                return (float) 1;
                break;
            default:
                return (float) 1;
                break;
        }
    }

    public float findScore(float value, float center){
        return (1 - ((value / center) - 1) * ((value / center) - 1));
    }

    public float evaluateFitness(Lion lion) {
        float result;

        float weightscore = findScore(lion.weight, 420);
        float speedscore = findScore(lion.speed, 50);
        float strengthscore = findScore(lion.strength, 650);
        float intelligencescore = findScore(lion.intelligence, 100);
        float furscore = furColorFitnessMap(lion.furcolor);

        result = weightscore * speedscore * strengthscore * intelligencescore * furscore;

        return result;
    }

}
