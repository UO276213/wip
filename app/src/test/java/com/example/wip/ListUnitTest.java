package com.example.wip;

import static com.example.wip.layouts.ListaFragments.ARG_FIESTAS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.util.Log;

import com.example.wip.layouts.CalendarFragment;
import com.example.wip.layouts.ListaFragments;
import com.example.wip.modelo.Fiesta;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ListUnitTest {

    ArrayList<Fiesta> fiestas;
    @Mock
    Bundle args;

    ListaFragments listaFragment;

    @Before
    public void init(){
        fiestas=new ArrayList<Fiesta>();

        Fiesta f1 = new Fiesta();
        f1.setId(1);
        f1.setName("Fiesta1");
        f1.setDate("13 de enero");
        f1.setPlace("Andalucía");
        f1.setDetails("Fiesta 1 de Andalucía");

        Fiesta f2 = new Fiesta();
        f2.setId(2);
        f2.setName("Fiesta2");
        f2.setDate("14 de enero");
        f2.setPlace("Andalucía");
        f2.setDetails("Fiesta 2 de Andalucía");

        fiestas.add(f1);
        fiestas.add(f2);

        args=new Bundle();
        args.putParcelableArrayList(ARG_FIESTAS,fiestas);

    }

    @Test
    public void addition_isCorrect() {

        assertEquals(listaFragment.getArguments().getParcelableArrayList(ARG_FIESTAS).size()
                , 2);
    }
}