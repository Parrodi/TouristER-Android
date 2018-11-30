/*Actividad para desplegar las pantallas de ayuda*/
package com.example.carlo.androidapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.carlo.androidapp.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_recomendacion);
        Button botonSiguiente = (Button)findViewById(R.id.button8);

        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.help_parada);
                Button otroBotonSiguiente = (Button)findViewById(R.id.button9);

                otroBotonSiguiente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.help_punto_de_interes);
                        Button botonEmpezar = (Button)findViewById(R.id.button10);
                        botonEmpezar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(HelpActivity.this, MapsActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        });
    }
}
