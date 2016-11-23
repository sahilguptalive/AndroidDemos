package sahilguptalive.com.androiddemos.directshare;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.support.annotation.RequiresApi;
import sahilguptalive.com.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Direct Share makes share intuitive and quick.
 * It helps in selecting a target for a specific target for an activity, instead of just selecting activity.
 * We can take an example of simple chat application. Now, when we wish to share some image.
 * Below Marshmallow API, user had to select our activity which is responsible for sharing images.
 * After that, user would select one or multiple contacts with which user wants to share image.
 * This involves two step for user:
 * <ol>
 * <li>Select activity(& application) to share an image.</li>
 * <li>Select contact(s) to share image with.</li>
 * </ol>
 * <p>
 * Now, on & after Marshmallow(API 23), user can be shown some frequently contacted contacts, and
 * user can share image with just one tap.
 * User is still shown the conventional activities which were previously shown in bottom portion of share dialog.
 * But, now there is a top portion of share dialog which shows the targets for enabled activities.
 * This helps in quick & accurate performance of action.
 * <p>
 * We can list down the steps to do so:
 * <ol>
 * <li>Create an activity which would be responsible for receiving intent from user and perform action on them.</li>
 * <li>Declare above activity in AndroidManifest file with correct intent filter(action, category, data)</li>
 * <li>Create a service which extends {@link ChooserTargetService}</li>
 * <li>Declare above service in Android Manifest.  </li>
 * <li>Add permission in service tag: <strong>android.permission.BIND_CHOOSER_TARGET_SERVICE</strong></li>
 * <li>Add intent filter in service to add action as: <strong>android.service.chooser.ChooserTargetService</strong></li>
 * <li>Add metadata in above created activity to include name as: <strong>android.service.chooser.chooser_target_service</strong></li>
 * <li>Add value for this metadata name to include path of above created service.</li>
 * <li>Create instance(s) of ChooserTarget for the above activity in
 * override method {@link ChooserTargetService#onGetChooserTargets(ComponentName, IntentFilter)} of above created service.</li>
 * <li>Each chooser taget includes some information about the target like:
 * <ul>
 * <li>Label of target</li>
 * <li>Icon of target</li>
 * <li>Score of target(0.0F - 1.0F)</li>
 * <li>component name which would open when this target is selected</li>
 * <li>Bundle which would be merged with original intent bundle as extra(may contain info regarding selected target)</li>
 * </ul>
 * </li>
 * <li>Put created chooser target(s) if any inside a list and return them.</li>
 * </ol>
 * For above steps you refer to {@link StringManipulationActivity} as activity which would handle actual intent and targets.
 * And {@link DemoChooserTargetService} as service which would return the chooser targets for intent and component name.
 * <p>
 * <strong>Additional Info:</strong>
 * <ul>
 * <li>Chooser Target Service would be invoked for each activity which declares it as a service in meta data.</li>
 * <li>We need to check target activity name before create chooser targets. So, that we don't create incorrect targets.</li>
 * <li>We need to check matched filter so that we handle only those intents which we can handle for particular component name.</li>
 * </ul>
 * <ul>
 * </ul>
 * <p>
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class DemoChooserTargetService extends ChooserTargetService {
    /**
     * @param targetActivityName name of activity for which service wants to query
     * @param matchedFilter      intent for which service wants to query
     * @return chooser targets for target activity name and matched filter if any, else empty list
     */
    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName targetActivityName, IntentFilter matchedFilter) {
        final ComponentName stringManipulation = new ComponentName(this, StringManipulationActivity.class);
        final List<ChooserTarget> chooserTargets = new ArrayList<>();
        if (targetActivityName.equals(stringManipulation)
                && matchedFilter.hasDataType("text/plain")
                && matchedFilter.hasAction(Intent.ACTION_SEND)) {
            chooserTargets.add(new ChooserTarget(getString(R.string.action_to_uppercase)
                    , getIcon(android.R.drawable.ic_media_play), 1.0F, stringManipulation
                    , getActionBundleForStringManipulation(StringManipulationActivity.ACTION_UPPERCASE)));
            chooserTargets.add(new ChooserTarget(getString(R.string.action_get_word_count)
                    , getIcon(android.R.drawable.ic_media_pause), .3f, stringManipulation
                    , getActionBundleForStringManipulation(StringManipulationActivity.ACTION_WORD_COUNT)));
            chooserTargets.add(new ChooserTarget(getString(R.string.action_char_count)
                    , getIcon(android.R.drawable.ic_media_next), .6f, stringManipulation
                    , getActionBundleForStringManipulation(StringManipulationActivity.ACTION_CHARACTER_COUNT)));
            chooserTargets.add(new ChooserTarget(getString(R.string.action_remove_extra_space)
                    , getIcon(android.R.drawable.ic_media_previous), .1f, stringManipulation
                    , getActionBundleForStringManipulation(StringManipulationActivity.ACTION_REMOVE_EXTRA_SPACES)));
        }
        return chooserTargets;
    }

    /**
     * @return the icon from using drawable resource id
     */
    private Icon getIcon(int drawableResId) {
        return Icon.createWithResource(this, drawableResId);
    }

    /**
     * @return the bundle containing the action.
     */
    private Bundle getActionBundleForStringManipulation(String action) {
        return StringManipulationActivity.getActionBundle(action);
    }
}
