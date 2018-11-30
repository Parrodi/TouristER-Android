package com.example.carlo.androidapp.actividades;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.modelos.Place;
import com.example.carlo.androidapp.modelos.Tour;

import java.util.Objects;

public class PlacePopActivity extends Activity {

    private TextView name;
    private TextView description;
    private Button placeButton;
    private Place place;
    Tour tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_pop);

        place = (Place) getIntent().getExtras().getSerializable("place");
        tour = (Tour)Objects.requireNonNull(getIntent().getExtras()).getSerializable("tour");
        int placeType = getIntent().getExtras().getInt("place_type");

        ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.constrainLayout);
        name = (TextView)findViewById(R.id.namePlace);
        description = (TextView)findViewById(R.id.descriptionPlace);
        placeButton = (Button)findViewById(R.id.botonPlace);

        name.setText(place.getName());

        switch (placeType){
            case 1: cl.setBackgroundColor(Color.parseColor("#FCB600"));
                break;
            case 2: cl.setBackgroundColor(Color.parseColor("#3DAE2A"));
                break;
            case 3: cl.setBackgroundColor(Color.parseColor("#E21181"));
                break;
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.x = 0;
        params.y = 350;

        getWindow().setAttributes(params);

        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlacePopActivity.this, PlaceDescriptionActivity.class);
                Bundle bundle = new Bundle();
                Bundle bundle1 = new Bundle();
                bundle.putSerializable("place", place);
                bundle1.putSerializable("tour", tour);
                i.putExtras(bundle1);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


    }
}
