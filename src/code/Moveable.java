package code;


import java.util.List;

public interface Moveable   {

    /**
     * Gives list of avaiable positions near the charger
     *
     * Unfinished. There is need to create board based on integer positons
     * @param position
     * @return List of Available fields mapped as integers.
     */
    List<Integer> getAvailableFields(int position);
}
