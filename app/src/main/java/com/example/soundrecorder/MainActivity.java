package com.example.soundrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button play;
    private Button stop;
    private Button record;
    private Button stopRecord;
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    String pathsave="";

    final int REQUEST_PERMISSION_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermissionFromDevice()){
            RequestPermission();
        }
        this.stop= (Button) findViewById(R.id.stop);
        this.play= (Button) findViewById(R.id.play);
        this.stopRecord= (Button) findViewById(R.id.stopRecord);
        this.record= (Button) findViewById(R.id.record);

            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pathsave = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                            + UUID.randomUUID()+toString()+"Audio_recorder.Jgp";
                    setUpMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, "Recording", Toast.LENGTH_SHORT).show();
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stop.setEnabled(true);

                    mediaPlayer = new MediaPlayer();

                    try{
                        mediaPlayer.setDataSource(pathsave);
                        mediaPlayer.prepare();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Playing", Toast.LENGTH_SHORT).show();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer != null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
            });

            stopRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaRecorder.stop();

                }
            });


     /*   else{
            RequestPermission();
        }
*/


    }

    private void setUpMediaRecorder(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);
    }

    private boolean checkPermissionFromDevice() {
        int result_from_storage_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return (result_from_storage_permission == PackageManager.PERMISSION_GRANTED) &&
                (record_audio_result == PackageManager.PERMISSION_GRANTED);
    }

   private void RequestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }

    }
}

///https://www.youtube.com/watch?v=-eFoX4K59qM