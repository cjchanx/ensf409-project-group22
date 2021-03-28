package edu.ucalgary.ensf409;

/*
    PriceOptimizer contains three parallel arrays, one with
    the specified furniture ID and one with the specified parts for a
    specific furniture configuration. For instance
     id[0] = D0890, boolean[0] = {F, F, T}, price[0] = 25
     And provides methods for optimization of the price.
 */
public class PriceOptimizer {
    private int partCount;
    private boolean[][] parts;
    private String[] id;
    private int[] price;
    private int min = 0;
    private int[] minIndex;
    /**
     * PriceOptimizer will construct
     * the PriceOptimizer object with the given data.
     * @param id String for id of the specific piece of furniture
     * @param parts 2D boolean array of parts list
     */
    public PriceOptimizer(String[] id, boolean[][] parts, int[] price) {
        this.id = id;
        this.parts = parts;
        this.price = price;
        this.partCount = parts[0].length;
    }

    /**
     * optimize() will try to find the optimal configuration of
     * furniture parts for the lowest price based on the
     * current FurnitureConfigurationData.
     */
    public String[] optimize() {
        min = 0;
        for(int p : price){
            min += p;
        }
        int[] indexs = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            indexs[i] = i;
        }
        for (int i = 1; i < partCount; i++) {
            combination(indexs, i);
        }
        String[] ids = new String[minIndex.length];

        for (int i = 0; i < ids.length; i++) {
            ids[i] = id[indexs[i]];
        }
        return ids;
    }

    private void recursion(int[] array, int[] data, int start, int end, int index, int r){
        if(index == r){
            if(compatible(data)){
                if(getPrice(data) < min){
                    minIndex = new int[data.length];
                    minIndex = data;
                }
            }
        }

        for (int i = start; i <= end && (end + 1 - i) >= (r - index); i++) {
            data[index] = array[i];
            recursion(array, data, i+1, end, index+1, r);
        }
    }

    private void combination(int[] array, int comb){
        int[] data = new int[comb];
        recursion(array, data, 0, data.length-1, 0, comb);
    }


    /**
     * Compatible checks if a specified list of indices for the boolean
     * array is compatible (will have all parts fulfilled)
     * For instance if the list of specified furniture indices of parts[][]
     * as follows,
     * T F F T
     * F T T F
     * it would return true, since it can be fit together to
     * make one complete set of furniture.
     * @param list int[] of the indices to be checked together.
     * @return Boolean, true if compatible, false otherwise.
     */
    public boolean compatible(int[] list) {
        boolean[] fulfilledParts = new boolean[partCount];
        boolean ok = true;
        for(int i = 0; i < list.length; i++) {
            int indexNum = list[i];
            for(int j = 0; j < partCount; j++) {
                if(parts[indexNum][j]) {
                    fulfilledParts[j] = true;
                }
            }
        }
        // Make sure whole array is true
        for(int i = 0; i < partCount; i++) {
            if(!fulfilledParts[i]) {
                return false;
            }
        }
        return true;
    }


    public int getPrice(int[] index){
        int sum = 0;
        for(int i = 0; i < index.length; i++) {
            sum += price[index[i]];
        }
        return sum;
    }

    /**
     * sortOnPrice() will sort the arrays based on price, keeping data in the same order
     * for all the arrays using bubble sort. (TODO: Better if its possible to do this on object)
     */
    public void sortOnPrice() {
        for(int i = 0; i < price.length; i++) {
            for(int j = i; j < price.length; j++) {
                if(price[i] > price[j]) {
                    // Sort price
                    int temp = price[i];
                    price[i] = price[j];
                    price[j] = temp;
                    // Sort parts
                    boolean[] tempBool = parts[i];
                    parts[i] = parts[j];
                    parts[j] = tempBool;
                    // Sort id
                    String tempID = id[i];
                    id[i] = id[j];
                    id[j] = tempID;
                }
            }
        }
    }
}
