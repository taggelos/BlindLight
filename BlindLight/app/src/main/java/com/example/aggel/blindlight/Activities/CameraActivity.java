package com.example.aggel.blindlight.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import com.example.aggel.accelerometerapplication.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    Button camBtn;
    ImageView camImg;
    private static final int CAM_REQUEST = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camBtn = (Button) findViewById(R.id.camBtn);
        camImg = (ImageView) findViewById(R.id.camImg);

        camBtn.setOnClickListener(new BtnClicker());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAM_REQUEST) {
                //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //camImg.setImageBitmap(thumbnail);

            }
        }
    }



    class BtnClicker implements Button.OnClickListener{
        private String getPictureName(){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            System.out.println("PlantPlacesImage" + timestamp + ".jpg" + "777777");
            return "PlantPlacesImage" + timestamp + ".jpg";
        }

        @Override
        public void onClick(View v) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String pictureName = getPictureName();
            File imageFile = new File(pictureDirectory,pictureName);
            Uri pictureUri = Uri.fromFile(imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);

            System.out.println(pictureDirectory.getAbsolutePath() + "<----------- my path");
            startActivityForResult(cameraIntent,CAM_REQUEST);
        }
    }

}
