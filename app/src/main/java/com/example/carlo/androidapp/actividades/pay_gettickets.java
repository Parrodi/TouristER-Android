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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.adapters.RecyclerViewAdapterTicket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class pay_gettickets extends AppCompatActivity {

    SharedPreferences prf;
    private static final String TAG = "pay_gettickets";
    private ArrayList<String> mTicketTypes = new ArrayList<>();
    private ArrayList<Double> mTicketPrices = new ArrayList<>();
    Intent myintent;
    private long unixtimestamp;
    Button cont;
    private String accesstoken;
    private int tourid;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payform2);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);

        cont = (Button)findViewById(R.id.Continuar);
        myintent = getIntent();
        unixtimestamp = myintent.getLongExtra("dateselected",0);

        accesstoken = prf.getString("token", null);

        tourid = myintent.getIntExtra("tour_id",0);
        getVariables();

    }

    private void getVariables(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://er-citytourister.appspot.com/Price?tour_id="+tourid;

        JsonArrayRequest requestPrices = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject price = response.getJSONObject(i);
                        JSONObject tickettype = price.getJSONObject("ticket_type_id");
                        String name = tickettype.getString("name");
                        mTicketTypes.add(name);
                        Double mprice = price.getDouble("priceAmount");
                        mTicketPrices.add(mprice);

                    } catch (JSONException e) {
                        Toast.makeText(pay_gettickets.this, "Hubo un error al obtener información del sistema",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
                initRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(pay_gettickets.this, "Hubo un error al conectarse al servidor",
                        Toast.LENGTH_LONG).show();
                error.printStackTrace();
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
        RecyclerView recyclerView = findViewById(R.id.recyclerviewticket);
        final RecyclerViewAdapterTicket adapter = new RecyclerViewAdapterTicket(this,mTicketTypes,mTicketPrices,unixtimestamp,tourid);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               adapter.startIntent();
            }
        });
    }
}


