package com.horvath.ballparkpalapi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvertersTest {

    @Test
    void convertToImpliedProbabilityNegativeOdds(){
        double odds = Converters.convertToImpliedProbability(-130);
        assertEquals(.5652,odds,0.01);
    }

    @Test
    void convertToImpliedProbabilityPositiveOdds(){
        double odds = Converters.convertToImpliedProbability(130);
        assertEquals(.4348,odds,0.01);
    }

    @Test
    void calculateWinProbability() {
        double winProb = Converters.calculateWinProbability(.75,.50);
        assertEquals(.625,winProb,0.01);
    }

    @Test
    void calculateExpectedValue() {
        double ev = Converters.calculateExpectedValue(.4348,.5652,1.5);
        assertEquals(8.7,ev,.01);

    }

    @Test
    void calculateBetSizeNegativeOdds() {
        double betSize = Converters.calculateBetSize(1.2173913,.85,.15);
        assertEquals(16.0,betSize,.01);
    }

    @Test
    void calculateBetSizePositiveOdds() {
        double betSize = Converters.calculateBetSize(3.42,.35,.65);
        assertEquals(8.14,betSize,.01);
    }


    @Test
    void calculateProfitNegativeOdds() {
        double profit = Converters.calculateProfit(-180);
        assertEquals(.5556,profit,.0001);
    }

    @Test
    void calculateProfitPositiveOdds() {
        double profit = Converters.calculateProfit(357);
        assertEquals(3.57,profit,.0001);
    }

    @Test
    void convertToDecimalOddsNegativeOdds() {
        double odds = Converters.convertToDecimalOdds(-150);
        assertEquals(1.66,odds,.01);
    }

    @Test
    void convertToDecimalOddsPositiveOdds() {
        double odds = Converters.convertToDecimalOdds(150);
        assertEquals(2.5,odds,.01);
    }

}