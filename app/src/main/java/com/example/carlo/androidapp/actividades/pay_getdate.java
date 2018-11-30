package com.example.carlo.androidapp.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carlo.androidapp.R;

import java.util.Calendar;

public class pay_getdate extends AppCompatActivity {
    SharedPreferences pref;
    EditText txt;
    private long date;
    private int tourid;
    Intent mintent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payform1);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        CalendarView mCalendar = (CalendarView) findViewById(R.id.MyCalendar);
        txt = (EditText) findViewById(R.id.correoFormaPago);
        txt.setText(pref.getString("useremail",""));
        mCalendar.setMinDate(mCalendar.getDate());
        mintent = getIntent();
        tourid = mintent.getIntExtra("tour_id", 0);
        date = mCalendar.getDate()/1000;
        final Calendar c = Calendar.getInstance();
        mCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                c.set(Calendar.HOUR, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                date = c.getTimeInMillis()/1000;

            }
        });
    }

    public void dateSelected(View V){
        String text = txt.getText().toString();
        if(text.equals(""))
            Toast.makeText(pay_getdate.this, "Falta llenar el campo del correo",
                    Toast.LENGTH_LONG).show();
        else{
            Intent i = new Intent(pay_getdate.this, pay_gettickets.class);
            i.putExtra("dateselected",date);
            i.putExtra("tour_id",tourid);
            startActivity(i);
        }

    }

}
