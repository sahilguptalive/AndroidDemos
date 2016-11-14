package sahilguptalive.com.androiddemos.scopped_acess;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import sahilguptalive.com.androiddemos.R;

/**
 * {@link ScopedDirectoryAccess} is an activity to show the use of Scoped Directory Access API available in Android Nougat.
 * All required information for this API is shown in step-by-step nature.
 * We can follow these steps to add Scoped Access in our code, in an easy manner.
 * In this demo, we are using {@link Environment#DIRECTORY_DOCUMENTS},
 * but can be replaced by any constant for directory available in Environment
 */
@TargetApi(Build.VERSION_CODES.N)
public class ScopedDirectoryAccess extends AppCompatActivity {

    private static final int REQ_SCOPE_ACCESS = 123;
    private static final String[] QUERY_PROJECTION_FIELDS = new String[]{
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_SIZE
    };
    private static final char NEW_LINE = '\n';
    private static final char SPACE_CHAR = ' ';
    private static final char[] TAB_SPACE = new char[]{SPACE_CHAR, SPACE_CHAR, SPACE_CHAR, SPACE_CHAR};
    private static final char COLUMN_VALUE_SEPARATOR = ':';
    private static final String SIZE_IN_BYTES = " Bytes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoped_access);
        //We need to perform following operations for getting a scope base access for any folder or file.
        //Step-1 Get access to storage manager
        StorageManager storageManager = getStorageService();
        if (storageManager == null) {
            return;
        }
        //Step-2 Get access to storage volume using storage manager
        StorageVolume storageVolume = getStorageVolume(storageManager);
        //Step-3 Create access intent for required scope for folder "Documents".
        //You may replace name of directory this with any other folder name like "Downloads","Music" etc
        //We can request permission for access to entire volume only when volume is a Primary Storage Volume.
        //For secondary storage volumes, we can request permission for specific directories only.
        Intent accessIntent = storageVolume.createAccessIntent(Environment.DIRECTORY_DOCUMENTS);
        if (accessIntent == null) {
            return;
        }
        //Step-4 Start activity for getting access
        startActivityForResult(accessIntent, REQ_SCOPE_ACCESS);
        //Step-5 Wait for user response. It would be either "Allow" or "Deny".
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isFinishing()) {
            return;
        }
        if (requestCode == REQ_SCOPE_ACCESS) {
            //if result code is Activity.RESULT_OK
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.getData() == null) {
                    return;
                }
                //Step-6(a) Persist the permissions so that granted permissions remain even after reboot.
                this.getContentResolver().takePersistableUriPermission(data.getData()
                        , Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //Step-7 Display the contents of folder if required.(Application and Requirement Specific)
                displayDirectoryDetails(data.getData());
                return;
            }
            //if result code is Activity.RESULT_CANCELED
            if (resultCode == Activity.RESULT_CANCELED) {
                //Step-6(b) Inform user for non-continuation of task since required permissions are denied.
                //This is application requirement specific.
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayDirectoryDetails(@NonNull Uri directoryUri) {
        String dirDetails =
                getDirectoryDescription(directoryUri)
                        + getDirectoryContentDetails(directoryUri);
        showDetailsOnView(dirDetails);
    }

    private void showDetailsOnView(@NonNull String dirDetails) {
        ((TextView) findViewById(R.id.directory_contents)).setText(dirDetails);
    }

    @NonNull
    private String getDirectoryContentDetails(@NonNull Uri directoryUri) {
        ContentResolver contentResolver = this.getContentResolver();
        final String treeDocumentId = DocumentsContract.getTreeDocumentId(directoryUri);
        //Initialize directory content uri
        //We use Tree to build document so that we do not have to ask for permissions again for sub-dir access.
        Uri docChildrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(directoryUri, treeDocumentId);
        //query from content resolver to get directory children info
        try (Cursor cursor
                     = contentResolver.query(docChildrenUri, QUERY_PROJECTION_FIELDS, null, null, null)) {
            //check if cursor is not null
            if (cursor != null) {
                StringBuilder stringBuilder = new StringBuilder(30 * cursor.getCount());
                while (cursor.moveToNext()) {
                    //adds name, mime type and size for directory content
                    addDataForDirectoryContent(cursor, stringBuilder);
                }
                return stringBuilder.toString();
            }
        }
        return "";
    }

    private void addDataForDirectoryContent(@NonNull Cursor cursor, @NonNull StringBuilder stringBuilder) {
        stringBuilder
                .append(NEW_LINE)
                .append(TAB_SPACE)
                .append(getString(cursor, DocumentsContract.Document.COLUMN_DISPLAY_NAME))
                .append(COLUMN_VALUE_SEPARATOR)
                .append(getString(cursor, DocumentsContract.Document.COLUMN_MIME_TYPE))
                .append(COLUMN_VALUE_SEPARATOR)
                .append(getString(cursor, DocumentsContract.Document.COLUMN_SIZE))
                .append(SIZE_IN_BYTES);
    }

    private String getString(@NonNull Cursor cursor, @NonNull String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    private String getDirectoryDescription(@NonNull Uri directoryUri) {
        ContentResolver contentResolver = this.getContentResolver();
        final String treeDocumentId = DocumentsContract.getTreeDocumentId(directoryUri);
        //Initialize directory uri
        //We use Tree to build document so that we do not have to ask for permissions again for sub-dir access.
        Uri docUri = DocumentsContract.buildDocumentUriUsingTree(directoryUri, treeDocumentId);
        //query from content resolver to get directory info
        try (Cursor cursor
                     = contentResolver.query(docUri, QUERY_PROJECTION_FIELDS, null, null, null)) {
            //check if cursor has some contents
            if (cursor != null && cursor.moveToLast()) {
                //return a formatted string containing directory specific data
                return getDirectoryDescription(cursor);
            }
        }
        return "";
    }

    private String getDirectoryDescription(@NonNull Cursor cursor) {
        return String.format("%s%c %s%s"
                , getString(cursor, DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                , COLUMN_VALUE_SEPARATOR
                , getString(cursor, DocumentsContract.Document.COLUMN_SIZE)
                , SIZE_IN_BYTES);
    }

    /**
     * we can use any available storage volume.
     * storage volumes are of two type:
     * <ul><li>Primary Storage Volume</li><li>Non-Primary(Secondary) Storage Volume</li></ul>
     * In this demo we are using primary storage volume,
     * but we can also access list of available storage volumes, and then we can use any one we want
     * <p>
     * Method to get available storage volumes:   storageManager.getStorageVolumes()
     *
     * @param storageManager storage manager to use for get storage volume.
     * @return storage volume
     */
    @NonNull
    private StorageVolume getStorageVolume(@NonNull StorageManager storageManager) {
        return storageManager.getPrimaryStorageVolume();
    }

    @Nullable
    private StorageManager getStorageService() {
        return (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
    }
}
