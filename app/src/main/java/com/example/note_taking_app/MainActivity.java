package com.example.note_taking_app;

import android.annotation.SuppressLint;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.note_taking_app.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail, mloginpassword;
    private RelativeLayout mlogin, mgotosignup;
    private TextView mgotoforgotpassword;

    ProgressBar mprogressbarofmainactivity;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new ThemeChangeListener());

        ActionBar actionBar = getSupportActionBar(); // Get the ActionBar

        if (actionBar != null) {
            actionBar.hide(); // Hide the ActionBar if it's not null
        }

        mloginemail=findViewById(R.id.loginemail);
        mloginpassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.login);
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword);
        mgotosignup=findViewById(R.id.gotosignup);
        mprogressbarofmainactivity=findViewById(R.id.progressbarofmainactivity);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,notesactivity.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,forgotpassword.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All Fields Are Required",Toast.LENGTH_SHORT).show();
                }else{
                    mprogressbarofmainactivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkMailVerification();
                            }else {
                                    Toast.makeText(getApplicationContext(),"Account Does Not Exist",Toast.LENGTH_SHORT).show();
                                mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
    private void checkMailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notesactivity.class));
        }else {
            mprogressbarofmainactivity.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Verify Your Email First",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void updateBackgroundBasedOnTheme(Configuration configuration) {
        int nightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        TextView mlogintitle = findViewById(R.id.logintitle);
        TextView mdonthaveaccounttext = findViewById(R.id.donthaveaccounttext);
        TextView mgotoforgotpassword = findViewById(R.id.gotoforgotpassword);
        TextInputLayout memailinput = findViewById(R.id.email);
        TextInputLayout mpasswordinput = findViewById(R.id.password);

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                findViewById(R.id.logo_main).setBackgroundResource(R.drawable.logo_dark);
                findViewById(R.id.background_image_main).setBackgroundResource(R.drawable.background_image_dark);
                mlogintitle.setTextAppearance(R.style.TextAppearance_DarkTheme);
                mdonthaveaccounttext.setTextAppearance(R.style.TextAppearance_DarkTheme);
                mgotoforgotpassword.setTextAppearance(R.style.TextAppearance_DarkTheme);
                memailinput.setBoxBackgroundColor(R.color.text_input_dark);
                mpasswordinput.setBoxBackgroundColor(R.color.text_input_dark);
            }
        } else {
            findViewById(R.id.background_image_main).setBackgroundResource(R.drawable.background_image_light);
            findViewById(R.id.logo_main).setBackgroundResource(R.drawable.logo_light);
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