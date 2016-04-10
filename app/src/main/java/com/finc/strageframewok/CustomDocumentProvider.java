package com.finc.strageframewok;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract.Document;
import android.provider.DocumentsContract.Root;
import android.provider.DocumentsProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
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

    // This is projection for document.
    private final String[] documentProjection = new String[]{
            Document.COLUMN_DOCUMENT_ID,
            Document.COLUMN_DISPLAY_NAME,
            Document.COLUMN_MIME_TYPE,
            Document.COLUMN_SIZE,
            Document.COLUMN_FLAGS,
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
        // create document cursor
        MatrixCursor cursor = new MatrixCursor(resolveDocumentProjection(projection));

        // create file from document id
        // the document id is supposed to be unique.
        File file = getFile(documentId);

        // check if the file is directory or file
        // and diverge the cursor row.
        DocumentObject document;
        if (file.isDirectory()) {
            document = new DocumentObject(
                    documentId,
                    file.getName(),
                    Document.MIME_TYPE_DIR,
                    Integer.MAX_VALUE,
                    Document.FLAG_DIR_SUPPORTS_CREATE
            );
        } else {
            document = new DocumentObject(
                    documentId,
                    file.getName(),
                    "text/plain",
                    Integer.MAX_VALUE,
                    Document.FLAG_SUPPORTS_WRITE
            );
        }

        return getDocumentCursor(cursor, document);
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

    @NonNull
    private String[] resolveDocumentProjection(@Nullable String[] projection) {
        if (projection == null
                || projection.length == 0) {
            return documentProjection;
        }
        return projection;
    }

    @NonNull
    private File getFile(@NonNull String documentId) {
        String filePath = CustomDocumentProvider.class.getName() + "/" + documentId;
        return new File(filePath);
    }

    // Gets cursor for document.
    private MatrixCursor getDocumentCursor(@NonNull MatrixCursor cursor, @NonNull DocumentObject obj) {
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(Document.COLUMN_DOCUMENT_ID, obj.documentId);
        rowBuilder.add(Document.COLUMN_DISPLAY_NAME, obj.displayName);
        rowBuilder.add(Document.COLUMN_MIME_TYPE, obj.mimeType);
        rowBuilder.add(Document.COLUMN_SIZE, obj.size);
        rowBuilder.add(Document.COLUMN_FLAGS, obj.flags);
        return cursor;
    }

    private static class DocumentObject {

        private final String documentId;

        private final String displayName;

        private final String mimeType;

        private final int size;

        private final int flags;

        public DocumentObject(String documentId, String displayName, String mimeType, int size, int flags) {
            this.documentId = documentId;
            this.displayName = displayName;
            this.mimeType = mimeType;
            this.size = size;
            this.flags = flags;
        }
    }

}
