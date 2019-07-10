package be.bt.utils;

import java.util.ArrayList;

public interface ReelNumbersGeneratorFacade {

//    String genReelNumbers(int maxVal, int minVal);
    String genReelNumbers();
    public String genReelNumbers(int maxVal, int minVal);
    int generateRandom(int start, int end, ArrayList<Integer> excludeRows);
    String changeString(String s, char r);
}
