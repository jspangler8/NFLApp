package com.example.jacobspangler.nflapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        Button playerSearch = findViewById(R.id.playerSearch);
        TextView resultTextView = findViewById(R.id.Result);
        resultTextView.setText("Hello");

        final android.widget.Button click = findViewById(R.id.playerSearch);
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
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://nflarrest.com/api/v1/player/topCrimes/" + Name + "?limit=1",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.e(TAG, error.toString());
                }
            });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle the response from the NFL arrest API.
     *
     * @param response response from our NFL arrest API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            // Log.i(TAG, response.get("hostname").toString());
            final android.widget.TextView result = findViewById(R.id.Result);
            String crime = response.get("category").toString();
            result.setText(crime);
        } catch (JSONException ignored) { }
    }
}
