package com.example.wip.utils;

import com.example.wip.modelo.Fiesta;

import java.util.ArrayList;

public class ParserFiestas {

    public static ArrayList<Fiesta> ParseFiestas(String resultado) {
        ArrayList<Fiesta> fiestas = new ArrayList<Fiesta>();
        String[] splitted = resultado.split("<li>|<li");
        //Las fiestas siemprea se encuentras entre estas posiciones: -35 -25
        for (int i = splitted.length - 35; i < splitted.length - 25; i++) {
            //Parseamos una fiesta y la añadimos al arary
            Fiesta fiesta = parseFiesta(splitted[i]);
            fiestas.add(fiesta);
        }

        return fiestas;
    }

    private static Fiesta parseFiesta(String texto) {
        Fiesta fiesta = new Fiesta();
        //Parseamos, gracias a las clases, ademas quitamos los <a>, <span> y \n
        fiesta.setPlace(texto.split("class=\"town\">")[1].split("</a>|</span>")[0]);
        fiesta.setDate(texto.split("class=\"dates\">")[1].split("</span>")[0].split("\n")[0]);
        fiesta.setName(texto.split("class=\"name\">")[1].split("</a>|</span>")[0]);
        return fiesta;
    }
}
