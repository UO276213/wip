package com.example.wip.layouts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wip.R;
import com.example.wip.modelo.Fiesta;
import com.example.wip.utils.ParserFiestas;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    //Calendar calendar;
    ArrayList<Fiesta> fiestas;

    private static final String ARG_FIESTAS = "arg_fiestas";
    CompactCalendarView compactCalendarView;

    ListView listEvents;

    TextView textViewDate;

    public CalendarFragment(){}
    /**
     * @return A new instance of fragment MapsFragment.
     */
    public static CalendarFragment newInstance(ArrayList<Fiesta> fiestas) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        if (fiestas == null) fiestas = new ArrayList<>();
        args.putParcelableArrayList(ARG_FIESTAS, fiestas);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fiestas = getArguments().getParcelableArrayList(ARG_FIESTAS);
    }

    private void isPartyOnSelectedDay(int year, int month, int dayOfTheMonth) {
        for(Fiesta fiesta: fiestas){
            ArrayList<Integer> date = ParserFiestas.parserDate(fiesta.getDate());
            if(date!=null){
                if(date.get(1)==month){
                    if(date.get(0)==dayOfTheMonth){
                        Toast.makeText(getContext(),"Hay fiesta",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }

        }
    }


    private long getDateInMilis(int day, int month){
        Calendar calendar = Calendar.getInstance();
        String toParse = day + "-" + (month+1) + "-2023"+ " " + "5:00";
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
        compactCalendarView.removeAllEvents();
        for(Fiesta fiesta: fiestas){
            ArrayList<Integer> date = ParserFiestas.parserDate(fiesta.getDate());
            if(date!=null){
                if(date.get(1)==month){
                    compactCalendarView.addEvent(new Event(
                            Color.GRAY, getDateInMilis(date.get(0),month),
                            fiesta.getName()
                    ));
                }
            }

        }
    }

    private String extractDate(Date date){
        Locale idioma = Locale.getDefault();
        String month = (new SimpleDateFormat("MMMM", idioma)).format(date);
        String year = (new SimpleDateFormat("y", idioma)).format(date);
        return month + " " + year;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addToLayout();
    }

    private void addToLayout() {
        Calendar calendar = Calendar.getInstance();

        compactCalendarView = (CompactCalendarView) getView().findViewById(R.id.compactcalendar_view);

        listEvents = getView().findViewById(R.id.listEvents);

        textViewDate = getView().findViewById(R.id.textViewDate);

        textViewDate.setText(extractDate(new Date()));

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
                            new ArrayAdapter<String>(getContext(),
                                    android.R.layout.simple_list_item_1, partys);
                    listEvents.setAdapter(adapter);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textViewDate.setText(extractDate(firstDayOfNewMonth));
                calendar.setTime(firstDayOfNewMonth);
                getEventsForEachMonth(calendar.get(Calendar.MONTH));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
}