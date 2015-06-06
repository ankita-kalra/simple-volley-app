package com.example.ankita.volleytrial;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ankita on 6/7/2015.
 */
public class AuthJSONObjectRequest extends JsonObjectRequest {

public AuthJSONObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,Response.ErrorListener errorListener) {

    super(method, url, jsonRequest, listener, errorListener);
}
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s","systerspcweb","systerspcweb");
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
        params.put("Authorization", auth);
        return params;
    }



}
