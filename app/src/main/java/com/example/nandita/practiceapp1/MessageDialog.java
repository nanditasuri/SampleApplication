package com.example.nandita.practiceapp1;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MessageDialog extends Activity implements AdapterView.OnItemSelectedListener{

    ContentResolver cr;
    Cursor c;
    Spinner spinner;
    EditText phNum,smsMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Send SMS");
        setContentView(R.layout.activity_message_dialog);
        phNum = (EditText)findViewById(R.id.phonetext);
        smsMsg = (EditText)findViewById(R.id.messagetxt);

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        cr = getContentResolver();
        c = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(c!=null){
            Log.i("onCreate", "Before adapter");
            SimpleCursorAdapter curAdap = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,
                    c,new String[]{ContactsContract.Contacts.DISPLAY_NAME},new int[] {android.R.id.text1},0);

        spinner.setAdapter(curAdap);

        }else{
            Toast.makeText(this, "Rows in the db are 0", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_dialog, menu);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.i("Id is ","avlue is"+id);
        c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                ContactsContract.CommonDataKinds.Phone._ID + " = " + String.valueOf(id),null,null);


        if(c.getCount()>0){
            c.moveToFirst();
            Log.i("NUMBER IS ",c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            phNum.setText(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

        }else{
            Toast.makeText(this, "Rows in the db are 0", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Log.i("DA","onNothingSelected");

    }
    public void onSendSMS(View v){

        SmsManager mgrSMS = SmsManager.getDefault();
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mData = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(mData.isConnected()) {

            mgrSMS.sendTextMessage(phNum.getText().toString(), null, smsMsg.getText().toString(), null, null);
            Log.i("OnSendSMS", "Sent text");
        }else{
            Toast.makeText(this,"Cannot send the SMS as data connection is not there",Toast.LENGTH_LONG).show();
        }

    }
}
