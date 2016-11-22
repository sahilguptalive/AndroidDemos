package sahilguptalive.com.androiddemos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import sahilguptalive.com.androiddemos.accessibility.AccessibleActivity;
import sahilguptalive.com.androiddemos.marshmallow.permissions.AndroidRuntimeSystemPermissions;
import sahilguptalive.com.androiddemos.nougat.scopped_acess.ScopedDirectoryAccess;

public class HomeActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    @NonNull
    public native String stringFromJNI();

    public void onClickScopedAccess(View view) {
        startActivity(ScopedDirectoryAccess.class);
    }

    public void onClickRuntimePermissions(View view) {
        startActivity(AndroidRuntimeSystemPermissions.class);
    }

    public void onClickAccessibility(View view) {
        startActivity(AccessibleActivity.class);
    }

    private void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
