package com.example.nandita.practiceapp1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MapDialog extends Activity {

    EditText toLoc,fromLoc;
    Button bGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dialog);
        toLoc = (EditText)findViewById(R.id.editText2);
        fromLoc = (EditText)findViewById(R.id.editText);
        bGo = (Button) findViewById(R.id.button_direct);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_dialog, menu);
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

    public void onShowDirections(View v){

        String toText=null;
        String fromText=null;

        toText = toLoc.getText().toString();
        fromText = fromLoc.getText().toString();
        if(toText==null || fromText == null)
            Toast.makeText(this,"Enter the location where you want to go",Toast.LENGTH_LONG).show();
        else{
            Intent i = new Intent(this,ShowMapActivity.class);
            i.putExtra("DIRECTIONS",true);
            i.putExtra("FROM_LOCATION",fromText);
            i.putExtra("TO_LOCATION",toText);

            startActivity(i);
            finish();
        }

    }
}
