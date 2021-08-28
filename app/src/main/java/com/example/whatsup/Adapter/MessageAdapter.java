package com.example.whatsup.Adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsup.Model.messsagemodel;
import com.example.whatsup.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    ArrayList<messsagemodel>messsagemodel;
    Context context;
    String receiveid;
    int sender_view_type =1;
    int receiver_view_type=2;

    public MessageAdapter(ArrayList<com.example.whatsup.Model.messsagemodel> messsagemodel, Context context, String receiveid) {
        this.messsagemodel = messsagemodel;
        this.context = context;
        this.receiveid = receiveid;
    }

    public MessageAdapter(ArrayList<com.example.whatsup.Model.messsagemodel> messsagemodel, Context context) {
        this.messsagemodel = messsagemodel;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==sender_view_type)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sampelsender,parent,false);
            return new senderviewholder(view);
        }
        else {

            View view= LayoutInflater.from(context).inflate(R.layout.sampelreceiver,parent,false);
            return new receiverviewholder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messsagemodel.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            return sender_view_type;
        }
        else {
            return receiver_view_type;
        }

        //return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        messsagemodel messsagemode= messsagemodel.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                .setTitle("Delete").setMessage("Are you sure want to Delete this message").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      FirebaseDatabase database=FirebaseDatabase.getInstance();
                      String sender=FirebaseAuth.getInstance().getUid()+receiveid;
                        DatabaseReference reference = database.getReference();
                        Log.d("sendert",sender + "    =>   "+messsagemode.getMessageid());

                        reference.child("chats").child(sender).child(messsagemode.getMessageid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                messsagemodel.remove(messsagemode);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                return false;
            }
        });
        if(holder.getClass()==senderviewholder.class){
            ((senderviewholder)holder).sendermsg.setText(messsagemode.getMessage());
        }
        else {
            ((receiverviewholder)holder).receivermsg.setText(messsagemode.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messsagemodel.size();
    }

    public class receiverviewholder extends RecyclerView.ViewHolder{
        TextView receivermsg,receivertime;
        public receiverviewholder(@NonNull View itemView) {
            super(itemView);
            receivermsg=itemView.findViewById(R.id.receiverid1);

            receivertime=itemView.findViewById(R.id.receiverid2);


        }}
        public class senderviewholder extends RecyclerView.ViewHolder{
            TextView sendermsg,sendertime;
            public senderviewholder(@NonNull View itemView) {
                super(itemView);
                sendermsg=itemView.findViewById(R.id.sendermsg1);

                sendertime=itemView.findViewById(R.id.sendermsg2);


            }
    }
}
