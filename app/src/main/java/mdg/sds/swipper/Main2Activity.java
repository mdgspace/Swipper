package mdg.sds.swipper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.library.MainActivity;

public class Main2Activity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        set(this);
    }
}
