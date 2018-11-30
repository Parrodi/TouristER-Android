package com.example.carlo.androidapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.actividades.showTicketActivity;
import com.example.carlo.androidapp.modelos.Purchase;
import com.example.carlo.androidapp.modelos.Tour;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapterPurchase extends RecyclerView.Adapter<RecyclerViewAdapterPurchase.ViewHolder>{

    private Tour tour;
    private ArrayList<Purchase> purchases;
    private Context mcontext;
    private Intent mintent;

    public RecyclerViewAdapterPurchase(ArrayList<Purchase> purchases, Context mcontext, Tour tour) {
        this.purchases = purchases;
        this.mcontext = mcontext;
        this.tour = tour;
        mintent = new Intent(mcontext,showTicketActivity.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchaseform,viewGroup,false);
        RecyclerViewAdapterPurchase.ViewHolder holder = new RecyclerViewAdapterPurchase.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        String name = purchases.get(i).getTourname();
        Long unixtimestamp = purchases.get(i).getUnixdate();
        viewHolder.tour.setText(name);
        Date date = new java.util.Date(unixtimestamp*1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(date);
        viewHolder.date.setText(formattedDate);
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Bundle bundleTour = new Bundle();
                bundleTour.putSerializable("tour", tour);
                bundle.putSerializable("purchase", purchases.get(i));
                mintent.putExtras(bundle);
                mintent.putExtras(bundleTour);
                mcontext.startActivity(mintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tour, date;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tour = (TextView)itemView.findViewById(R.id.nombreTour);
            date = (TextView)itemView.findViewById(R.id.textoDeFecha);
            relativeLayout =(RelativeLayout)itemView.findViewById(R.id.purchase);
        }
    }
}
