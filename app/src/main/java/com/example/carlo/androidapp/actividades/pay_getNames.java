package com.example.carlo.androidapp.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.adapters.RecyclerViewAdapterNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class pay_getNames extends AppCompatActivity {

    SharedPreferences prf;
    private static final String TAG = "pay_getNames";
    private ArrayList<String> mTicketTypes = new ArrayList<>();
    private ArrayList<Integer> mTicketCount = new ArrayList<>();
    private ArrayList<String> ticketType = new ArrayList<>();
    private ArrayList<Integer> ticketCount = new ArrayList<>();
    private long unixtimestamp;
    Intent myintent;
    Button cont;
    private String accesstoken;
    private int tourid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payform3);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);

        cont = (Button)findViewById(R.id.Continuar);
        myintent = getIntent();
        unixtimestamp = myintent.getLongExtra("dateselected",0);
        accesstoken = prf.getString("token", null);
        tourid = myintent.getIntExtra("tour_id",tourid);
        getVariables();
    }

    private void getVariables(){
        final Intent data = getIntent();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://er-citytourister.appspot.com/Price?tour_id="+tourid;

        final JsonArrayRequest requestPrices = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject price = response.getJSONObject(i);
                        JSONObject tickettype = price.getJSONObject("ticket_type_id");
                        String name = tickettype.getString("name");
                        if (data.hasExtra(name)) {
                            setArrays(name,data.getIntExtra(name, 0));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        
                    }

                }
                Log.d(TAG, "CHECKING PARAMETERS FROM INTENT");
                for(int i=0;i<mTicketTypes.size();i++){
                    Log.d(TAG, mTicketTypes.get(i) + mTicketCount.get(i));
                }
                initRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d(TAG, "onErrorResponse: failed second json");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("auth", accesstoken);
                return headers;
            }
        };
        mQueue.add(requestPrices);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recyclerviewnames);
        final RecyclerViewAdapterNames adapter = new RecyclerViewAdapterNames(this,mTicketTypes,mTicketCount,ticketType,ticketCount,unixtimestamp,tourid);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.startIntent();
            }
        });

    }

    private void setArrays(String typename, int typecount){
        for(int i=1; i<=typecount;i++){
            mTicketTypes.add(typename);
            mTicketCount.add(i);

        }
        ticketType.add(typename);
        ticketCount.add(typecount);
    }
}
