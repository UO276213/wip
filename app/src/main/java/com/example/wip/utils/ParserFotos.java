package com.example.wip.utils;

public class ParserFotos {

    public static String getURLSearchImage(String resultado) {
        String returned = resultado.split(",\\[\"https://www.")[1].split("\"")[0];
        return "https://www."+returned;
    }

}
