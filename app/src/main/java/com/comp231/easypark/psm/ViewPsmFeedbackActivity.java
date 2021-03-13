package com.comp231.easypark.psm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.comp231.easypark.R;
import com.comp231.easypark.feedback.Feedback;
import com.comp231.easypark.reservation.ParkingLot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPsmFeedbackActivity extends AppCompatActivity {

    ListView listFeedback;
    FeedbackListAdapter feedbackListAdapter;
    List<Feedback> feedbacks;
    Button btnBack;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_psm_feedback);

        db = FirebaseFirestore.getInstance();
        listFeedback = (ListView)findViewById(R.id.listViewFeedback);
        feedbacks = new ArrayList<>();

        btnBack = (Button)findViewById(R.id.applyLicense_BackBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Get feedback data from database
        db.collection("Feedback")
                .whereEqualTo("psmId", PSMManager.getPSM().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Add query result to feedbacks
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                feedbacks.add(document.toObject(Feedback.class));
                            }
                            insertLatestData();
                        } else {
                            Log.e("TAG", "Failed to get feedback from the database.");
                            Toast.makeText(ViewPsmFeedbackActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void insertLatestData() {
        // Prepare data for the adapter
        String[] names = new String[feedbacks.size()];
        String[] ratings = new String[feedbacks.size()];
        String[] reviews = new String[feedbacks.size()];
        for (int i = 0; i < feedbacks.size(); i++) {
            names[i] = "Name: " + feedbacks.get(i).getName();
            ratings[i] = "Rating: " + feedbacks.get(i).getRatingBar();
            reviews[i] = "Review: " + feedbacks.get(i).getReview();
        }
        feedbackListAdapter = new FeedbackListAdapter(ViewPsmFeedbackActivity.this, names, ratings, reviews);
        listFeedback.setAdapter(feedbackListAdapter);
    }
}