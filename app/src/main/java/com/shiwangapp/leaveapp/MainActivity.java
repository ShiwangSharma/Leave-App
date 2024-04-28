package com.shiwangapp.leaveapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private CardView leave_card, logOut_card;
    private RecyclerView recyclerView;
    private LeaveAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leave_card = findViewById(R.id.leave_card);
        recyclerView = findViewById(R.id.recycleView);
        logOut_card = findViewById(R.id.log_out_card);
        logOut_card.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetStyle);
            View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.logout_bottom_sheet, findViewById(R.id.bottomSheet));
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.SheetAnimation;

            TextView no, yes;

            yes = view1.findViewById(R.id.logYes);
            no = view1.findViewById(R.id.logNo);

            yes.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });

            no.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.show();
        });

        leave_card.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LeaveActivity.class));
        });

        setUpRecycler();
    }

    private void setUpRecycler() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Query query = FirebaseFirestore.getInstance().collection("Students")
                .document(currentUser.getUid()).collection("Student_leaves").orderBy("requestDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Leaves> options = new FirestoreRecyclerOptions.Builder<Leaves>()
                .setQuery(query, Leaves.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new LeaveAdapter(options, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}