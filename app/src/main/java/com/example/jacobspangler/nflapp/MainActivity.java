package com.example.jacobspangler.nflapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Main class for our app
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "NFLApp:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        final android.widget.Button click = findViewById(R.id.playerSearch);
        final android.widget.TextView input = findViewById(R.id.playerName);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String name = input.getText().toString();
                System.out.println(name);
                startAPICall(name);
            }
        });
    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the NFL arrest record API.
     *
     * @param Name player to look up
     */
    void startAPICall(final String Name) {
        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://nflarrest.com/api/v1/player/topCrimes/" + Name,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonArrayRequest.setShouldCache(false);
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from the NFL arrest API.
     *
     * @param response response from our NFL arrest API.
     */
    void apiCallDone(final JSONArray response) {
        try {
            Log.d(TAG, response.toString(2));
            final android.widget.TextView result = findViewById(R.id.Result);
            final android.widget.TextView result2 = findViewById(R.id.Result2);
            final android.widget.TextView result3 = findViewById(R.id.Result3);
            if (response.length() == 0) {
                result.setText("Invalid Name");
                result2.setText("");
                result3.setText("");
            }
            if (response.length() > 0) {
                String crime = response.getJSONObject(0).get("category").toString();
                String count = response.getJSONObject(0).get("arrest_count").toString();
                result.setText(crime + " : " + count);
            }
            if (response.length() > 1) {
                String crime = response.getJSONObject(1).get("category").toString();
                String count = response.getJSONObject(1).get("arrest_count").toString();
                result2.setText(crime + " : " + count);
            }
            if (response.length() > 2) {
                String crime = response.getJSONObject(2).get("category").toString();
                String count = response.getJSONObject(2).get("arrest_count").toString();
                result3.setText(crime + " : " + count);
            }
        } catch (JSONException ignored) { }
    }
}
