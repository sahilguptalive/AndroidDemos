package sahilguptalive.com.androiddemos.flipped;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import sahilguptalive.com.androiddemos.R;

public class FlippedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flipped_view_activity);
        View viewToAnimate = findViewById(R.id.flipped_view_animation);
        Animation flipAnimation = AnimationUtils.loadAnimation(this, R.anim.view_flip_animation);
        viewToAnimate.startAnimation(flipAnimation);
    }
}
