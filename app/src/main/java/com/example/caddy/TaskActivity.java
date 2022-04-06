package com.example.caddy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {
    public static final String EXTRA_TASK="task";

    private TextView taskname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskname=findViewById(R.id.taskName);

        final Intent intent = getIntent();
        final String taskNameValue = intent.getStringExtra(EXTRA_TASK);

        intent.getStringExtra(EXTRA_TASK);

    }
}