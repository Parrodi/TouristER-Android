package com.example.carlo.androidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.actividades.pay_getNames;

import java.util.ArrayList;

public class RecyclerViewAdapterTicket extends RecyclerView.Adapter<RecyclerViewAdapterTicket.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapterTick";
    private ArrayList<String> mTicketTypes = new ArrayList<>();
    private ArrayList<Double> mTicketPrice = new ArrayList<>();
    private Context mContext;
    private long unixtimestamp;
    private Intent mintent;
    private int tourid, ticketcounter=0;


    public RecyclerViewAdapterTicket(Context mContext,ArrayList<String> mTicketTypes, ArrayList<Double> mTicketPrice, long unixtimestamp,int tourid) {
        this.mTicketTypes = mTicketTypes;
        this.mTicketPrice = mTicketPrice;
        this.mContext = mContext;
        this.unixtimestamp = unixtimestamp;
        this.tourid =tourid;
        mintent = new Intent(mContext,pay_getNames.class);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticketcountselection,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        String price = mTicketPrice.get(i).toString();
        viewHolder.ticketType.setText(mTicketTypes.get(i));
        viewHolder.ticketPrice.setText(price);

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int ticketcount = Integer.parseInt(viewHolder.ticketNum.getText().toString())+1;
                ticketcounter +=1;
              if(ticketcounter>10){
                  ticketcounter-=1;
                  ticketcount-=1;
                  Toast.makeText(mContext, "El máximo de boletos por compra es 10",
                          Toast.LENGTH_LONG).show();
              }
                  String count = ticketcount + "";
                  viewHolder.ticketNum.setText(count);
                  mintent.putExtra(mTicketTypes.get(i), ticketcount);


            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ticketcount = Integer.parseInt(viewHolder.ticketNum.getText().toString())-1;
                ticketcounter -=1;
                if(ticketcount<0) {
                    ticketcount = 0;
                    ticketcounter +=1;
                }
                if(ticketcounter<0)
                    ticketcounter=0;
                String count = ticketcount+"";
                viewHolder.ticketNum.setText(count);
                mintent.putExtra(mTicketTypes.get(i),ticketcount);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mTicketTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView ticketType, ticketPrice, ticketNum;
        Button plus, minus;
        RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketType = itemView.findViewById(R.id.ticketType);
            ticketPrice = itemView.findViewById(R.id.ticketPrice);
            ticketNum = itemView.findViewById(R.id.numOfTickets);
            plus = itemView.findViewById(R.id.moreTickets);
            minus = itemView.findViewById(R.id.lessTickets);
            relativeLayout = itemView.findViewById(R.id.relLayouttickets);
            
        }
    }

    public void startIntent(){
        if(ticketcounter == 0)
            Toast.makeText(mContext, "Se necesita mínimo un boleto para realizar la compra",
                    Toast.LENGTH_LONG).show();
        else {
            mintent.putExtra("dateselected", unixtimestamp);
            mintent.putExtra("tour_id", tourid);
            mContext.startActivity(mintent);
        }
    }
}
