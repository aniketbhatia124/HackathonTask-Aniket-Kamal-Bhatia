package com.example.android.photoeditor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;
    TextView gallery_number;

    private static final int PERMISSIONCODE=101;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery_number= findViewById(R.id.gallerynumber);
        recyclerView= findViewById(R.id.recyclerviewgallery);


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONCODE);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                loadimages();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)

    private void loadimages() {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        images = ImagesGallery.listOfImages(this);
        galleryAdapter = new GalleryAdapter(this, images, new GalleryAdapter.Photolistener() {
            @Override
            public void onPhotoClick(String path, int position) {
                Toast.makeText(MainActivity.this, ""+ path, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PhotoEditorActivity.class);
                intent.putExtra("position", position);

                startActivity(intent);





            }
        });
        recyclerView.setAdapter(galleryAdapter);

        gallery_number.setText("Photos(" +images.size()+")");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode ==PERMISSIONCODE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    loadimages();
                }
            }
            else {
                Toast.makeText(this, "External Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


