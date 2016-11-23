package sahilguptalive.com.androiddemos.directshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import sahilguptalive.com.androiddemos.R;

public class StringManipulationActivity extends AppCompatActivity {


    public static final String ACTION_UPPERCASE = "action_uppercase";
    public static final String ACTION_WORD_COUNT = "action_word_count";
    public static final String ACTION_CHARACTER_COUNT = "action_character_count";
    public static final String ACTION_REMOVE_EXTRA_SPACES = "action_remove_extra_spaces";
    public static final String ACTION_REVERSE = "action_reverse";
    private static final String KEY_ACTION = "key_action";
    private static final char SPACE = ' ';

    public static Bundle getActionBundle(String action) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ACTION, action);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_shared_text);
        final TextView origTextview = (TextView) findViewById(R.id.receive_shared_text_original_textview);
        final TextView reversedTextView = (TextView) findViewById(R.id.receive_shared_text_reversed_textview);
        final String textShared = extractSharedText();
        final String actionToPerform = extractStringManipulationAction() == null ? ACTION_REVERSE : extractStringManipulationAction();
        if (textShared != null) {
            origTextview.setText(String.format("\"%s\"", textShared));
            String manipulatedString = performAction(textShared, actionToPerform);
            reversedTextView.setText(String.format("%s: \"%s\"", getActionName(actionToPerform), manipulatedString));
        } else {
            origTextview.setText(R.string.no_data_to_display);
            reversedTextView.setText(R.string.no_data_to_display);
        }
    }

    private String performAction(String textShared, String actionToPerform) {
        switch (actionToPerform) {
            case ACTION_REVERSE:
            default:
                return reverse(textShared);
            case ACTION_CHARACTER_COUNT:
                return String.valueOf(textShared.length());
            case ACTION_REMOVE_EXTRA_SPACES:
                return removeExtraSpaces(textShared);
            case ACTION_UPPERCASE:
                return textShared.toUpperCase();
            case ACTION_WORD_COUNT:
                return String.valueOf(wordCount(textShared));
        }
    }

    private String removeExtraSpaces(String textShared) {
        char[] charArr = textShared.toCharArray();
        char[] charArrFinal = new char[charArr.length];
        char lastChar = '\n';
        int index = 0;
        for (char ch : charArr) {
            if (ch != SPACE || lastChar != SPACE) {
                charArrFinal[index++] = ch;
            }
            lastChar = ch;
        }
        boolean isSpaceInBeg = false;
        if (charArrFinal[0] == SPACE) {
            isSpaceInBeg = true;
        }
        boolean isSpaceInEnd = false;
        if (charArrFinal[index - 1] == SPACE) {
            isSpaceInEnd = true;
        }
        return String.valueOf(charArrFinal, isSpaceInBeg ? 1 : 0, index - (isSpaceInEnd ? 1 : 0) + (isSpaceInBeg ? 1 : 0));
    }

    private String getActionName(String actionToPerform) {
        switch (actionToPerform) {
            case ACTION_REVERSE:
            default:
                return getString(R.string.action_reverse);
            case ACTION_CHARACTER_COUNT:
                return getString(R.string.action_char_count);
            case ACTION_REMOVE_EXTRA_SPACES:
                return getString(R.string.action_remove_extra_space);
            case ACTION_UPPERCASE:
                return getString(R.string.action_to_uppercase);
            case ACTION_WORD_COUNT:
                return getString(R.string.action_get_word_count);
        }
    }

    private int wordCount(String textShared) {
        String stringWithSpacesRemoved = removeExtraSpaces(textShared);
        int wordCount = 0;
        for (int i = 0; i < stringWithSpacesRemoved.length(); i++) {
            if (stringWithSpacesRemoved.charAt(i) == SPACE) {
                wordCount++;
            }
        }
        return wordCount;
    }

    private String extractStringManipulationAction() {
        return getIntent().getExtras() != null ? getIntent().getExtras().getString(KEY_ACTION) : null;
    }

    private String reverse(String textShared) {
        char[] charArr = textShared.toCharArray();
        for (int i = 0, j = charArr.length - 1; i < j; i++, j--) {
            char temp = charArr[i];
            charArr[i] = charArr[j];
            charArr[j] = temp;
        }
        return String.valueOf(charArr);
    }

    private String extractSharedText() {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }
}
