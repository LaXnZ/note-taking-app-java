package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class scanner extends AppCompatActivity {

    ImageView mclear, mgetimage, mcopy;
    EditText mrecgtext;
    Uri mimageuri;
    TextRecognizer textRecognizer;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        mclear = findViewById(R.id.clear);
        mgetimage = findViewById(R.id.getimage);
        mcopy = findViewById(R.id.copy);
        mrecgtext = findViewById(R.id.recgText);

        bottomNavigationView = findViewById(R.id.bottomNavView);

        // Set initial background image based on the current theme
        updateBackgroundBasedOnTheme(getResources().getConfiguration());

        // Register a listener to track theme changes
        getApplication().registerComponentCallbacks(new scanner.ThemeChangeListener());

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        mgetimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(scanner.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        mcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mrecgtext.getText().toString();
                
                if(text.isEmpty()){
                    Toast.makeText(scanner.this, "There is no text to copy", Toast.LENGTH_SHORT).show();
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(scanner.this.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Data",mrecgtext.getText().toString());
                        clipboardManager.setPrimaryClip(clipData);

                        Toast.makeText(scanner.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mrecgtext.getText().toString();

                if(text.isEmpty()){
                    Toast.makeText(scanner.this, "There is no text to erase", Toast.LENGTH_SHORT).show();
                }else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        mrecgtext.setText("");
                    }
                }

            }
        });
        bottomNavigationView.getMenu().findItem(R.id.navScanner).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.navHome){
                    startActivity(new Intent(scanner.this, MainActivity.class));

                } else if (itemId == R.id.navSearch) {
                    startActivity(new Intent(scanner.this, todo_tasks.class));

                } else if (itemId == R.id.navProfile) {
                    startActivity(new Intent(scanner.this, profile.class));

                } else if (itemId == R.id.navScanner) {
                    //

                } else { // nav NewNote
                    startActivity(new Intent(scanner.this, createnote.class));

                }

                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(data!=null){
                mimageuri = data.getData();
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
                recognizeText();
            }else{
                Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void recognizeText() {
        if(mimageuri != null){
            try {
                InputImage inputImage = InputImage.fromFilePath(scanner.this,mimageuri);

                Task<Text> result =textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        String recognizeText = text.getText();
                        mrecgtext.setText(recognizeText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(scanner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
    }


    @SuppressLint("ResourceAsColor")
    private void updateBackgroundBasedOnTheme(Configuration configuration) {
        int nightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        ConstraintLayout mconstraintLayout = findViewById(R.id.constraintLayout);
        ImageView clearImageView = findViewById(R.id.clear);
        ImageView copyImageView = findViewById(R.id.copy);
        ImageView getImageImageView = findViewById(R.id.getimage);
        TextView copyTextView = findViewById(R.id.textView);
        TextView clearTextView = findViewById(R.id.textView2);
        ImageView mlogoImageScanner = findViewById(R.id.logoImageScanner);

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mconstraintLayout.setBackgroundColor(Color.parseColor("#201f25"));

                // Change ImageView tints to white
                clearImageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                copyImageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                getImageImageView.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                // Change TextView text colors to white
                copyTextView.setTextColor(Color.WHITE);
                clearTextView.setTextColor(Color.WHITE);

                mlogoImageScanner.setImageResource(R.drawable.logo_dark);
            }
        } else {
            mconstraintLayout.setBackgroundColor(Color.parseColor("#f3edf7"));

            // Remove ImageView tints (reset to default)
            clearImageView.setColorFilter(null);
            copyImageView.setColorFilter(null);
            getImageImageView.setColorFilter(null);

            // Reset TextView text colors to default
            copyTextView.setTextColor(Color.parseColor("#000000"));
            clearTextView.setTextColor(Color.parseColor("#000000"));

            mlogoImageScanner.setImageResource(R.drawable.logo_light);
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