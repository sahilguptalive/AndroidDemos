package sahilguptalive.com.androiddemos.android_26.picture_in_picture_mode

import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_video_list.*
import sahilguptalive.com.androiddemos.R



class VideoListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        video_list_recycler_view.layoutManager = (LinearLayoutManager(this))
        video_list_recycler_view.adapter = getVideoUrls()?.let { VideoListAdapter(it) }
    }

    private fun getVideoUrls(): List<String>? {
        val listOfData = ArrayList<String>()
        val audioCursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)
        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    val audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                    listOfData.add(audioCursor.getString(audioIndex))
                } while (audioCursor.moveToNext())
            }
        }
        audioCursor.close()
        return listOfData
    }
}
