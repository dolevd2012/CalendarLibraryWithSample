package com.example.mycalendar;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class currentDayDecorator implements DayViewDecorator {

    private Drawable drawable;
    CalendarDay currentDay ;
    public currentDayDecorator(Activity context,CalendarDay dateToDecorate) {
        drawable = ContextCompat.getDrawable(context,R.drawable.selected_day_month);
        currentDay=dateToDecorate;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(currentDay);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
