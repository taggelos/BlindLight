package com.example.aggel.blindlight.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;

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

       /* t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        String text = "Hello World 2";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //t1.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
            System.out.println(t1.speak(text,TextToSpeech.QUEUE_FLUSH,null,null));
        } else {
            System.out.println("55555IIIIN2222?");
            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

        System.out.println("WORKIIIIIIIN2222?");
    }

    public void onPause(){
        if(t1 !=null){
            //t1.stop();
            //t1.shutdown();
        }
        super.onPause();
    }



    /////////////////////////////////////////////////


    public static Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault()
                        .createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName("BlindLightApp")
                .build();
    }

    private  Vision vision;


    /**
     * Prints the labels received from the Vision API.
     */
    public static void printLabels(PrintStream out, List<EntityAnnotation> labels) {
        //out.printf("Labels for image %s:\n", imagePath);
        for (EntityAnnotation label : labels) {
            out.printf(
                    "\t%s (score: %.3f)\n",
                    label.getDescription(),
                    label.getScore());
        }
        if (labels.isEmpty()) {
            out.println("\tNo labels found.");
        }
    }


    public List<EntityAnnotation> labelImage(byte[] data, int maxResults) throws IOException, GeneralSecurityException {
        //byte[] data = File.readAllBytes(path);


        vision= getVisionService();

        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(ImmutableList.of(
                                new Feature()
                                        .setType("LABEL_DETECTION")
                                        .setMaxResults(maxResults)));

        Vision.Images.Annotate annotate =
                vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);

        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getLabelAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }
        return response.getLabelAnnotations();
    }

    //////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAM_REQUEST) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                camImg.setImageBitmap(thumbnail);
                try {
                    //labelImage (getBytesFromBitmap(thumbnail),100);
                    try {
                        printLabels(System.out, labelImage (getBytesFromBitmap(thumbnail),100));
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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

           // File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //String pictureName = getPictureName();
            // File imageFile = new File(pictureDirectory,pictureName);
            //Uri pictureUri = Uri.fromFile(imageFile);
           // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);

           // System.out.println(pictureDirectory.getAbsolutePath() + "<----------- my path");
            startActivityForResult(cameraIntent,CAM_REQUEST);
        }
    }

}
