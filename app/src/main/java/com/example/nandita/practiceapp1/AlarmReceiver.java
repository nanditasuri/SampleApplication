package com.example.nandita.practiceapp1;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by nandita on 2/18/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReciever", "Recieved alarm");
        Bundle bundle = intent.getExtras();
        bundle.getString("ALARM DESC");
        Toast.makeText(context, "Alarm for "+bundle.getString("ALARM DESC")+" is Ringing", Toast.LENGTH_LONG).show();

        Intent i = new Intent(context,AlarmNotficationService.class);
        i.putExtra("ALARM DESC",   bundle.getString("ALARM DESC"));
        context.startService(i);


    }
}
