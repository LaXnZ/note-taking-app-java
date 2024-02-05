package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class todo_tasks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton mfloatingActionButton;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_tasks);

        recyclerView = findViewById(R.id.todoRecyclerView);
        mfloatingActionButton = findViewById(R.id.todoFloatingActionButton);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(todo_tasks.this));

        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new todo_tasks.ThemeChangeListener());

        bottomNavigationView = findViewById(R.id.bottomNavView);

        bottomNavigationView.getMenu().findItem(R.id.navSearch).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                    startActivity(new Intent(todo_tasks.this, MainActivity.class));

                } else if (itemId == R.id.navNewNote) {
                    startActivity(new Intent(todo_tasks.this, createnote.class));

                } else if (itemId == R.id.navProfile) {
                    //

                } else if (itemId == R.id.navScanner) {
                    startActivity(new Intent(todo_tasks.this, scanner.class));

                } else { // nav NewNote
                    //
                }
                bottomNavigationView.getMenu().findItem(R.id.navSearch).setChecked(true);
                return true;
            }
        });

        mfloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void updateBackgroundBasedOnTheme(Configuration configuration) {
        int nightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        ImageView mlogoImageToDO = findViewById(R.id.logoImageToDo);
        ConstraintLayout mconstraintlayouttodo = findViewById(R.id.constraintlayouttodo);
        FloatingActionButton mtodosave = findViewById(R.id.todoFloatingActionButton);
        RecyclerView mtodoRecyclerView = findViewById(R.id.todoRecyclerView);

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                bottomNavigationView.setBackgroundColor(Color.parseColor("#201f25"));
                bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#c9c4cf")));
                bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#e9dff8")));
                mlogoImageToDO.setImageResource(R.drawable.logo_dark);
                mlogoImageToDO.setBackgroundColor(Color.parseColor("#201f25"));
                mconstraintlayouttodo.setBackgroundColor(Color.parseColor("#201f25"));
                mtodoRecyclerView.setBackgroundColor(Color.parseColor("#201f25"));
                mtodosave.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#495d64")));
                mtodosave.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }
        } else {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#f3edf7"));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#48454e")));
            bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#010101")));
            mlogoImageToDO.setImageResource(R.drawable.logo_light);
            mlogoImageToDO.setBackgroundColor(Color.parseColor("#f3edf7"));
            mconstraintlayouttodo.setBackgroundColor(Color.parseColor("#f3edf7"));
            mtodoRecyclerView.setBackgroundColor(Color.parseColor("#f3edf7"));
            mtodosave.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#97bdcb")));
            mtodosave.getDrawable().setColorFilter(null);
        }
    }

    private class ThemeChangeListener implements ComponentCallbacks {

        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig) {
            updateBackgroundBasedOnTheme(newConfig);
        }

        @Override
        public void onLowMemory() {
            // Handle low memory situations if necessary
        }
    }


}