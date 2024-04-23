package com.horvath.ballparkpalapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prop {

    @JsonProperty("playerName")
    private String playerName;

    @JsonProperty("betName")
    private String betName;

    @JsonProperty("bp")
    private Integer bp;

    @JsonProperty("line")
    private String line;

    @JsonProperty("team")
    private String team;

    @JsonProperty("opponent")
    private String opponent;

    private double expectedValue;

    private double betSize;

    @JsonProperty("books")
    private Map<String, Integer> books;

    public boolean isBookNotNull(String bookName) {
        return books.get(bookName) != null;
    }

    public int getBestOdds() {
        int max = Integer.MIN_VALUE;
        for (Integer odds : books.values()) {
            if (odds != null && odds > max) {
                max = odds;
            }
        }
        return max;
    }
}
