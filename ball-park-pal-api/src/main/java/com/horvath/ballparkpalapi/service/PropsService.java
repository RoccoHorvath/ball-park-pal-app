package com.horvath.ballparkpalapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.horvath.ballparkpalapi.model.Prop;
import com.horvath.ballparkpalapi.utils.Converters;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PropsService {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final static String PROPS_JSON_PATH = System.getenv("PROPS_JSON_PATH");

    private final static Map<String, File> batterFiles = new HashMap<>() {{
        put("Hits", new File(PROPS_JSON_PATH+"Hits.json"));
        put("Home Runs", new File(PROPS_JSON_PATH+"Home Runs.json"));
        put("Bases", new File(PROPS_JSON_PATH+"Bases.json"));
        put("Stolen Bases", new File(PROPS_JSON_PATH+"Stolen Bases.json"));
    }};

    private final static Map<String, File> pitcherFiles = new HashMap<>() {{
        put("Strikeouts", new File(PROPS_JSON_PATH+"Strikeouts.json"));
        put("Hits Allowed", new File(PROPS_JSON_PATH+"Hits Allowed.json"));
    }};


    public static List<Prop> createProp(File file, String team, String book) throws IOException{
        List<Prop> props = objectMapper.readValue(file, new TypeReference<List<Prop>>() {
        });

        if(team!=null){
            props = filterByTeam(props,team);
        }
        if(book!=null){
            props = filterByBook(props,book);
            setEVandBetSize(props,book);
        }else{
            setEVandBetSize(props);
        }
        return props;
    }
    public static Map<String,List<Prop>> createBatterProps(String team, String book) throws IOException{

        Map<String,List<Prop>> propsMap  = new HashMap<>();

        for (Map.Entry<String, File> entry : batterFiles.entrySet()) {
            String prop = entry.getKey();
            File file = entry.getValue();
            propsMap.put(prop, createProp(file, team, book));
        }
        return propsMap;
    }

    public static Map<String,Map<String,List<Prop>>> createPitcherProps(String team, String book) throws IOException{
        Map<String,Map<String,List<Prop>>> propsMap  = new HashMap<>();

        for (Map.Entry<String, File> entry : pitcherFiles.entrySet()) {
            String prop = entry.getKey();
            File file = entry.getValue();
            propsMap.put(prop, createPitcherPropMap(createProp(file,team,book)));
        }
        return propsMap;
    }

    public static Map<String,Map<String,List<Prop>>> createPitcherProp(String prop, String team, String book) throws IOException {
        return new HashMap<>() {{
            put(prop,createPitcherPropMap(createProp(pitcherFiles.get(prop),team,book)));
        }};
    }

    public static Map<String, List<Prop>> createPitcherPropMap(List<Prop> pitcherProps) throws IOException {
        Map<String, List<Prop>> propsMap = pitcherProps.stream()
                .collect(Collectors.groupingBy(Prop::getPlayerName));

        propsMap.forEach((playerName, batterProps) ->
                batterProps.sort(Comparator.comparingDouble(Prop::getExpectedValue)));

        return propsMap;
    }



    public static void setEVandBetSize(List<Prop> props, String book){
        for (Prop prop : props) {
            int bestOdds = book==null? prop.getBestOdds(): prop.getBooks().get(book);
            double BPImpliedProbability = Converters.convertToImpliedProbability(prop.getBp());;
            double bestSportsbookImpliedProbability = Converters.convertToImpliedProbability(bestOdds);
            double winProb = Converters.calculateWinProbability(BPImpliedProbability, bestSportsbookImpliedProbability);
            double loseProb = 1 - winProb;
            double decimalOdds = Converters.convertToDecimalOdds(bestOdds);
            double profit = Converters.calculateProfit(bestOdds);

            prop.setBetSize(Converters.calculateBetSize(decimalOdds, winProb, loseProb));
            prop.setExpectedValue(Converters.calculateExpectedValue(winProb, loseProb, profit));
        }
        Comparator<Prop> comparator = Comparator.comparing(Prop::getBetSize,Comparator.reverseOrder());
        props.sort(comparator);
        }
    public static void setEVandBetSize(List<Prop> props){
        setEVandBetSize(props,null);
    }



    public static Map<String, List<Prop>> createBatterProp(String prop, String team, String book) throws IOException {
        return new HashMap<>() {{
            put(prop,createProp(batterFiles.get(prop),team,book));
        }};
    }




    public static List<Prop> filterByTeam(List<Prop> props, String team){
        List<Prop> filteredList = new ArrayList<>();
        for (Prop prop : props) {
            if (prop.getTeam().equals(team) || prop.getOpponent().equals(team)) {
                filteredList.add(prop);
            }
        }
        return filteredList;
    }

    public static List<Prop> filterByBook(List<Prop> props, String book) {
        List<Prop> filteredList = new ArrayList<>();

        for (Prop prop : props) {
            if (prop.isBookNotNull(book)) {
                filteredList.add(prop);
            }
        }
        return filteredList;
    }


}
