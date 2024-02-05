package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.interfaces.RSAKey;

public class notedetails extends AppCompatActivity {

    private TextView mtitleofnotedetail, mcontentofnotedetail;
    FloatingActionButton mgotoeditnote;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        bottomNavigationView = findViewById(R.id.bottomNavView);

        //mtitleofnotedetail=findViewById(R.id.titleofnotedetail);
        mcontentofnotedetail=findViewById(R.id.contentofnotedetail);
        mgotoeditnote=findViewById(R.id.gotoeditnote);


        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new notedetails.ThemeChangeListener());


        Toolbar toolbar = findViewById(R.id.my_toolbar_notedetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent data=getIntent();

        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),editnoteactivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteId",data.getStringExtra("noteId"));

                view.getContext().startActivity(intent);
            }
        });
        mcontentofnotedetail.setText(data.getStringExtra("content"));
        //mtitleofnotedetail.setText(data.getStringExtra("title"));

        getSupportActionBar().setTitle(data.getStringExtra("title"));



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                    startActivity(new Intent(notedetails.this, MainActivity.class));

                } else if (itemId == R.id.navSearch) {
                    startActivity(new Intent(notedetails.this, todo_tasks.class));

                } else if (itemId == R.id.navProfile) {
                    //

                } else if (itemId == R.id.navScanner) {
                    startActivity(new Intent(notedetails.this, scanner.class));

                } else { // nav NewNote
                    startActivity(new Intent(notedetails.this, createnote.class));

                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("ResourceAsColor")
    private void updateBackgroundBasedOnTheme(Configuration configuration) {
        int nightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        TextView contentofnote = findViewById(R.id.contentofnotedetail);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        FloatingActionButton mgotoeditnote = findViewById(R.id.gotoeditnote);
        ImageView mlogoImageDetailsofNote = findViewById(R.id.logoImageDetailsofNote);
        RelativeLayout mrelativelayoutofnotedetails = findViewById(R.id.relativelayoutofnotedetails);

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                contentofnote.setTextAppearance(R.style.TextAppearance_DarkTheme);
                bottomNavigationView.setBackgroundColor(Color.parseColor("#201f25"));
                bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#c9c4cf")));
                bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#e9dff8")));
                contentofnote.setBackgroundColor(Color.parseColor("#212121"));
                mgotoeditnote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#495d64")));
                mgotoeditnote.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                mlogoImageDetailsofNote.setImageResource(R.drawable.logo_dark);
                mrelativelayoutofnotedetails.setBackgroundColor(Color.parseColor("#201f25"));
            }
        } else {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#f3edf7"));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#48454e")));
            mgotoeditnote.getDrawable().setColorFilter(null);
            mlogoImageDetailsofNote.setImageResource(R.drawable.logo_light);
            mlogoImageDetailsofNote.setBackgroundColor(Color.parseColor("#f3edf7"));
            mrelativelayoutofnotedetails.setBackgroundColor(Color.parseColor("#f3edf7"));
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