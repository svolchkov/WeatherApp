package com.example.sergeyv.weatherapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sergeyv on 5/07/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    Context mContext;
    final String imageDir = "imageDir";

    public DownloadImageTask(ImageView bmImage, Context context) {
        this.bmImage = bmImage;
        this.mContext = context;
    }



    protected Bitmap doInBackground(String... fileNames) {
        String fileName = fileNames[0];
        Bitmap mIcon11 = null;
        mIcon11 = loadImageFromStorage(imageDir,fileName);
        if (mIcon11 != null){
            // icon found in local storage
            Log.e("MYAPP","Loaded file from local storage");
            return mIcon11;
        }
        try {
            String s = mContext.getString(R.string.loadImageString);
            String urldisplay = String.format(s, fileName);
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
            saveToInternalStorage(mIcon11,fileName);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        bmImage.setAdjustViewBounds(true);
    }

    private Bitmap loadImageFromStorage(String path, String fileName)
    {

        try {
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            //bmImage.setImageBitmap(b);
            return b;
        }
        catch (FileNotFoundException e)
        {
            return null;
        }

    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(mContext);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(imageDir, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e("MYAPP", "Failed to save file", e);
            }
        }
        return directory.getAbsolutePath();
    }
}
