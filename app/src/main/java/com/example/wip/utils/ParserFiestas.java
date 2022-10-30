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

    public static ArrayList<Integer> parserDate(String date){
        String[] dateSplitted = date.split(" ");
        if(dateSplitted.length<=3){
            int day = Integer.parseInt(dateSplitted[0]);
            String month = dateSplitted[2];
            int result = parserMonth(month);
            ArrayList<Integer> parse_date= new ArrayList<Integer>();
            parse_date.add(day);
            parse_date.add(result);
            return parse_date;
        }
        return null;

    }

    private static int parserMonth(String month) {
        int result = -1;
        switch(month){
            case "enero":
            {
                result =0;
                break;
            }
            case "febrero":
            {
                result =1;
                break;
            }
            case "marzo":
            {
                result =2;
                break;
            }
            case "abril":
            {
                result =3;
                break;
            }
            case "mayo":
            {
                result =4;
                break;
            }
            case "junio":
            {
                result =5;
                break;
            }
            case "julio":
            {
                result =6;
                break;
            }
            case "agosto":
            {
                result =7;
                break;
            }
            case "septiembre":
            {
                result =8;
                break;
            }
            case "octubre":
            {
                result =9;
                break;
            }
            case "noviembre":
            {
                result =10;
                break;
            }
            case "diciembre":
            {
                result =11;
                break;
            }
        }
        return result;
    }
}
