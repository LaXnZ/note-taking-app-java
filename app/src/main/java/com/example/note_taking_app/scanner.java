package com.example.note_taking_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class scanner extends AppCompatActivity {

    ImageView mclear, mgetimage, mcopy;
    EditText mrecgtext;
    Uri mimageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        mclear = findViewById(R.id.clear);
        mgetimage = findViewById(R.id.getimage);
        mcopy = findViewById(R.id.copy);
        mrecgtext = findViewById(R.id.recgText);

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Activity.RESULT_OK){
            if(data!=null){
                mimageuri = data.getData();
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}