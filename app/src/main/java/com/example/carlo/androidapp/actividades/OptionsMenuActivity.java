/*Actividad encargada de operar el menú de optiones*/
package com.example.carlo.androidapp.actividades;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.modelos.Tour;

public class OptionsMenuActivity extends Activity {

    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        /*Listener para llevar al usuario a HelpActivity*/
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.ayuda);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionsMenuActivity.this, HelpActivity.class);
                startActivity(i);
            }
        });
        /*Listener para cerrar sesión*/
        RelativeLayout rl2 = (RelativeLayout)findViewById(R.id.logOut);
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionsMenuActivity.this, LoginActivity.class);
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.apply();
                startActivity(i);
            }
        });

        /*EL menú de opciones funciona como un PopUp
        * Se usa DisplayMetrics para cargar y deplegar el popUp*/
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width), (int)(height*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        getWindow().setAttributes(params);

        /*Configuración de navegación con la barra inferior*/
        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.botomNavigation);
        menu.setSelectedItemId(R.id.menus);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tours :
                        Intent in = new Intent(OptionsMenuActivity.this, MapsActivity.class);
                        startActivity(in);
                        break;
                    case R.id.tickets :
                        Intent tick = new Intent(OptionsMenuActivity.this, showPurchaseActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("tour", (Tour) getIntent().getExtras().getSerializable("tour"));
                        tick.putExtras(bundle3);
                        startActivity(tick);
                        break;
                    case R.id.mapa :
                        Intent i = new Intent(OptionsMenuActivity.this, UserMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tour", (Tour) getIntent().getExtras().getSerializable("tour"));
                        i.putExtras(bundle);
                        startActivity(i);
                        break;
                    case R.id.salidas :
                        Intent intent = new Intent(OptionsMenuActivity.this, TimeIntervaleActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("tour", (Tour) getIntent().getExtras().getSerializable("tour"));
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        break;
                    case R.id.menus :
                        finish();
                        break;
                }
                return false;
            }
        });
    }
}
