package sahilguptalive.com.androiddemos.dream;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.service.dreams.DreamService;
import android.support.annotation.Nullable;
import android.widget.TextView;
import sahilguptalive.com.androiddemos.R;

public class RotateDreamService extends DreamService {

    @Nullable
    private ObjectAnimator mTextAnimator;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setInteractive(false);
        setFullscreen(true);
        setScreenBright(false);
        setContentView(R.layout.dream_service_layout);
        TextView textViewToAnimate = (TextView) findViewById(R.id.dream_service_layout_textview);
        //initialize text animator
        initializeObjectAnimator(textViewToAnimate);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        if (mTextAnimator.isPaused()) {
            mTextAnimator.resume();
        } else {
            mTextAnimator.start();
        }
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
        if (mTextAnimator != null) {
            mTextAnimator.pause();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTextAnimator != null) {
            mTextAnimator.cancel();
            mTextAnimator = null;
        }
    }

    private void initializeObjectAnimator(TextView textViewToAnimate) {
        mTextAnimator = ObjectAnimator.ofFloat(textViewToAnimate, "rotation", 0.0F, 360.0F);
        mTextAnimator.setDuration(1000);
        mTextAnimator.setRepeatMode(ValueAnimator.RESTART);
        mTextAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }
}
