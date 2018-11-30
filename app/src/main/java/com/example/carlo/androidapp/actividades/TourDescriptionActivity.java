/*Actividad encargada de desplegar información de cada tour y dar acceso al botón de compra*/
package com.example.carlo.androidapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.modelos.Tour;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class TourDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_description);

        /*Obtención del tour por medio de Intents*/
        final Tour tour = (Tour)Objects.requireNonNull(getIntent().getExtras()).getSerializable("tour");

        String name = tour.getName();
        String description = tour.getDescription();
        String image_url = tour.getImage().toString();
        final int id = tour.getId();

        ImageView image_tour = (ImageView) findViewById(R.id.foto_de_tour);
        TextView descripcion_tour = (TextView) findViewById(R.id.tour_description);
        TextView nombre_tour = (TextView) findViewById(R.id.nombre_tour);
        Button botonCompra = (Button)findViewById(R.id.botonCompra);

        descripcion_tour.setText(description);
        nombre_tour.setText(name);
        Picasso.with(this).load(image_url).into(image_tour);
        /*Botón para dirijir a la compra*/
        botonCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TourDescriptionActivity.this,pay_getdate.class);
                i.putExtra("tour_id", id);
                startActivity(i);
            }
        });

        /*Configuración de navegación con la barra inferior*/
        BottomNavigationView menu = (BottomNavigationView) findViewById(R.id.botomNavigation);

        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tours :
                        finish();
                        break;
                    case R.id.tickets :
                        Intent mIntent = new Intent(TourDescriptionActivity.this, showPurchaseActivity.class);
                        Bundle bundl = new Bundle();
                        bundl.putSerializable("tour", tour);
                        mIntent.putExtras(bundl);
                        startActivity(mIntent);
                        break;
                    case R.id.mapa :
                        Intent i = new Intent(TourDescriptionActivity.this, UserMapActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("tour", tour);
                        i.putExtras(bundle);
                        startActivity(i);
                        break;
                    case R.id.salidas :
                        Intent in = new Intent(TourDescriptionActivity.this, TimeIntervaleActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("tour", tour);
                        in.putExtras(bundle2);
                        startActivity(in);
                        break;
                    case R.id.menus :
                        Intent intent = new Intent(TourDescriptionActivity.this, OptionsMenuActivity.class);
                        Bundle bundle3 = new Bundle();
                        bundle3.putSerializable("tour", tour);
                        intent.putExtras(bundle3);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}