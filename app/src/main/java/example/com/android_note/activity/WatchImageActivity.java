package example.com.android_note.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import example.com.android_note.R;

/**
 * This class is used to watch image of defined note (this image is got from EditorActivity)
 */
public class WatchImageActivity extends AppCompatActivity
{

    /**
     * Creates Activity.
     *
     * @param savedInstanceState
     *                          object of class Bundle. Is saved instance state of activity.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_image);

        ImageView ivImage=findViewById(R.id.image);
        String imPath=getIntent().getExtras().getString("path");
        ivImage.setImageDrawable(Drawable.createFromPath(imPath));
    }
}
