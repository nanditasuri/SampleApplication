package com.example.nandita.practiceapp1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by nandita on 2/23/2015.
 */
public class GetDirectionsAsyncTask extends AsyncTask<Map<String,String>,Object,ArrayList> {
    private ShowMapActivity activity;
    private Exception exception;
    private ProgressDialog progressDialog;

    public GetDirectionsAsyncTask(ShowMapActivity activity)
    {
        super();
        this.activity = activity;
    }

    public void onPreExecute()
    {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Calculating directions");
        progressDialog.show();
    }

    @Override
    public void onPostExecute(ArrayList result)
    {
        Log.i("Asynctask", "in post execurte");
        progressDialog.dismiss();
        if (exception == null)
        {
            Log.i("asynctask", "in exception null");
            activity.handleGetDirectionsResult(result);
        }
        else
        {
            Log.i("asynctask", "in exception not null");
            processException();
        }
    }
    @Override
    protected ArrayList doInBackground(Map<String, String>... params) {
        Map<String, String> paramMap = params[0];
        Log.i("AsynctAsk", "In background");

        try
        {
            LatLng fromPosition = new LatLng(Double.valueOf(paramMap.get("user_current_lat")) , Double.valueOf(paramMap.get("user_current_long")));
            LatLng toPosition = new LatLng(Double.valueOf(paramMap.get("destination_lat")) , Double.valueOf(paramMap.get("destination_long")));
            GMapV2Direction md = new GMapV2Direction();
            Document doc = md.getDocument(fromPosition, toPosition, "driving");
            Log.i("AsynctAsk", "after get document");

            ArrayList directionPoints = md.getDirection(doc);
            Log.i("asynctask", "in after getdirections");

            return directionPoints;
        }
        catch (Exception e)
        {
            exception = e;
            return null;
        }

    }
    private void processException()
    {
        Toast.makeText(activity, "Error while getting the data", Toast.LENGTH_LONG).show();
    }
}

