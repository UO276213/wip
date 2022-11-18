package com.example.wip.utils;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class UnderLine {

    //Subraya un texto
    public static SpannableString underLine(String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }
}
