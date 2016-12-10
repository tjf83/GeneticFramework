import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trevor on 11/8/16.
 */
public class Population<E> {

    ArrayList<E> members;

    public void setMembers(ArrayList<E> members) {
        this.members = members;
    }

    public ArrayList<E> getMembers() {
        return this.members;
    }

    public Population (ArrayList<E> members) {
        this.setMembers(members);
    }

    public Population() {
        ArrayList<E> members = new ArrayList<E>();
        this.setMembers(members);
    }
}

