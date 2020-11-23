package com.comp231.easypark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MapPassActivity extends AppCompatActivity {
    private TextView txtDisplayDocId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_pass);
        txtDisplayDocId = findViewById(R.id.txtDisplayDocId);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String docId = bundle.getString("docId");
        String lotName = bundle.getString("lotName");
        txtDisplayDocId.setText(String.format("[%s] -> %s", docId, lotName));
    }
}