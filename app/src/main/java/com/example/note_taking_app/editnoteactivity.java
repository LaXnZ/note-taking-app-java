package com.example.note_taking_app;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {

    Intent data;
    EditText medittitleofnote,meditcontentofnote;
    FloatingActionButton msaveeditnote;
    private BottomNavigationView bottomNavigationView;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);

        medittitleofnote=findViewById(R.id.edittitleofnote);
        meditcontentofnote=findViewById(R.id.editcontentofnote);
        msaveeditnote=findViewById(R.id.saveeditnote);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();


        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new editnoteactivity.ThemeChangeListener());

        Toolbar toolbar = findViewById(R.id.toolbarofeditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        bottomNavigationView = findViewById(R.id.bottomNavView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                    startActivity(new Intent(editnoteactivity.this, MainActivity.class));

                } else if (itemId == R.id.navSearch) {
                    startActivity(new Intent(editnoteactivity.this, todo_tasks.class));

                } else if (itemId == R.id.navProfile) {
                    startActivity(new Intent(editnoteactivity.this, profile.class));

                } else if (itemId == R.id.navScanner) {
                    startActivity(new Intent(editnoteactivity.this, scanner.class));

                } else { // nav NewNote
                    startActivity(new Intent(editnoteactivity.this, createnote.class));

                }

                return true;
            }
        });
        bottomNavigationView.getMenu().findItem(R.id.navNewNote).setChecked(true);

        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"saved button",Toast.LENGTH_SHORT).show();

                String newtitle=medittitleofnote.getText().toString();
                String newcontent=meditcontentofnote.getText().toString();

                if(newcontent.isEmpty() || newtitle.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fields can't be empty",Toast.LENGTH_SHORT).show();
                }else {
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map <String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note is Updated",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(editnoteactivity.this,notesactivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to Update",Toast.LENGTH_SHORT).show();
                        }
                    });
                    }
                }

        });

        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        meditcontentofnote.setText(notecontent);
        medittitleofnote.setText(notetitle);
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
        EditText edittitleofnote = findViewById(R.id.edittitleofnote);
        EditText editcontentofnote = findViewById(R.id.editcontentofnote);
        Toolbar toolbarofeditnote = findViewById(R.id.toolbarofeditnote);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        FloatingActionButton msaveeditnote = findViewById(R.id.saveeditnote);
        ImageView mlogoImageEditNote = findViewById(R.id.logoImageEditNote);
        RelativeLayout mrelativelayoutofnotedit = findViewById(R.id.relativelayoutofnotedit);

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Apply dark theme styles for EditText and Toolbar
                edittitleofnote.setTextAppearance(R.style.TextAppearance_DarkTheme);
                editcontentofnote.setTextAppearance(R.style.TextAppearance_DarkTheme);
                editcontentofnote.setBackgroundColor(Color.parseColor("#212121")); // Set dark background color
                toolbarofeditnote.setBackgroundColor(Color.parseColor("#212121")); // Set dark background color
                bottomNavigationView.setBackgroundColor(Color.parseColor("#201f25"));
                bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#c9c4cf")));
                bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#e9dff8")));
                msaveeditnote.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#495d64")));
                msaveeditnote.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                mlogoImageEditNote.setImageResource(R.drawable.logo_dark);
                mrelativelayoutofnotedit.setBackgroundColor(Color.parseColor("#201f25"));
            }
        } else {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#f3edf7"));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#48454e")));
            msaveeditnote.getDrawable().setColorFilter(null);
            mlogoImageEditNote.setImageResource(R.drawable.logo_light);
            mlogoImageEditNote.setBackgroundColor(Color.parseColor("#f3edf7"));
            mrelativelayoutofnotedit.setBackgroundColor(Color.parseColor("#f3edf7"));
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