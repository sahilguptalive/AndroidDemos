package sahilguptalive.com.androiddemos.android_26.picture_in_picture_mode

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.provider.MediaStore.Video.Thumbnails.MINI_KIND
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created on 26-04-2018.
 */
class VideoListAdapter(val data: List<String>) : RecyclerView.Adapter<VideoListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoListItemViewHolder {
        return VideoListItemViewHolder(createView(parent, viewType))
    }

    private fun createView(parent: ViewGroup?, viewType: Int): ImageView {
        return ImageView(parent!!.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VideoListItemViewHolder?, position: Int) {
        holder!!
                .listItemView.setImageBitmap(getImageUri(data[position]))
        holder!!.itemView.setOnClickListener {
            VideoPlayerActivity. }
    }

    private fun getImageUri(filePath: String): Bitmap? {
        MediaStore.Video.Media.MINI_THUMB_MAGIC
        MediaMetadataRetriever().frameAtTime()
        return ThumbnailUtils.createVideoThumbnail(filePath, MINI_KIND)
    }
}