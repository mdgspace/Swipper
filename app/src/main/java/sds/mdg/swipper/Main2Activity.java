package sds.mdg.swipper;

import android.net.Uri;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.swipper.library.Swipper;

public class Main2Activity extends Swipper {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        videoView = (VideoView) findViewById(R.id.videoView);
        Uri video1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(video1);
        videoView.start();
        Brightness(Orientation.CIRCULAR);
        Volume(Orientation.VERTICAL);
        Seek(Orientation.HORIZONTAL, videoView);
        set(this);
    }
}
