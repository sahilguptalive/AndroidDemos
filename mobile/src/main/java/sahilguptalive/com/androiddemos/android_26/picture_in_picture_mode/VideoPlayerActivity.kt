package sahilguptalive.com.androiddemos.android_26.picture_in_picture_mode

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import sahilguptalive.com.androiddemos.R

/**
 * Created on 26-04-2018.
 */
class VideoPlayerActivity : AppCompatActivity() {

    static
    {
        public inline fun createIntent() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_player_activity)
    }
}