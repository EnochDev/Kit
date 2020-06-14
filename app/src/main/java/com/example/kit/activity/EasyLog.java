package com.example.kit.activity;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class EasyLog extends AppCompatActivity {

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals( state ) ) {
            return true;
        }
        return false;
    }
    private static final int PERMISSION_REQUEST_CODE = 1;
    public void appendLog(String text, Context context) throws FileNotFoundException {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            }
        }

        File fsd = Environment.getExternalStorageDirectory();
        File appDirectory = new File( fsd.getAbsolutePath() + "/Kit" );
        File logDirectory = new File( appDirectory + "/log" );
        File logFile = new File( logDirectory, "log.txt");
        // create app folder
        if ( !appDirectory.exists() ) {
            try {
                if (!appDirectory.mkdir())
                    Log.d("createLog", "mkdir failed!");
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        // create log folder
        if ( !logDirectory.exists() ) {
            logDirectory.mkdir();
            Log.d("createLog", "try to create logDirectory");
        }
//
//        // clear the previous logcat and then write the new one to the file
//        try {
//            Process process = Runtime.getRuntime().exec("logcat -c");
//            process = Runtime.getRuntime().exec("logcat -f " + logFile);
//        } catch ( IOException e ) {
//            e.printStackTrace();
//        }

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(new Date());
            StringBuffer sb = new StringBuffer();
            sb.append(time+"\n");
            sb.append(text+"\n");


            //BufferedWriter for performance, true to set append to file flag
            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter buf = new BufferedWriter(fw);
            buf.append(sb.toString());
            //buf.append(text);
            buf.newLine();
            buf.close();


        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
