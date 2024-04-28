package com.shiwangapp.leaveapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LeaveAdapter extends FirestoreRecyclerAdapter<Leaves, LeaveAdapter.LeaveViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    public LeaveAdapter(@NonNull FirestoreRecyclerOptions<Leaves> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull LeaveViewHolder holder, int position, @NonNull Leaves model) {

        holder.leaveType.setText(model.leaveType);
        holder.leavePurpose.setText(model.purpose);
        holder.leaveRequestDate.setText(model.requestDate);

        switch (model.status){
            case "pending":
                holder.leaveStatus.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case "rejected":
                holder.leaveStatus.setBackgroundResource(R.drawable.red_shape);
                break;
            case "approved":
                holder.leaveStatus.setBackgroundResource(R.drawable.green_shape);
                break;
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, LeaveViewingActivity.class);
            intent.putExtra("request", model.requestDate);
            intent.putExtra("type", model.leaveType);
            intent.putExtra("status", model.status);
            switch (model.leaveType){
                case "IntraDay":
                    intent.putExtra("purpose", model.purpose);
                    context.startActivity(intent);
                    break;
                case "Some Days":
                    intent.putExtra("purpose", model.purpose);
                    intent.putExtra("from", model.fromDate);
                    intent.putExtra("till", model.tillDate);
                    intent.putExtra("description", model.description);
                    context.startActivity(intent);
                    break;
                case "Emergency":
                    intent.putExtra("from", model.fromDate);
                    intent.putExtra("till", model.tillDate);
                    intent.putExtra("description", model.description);
                    context.startActivity(intent);
                    break;

            }
        });

    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeaveViewHolder(LayoutInflater.from(context).inflate(R.layout.item_leaves, parent, false));
    }

    public class LeaveViewHolder extends RecyclerView.ViewHolder{
        TextView leaveType, leavePurpose, leaveRequestDate;
        View leaveStatus;
        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            leavePurpose = itemView.findViewById(R.id.leavePurpose);
            leaveType = itemView.findViewById(R.id.leaveType);
            leaveStatus = itemView.findViewById(R.id.leaveStatus);
            leaveRequestDate = itemView.findViewById(R.id.requestDate);
        }
    }
}
