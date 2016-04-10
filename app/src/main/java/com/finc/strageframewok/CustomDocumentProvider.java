package com.finc.strageframewok;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract.Root;
import android.provider.DocumentsProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.FileNotFoundException;

/**
 * This is custom document provider.
 * Because of this class, the app can create directory
 * for that app like BisonFolder.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class CustomDocumentProvider extends DocumentsProvider {

    // This is projection for root of document.
    private final String[] rootProjection = new String[]{
            Root.COLUMN_ROOT_ID,
            Root.COLUMN_DOCUMENT_ID,
            Root.COLUMN_FLAGS,
            Root.COLUMN_ICON,
            Root.COLUMN_MIME_TYPES,
            Root.COLUMN_SUMMARY,
            Root.COLUMN_TITLE
    };

    @Override
    public Cursor queryRoots(String[] projection) throws FileNotFoundException {
        // create root cursor for creating new document in root.
        // define the columns for that root document.
        MatrixCursor cursor = new MatrixCursor(resolveRootProjection(projection));

        // insert data for each column.
        final MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(
                Root.COLUMN_ROOT_ID,
                CustomDocumentProvider.class.getName() + "/" + ".bison"
        );
        rowBuilder.add(
                Root.COLUMN_DOCUMENT_ID,
                "/bison"
        );
        rowBuilder.add(
                Root.COLUMN_FLAGS,
                Root.FLAG_SUPPORTS_CREATE | Root.FLAG_SUPPORTS_SEARCH
        );
        rowBuilder.add(
                Root.COLUMN_ICON,
                R.drawable.bison
        );
        rowBuilder.add(
                Root.COLUMN_MIME_TYPES,
                "*/*"
        );
        rowBuilder.add(
                Root.COLUMN_SUMMARY,
                "Bison's document"
        );
        rowBuilder.add(
                Root.COLUMN_TITLE,
                "Bison Directory"
        );

        return cursor;
    }

    @Override
    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        return null;
    }

    @Override
    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        return null;
    }

    @Override
    public ParcelFileDescriptor openDocument(String documentId, String mode, CancellationSignal signal) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @NonNull
    private String[] resolveRootProjection(@Nullable String[] projection) {
        if (projection == null
                || projection.length == 0) {
            return rootProjection;
        }
        return projection;
    }
}
