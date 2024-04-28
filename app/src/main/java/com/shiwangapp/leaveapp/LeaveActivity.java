package com.shiwangapp.leaveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LeaveActivity extends AppCompatActivity {

    LinearLayout home_ll, purpose_ll, from_ll, till_ll;
    private String[] leaveTypes, purposeTypes;
    private EditText descriptionEt, from_date_et, till_date_et;
    private String getSpinner, userId, getPurpose;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FloatingActionButton doneLeaveBt;
    private FirebaseFirestore database;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        database = FirebaseFirestore.getInstance();
        collectionReference = database.collection("Students");




        leaveTypes = getResources().getStringArray(R.array.leave_types);
        purposeTypes = getResources().getStringArray(R.array.purpose_types);

        Spinner spinner_leave_type = findViewById(R.id.spinner_type);
        Spinner spinner_purpose = findViewById(R.id.spinner_purpose);
        descriptionEt = findViewById(R.id.descriptionEt);
        from_date_et = findViewById(R.id.from_date_et);
        till_date_et = findViewById(R.id.till_date_et);
        till_date_et.setInputType(InputType.TYPE_NULL);
        till_date_et.setOnClickListener(view -> {
            showTillDatePicker();
        });
        from_date_et.setInputType(InputType.TYPE_NULL);
        from_date_et.setOnClickListener(view -> {
            showDatePickerDialog();
        });
        doneLeaveBt = findViewById(R.id.doneLeaveBt);

        doneLeaveBt.setOnClickListener(view -> {
            setUpLeave();
        });

        home_ll = findViewById(R.id.home_ll);
        purpose_ll = findViewById(R.id.purpose_ll);
        from_ll = findViewById(R.id.from_ll);
        till_ll = findViewById(R.id.till_ll);

        home_ll.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, leaveTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_leave_type.setAdapter(adapter);
        spinner_leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSpinner = leaveTypes[i];

                if (getSpinner.equals("Emergency")){
                    descriptionEt.setVisibility(View.VISIBLE);
                    from_ll.setVisibility(View.VISIBLE);
                    till_ll.setVisibility(View.VISIBLE);
                    purpose_ll.setVisibility(View.GONE);
                } else if (getSpinner.equals("Some Days")) {
                    descriptionEt.setVisibility(View.VISIBLE);
                    from_ll.setVisibility(View.VISIBLE);
                    till_ll.setVisibility(View.VISIBLE);
                    purpose_ll.setVisibility(View.VISIBLE);

                } else if (getSpinner.equals("IntraDay")){
                    descriptionEt.setVisibility(View.GONE);
                    from_ll.setVisibility(View.GONE);
                    till_ll.setVisibility(View.GONE);
                    purpose_ll.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, purposeTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_purpose.setAdapter(adapter1);
        spinner_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPurpose = purposeTypes[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void showTillDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, (monthOfYear + 1), year1);
                    till_date_et.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());


        datePickerDialog.show();
    }

    private void setUpLeave() {
        if (getSpinner.equals("IntraDay")){
            Toast.makeText(this, "Intraday", Toast.LENGTH_SHORT).show();

            Date date = new Date();
            CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

            Leaves leaves = new Leaves();
            leaves.setLeaveType(getSpinner);
            leaves.setPurpose(getPurpose);
            leaves.setStatus("pending");
            leaves.setRequestDate(sequence.toString());
//            HashMap<String,Object> leaveInfo = new HashMap<>();
//            leaveInfo.put("Leave_Type", "IntraDay");
//            leaveInfo.put("Purpose", getPurpose);
//            leaveInfo.put("status", "pending");
//            leaveInfo.put("request_date", sequence.toString());

            database.collection("Students").document(userId).collection("Student_leaves").add(leaves)
                    .addOnSuccessListener(unused -> {
                        startActivity(new Intent(LeaveActivity.this, MainActivity.class));
                        Toast.makeText(this, "Leave applied", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "leave not updated", Toast.LENGTH_SHORT).show();
                    });
        } else if (getSpinner.equals("Some Days")) {
            if(checkAllDetail()){
                Toast.makeText(this, "Some Days", Toast.LENGTH_SHORT).show();

                Date date = new Date();
                CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

                Leaves leaves = new Leaves();
                leaves.setLeaveType(getSpinner);
                leaves.setPurpose(getPurpose);
                leaves.setStatus("pending");
                leaves.setRequestDate(sequence.toString());
                leaves.setFromDate(from_date_et.getText().toString());
                leaves.setTillDate(till_date_et.getText().toString());
                leaves.setDescription(descriptionEt.getText().toString());

//                HashMap<String,Object> leaveInfo = new HashMap<>();
//                leaveInfo.put("Leave_Type", "Some Days");
//                leaveInfo.put("Purpose", getPurpose);
//                leaveInfo.put("From", from_date_et.getText().toString());
//                leaveInfo.put("Till", till_date_et.getText().toString());
//                leaveInfo.put("Description", descriptionEt.getText().toString());
//                leaveInfo.put("status", "pending");
//                leaveInfo.put("request_date", sequence.toString());

                database.collection("Students").document(userId).collection("Student_leaves").add(leaves)
                        .addOnSuccessListener(unused -> {
                            startActivity(new Intent(LeaveActivity.this, MainActivity.class));
                            Toast.makeText(this, "Leave applied", Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "leave not updated", Toast.LENGTH_SHORT).show();
                        });
            }

        } else if (getSpinner.equals("Emergency")) {
            if (checkAllDetail()){
                Toast.makeText(this, "Emergency", Toast.LENGTH_SHORT).show();

                Date date = new Date();
                CharSequence sequence = DateFormat.format("MMMM d, yyyy", date.getTime());

                Leaves leaves = new Leaves();
                leaves.setLeaveType(getSpinner);
                leaves.setStatus("pending");
                leaves.setPurpose("Emergency");
                leaves.setRequestDate(sequence.toString());
                leaves.setFromDate(from_date_et.getText().toString());
                leaves.setTillDate(till_date_et.getText().toString());
                leaves.setDescription(descriptionEt.getText().toString());

                database.collection("Students").document(userId).collection("Student_leaves").add(leaves)
                        .addOnSuccessListener(unused -> {
                            startActivity(new Intent(LeaveActivity.this, MainActivity.class));
                            Toast.makeText(this, "Leave applied", Toast.LENGTH_SHORT).show();
                            finish();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(this, "leave not updated", Toast.LENGTH_SHORT).show();
                        });

            }

        }
    }


    private boolean checkAllDetail() {
        if (from_date_et.getText().toString().trim().isEmpty()){
            from_date_et.setError("Please select start date");
            return false;
        }else if (till_date_et.getText().toString().trim().isEmpty()) {
            till_date_et.setError("Please select end date");
            return false;
        }else if (descriptionEt.getText().toString().trim().isEmpty()){
            descriptionEt.setError("Please enter description");
            return false;
        }
        else {
            return true;
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, (monthOfYear + 1), year);
                        from_date_et.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());


        datePickerDialog.show();
    }
}