package org.aboutcanada.com.Ui;


import android.app.Activity;
import android.os.Bundle;
import org.aboutcanada.com.Fragment.MainFragment;
import org.aboutcanada.com.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initiating the Fragment

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame, new MainFragment(), "main_fragment_tag").addToBackStack(null).commit();
        } else {
            MainFragment main_fragment = (MainFragment) getFragmentManager().findFragmentByTag("main_fragment_tag");
        }
    }
}
