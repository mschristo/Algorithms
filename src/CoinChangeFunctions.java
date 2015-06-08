/**
 * @author Christos Boutsikas
 * @aem 2473
 * @email mschristo@csd.auth.gr
 */

public class CoinChangeFunctions {

    public CoinChangeFunctions() {
    }

    /**
     * Algorithms that solve coin change problem
     *
     * @param seeds          Array with the weight of each seed
     * @param differentSeeds The number of different types of seeds
     * @param capacity       Tha capacity of red ant's bin
     * @param seedUsed       The array of the seed that will be used
     * @param lastSeed       The array of the last of the seed will be used to fill the bin
     */
    public void coinChangeCalc(int[] seeds, int differentSeeds, int capacity, int[] seedUsed, int[] lastSeed) {
        seedUsed[0] = 0;
        lastSeed[0] = 1;

        for (int i = 1; i <= capacity; i++) {
            int minSeeds = i;
            int newSeed = 1;
            for (int j = 0; j < differentSeeds; j++) {
                if (seeds[j] > i) continue;
                if (seedUsed[i - seeds[j]] + 1 < minSeeds) {
                    minSeeds = seedUsed[i - seeds[j]] + 1;
                    newSeed = seeds[j];
                }
            }
            seedUsed[i] = minSeeds;
            lastSeed[i] = newSeed;
        }
    }

}
