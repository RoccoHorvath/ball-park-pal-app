package com.horvath.ballparkpalapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horvath.ballparkpalapi.model.BatterProp;
import com.horvath.ballparkpalapi.model.BatterPropCategory;
import com.horvath.ballparkpalapi.model.PitcherData;
import com.horvath.ballparkpalapi.model.PitcherProp;
import com.horvath.ballparkpalapi.utils.Converters;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class PropsService {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final static File BATTER_PROPS_JSON_PATH = new File(System.getenv("BATTER_PROPS_JSON_PATH"));
    private final static File PITCHER_PROPS_JSON_PATH = new File(System.getenv("PITCHER_PROPS_JSON_PATH"));

    public static BatterPropCategory createBatterPropsFromJSON() throws IOException {
        BatterPropCategory batterPropCategory = objectMapper.readValue(BATTER_PROPS_JSON_PATH, BatterPropCategory.class);
        setEVandBetSize(batterPropCategory.getHits());
        setEVandBetSize(batterPropCategory.getHomeruns());
        setEVandBetSize(batterPropCategory.getBases());
        return batterPropCategory;
    }

    public static void setEVandBetSize(List<BatterProp> batterProps){
        for (BatterProp batterProp : batterProps) {
            int bestOdds = Converters.findBestOdds(batterProp.getFd(), batterProp.getDk());
            double BPImpliedProbability = Converters.convertToImpliedProbability(batterProp.getBp());;
            double bestSportsbookImpliedProbability = Converters.convertToImpliedProbability(bestOdds);
            double winProb = Converters.calculateWinProbability(BPImpliedProbability, bestSportsbookImpliedProbability);
            double loseProb = 1 - winProb;
            double decimalOdds = Converters.convertToDecimalOdds(bestOdds);
            double profit = Converters.calculateProfit(bestOdds);

            batterProp.setBetSize(Converters.calculateBetSize(decimalOdds, winProb, loseProb));
            batterProp.setExpectedValue(Converters.calculateExpectedValue(winProb, loseProb, profit));
        }
        Comparator<BatterProp> comparator = Comparator.comparing(BatterProp::getExpectedValue,Comparator.reverseOrder());
        batterProps.sort(comparator);
        }

    public static PitcherData createPitcherPropsFromJSON() throws IOException {
        PitcherData pitcherData = objectMapper.readValue(PITCHER_PROPS_JSON_PATH, PitcherData.class);

        for (Map.Entry<String, List<PitcherProp>> entry : pitcherData.getPitchers().entrySet()) {
            List<PitcherProp> props = entry.getValue();
            for (PitcherProp prop : props) {
                setEVandBetSize(prop);
            }
            Comparator<PitcherProp> comparator = Comparator.comparing(PitcherProp::getExpectedValue,Comparator.reverseOrder());
            props.sort(comparator);
        }
        return pitcherData;
    }

    public static void setEVandBetSize(PitcherProp pitcherProp){
        int bestOdds = Converters.findBestOdds(pitcherProp.getFd(), pitcherProp.getDk());
        double BPImpliedProbability = Converters.convertToImpliedProbability(pitcherProp.getBp());;
        double bestSportsbookImpliedProbability = Converters.convertToImpliedProbability(bestOdds);
        double winProb = Converters.calculateWinProbability(BPImpliedProbability, bestSportsbookImpliedProbability);
        double loseProb = 1 - winProb;
        double decimalOdds = Converters.convertToDecimalOdds(bestOdds);
        double profit = Converters.calculateProfit(bestOdds);

        pitcherProp.setBetSize(Converters.calculateBetSize(decimalOdds, winProb, loseProb));
        pitcherProp.setExpectedValue(Converters.calculateExpectedValue(winProb, loseProb, profit));
    }
}
