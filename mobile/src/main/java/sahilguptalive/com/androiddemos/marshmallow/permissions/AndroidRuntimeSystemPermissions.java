package sahilguptalive.com.androiddemos.marshmallow.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import sahilguptalive.com.androiddemos.R;
import sahilguptalive.com.androiddemos.contacts.ContactsFragment;

/**
 * This activity would demonstrate the new marshmallow run time permissions feature.
 * Implementing new APIs have been explained in a step-by-step nature.
 */
public class AndroidRuntimeSystemPermissions extends AppCompatActivity {

    private static final int REQ_GRANT_CONTACT_PERMISSION = 123;
    private static final int REQ_UPDATE_CONTACT_PERMISSION = 124;
    private static final String[] CONTACT_PERMISSIONS_ARRAY
            = new String[]{Manifest.permission.READ_CONTACTS
            , Manifest.permission.WRITE_CONTACTS};
    private View mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_permissions);
        mRootLayout = findViewById(R.id.activity_runtime_permissions_root_layout);
    }

    public void onClickAccessContact(View view) {
        //Step-1 check if SDK is below marshmallow, if yes show contacts directly. Else move to Step-2
        //This step can be skipped, since we are using app compat activity, which is backward compatible.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            showContacts();
            return;
        }
        //Step-2 check if required permissions have been granted. If yes show contacts directly. Else move to Step-3
        if (isPermissionGranted(Manifest.permission.READ_CONTACTS)
                && isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            showContacts();
            return;
        }
        //Step-3 Check if we need to display rationale behind requesting this permission.
        //If yes, show some relevant text to show rationale behind this permission.
        //Else move to Step-6
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)) {
            showRationaleForContactPermission();
            return;
        }

        //Step-6 Request for contacts permission.
        requestForContactRelatedPermission(REQ_GRANT_CONTACT_PERMISSION);
    }

    private void requestForContactRelatedPermission(int requestCode) {
        ActivityCompat.requestPermissions(this
                , CONTACT_PERMISSIONS_ARRAY
                , requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_GRANT_CONTACT_PERMISSION) {
            //Step-7 We need to check if we have been granted the requested permissions
            //Since grant results and permissions array can be empty if interaction with user is interrupted
            //Therefore, We have to regard this as a failure to have requested permissions.
            if (grantResults.length != 2 || permissions.length != 2) {
                //no permission have been granted. This is a failure.
                //If this requested permission is very critical for app,
                //then app may request for granting permissions again from this point of code.
                informOfContactPermissionGrantFailure();
                return;
            }
            //If granted results have any one of the element as PackageManager.PERMISSION_DENIED
            //,then it is also a failure to have requested permissions.
            if ((grantResults[0] == PackageManager.PERMISSION_DENIED
                    || grantResults[1] == PackageManager.PERMISSION_DENIED)) {
                //any one or all of requested permission have not been granted. This is a failure.
                //If this requested permission is very critical for app,
                //then app may request for granting permissions again from this point of code.
                informOfContactPermissionGrantFailure();
                return;
            }
            //Step-8 Since all checks have been made we have been granted the requested permissions.
            //Therefore we can show contacts
            informOfContactPermissionGrantSuccess();
            showContacts();
        }
    }

    /**
     * This implementation is application requirements specific. Some app may opt to skip this part
     */
    private void informOfContactPermissionGrantFailure() {
        Snackbar.make(mRootLayout
                , R.string.failure_of_contact_permission
                , Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * This implementation is application requirements specific. Some app may opt to skip this part
     */
    private void informOfContactPermissionGrantSuccess() {
        Snackbar.make(mRootLayout
                , R.string.success_of_contact_permission
                , Snackbar.LENGTH_LONG)
                .show();
    }

    /**
     * Displa
     */
    private void showRationaleForContactPermission() {
        //Step-4 show snack-bar with string and action
        Snackbar.make(mRootLayout
                , R.string.rationale_for_contact_permission
                , Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, new ContactRationaleActionClickListener())
                .show();
    }

    private boolean isPermissionGranted(String readContacts) {
        return ActivityCompat.checkSelfPermission(this, readContacts) == PackageManager.PERMISSION_GRANTED;
    }

    private void showContacts() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.runtime_permissions_frame_layout, new ContactsFragment())
                .commit();
    }

    public void onClickUpdateContactPermission(View view) {
        //Check if permission have been granted or not.
        //If yes revoke them else show failure message
        if (isPermissionGranted(Manifest.permission.READ_CONTACTS)
                && isPermissionGranted(Manifest.permission.WRITE_CONTACTS)) {
            requestForContactRelatedPermission(REQ_UPDATE_CONTACT_PERMISSION);
        } else {
            Snackbar.make(mRootLayout
                    , R.string.contact_permission_update_failed
                    , Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private class ContactRationaleActionClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Step-5 When user accepts the rationale, we can again request for permissions.
            requestForContactRelatedPermission(REQ_GRANT_CONTACT_PERMISSION);
        }
    }
}
