/*Adaptador encargado de configurar el ViewPager para el carrusel de tours*/
package com.example.carlo.androidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.actividades.MapsActivity;
import com.example.carlo.androidapp.actividades.TourDescriptionActivity;
import com.example.carlo.androidapp.actividades.UserMapActivity;
import com.example.carlo.androidapp.modelos.Tour;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    /*Atributos*/
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Tour> tours;

    /*Constructor*/
    public ViewPagerAdapter(Context context, List<Tour> tours) {
        this.tours = tours;
        this.context = context;
    }

    /*Método para obtener el tamaño de tours*/
    @Override
    public int getCount() {
        return tours.size();
    }
    /*Método para determinar si el objeto es igual a la view*/
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /*Método para desplegar el carrusel*/
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position){

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        final Tour recorrido = tours.get(position);

        URL url = recorrido.getImage();

        Picasso.with(context).load(url.toString()).into(imageView);
        ViewPager vp =(ViewPager) container;
        vp.addView(view, 0);

        /*Listener para que cuando se seleccione una imagen del tour, se dirija a TourDescriptionActivity*/
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, TourDescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("tour", recorrido);
                i.putExtras(bundle);

                context.startActivity(i);
            }
        });
        return view;
    }


    /*Método para borrar el viewPager*/
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object){

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}