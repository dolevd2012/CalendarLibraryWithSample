package com.example.commoncalendar;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private mySQLiteDBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;
    TextView appTitle;
    MaterialCalendarView calendarView;
    MyAdapter myAdapter;
    ArrayList<mainData> dataList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String selectedDate;
    CalendarDay dateToDecorate;
    FloatingActionButton fab ;
    int t1Hour,t1Minute,t2Hour,t2Minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appTitle=findViewById(R.id.TV_MAIN_TITLE);
        calendarView=findViewById(R.id.CV_MAIN_CALENDAR);
        recyclerView = findViewById(R.id.RV_MAIN_Rview);
        linearLayoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(dataList, CalendarActivity.this);
        recyclerView.setAdapter(myAdapter);
        setTitle("My Private Calendar");
        fab=findViewById(R.id.fab_MAIN_addEvent);
        createDatabase();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert  = new AlertDialog.Builder(CalendarActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.eventdialog,null);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                Button cancel = mView.findViewById(R.id.BUTTON_MAIN_DIALOG_MAIN_BUTTONCANCEL);
                Button add =mView.findViewById(R.id.BUTTON_MAIN_DIALOG_MAIN_BUTTONADD);
                TextView startClock = mView.findViewById(R.id.TV_MAIN_startClock);
                TextView endClock = mView.findViewById(R.id.TV_MAIN_endClock);
                EditText dialogTitle = mView.findViewById(R.id.ET_MAIN_DialogEVENT);
                if(selectedDate==null) { //so we wont enter null value to our database we create default value - today's date!
                    selectedDate = String.valueOf(CalendarDay.today().getDay() + String.valueOf(CalendarDay.today().getMonth() +
                            String.valueOf(CalendarDay.today().getYear())));
                    dateToDecorate = CalendarDay.today();
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                startClock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t1Hour = hourOfDay;
                                t1Minute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t1Hour,t1Minute);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                startClock.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },12,0,false
                        );
                        timePickerDialog.updateTime(t1Hour,t1Minute);
                        timePickerDialog.show();
                    }
                });
                endClock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                t2Hour = hourOfDay;
                                t2Minute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t2Hour,t2Minute);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                endClock.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        },12,0,false
                        );
                        timePickerDialog.updateTime(t2Hour,t2Minute);
                        timePickerDialog.show();
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dialogAddButton(mView)) { // if value is true it means all input was given
                            readEvents();
                            mainData mainData = new mainData(dialogTitle.getText().toString(),startClock.getText() + "-" + endClock.getText());
                            dataList.add(mainData);
                            insertEvents(dataList,selectedDate);
                            Toast.makeText(mView.getContext(),"Event Successfully added !",Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }

        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date.getDay() + "" + date.getMonth()  + "" + date.getYear();
                dateToDecorate = date;
                readEvents();
                myAdapter.notifyDataSetChanged();
            }
        });
        myAdapter.notifyDataSetChanged();

    }
    public void createDatabase(){
        try {
            dbHandler = new mySQLiteDBHandler(this, "CalendarDataBase", null, 2);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            //deleteAllEvents();
            sqLiteDatabase.execSQL("CREATE TABLE if not exists EventCalendar(Date TEXT,Event TEXT,UNIQUE(Date))");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void setTitle(String title){
        appTitle.setText(title);
    }
    public void deleteAllEvents(){
        sqLiteDatabase.execSQL("delete from EventCalendar");
    }

    public void insertEvents(ArrayList<mainData> events, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Date", date);
        contentValues.put("Event", String.valueOf(events));
        calendarView.addDecorators(new currentDayDecorator(this,dateToDecorate));
        sqLiteDatabase.insertWithOnConflict("EventCalendar",null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        myAdapter.notifyDataSetChanged();

    }

    public void readEvents() {
        String query = "Select Event from EventCalendar where Date = " + selectedDate;
        Log.d("HTTPS", query);
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            dataList.clear();
            int lastIndex = cursor.getString(0).length() - 1;
            String[] events = cursor.getString(0).substring(1, lastIndex).split(",");
            calendarView.addDecorators(new currentDayDecorator(this,dateToDecorate));
            for (int i = 0; i < events.length; i++) {
                mainData mainData = new mainData(events[i].split("!")[0], events[i].split("!")[1]);
                dataList.add(mainData);
            }
        } catch (Exception e) {
            Log.d("HTTPS", "query not found " + e.getMessage());
            e.printStackTrace();
            dataList.clear();
        }
    }
    public Boolean dialogAddButton(View mView){
        TextView startClock = mView.findViewById(R.id.TV_MAIN_startClock);
        TextView endClock = mView.findViewById(R.id.TV_MAIN_endClock);
        EditText dialogTitle = mView.findViewById(R.id.ET_MAIN_DialogEVENT);
        if(!dialogTitle.getText().toString().equals("")){
            if(!startClock.getText().toString().equals("")){
                if(!endClock.getText().toString().equals("")){
                    return true;
                }
                Toast.makeText(mView.getContext(),"Missing end time!",Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(mView.getContext(),"Missing start time!",Toast.LENGTH_SHORT).show();
            return false;
        }
        Toast.makeText(mView.getContext(),"Missing title !",Toast.LENGTH_SHORT).show();
        return false;
    }

}
