/**
 * Created by sahityamantravadi on 12/7/16.
 */

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


    public Lion (float weight, float speed, float strength, float intelligence, FurColor furcolor, Gender gender) {
        this.weight = weight;
        this.speed = speed;
        this.strength = strength;
        this.intelligence = intelligence;
        this.furcolor = furcolor;
        this.gender = gender;
    }

}
