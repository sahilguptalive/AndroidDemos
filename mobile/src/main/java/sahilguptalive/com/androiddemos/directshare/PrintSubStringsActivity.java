package sahilguptalive.com.androiddemos.directshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import sahilguptalive.com.androiddemos.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PrintSubStringsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_substrings);
        TextView textView = (TextView) findViewById(R.id.activity_print_substring_textview);
        String receivedString = extractStringShared();
        if (receivedString == null) {
            textView.setText(R.string.no_data_to_display);
            return;
        }
        String[] subStringArray = getAllSubStrings(receivedString);
        String subStringCompiled = appendStringArrayElements(subStringArray);
        textView.setText(subStringCompiled);
    }

    private String appendStringArrayElements(String[] subStringArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : subStringArray) {
            stringBuilder.append(str)
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private String[] getAllSubStrings(String receivedString) {

        ArrayList<String> subString = new ArrayList<>();
        for (int i = 0; i < receivedString.length() ; i++) {
            for (int j = i; j < receivedString.length(); j++) {
                subString.add(receivedString.substring(i, j + 1));
            }
        }
        Collections.sort(subString, new SubStringComparator());
        return subString.toArray(new String[subString.size()]);
    }

    private String extractStringShared() {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    class SubStringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }
}
