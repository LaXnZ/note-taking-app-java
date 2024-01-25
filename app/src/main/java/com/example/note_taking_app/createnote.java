package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;

public class createnote extends AppCompatActivity {

    EditText mcreatetitleofnote,mcreatecontentofnote;
    FloatingActionButton msavenote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    private FrameLayout frameLayout;

    private BottomNavigationView bottomNavigationView;
    ProgressBar mprogressbarofcreatenote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);

        msavenote=findViewById(R.id.savenote);
        mcreatecontentofnote=findViewById(R.id.createcontentofnote);
        mcreatetitleofnote=findViewById(R.id.createtitleofnote);

        mprogressbarofcreatenote=findViewById(R.id.progressbarofcreatenote);

        Toolbar toolbar = findViewById(R.id.toolbarofcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setItemIconTintList(null);

        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new createnote.ThemeChangeListener());



        bottomNavigationView.getMenu().findItem(R.id.navNewNote).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                    startActivity(new Intent(createnote.this, MainActivity.class));

                } else if (itemId == R.id.navSearch) {
                    //

                } else if (itemId == R.id.navProfile) {
                    //

                } else if (itemId == R.id.navScanner) {
                    startActivity(new Intent(createnote.this, scanner.class));

                } else { // nav NewNote
                    //
                }
                bottomNavigationView.getMenu().findItem(R.id.navNewNote).setChecked(true);
                return true;
            }
        });



        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=mcreatetitleofnote.getText().toString();
                String content=mcreatecontentofnote.getText().toString();
                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Both Fields are Required",Toast.LENGTH_SHORT).show();
                }else{
                    mprogressbarofcreatenote.setVisibility(View.VISIBLE);

                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note= new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note Created Successfully",Toast.LENGTH_SHORT).show();
                            mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(createnote.this,notesactivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to Create Note",Toast.LENGTH_SHORT).show();
                            mprogressbarofcreatenote.setVisibility(View.INVISIBLE);
                            // startActivity(new Intent(createnote.this,notesactivity.class));
                        }
                    });
                }
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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        EditText mcreatecontentofnote = findViewById(R.id.createcontentofnote);
        EditText mcreatetitleofnote = findViewById(R.id.createtitleofnote);
        Toolbar mtoolbar = findViewById(R.id.toolbarofcreatenote);
        FloatingActionButton msavenote = findViewById(R.id.savenote);
        ImageView mlogoImageViewNewNote = findViewById(R.id.logoImageViewNewNote);
        RelativeLayout mrelativenoteofcreatenote = findViewById(R.id.relativenoteofcreatenote);

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                bottomNavigationView.setBackgroundColor(Color.parseColor("#201f25"));
                bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#c9c4cf")));
                bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#e9dff8")));
                mcreatecontentofnote.setBackgroundColor(Color.parseColor("#212121"));
                mcreatetitleofnote.setBackgroundColor(Color.parseColor("#212121"));
                mcreatecontentofnote.setTextColor(getResources().getColor(android.R.color.white));
                mcreatetitleofnote.setTextColor(getResources().getColor(android.R.color.white));
                mtoolbar.setBackgroundColor(Color.parseColor("#212121"));
                msavenote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#495d64")));
                msavenote.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                mlogoImageViewNewNote.setImageResource(R.drawable.logo_dark);
                mrelativenoteofcreatenote.setBackgroundColor(Color.parseColor("#201f25"));
            }
        } else {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#f3edf7"));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#48454e")));
            msavenote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#97bdcb")));
            bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#010101")));
            msavenote.getDrawable().setColorFilter(null);
            mlogoImageViewNewNote.setImageResource(R.drawable.logo_light);
            mlogoImageViewNewNote.setBackgroundColor(Color.parseColor("#f3edf7"));
            mrelativenoteofcreatenote.setBackgroundColor(Color.parseColor("#f3edf7"));
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