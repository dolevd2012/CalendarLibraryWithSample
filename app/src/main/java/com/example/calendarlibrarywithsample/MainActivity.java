package com.example.calendarlibrarywithsample;

import android.os.Bundle;
import com.example.commoncalendar.CalendarActivity;
import com.example.commoncalendar.mainData;

import java.util.ArrayList;

public class MainActivity extends CalendarActivity {

    //you can check Database Inspector to see database works under CalendarDataBase -> EventCalendar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteAllEvents();
        ArrayList<mainData> arrayList = new ArrayList<>();
        setTitle("MY Routine App");
        mainData a = new mainData("WALKING","12:00-23:00");
        mainData b = new mainData("CYCLING","15:00-19:00");
        arrayList.add(a);
        arrayList.add(b);
        insertEvents(arrayList,"822021");
    }
}
