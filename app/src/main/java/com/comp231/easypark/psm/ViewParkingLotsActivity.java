package com.comp231.easypark.psm;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.comp231.easypark.Login;
import com.comp231.easypark.R;
import com.comp231.easypark.reservation.ParkingLot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class ViewParkingLotsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ListView listParkingLots;
    ParkingLotListAdapter parkingLotListAdapter;
    Button btnCreateNew;
    Button btnEditSelected;
    Button btnDeleteSelected;

    private FirebaseFirestore db;
    private List<ParkingLot> parkingLots;
    private int loadingCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parking_lots);

        // Get view references
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        listParkingLots = (ListView)findViewById(R.id.listViewParkingLots);
        btnCreateNew = (Button)findViewById(R.id.viewParkingLots_btnCreate);
        btnEditSelected = (Button)findViewById(R.id.viewParkingLots_btnEdit);
        btnDeleteSelected = (Button)findViewById(R.id.viewParkingLots_btnDelete);

        // Set up button listeners
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateParkingLotActivity.class));
            }
        });
        btnEditSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected ParkingLot and pass it to the editing activity
                int selectedParkingLot = parkingLotListAdapter.getSelectedPosition();
                PSMManager.setCurrentParkingLot(parkingLots.get(selectedParkingLot));
                startActivity(new Intent(getApplicationContext(), EditParkingLotActivity.class));
            }
        });
        btnDeleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinner();
                // Delete the parking lot currently selected
                int selectedParkingLot = parkingLotListAdapter.getSelectedPosition();
                db.collection("ParkingLotDemo").document(parkingLots.get(selectedParkingLot).getDocId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Also update the PSM data
                                PSM newData = PSMManager.getPSM();
                                newData.getParkingLots().remove(selectedParkingLot);
                                PSMManager.setPSM(newData);
                                DocumentReference sfDocRef = db.collection("PSM").document(PSMManager.getPSM().getEmail());
                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Override
                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                        //DocumentSnapshot snapshot = transaction.get(sfDocRef);
                                        transaction.update(sfDocRef, "parkingLots", newData.getParkingLots());
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        loadLatestData();
                                    }
                                });
                            }
                        });
            }
        });

        db = FirebaseFirestore.getInstance();
        parkingLots = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get ParkingLot data from the database
        loadLatestData();
    }

    private void loadLatestData() {
        loadingCounter = 0;
        parkingLots = new ArrayList<>();
        showSpinner();
        // First get fresh list of ParkingLots/PSM data
        if (PSMManager.getPSM() != null) {
            DocumentReference docRef = db.collection("PSM").document(PSMManager.getPSM().getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            PSMManager.setPSM(document.toObject(PSM.class));
                            List<String> pLots = PSMManager.getPSM().getParkingLots();
                            if (pLots != null && pLots.size() > 0) {
                                DocumentReference docRef;
                                for (int i = 0; i < pLots.size(); i++) {
                                    docRef = db.collection("ParkingLotDemo").document(pLots.get(i));
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    parkingLots.add(document.toObject(ParkingLot.class));
                                                    checkLoadingCounter();
                                                }
                                            }
                                        }
                                    });
                                }
                            } else {
                                Log.e("TAG", "Couldn't find any documents.");
                                Toast.makeText(ViewParkingLotsActivity.this, "We couldn't find any parking lots", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    private void showSpinner() {
        progressBar.setVisibility(View.VISIBLE);
        listParkingLots.setVisibility(View.INVISIBLE);
        btnCreateNew.setVisibility(View.INVISIBLE);
        btnEditSelected.setVisibility(View.INVISIBLE);
        btnDeleteSelected.setVisibility(View.INVISIBLE);
    }

    private void hideSpinner() {
        progressBar.setVisibility(View.INVISIBLE);
        listParkingLots.setVisibility(View.VISIBLE);
        btnCreateNew.setVisibility(View.VISIBLE);
        btnEditSelected.setVisibility(View.VISIBLE);
        btnDeleteSelected.setVisibility(View.VISIBLE);
    }

    private void checkLoadingCounter() {
        loadingCounter++;
        Log.d("TAG", "LoadingCounter: " + loadingCounter);
        if (loadingCounter >= parkingLots.size()) {
            // Loading is complete so set up everything else
            // Set up list of ParkingLots
            listParkingLots.setVisibility(View.VISIBLE);
            String[] names = new String[parkingLots.size()];
            for (int n = 0; n < parkingLots.size(); n++) {
                names[n] = parkingLots.get(n).getName();
            }
            String[] descriptions = new String[parkingLots.size()];
            for (int d = 0; d < parkingLots.size(); d++) {
                descriptions[d] = parkingLots.get(d).getDescription();
            }
            parkingLotListAdapter = new ParkingLotListAdapter(ViewParkingLotsActivity.this, names, descriptions);
            listParkingLots.setAdapter(parkingLotListAdapter);

            hideSpinner();
        }
    }
}