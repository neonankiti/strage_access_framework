package com.finc.strageframewok;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_PICKER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // transition to creation of folder in android device.
        findViewById(R.id.transition_to_folder_creation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FolderCreateActivity.class));
            }
        });

        // pick image from document.
        findViewById(R.id.pick_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDocumentPicker();
            }
        });
    }

    private void showDocumentPicker() {
        // this is for editing and deleting. the other is ACTION_GET_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // add category filtering with openable document in the device.
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // filtering with mime type
        intent.setType("image/*");

        startActivityForResult(intent, CODE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_PICKER) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    try {
                        runInBackground(data);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    private Bitmap bitmap;

    private void runInBackground(@Nullable final Intent data) throws InterruptedException {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = getPickerBitmap(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("bitmap instance is ", "" + bitmap);
            }
        });
        th.start();
        th.join();

        ImageView bitmapView = (ImageView) findViewById(R.id.bitmap);
        bitmapView.setImageBitmap(bitmap);
    }

    @Nullable
    private Bitmap getPickerBitmap(@Nullable Intent data) throws IOException {
        if (data == null) {
            return null;
        }

        Uri uri = data.getData();
        if (uri == null) {
            return null;
        }

        ParcelFileDescriptor parcelFileDescriptor
                = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return bitmap;
    }

}
