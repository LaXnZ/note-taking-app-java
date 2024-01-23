package com.example.note_taking_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        mclear = findViewById(R.id.clear);
        mgetimage = findViewById(R.id.getimage);
        mcopy = findViewById(R.id.copy);
        mrecgtext = findViewById(R.id.recgText);

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
}