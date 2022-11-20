package com.example.wip.utils;

import com.example.wip.modelo.Fiesta;

import java.util.ArrayList;
import java.util.HashMap;

public class ParserLugares {

    private static String COMUNIDAD="1";
    private static String PROVINCIA="2";

    /**
     * Devuelve las comunidades españolas
     * @param resultado
     * @return
     */
    public static ArrayList<String>[] parseComunidades(String resultado) {
        return parseLugar(resultado,COMUNIDAD);
    }
    /**
     * Devuelve las provincias de una comunidad
     * @param resultado
     * @return
     */
    public static ArrayList<String>[] parseProvincia(String resultado) {
        return parseLugar(resultado,PROVINCIA);
    }
    /**
     * Parsea el html
     * @param resultado
     * @return
     */
    public static ArrayList<String>[] parseLugar(String resultado,String level) {
        ArrayList<String> lugares = new ArrayList<String> ();
        ArrayList<String>  lugaresUrl = new ArrayList<String> ();
        String[] splitted = resultado.split("<ul id=\"regionList\">|</ul>");
        splitted = splitted[1].split("<li class=\"level"+level+"\">");
        for (int i = 1; i < splitted.length ; i++) {
            String name=splitted[i].split(">|</a>")[1];
            String url=splitted[i].split("<a href=|>")[1].replace("\"","");
            lugares.add(name);
            lugaresUrl.add(url);
        }
        ArrayList<String>[] returned= new ArrayList[2];
        returned[0]=lugares;
        returned[1]=lugaresUrl;
        return returned;
    }

}
