package com.example.nandita.practiceapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Created by nandita on 2/15/2015.
 */
public class DialogActivity extends Activity implements TimePicker.OnTimeChangedListener {

    EditText remDesc;
    int alarmHour,alarmMin;
    TimePicker tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        tp = (TimePicker) findViewById(R.id.timePicker);
        tp.setIs24HourView(true);
    }
    public void onSave(View v){
        String descVal;
        Bundle data = new Bundle();
        alarmHour = tp.getCurrentHour();
        alarmMin = tp.getCurrentMinute();
        remDesc = (EditText)findViewById(R.id.editText_desc);
        descVal = remDesc.getText().toString();

        data.putString("ALARM DESC",descVal);
        data.putInt("ALARM HOUR", alarmHour);
        data.putInt("ALARM MIN",alarmMin);
        Log.i("onSave","Alarm values are "+descVal +alarmHour + alarmMin);
        Intent i = new Intent(this,MyCalendar.class);
        i.putExtras(data);
        Log.i("Dialog :", " reversing alarm");

        setResult(RESULT_OK,i);
        super.finish();
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.i("onTimeChanged","Here in when the time is changed");
        alarmHour = hourOfDay;
        alarmMin = minute;
    }
}
