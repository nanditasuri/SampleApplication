package com.example.nandita.practiceapp1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;

public class MyCalendar extends Activity implements CalendarView.OnDateChangeListener{

    final static int GET_ALARM_SETTINGS=1;
    int alarmDay;
    int alarmMonth;
    int alarmYear;

    CalendarView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        cv = (CalendarView) findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

       Log.i("CALENDAR VIEW","The Current Day selected is " + dayOfMonth);
        Log.i("CALENDAR VIEW","The Current month selected is " + month);
        Log.i("CALENDAR VIEW","The Current year selected is " +year);
       alarmDay = dayOfMonth;
       alarmMonth = month;
       alarmYear = year;
    }

    public void onSetReminder(View view){

        Intent i = new Intent(this,DialogActivity.class);
        startActivityForResult(i, GET_ALARM_SETTINGS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == GET_ALARM_SETTINGS) && (resultCode == RESULT_OK)) {
            Log.i("MyCalendar :"," in loop");

            Bundle bundle = data.getExtras();

            int alarmHr = bundle.getInt("ALARM HOUR");
            int alarmMin = bundle.getInt("ALARM MIN");

            Calendar calendar = Calendar.getInstance();
            calendar.set(alarmYear, alarmMonth, alarmDay, alarmHr, alarmMin);

            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(this,AlarmReceiver.class);
            i.putExtra("ALARM DESC",bundle.getString("ALARM DESC"));

            PendingIntent pi = PendingIntent.getBroadcast(this, 3, i, 0);
            Log.i("MyCalendar :"," calendar milli secs is "+calendar.getTimeInMillis());
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);//
            //am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10 , pi);

            Log.i("MyCalendar :"," setting alarm");
            Log.i("onActivity","Alarm values are *********");
            Log.i("OnActivity: ","Alarm desc "+bundle.getString("ALARM DESC"));
            Log.i("OnActivity: ","Alarm year "+ alarmYear);
            Log.i("OnActivity: ","Alarm month "+ alarmMonth);
            Log.i("OnActivity: ","Alarm day "+ alarmDay);
            Log.i("OnActivity: ","Alarm hour "+ alarmHr);
            Log.i("OnActivity: ","Alarm min "+ alarmMin);


            Toast.makeText(this, "Alarm for " + bundle.getString("ALARM DESC") + "is set", Toast.LENGTH_LONG).show();
        }

    }


}
