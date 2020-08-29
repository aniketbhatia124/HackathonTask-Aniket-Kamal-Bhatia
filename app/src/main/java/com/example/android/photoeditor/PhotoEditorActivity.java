package com.example.android.photoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhotoEditorActivity extends AppCompatActivity {


    ArrayList<String> images;
    ImageView imageedit;
    Spinner selectcolourfilter;
   String[] colours;
   Button rotate,saveimage,shareimage,crop;
    String date;
    CropImageCanvas view;
    Canvas canvas1;
   Button doodle;
   DrawView drawView;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editimage);

        imageedit = findViewById(R.id.imageedit);
        saveimage= findViewById(R.id.saveimage);
        shareimage = findViewById(R.id.share);
        crop =findViewById(R.id.crop);
        drawView = findViewById(R.id.doodleview);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        rotate = findViewById(R.id.rotate);
        view=findViewById(R.id.imagedoodle);
        doodle=findViewById(R.id.doodle);

        images = ImagesGallery.listOfImages(this);
        final String image = images.get(position);

        Glide.with(getApplicationContext()).load(image).into(imageedit);


        doodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setVisibility(View.VISIBLE);
            }
        });

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.VISIBLE);

            }
        });



        selectcolourfilter= (Spinner) findViewById(R.id.colourfilters);
        colours= new String[]{" ","Red", "Blue","Grey","Green","Yellow","Violet","Orange"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colours);
        selectcolourfilter.setAdapter(adapter);


        selectcolourfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: imageedit.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 1: imageedit.setColorFilter(0xFFD54A4A, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 2: imageedit.setColorFilter(0xFF5E55DC, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 3: imageedit.setColorFilter(0xFF8E8E8F, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 4: imageedit.setColorFilter(0xFF6FDD6F, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 5: imageedit.setColorFilter(0xFFEFE167, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 6: imageedit.setColorFilter(0xFFA159EA, PorterDuff.Mode.LIGHTEN);
                        break;
                    case 7: imageedit.setColorFilter(0xFFF49E5C, PorterDuff.Mode.LIGHTEN);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageedit.setRotation(imageedit.getRotation()+90);
            }
        });

        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream fileOutputStream;
                File file = getdisc();
                if(!file.exists() && !file.mkdirs()){
                    Toast.makeText(getApplicationContext(),"Cannot make Directory",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyymmsshhmmss");
                date= simpleDateFormat.format(new Date());
                String name = "img"+date+".jpeg";
                String file_name= file.getAbsolutePath()+"/"+name;
                File new_file = new File(file_name);
                try{
                    fileOutputStream= new FileOutputStream(new_file);
                    Bitmap bitmap = Bitmap.createBitmap(imageedit.getWidth(), imageedit.getHeight(),Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.clipRect(view.left,view.top,view.right,view.bottom);
                    imageedit.draw(canvas);


                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    Toast.makeText(getApplicationContext(),"Image Saved",Toast.LENGTH_SHORT).show();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                catch (FileNotFoundException e) {

                }catch (IOException e){

                }refreshGallery(file);
            }

            private void refreshGallery(File file) {
                Intent intent1= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file)); sendBroadcast(intent1);
            }

            @NonNull
            private File getdisc() {
                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                return new File(file,"PhotoEditor");
            }
        });

        shareimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareimage = new Intent(Intent.ACTION_SEND);
                shareimage.setType("image/*");
                File root = Environment.getExternalStorageDirectory();
                shareimage.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/storage/emulated/0/DCIM/PhotoEditor/img"+ date+".jpeg")));
                startActivity(Intent.createChooser(shareimage, "Share via"));
            }
        });




//        BitmapDrawable drawable = (BitmapDrawable) imageedit.getDrawable();
//        Bitmap bitmap = drawable.getBitmap();
    }

}

