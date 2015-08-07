
package com.example.nandita.practiceapp1;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener{

    PracticeApp app;
    int [] buttonIds = {R.id.onCall,R.id.onCalendar,R.id.onBrowser,R.id.onAddressBook,R.id.onLocation,R.id.onMessage};
    ConnectivityManager cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* creating app object which can be used*/
        app = new PracticeApp();
        app = (PracticeApp)getApplication();

        /*set onClick Listener to all buttons in the Main Activity*/
        setOnClickButtonListeners();

    }
    /* private method called within onCreate to set listeners to the buttons*/
    private void setOnClickButtonListeners(){
        int i;
        Log.i("setOnClickButtonListeners:","length is "+buttonIds.length);

        for(i=0;i<buttonIds.length;i++){
            Button b = (Button) findViewById(buttonIds[i]);
            b.setOnClickListener(this);
        }
    }
    @Override
    /*onClick method called when a button is clicked in the Main Activity*/
    public void onClick(View v) {

        int clickId;
        Intent i;

        clickId = v.getId();
        i = new Intent();

        switch (clickId){

            case R.id.onCall:{
                Log.i("onClick:","OnCall method is called");
                i.setAction(Intent.ACTION_DIAL);
                startActivity(i);
                break;
            }
            case R.id.onCalendar:{
                Log.i("onClick:","OnCalendar method is called");
                i.setClass(this,MyCalendar.class);
                startActivity(i);
                break;
            }
            case R.id.onBrowser:{
                Log.i("onClick:","OnBrowser method is called");
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mData = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if(mWifi.isConnected()){
                         /*For intent ,set the action as VIEW and Data as blank
                as we want just the browser to open
                 */
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("about:blank"));

                /* Before starting the activity, we are resolving the activity,
                meaning knowing the class/activity to be called by passing the package manager
                as parameter.
                returns the best action for the intent
                 */
                    if(i.resolveActivity(getPackageManager())!=null){
                        startActivity(i);
                    }
                    else
                        Log.i("onClick:","OnBrowser resolveActivity returned null");

                }else if(mData.isConnected()){
                         /*For intent ,set the action as VIEW and Data as blank
                as we want just the browser to open
                 */
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("about:blank"));

                /* Before starting the activity, we are resolving the activity,
                meaning knowing the class/activity to be called by passing the package manager
                as parameter.
                returns the best action for the intent
                 */
                    if(i.resolveActivity(getPackageManager())!=null){
                        startActivity(i);
                    }
                    else
                        Log.i("onClick:","OnBrowser resolveActivity returned null");

                }else{
                    Toast.makeText(this,"Wifi or Data Connection are not enabled",Toast.LENGTH_LONG).show();
                }



                break;
            }
            case R.id.onAddressBook:{
                Log.i("onClick:","OnAddressBook method is called");
                i.setClass(this,AddressBookActivity.class);
                startActivity(i);
                break;
            }
            case R.id.onLocation:{
                Log.i("onClick:","OnLocation method is called");
                i.setClass(this,MapActivity.class);
                startActivity(i);
                break;
            }
            case R.id.onMessage:{
                Log.i("onClick:","OnMessage method is called");
                i.setClass(this,MessageActivity.class);
                startActivity(i);
                break;
            }
            default:
                break;

        }//end of switch

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
