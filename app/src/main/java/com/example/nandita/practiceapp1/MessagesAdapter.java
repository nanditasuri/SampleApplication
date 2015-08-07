package com.example.nandita.practiceapp1;

/**
 * Created by nandita on 2/11/2015.
 */

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;

import static android.provider.Telephony.*;

public class MessagesAdapter extends BaseAdapter implements View.OnClickListener {

    Context cx;
    String phNum;
    String body;
    Message m;
    ArrayList<Message> lstSms;
    TextToSpeech tts;
    /*You are creating your own constructor*/
    public MessagesAdapter(Context cx) {
        this.cx = cx;

        lstSms = new ArrayList<Message>();

        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = cx.getContentResolver();
        Cursor cur = cr.query(message,null,null,null,null);

        if(cur.getCount()>0) {
            while (cur.moveToNext()) {
                phNum = cur.getString(cur.getColumnIndex("address"));
                body = cur.getString(cur.getColumnIndexOrThrow("body"));
                m = new Message(phNum,body);
                lstSms.add(m);
            }
        }

    }


    @Override
    public int getCount() {
        if(lstSms != null)
            return lstSms.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return lstSms.get(position);
    }

    @Override
    public void onClick(View v) {

        Log.i("OnCLick","clicked");

       if(v.getId()==R.id.button_readsms){
           LinearLayout ll = (LinearLayout)v.getParent();
           TextView tv = (TextView)ll.findViewById(R.id.body_tv);

           String txt = tv.getText().toString();
            tts = new TextToSpeech( cx,new TextToSpeech.OnInitListener(){

                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR){
                        tts.setLanguage(Locale.US);

                    }//if

                }//func
            });//obj
           try {
               Log.i("MessageAdapter","In Message adapter...");
               tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null, null);

           }catch(NoSuchMethodError e){
               e.printStackTrace();
           }

        }//read if
        else{

       }
    }


    static class ViewHolderPag
    {
        TextView numberTxt;
        TextView bodyTxt;
        Button bRead;
        Button bDelete;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        //So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.

        ViewHolderPag viewHolder;
        if(convertView==null){
            LayoutInflater inflate1 = (LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate1.inflate(R.layout.custommessagelist, parent, false);
            viewHolder = new ViewHolderPag();

            viewHolder.numberTxt = (TextView)convertView.findViewById(R.id.number_tv);
            viewHolder.bodyTxt = (TextView)convertView.findViewById(R.id.body_tv);
            viewHolder.bodyTxt.setMovementMethod(new ScrollingMovementMethod());
            viewHolder.bRead = (Button)convertView.findViewById(R.id.button_readsms);
            viewHolder.bDelete = (Button)convertView.findViewById(R.id.button_deletesms);


            // store the holder with the view.
            convertView.setTag(viewHolder);
        }
        else
        {
            Log.i("View holder is not null","adapter");
            viewHolder = (ViewHolderPag)convertView.getTag();
        }
        Message temp = lstSms.get(position);
        viewHolder.numberTxt.setText(temp.getPhNum());
        viewHolder.bodyTxt.setText(temp.getTxtBody());
        viewHolder.bodyTxt.setMovementMethod(new ScrollingMovementMethod());
        viewHolder.bRead.setOnClickListener(this);
        return convertView;
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
