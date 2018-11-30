package com.example.carlo.androidapp.actividades;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.modelos.Purchase;
import com.example.carlo.androidapp.modelos.Tour;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class showTicketActivity extends AppCompatActivity {
    private static final String TAG = "showTicketActivity";
    private LinearLayout passengers;
    private HashMap<String, Integer> typecount;
    private Purchase purchase;
    private TextView tour,mdate,total;
    private String accesstoken;
    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showticket);
        purchase = (Purchase) getIntent().getExtras().getSerializable("purchase");
        typecount = purchase.getTypecount();
        tour = (TextView)findViewById(R.id.nombreTour);
        mdate = (TextView)findViewById(R.id.textoDeFecha);
        total = (TextView)findViewById(R.id.Pagado);
        passengers = (LinearLayout)findViewById(R.id.listaPasajeros);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        accesstoken = pref.getString("token",null);
        setTextViews();

        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.botomNavigation);
        menu.setSelectedItemId(R.id.tickets);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tours :
                        Intent in = new Intent(showTicketActivity.this, MapsActivity.class);
                        startActivity(in);
                        break;
                    case R.id.tickets :
                        finish();
                        break;
                    case R.id.mapa :
                        Intent i = new Intent(showTicketActivity.this, UserMapActivity.class);
                        Bundle bundle = new Bundle();
                        Tour tour = (Tour) getIntent().getExtras().getSerializable("tour");
                        bundle.putSerializable("tour", tour);
                        i.putExtras(bundle);
                        startActivity(i);
                        break;
                    case R.id.salidas :
                        Intent intent = new Intent(showTicketActivity.this, TimeIntervaleActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("tour", (Tour) getIntent().getExtras().getSerializable("tour"));
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        break;
                    case R.id.menus :
                        Intent mIntent = new Intent(showTicketActivity.this, OptionsMenuActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("tour", (Tour) getIntent().getExtras().getSerializable("tour"));
                        mIntent.putExtras(bundle3);
                        startActivity(mIntent);
                        break;
                }
                return false;
            }
        });

    }

    private void setTextViews(){
        tour.setText(purchase.getTourname());
        long unixtimestamp = purchase.getUnixdate();
        Date date = new java.util.Date(unixtimestamp*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);
        mdate.setText(formattedDate);
        getTotal();
        for(String key : typecount.keySet()){
            TextView passenger = new TextView(this);
            passenger.setText(key);
            passenger.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            passenger.setTextColor(Color.parseColor("#043764"));
            passengers.addView(passenger);

        }


    }

    private void getTotal(){
        RequestQueue mqueue = Volley.newRequestQueue(this);
        String url = "https://er-citytourister.appspot.com/purchase/total/" + purchase.getId();

        final JsonObjectRequest gettotal = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    double mtotal = response.getDouble("subtotal");
                    String paid = "Pagado: "+ String.valueOf(mtotal);
                    total.setText(paid);
                } catch (JSONException e) {
                    Toast.makeText(showTicketActivity.this, "Hubo un error al tratar de obtener el total",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(showTicketActivity.this, "Hubo un error al conectarse con el servidor",
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
        mqueue.add(gettotal);
    }
}
