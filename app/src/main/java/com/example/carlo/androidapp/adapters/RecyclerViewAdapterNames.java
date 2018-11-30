package com.example.carlo.androidapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.actividades.pay_showticket;

import java.util.ArrayList;

public class RecyclerViewAdapterNames extends RecyclerView.Adapter<RecyclerViewAdapterNames.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapterName";
    private ArrayList<String> ticketTypes;
    private ArrayList<Integer> ticketCount;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> ages = new ArrayList<>();
    private ArrayList<String> ticketType;
    private ArrayList<Integer> countTicketType;
    private Context mContext;
    private long unixtimestamp;
    private Intent mintent;
    private int tourid;


    public RecyclerViewAdapterNames( Context mContext,ArrayList<String> ticketTypes, ArrayList<Integer> ticketCount, ArrayList<String> ticketType, ArrayList<Integer> countTicketType, long unixtimestamp, int tourid) {
        this.ticketTypes = ticketTypes;
        this.ticketCount = ticketCount;
        this.mContext = mContext;
        this.countTicketType =countTicketType;
        this.ticketType=ticketType;
        this.unixtimestamp = unixtimestamp;
        this.tourid = tourid;
        mintent = new Intent(mContext,pay_showticket.class);
        for(int i=0; i<ticketTypes.size();i++){
            names.add("");
            ages.add("");
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ticketgetnames,viewGroup,false);
        RecyclerViewAdapterNames.ViewHolder holder = new RecyclerViewAdapterNames.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
    String typename = ticketTypes.get(i);
    int typecount = ticketCount.get(i);
    String nametodisplay = typename + " " + typecount;
    viewHolder.displayname.setText(nametodisplay);
        viewHolder.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                names.set(i,s.toString());

            }
        });

        viewHolder.age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ages.set(i,s.toString());

            }
        });

    }

    @Override
    public int getItemCount() {
        return ticketTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView displayname;
        EditText name,age;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            displayname = itemView.findViewById(R.id.pasajero);
            name = itemView.findViewById(R.id.nombrePasajeroForm);
            age = itemView.findViewById(R.id.edadPasajeroForm);
            relativeLayout = itemView.findViewById(R.id.relLayoutticketsnames);
        }
    }
    public void startIntent(){
        if(correctData()) {
            int datasize = ticketCount.size();
            mintent.putExtra("datasize", datasize);
            mintent.putExtra("tour_id", tourid);
            mintent.putStringArrayListExtra("Names array", names);
            mintent.putIntegerArrayListExtra("Count array", countTicketType);
            mintent.putStringArrayListExtra("Types array", ticketType);
            mintent.putExtra("dateselected", unixtimestamp);
            mContext.startActivity(mintent);
        }else
            Toast.makeText(mContext, "Los datos están incompletos o no son aceptables",
                    Toast.LENGTH_LONG).show();
    }

    private boolean correctData(){
        for(int i=0; i<names.size();i++){
            Log.d(TAG, "correctData: name "+names.get(i));
            Log.d(TAG, "correctData: age "+ages.get(i));
            if (names.get(i).equals("") || ages.get(i).equals(""))
                return false;
            if(!InputFormatAuthentication.isNameValid(names.get(i)))
                return false;
        }
        return true;
    }
}
