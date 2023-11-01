package com.horvath.ballparkpalapi.utils;

public class Converters {
    private final static double KELLY_MULTIPLIER = 1.0; // How true to the Kelly Criterion formula you want the bet size to be. 1.0 is true Kelly. Lower values cause bet size to be more conservative.
    private final static double BP_WEIGHT = .50; // How correct you think the BP odds are. .50 will take the halfway point between the BP odds and the best odds from FD or DK. Basically acting as a margin of error.


    public static double convertToImpliedProbability(Integer americanOdds){
        double americanOddsDouble = americanOdds;
        if(americanOddsDouble > 0) {

            return 100 / (americanOddsDouble + 100);
        }
        americanOddsDouble = Math.abs(americanOddsDouble);
        return americanOddsDouble/(americanOddsDouble+100);
    }

    public static double calculateWinProbability(double BPImpliedProbability, double bestSportsbookImpliedProb){
     double edge = (BPImpliedProbability - bestSportsbookImpliedProb) * BP_WEIGHT;
     return bestSportsbookImpliedProb + edge;
    }

    public static double calculateExpectedValue(double winProbability, double loseProbability, double profit){
        // Expected Value =
        // (Probability of Winning) x (Amount Won per Bet) – (Probability of Losing) x (Amount Lost per Bet)
       return  (winProbability * profit - loseProbability) * 100;
    }

    public static double calculateBetSize(double b, double p, double q){
        // f = (bp-q)/b
        // where:
        // f = portion of bankroll to bet
        // b = decimal odds - 1
        // q = probability of losing
        // p = probability of winning
        b--;
        return ((b*p-q)/b) * KELLY_MULTIPLIER*100;
    }

    public static double calculateProfit(Integer americanOdds){
        double americanOddsDouble = americanOdds;
        if(americanOddsDouble < 0){
            return 1/(Math.abs(americanOddsDouble)/100);
        }
        return americanOddsDouble/100;
    }



    public static double convertToDecimalOdds(Integer americanOdds){
//        Positive odds: 1 plus (the american odds divided by 100) e.g. american odds of 300 = 1 + (300/100) = 4.
//        Negative odds: 1 minus (100 divided by the american odds) e.g. american odds of -300 = 1 - (100/-300) = 1.333.
        double americanOddsDouble = americanOdds;
        if(americanOddsDouble > 0) {
            return 1 + (americanOddsDouble/100);
        }
        return 1-(100/americanOddsDouble);
    }

    public static int findBestOdds(Integer FDodds, Integer DKodds){
        if(FDodds != null && DKodds != null){
            return FDodds > DKodds? FDodds : DKodds;
        }
        if(FDodds == null){
            return DKodds;
        }
        return FDodds;
    }

}
