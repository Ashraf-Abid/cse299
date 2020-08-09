package com.example.cseproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

public class Afterlogin extends AppCompatActivity {
    private TextView mtextView;
    private  Button CaptureImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //private Bitmap Bitmap,imageBitmap;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_afterlogin);
        Button  uploadBtn=findViewById(R.id.upload);


        mtextView=findViewById(R.id.text);
        //upload optionm

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,100);

            }
        });
    }
    //sign Out

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.signOutMenuId){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent =new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode==100 && resultCode==RESULT_OK) {
                {
                    FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(Afterlogin.this, data.getData());
                    FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
                    //mtextView.append("hi i am here");
                    recognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String text = firebaseVisionText.getText();
                            mtextView.setText("");
                            if (text.isEmpty()) {
                                mtextView.append("No text found");
                            } else {

                                for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {

                                    mtextView.append("\n \n" + block.getText());

                                }
                            }
                        }
                    });

                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
