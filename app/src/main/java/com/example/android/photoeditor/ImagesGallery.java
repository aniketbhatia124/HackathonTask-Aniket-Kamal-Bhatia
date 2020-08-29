package com.example.android.photoeditor;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;


import java.util.ArrayList;

class ImagesGallery {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static ArrayList<String> listOfImages(Context context){

        Uri uri;
        Cursor cursor;
        int columnindexdata;

        ArrayList<String> listofallImages= new ArrayList<>();
        String pathofimage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy+" DESC");

        columnindexdata = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);



        while(cursor.moveToNext()){
            pathofimage = cursor.getString(columnindexdata);

            listofallImages.add(pathofimage);

        }
        return listofallImages;
    }
}
