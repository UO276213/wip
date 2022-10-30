package com.example.wip.layouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    //Calendar calendar;
    ArrayList<Fiesta> fiestas;

    CompactCalendarView compactCalendarView;

    ListView listEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        fiestas = intent.getParcelableArrayListExtra(MainActivity.FIESTAS);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        listEvents = findViewById(R.id.listEvents);

        Calendar calendar = Calendar.getInstance();
        getEventsForEachMonth(calendar.get(Calendar.MONTH));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Log.d("TAG", dateClicked.toString());
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                ArrayList<String> partys = new ArrayList<String>();
                if(!events.isEmpty()){
                    for(Event e:events){
                        partys.add((String) e.getData());
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(CalendarActivity.this,
                                    android.R.layout.simple_list_item_1, partys);
                    listEvents.setAdapter(adapter);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });
    }

    private void isPartyOnSelectedDay(int year, int month, int dayOfTheMonth) {
        for(Fiesta fiesta: fiestas){
            ArrayList<Integer> date = ParserFiestas.parserDate(fiesta.getDate());
            if(date!=null){
                if(date.get(1)==month){
                    if(date.get(0)==dayOfTheMonth){
                        Toast.makeText(CalendarActivity.this,"Hay fiesta",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }

        }
    }


    private long getDateInMilis(int day, int month){
        Calendar calendar = Calendar.getInstance();
        String toParse = day + "-" + (month+1) + "-2022"+ " " + "5:00";
        // Results in "2-5-2012 20:43"
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        // I assume dM, you may refer to Md for month-day instead.
        Date date = null;
        try {
            date = formatter.parse(toParse);

            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis();
    }

    private void getEventsForEachMonth(int month){
        for(Fiesta fiesta: fiestas){
            ArrayList<Integer> date = ParserFiestas.parserDate(fiesta.getDate());
            if(date!=null){
                if(date.get(1)==month){
                    compactCalendarView.addEvent(new Event(
                            Color.GREEN, getDateInMilis(date.get(0),month),
                            fiesta.getName()
                    ));
                }
            }

        }
    }

}