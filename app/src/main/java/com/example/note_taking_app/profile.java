package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

    private TextView nameTextView;
    private EditText emailTextView;
    private EditText emailEditText;
    private Button saveButton;

    private TextView notesCountTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new profile.ThemeChangeListener());

        Button logoutButton = findViewById(R.id.logoutButton);

        // Initialize TextView
        emailTextView = findViewById(R.id.emailEditTextProfile);
        nameTextView = findViewById(R.id.nameTextViewProfile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.emailEditTextProfile);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        // Fetch email from Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                emailTextView.setText(email);
                nameTextView.setText("Sumuditha Lansakara");
            } else {
                emailTextView.setText("sumuditha@gmail.com");
                nameTextView.setText("Sumuditha Lansakara");
            }
        } else {
            // Handle the case where user is null
        }

        String userId = mAuth.getCurrentUser().getUid();
        fetchNotesCount(userId);
        fetchAccountCreationDate();

        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.getMenu().findItem(R.id.navProfile).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                    startActivity(new Intent(profile.this, notesactivity.class));

                } else if (itemId == R.id.navSearch) {
                    startActivity(new Intent(profile.this, todo_tasks.class));

                } else if (itemId == R.id.navProfile) {
                    //

                } else if (itemId == R.id.navScanner) {
                    startActivity(new Intent(profile.this, scanner.class));

                } else { // nav NewNote
                    startActivity(new Intent(profile.this, createnote.class));
                }

                bottomNavigationView.getMenu().findItem(R.id.navProfile).setChecked(true);
                return true;
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(profile.this, MainActivity.class));
                finish();
            }
        });
    }

    private void updateEmail() {
        final String newEmail = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newEmail)) {
            emailEditText.setError("Email is required");
            return;
        }

        mAuth.getCurrentUser().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Update email in Firestore if Firebase Authentication update is successful
                    updateEmailInFirestore(newEmail);
                } else {
                    Toast.makeText(profile.this, "Failed to update email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateEmailInFirestore(final String newEmail) {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.update("email", newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(profile.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(profile.this, "Failed to update email in Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchAccountCreationDate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get the timestamp of when the user account was created
            long creationTimestamp = user.getMetadata().getCreationTimestamp();

            // Calculate the difference between the current time and the account creation time in days
            long currentTime = System.currentTimeMillis();
            long elapsedTimeMillis = currentTime - creationTimestamp;
            long elapsedDays = elapsedTimeMillis / (1000 * 60 * 60 * 24); // Convert milliseconds to days

            // Display the number of days since the account was created
            TextView daysSinceCreationTextView = findViewById(R.id.daysSinceCreationTextView);
            if (daysSinceCreationTextView != null) {
                daysSinceCreationTextView.setText("Days since account creation: " + elapsedDays);
            } else {
                Log.e("TextView", "daysSinceCreationTextView is null");
            }
        } else {
            // Handle the case where user is null
        }
    }

    private void fetchNotesCount(String userId) {
        // Access Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        notesCountTextView = findViewById(R.id.notesCountTextView);
        String firebaseUser = mAuth.getCurrentUser().getUid();
        CollectionReference notesCollectionRef = db.collection("notes").document(firebaseUser).collection("myNotes");

        // Query the notes collection
        notesCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Get the number of documents (notes) in the collection
                    int notesCount = task.getResult().size();

                    // Update the TextView with the notes count
                    if (notesCountTextView != null) {
                        notesCountTextView.setText("Number of notes created: " + notesCount);
                    } else {
                        Log.e("TextView", "TextView is null");
                    }
                } else {
                    // Handle errors
                    Log.e("Firestore", "Error getting notes count: " + task.getException().getMessage());
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void updateBackgroundBasedOnTheme(Configuration configuration) {
        int nightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        ImageView mlogoImageViewProfile = findViewById(R.id.logoImageViewProfile);
        CircleImageView profileImage = findViewById(R.id.profile_image);


        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Apply dark theme styles for EditText and Toolbar

                bottomNavigationView.setBackgroundColor(Color.parseColor("#201f25"));
                bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#c9c4cf")));
                bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#e9dff8")));
                mlogoImageViewProfile.setImageResource(R.drawable.logo_dark);
            }
        } else {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#f3edf7"));
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#48454e")));
            mlogoImageViewProfile.setImageResource(R.drawable.logo_light);
            mlogoImageViewProfile.setBackgroundColor(Color.parseColor("#f3edf7"));
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