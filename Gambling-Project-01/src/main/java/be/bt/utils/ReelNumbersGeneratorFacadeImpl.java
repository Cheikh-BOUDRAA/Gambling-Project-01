package be.bt.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

@Component
public class ReelNumbersGeneratorFacadeImpl implements ReelNumbersGeneratorFacade {

    @Override
//    public String genReelNumbers(int maxVal, int minVal) { /* Generates the 3 reel numbers. */
    public String genReelNumbers() { /* Generates the 3 reel numbers. */
            Random rand = new Random();
            int reel1 = rand.nextInt(8);
            int reel2 = rand.nextInt(8);
            int reel3 = rand.nextInt(8);
            return Integer
                    .toString(reel1).concat(
                            Integer
                                    .toString(reel2).concat(
                                    Integer.toString(reel3)));
    }

        public String genReelNumbers(int maxVal, int minVal) { /* Generates the 3 reel numbers. */

        int reel1 = (int)(Math.random() * (maxVal - minVal) + minVal);
        int reel2 = (int)(Math.random() * (maxVal - minVal) + minVal);
        int reel3 = (int)(Math.random() * (maxVal - minVal) + minVal);
        return Integer
                .toString(reel1).concat(
                        Integer
                                .toString(reel2).concat(
                                Integer.toString(reel3)));

    }

    public int generateRandom(int start, int end, ArrayList<Integer> excludeRows) {
        Random rand = new Random();
        int range = end - start + 1;

        int random = rand.nextInt(range);
        while(excludeRows.contains(random)) {
            random = rand.nextInt(range);
        }
        return random;
    }

    @Override
    public String changeString(String s, char r) {
        char[] characters = s.toCharArray();
        int rand = (int)(Math.random() * s.length());
        characters[rand] = r;
        return new String(characters);
    }

}