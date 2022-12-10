package com.example.wip.utils;

import java.util.ArrayList;
import java.util.List;

public class ParserFotos {

    public static List<String> getURLSearchImage(String resultado) {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i < 5; i++){
            urls.add("https://www." + resultado.split(",\\[\"https://www.")[i].split("\"")[0]);
        }
        return urls;
    }

}
