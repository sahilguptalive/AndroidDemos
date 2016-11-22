package sahilguptalive.com.androiddemos.deeplinking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import sahilguptalive.com.androiddemos.R;

import java.util.Objects;

/**
 * Deep Linking is an excellent feature which allows us to enhance both web and native-app experience,
 * which makes both web and mobile platform work in a coupled manner. Deep linking allows us to view
 * the application page when navigated to some recognized and predefined web-page while browsing.
 * <p>
 * Following steps need to be followed to add deep-linking in any application:
 * <ol>
 * <li>Identify which web-page need to be linked with which android activity.
 * Lets say url of web-page is "http://www.androiddemo.com/productinfo".
 * And activity is "{@link ProductInfo}"</li>
 * <li>Create intent filter inside intended activity - {@link ProductInfo}</li>
 * <li>Add action as {@link Intent#ACTION_VIEW}</li>
 * <li>Add category as {@link Intent#CATEGORY_BROWSABLE}</li>
 * <li>Add category as {@link Intent#CATEGORY_DEFAULT}</li>
 * <li>Add data element</li>
 * <ol><li>Add schema as "http"</li>
 * <li>Add host as "www.androiddemo.com"</li>
 * <li>Add prefix as "/productinfo"</li>
 * </ol>
 * </ol>
 * <p>
 * On and After Marshmallow(API 23), App linking was introduced which enhanced the experience of user.
 * In app linking, we can enable auto verify for deep linking.
 * Following are the steps for app linking:
 * <ol>
 * <li>Add <strong>android:autoVerify="true"</strong> in intent filter tag for any one of the intent filters.</li>
 * <li>Generate sha-256 of application signing keystore</li>
 * <li>Generate a <strong>Digital Asset Links </strong> statement file which would have app package name and keystore sha-256 signature.</li>
 * <li>Store the generated statement file at specified location of host which you want to auto-verify</li>
 * </ol>
 * <p>
 * Lets take an example, where
 * <ul>
 * <li>schema = "http"</li>
 * <li>host = "www.androiddemo.com"</li>
 * <li>prefix = "/productInfo"</li>
 * </ul>
 * Then, rename(if required) to generated statement file to <strong>assetlinks.json</strong>.
 * And store this  file at "https://www.android.demo/productinfo/.wellknown/"
 * so, final location of <strong>Digital Asset Links </strong> statement file would be at
 * <strong>https://www.android.demo/productinfo/.wellknown/assetlinks.json</strong>.
 * <p>
 * We need to above specified steps for all intent filters which we need to be auto-verified by android.
 * <p>
 * To test Product Info activity you can use this command:
 * <p>
 * <strong>adb shell am start -W -a android.intent.action.VIEW -d "https://www.androiddemo.com/productinfo?id=123456" sahilguptalive.com.androiddemos</strong>
 * </p>
 *
 * @see <ul>
 * <li><a href="https://developer.android.com/training/app-links/index.html">App Link Ref</a></li>
 * <li><a href="https://developer.android.com/training/app-indexing/deep-linking.html">Deep Linking</a></li>
 * <li><a href="https://developers.google.com/digital-asset-links/v1/statements">About Statement List</a></li>
 * <li><a href="https://developers.google.com/digital-asset-links/tools/generator">Statement List Generator</a></li>
 * <li><a href="https://developers.google.com/digital-asset-links/v1/create-statement">Creating Statements</a></li>
 * </ul>
 **/
public class ProductInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        TextView textview = (TextView) findViewById(R.id.product_info_received_pid);
        String receivedProductId = extractProductId();
        textview.setText(receivedProductId == null ? "No product id received" : receivedProductId);
    }

    /**
     * @return extracted product id from intent uri if uri has expected format, else returns null.
     */
    private String extractProductId() {
        //We can extract the data inside intent using method {@link Intent#getData()}, which returns a URI.
        //We can apply queries on this URI, to get value for any expected query parameter.
        final boolean isActionView = Objects.equals(getIntent().getAction(), Intent.ACTION_VIEW);
        final Uri data = getIntent().getData();
        if (isActionView && data != null) {
            return data.getQueryParameter("id");
        }
        return null;
    }
}
