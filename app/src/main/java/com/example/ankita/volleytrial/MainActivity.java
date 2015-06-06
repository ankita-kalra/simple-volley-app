package com.example.ankita.volleytrial;

/* This code is based on the code...mentioned in the blog AndroidHive */

import com.example.ankita.volleytrial.R;
import com.example.ankita.volleytrial.AppController;
import com.example.ankita.volleytrial.AuthJSONObjectRequest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;



public class MainActivity extends ActionBarActivity {

    //json object response url
    //private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";
    private String urlJsonObj = "http://systerspcweb.herokuapp.com/api/activity/1/?format=json";
    //json array response url
    private String urlJsonArray = "http://api.androidhive.info/volley/person_array.json";

    private static String TAG = MainActivity.class.getSimpleName();
    private Button btnMakeObjectRequest, btnMakeArrayRequest;

    //progress dialog
    private ProgressDialog progressDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMakeObjectRequest = (Button)findViewById(R.id.btnObjRequest);
        btnMakeArrayRequest = (Button) findViewById(R.id.btnArryRequest);
        txtResponse = (TextView) findViewById(R.id.textView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mking json object Request
                Log.i(TAG,"INSIDE ON CLICK LISTENER");
                makeJsonObjectRequest();
            }
        });

        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //making json Array request
                makeJsonArrayRequest();
            }
        });
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

    private void makeJsonObjectRequest(){
        Log.i(TAG,"INSIDE JSON OBJECT REQUEST");
        showpDialog(); //progress dialog shows loading...while the data is being fetched

        //making a JSON Object  Request below
        AuthJSONObjectRequest jsonObjReq = new AuthJSONObjectRequest(Method.GET, urlJsonObj, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Log.i(TAG,"INSIDE JSON RESPONSE");
                    //parsing json object response
                    String name = response.getString("activity_title");
                    String desc = response.getString("activity_desc");
                    //JSONObject phone = response.getJSONObject("phone");
                    //String home = phone.getString("home");
                    //String mobile = phone.getString("mobile");

                    jsonResponse = "";
                    jsonResponse += "Activity Name: " + name + "\n\n";
                    jsonResponse += "Desc: " + desc + "\n\n";
                    //jsonResponse += "Home: " + name + "\n\n";
                    //jsonResponse += "Mobile: " + name + "\n\n";

                    txtResponse.setText(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                //hide progress dialog
                hidepDialog();
            }
    });

        //Adding Request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void makeJsonArrayRequest(){
        showpDialog();
        // this function is similar, but for making an JSON Array Request instead of Json Object
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArray,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("name");
                                String email = person.getString("email");
                                JSONObject phone = person
                                        .getJSONObject("phone");
                                String home = phone.getString("home");
                                String mobile = phone.getString("mobile");

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "Email: " + email + "\n\n";
                                jsonResponse += "Home: " + home + "\n\n";
                                jsonResponse += "Mobile: " + mobile + "\n\n\n";

                            }

                            txtResponse.setText(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog(){
        if(!progressDialog.isShowing())
            progressDialog.show();
        //for showing the loading animation in Activity
    }

    private void hidepDialog(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
        //for dismissing the loading animation in Activity
    }
}
