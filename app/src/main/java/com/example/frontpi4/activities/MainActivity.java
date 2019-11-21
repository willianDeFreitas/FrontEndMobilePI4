package com.example.frontpi4.activities;

import android.content.Intent;
import android.os.Bundle;
import com.example.frontpi4.R;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void chamaActivityLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}