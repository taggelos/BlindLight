package com.example.aggel.blindlight.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import com.example.aggel.accelerometerapplication.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private TextToSpeech t1;
    private EditText ed1;
    private Button b1;
    private Button camBtn;
    private ImageView camImg;
    private static final int CAM_REQUEST = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        camBtn = (Button) findViewById(R.id.camBtn);
        camImg = (ImageView) findViewById(R.id.camImg);

        camBtn.setOnClickListener(new BtnClicker());
        /////////////////////////////////////////////////
        //ed1=(EditText)findViewById(R.id.editText);
        //b1=(Button)findViewById(R.id.button);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        String text = "Hello World";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            t1.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
            //System.out.println(t1.speak(text,TextToSpeech.QUEUE_FLUSH,null,null));
        } else {
            System.out.println("55555IIIIN?");
            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

        System.out.println("WORKIIIIIIIN?");

       /* b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = ed1.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                //String text = editText.getText().toString();
                String text = "Axneeee";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    t1.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
                } else {
                    t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });*/

    }

    public void onPause(){
        if(t1 !=null){
            //t1.stop();
            //t1.shutdown();
        }
        super.onPause();
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
