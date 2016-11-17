/**
 * Created by Trevor on 11/8/16.
 */

public class Parabola {

    float a;
    float b;
    float c;

    int selectionSize = 1;

    public float evaluateFitness(float value) {
        float result;

        if (a > 0) {
            result = -1*(a*(value*value) + b*value + c);
        }
        else{
            result = (a*(value*value) + b*value + c);
        }
        return result;
    }

    public Parabola (float a, float b, float c) {
       setCoefficients(a, b, c);
    }

    private void setCoefficients(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getSelectionSize() {
        return this.selectionSize;
    }

}