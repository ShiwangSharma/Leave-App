package com.shiwangapp.leaveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeaveViewingActivity extends AppCompatActivity {

    private TextView leavePurposeTv, leaveTypeTv, leaveRequestDateTv, leaveTillTv, leaveFromTv, leaveDescriptionTv, leaveStatusTv;
    private View statusView;
    private LinearLayout home_ll, purpose_ll, type_ll, request_ll, till_ll, from_ll, description_ll, status_ll;
    private String leave_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_viewing);

        leavePurposeTv = findViewById(R.id.leave_purpose_tv);
        leaveStatusTv = findViewById(R.id.leave_status_tv);
        leaveTypeTv = findViewById(R.id.leave_type_tv);
        leaveRequestDateTv = findViewById(R.id.request_date_tv);
        leaveTillTv = findViewById(R.id.till_date_tv);
        leaveFromTv = findViewById(R.id.from_date_tv);
        leaveDescriptionTv = findViewById(R.id.leave_description_tv);
        statusView = findViewById(R.id.leaveStatusView);

        home_ll = findViewById(R.id.home_ll);
        purpose_ll = findViewById(R.id.leave_purpose_ll);
        type_ll = findViewById(R.id.leave_type_ll);
        request_ll = findViewById(R.id.request_ll);
        till_ll = findViewById(R.id.till_ll);
        from_ll = findViewById(R.id.from_ll);
        description_ll = findViewById(R.id.leave_description_ll);
        status_ll = findViewById(R.id.leave_status_ll);

        home_ll.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        leave_type = getIntent().getStringExtra("type");

        leaveTypeTv.setText(leave_type);
        leaveRequestDateTv.setText(getIntent().getStringExtra("request"));
        String status_l = getIntent().getStringExtra("status");
        leaveStatusTv.setText(status_l);
        switch (status_l){
            case "pending":
                statusView.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case "rejected":
                statusView.setBackgroundResource(R.drawable.red_shape);
                break;
            case "approved":
                statusView.setBackgroundResource(R.drawable.green_shape);
                break;
        }

        if(leave_type.equals("IntraDay")){

            purpose_ll.setVisibility(View.VISIBLE);

            leavePurposeTv.setText(getIntent().getStringExtra("purpose"));




        } else if (leave_type.equals("Some Days")) {
            purpose_ll.setVisibility(View.VISIBLE);
            from_ll.setVisibility(View.VISIBLE);
            till_ll.setVisibility(View.VISIBLE);
            description_ll.setVisibility(View.VISIBLE);

            leavePurposeTv.setText(getIntent().getStringExtra("purpose"));
            leaveFromTv.setText(getIntent().getStringExtra("from"));
            leaveTillTv.setText(getIntent().getStringExtra("till"));
            leaveDescriptionTv.setText(getIntent().getStringExtra("description"));



        }else if (leave_type.equals("Emergency")) {
            from_ll.setVisibility(View.VISIBLE);
            till_ll.setVisibility(View.VISIBLE);
            description_ll.setVisibility(View.VISIBLE);

            leaveFromTv.setText(getIntent().getStringExtra("from"));
            leaveTillTv.setText(getIntent().getStringExtra("till"));
            leaveDescriptionTv.setText(getIntent().getStringExtra("description"));
        }

    }
}