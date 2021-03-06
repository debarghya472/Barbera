package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Rating extends AppCompatActivity {

    private NumberPicker picker;
    private Button btn;
    private int valuePicker1;
    private String bookingId;
    private  ProgressDialog progressDialog;
    private CardView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        progressDialog =new ProgressDialog(Rating.this);
        picker = findViewById(R.id. numPick);
        btn = findViewById(R.id.btntt);
        skip=(CardView)findViewById(R.id.skip_rating);
        picker.setMaxValue(5);
        picker.setMinValue(1);
        Intent intent = getIntent();
        bookingId = intent.getStringExtra("docId");

        picker.setOnValueChangedListener((picker, oldVal, newVal) -> {
           valuePicker1 = picker.getValue();
        });

        btn.setOnClickListener(v -> {
            progressDialog.setMessage("Submitting Ratings....");
            progressDialog.show();
            progressDialog.setCancelable(false);
            updateInDb();
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateInDb() {
        Map<String,Object> user=new HashMap<>();
        user.put("rating",valuePicker1+"");
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("Bookings").document(bookingId).update(user).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(this, "Thank You for your Response", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    progressDialog.dismiss();
                });
    }
}