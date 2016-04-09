package com.finc.strageframewok;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * This is {@link android.app.Activity} for folder creation.
 */
public class FolderCreateActivity extends AppCompatActivity {

    private static final int WRITE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_create);

        findViewById(R.id.create_txt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile();
            }
        });
    }

    // create text file
    private void createFile() {
        // when you create document, you need to add Intent.ACTION_CREATE_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // filter to only show openable items.
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested Mime type
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "Neonankiti.txt");

        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFile(data.getData(), "bison is bision");
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    private void writeInFile(@NonNull Uri uri, @NonNull String text) {
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
