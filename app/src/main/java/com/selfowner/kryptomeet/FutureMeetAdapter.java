package com.selfowner.kryptomeet;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FutureMeetAdapter extends RecyclerView.Adapter<FutureMeetAdapter.MyHolder> {
    private Context context;
    private List<MeetFuture> list;

    public FutureMeetAdapter(Context context, List<MeetFuture> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview= LayoutInflater.from(context).inflate(R.layout.custom_list,parent,false);
        return new MyHolder(rootview);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MeetFuture meetFuture=list.get(position);
        holder.getdate.setText(meetFuture.getDate());
        holder.gettime.setText(meetFuture.getTime());
        holder.getpassword.setText(meetFuture.getPassword());
        holder.getroomname.setText(meetFuture.getRoomName());
        holder.getcode.setText(meetFuture.getCode());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView getdate;
        TextView gettime;
        TextView getpassword;
        TextView getroomname;
        Button getcode;
        public MyHolder(View itemView){
            super(itemView);
            getdate=itemView.findViewById(R.id.getdate);
            gettime=itemView.findViewById(R.id.gettime);
            getpassword=itemView.findViewById(R.id.getpassword);
            getroomname=itemView.findViewById(R.id.getroomname);
            getcode=itemView.findViewById(R.id.getcode);
        }
    }
}
