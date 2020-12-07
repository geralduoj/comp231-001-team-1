package com.comp231.easypark.feedback;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.comp231.easypark.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import static com.comp231.easypark.Login.userDocRef;

public class FeedbackActivity extends AppCompatActivity {
    //user.getDisplayName().
    private static final String TAG = "FeedbackActivity";
    private static final String Name_Key = "name";
    private static final String Review_Key = "review";
    private static final String Rating_Key = "ratingBar";

    private TextView TextViewName;
    private EditText editTextreview;
    private RatingBar RatingBarRating;
    private TextView FeedbackTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference feedRef;
    private DocumentSnapshot document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedRef = db.collection("Feedback").document();

        TextViewName = (TextView) findViewById(R.id.name);
        editTextreview = (EditText) findViewById(R.id.review);
        RatingBarRating = (RatingBar) findViewById(R.id.ratingBar);
        FeedbackTextView = (TextView) findViewById(R.id.textViewfeedback);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    document = task.getResult();
                    if (document.exists()) {
                        TextViewName.setText(document.getString("fName"));
                    }
                }
            }
        });
    }

    public void save(View v) {

        Feedback newFeedback = new Feedback();
        newFeedback.setName(TextViewName.getText().toString());
        String rating = "Rating :: " + RatingBarRating.getRating();
        newFeedback.setRatingBar(rating);
        newFeedback.setReview(editTextreview.getText().toString());

        Task insert = feedRef.set(newFeedback);
        insert.addOnSuccessListener(o -> {
            Context context = getApplicationContext();
            CharSequence text = "Success!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            FeedbackTextView.setText(editTextreview.getText());
        });
    }
}



