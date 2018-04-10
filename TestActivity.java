package www.seekerslab.com.seekeschat;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gjiazhe.panoramaimageview.GyroscopeObserver;
import com.gjiazhe.panoramaimageview.PanoramaImageView;
import com.jawnnypoo.physicslayout.PhysicsFrameLayout;
import com.jawnnypoo.physicslayout.PhysicsLinearLayout;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

public class TestActivity extends AppCompatActivity {

    PanoramaImageView imageView;
    GyroscopeObserver gyroscopeObserver;

    PhysicsFrameLayout physicsLayout;
    LinearLayout testLayout;

    SwitchCompat physicsSwitch;
    View impulseButton;
    View addViewButton;

    SwitchCompat flingSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testLayout = findViewById(R.id.test_layout);

        physicsLayout = findViewById(R.id.physics_layout);

        BrokenView brokenView = BrokenView.add2Window(this);
        BrokenTouchListener listener = new BrokenTouchListener.Builder(brokenView).build();

        testLayout.setOnTouchListener(listener);

        physicsSwitch = findViewById(R.id.physics_switch);

        flingSwitch = findViewById(R.id.fling_switch);

        impulseButton = findViewById(R.id.impulse_button);


        physicsSwitch.setChecked(physicsLayout.getPhysics().isPhysicsEnabled());
        physicsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    physicsLayout.getPhysics().enablePhysics();
                } else {
                    physicsLayout.getPhysics().disablePhysics();
                    for (int i=0; i<physicsLayout.getChildCount(); i++) {
                        physicsLayout.getChildAt(i)
                                .animate().translationY(0).translationX(0).rotation(0);
                    }
                }
            }
        });
        flingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    physicsLayout.getPhysics().enableFling();
                } else {
                    physicsLayout.getPhysics().disableFling();
                }
            }
        });

        impulseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                physicsLayout.getPhysics().giveRandomImpulse();
            }
        });


       /* gyroscopeObserver = new GyroscopeObserver();

        gyroscopeObserver.setMaxRotateRadian(Math.PI/9);

        imageView = findViewById(R.id.testactivity_imageview);
        imageView.setGyroscopeObserver(gyroscopeObserver);

        imageView.setImageURI((Uri)getIntent().getExtras().get("imageuri"));
        imageView.setEnableScrollbar(false);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
       /* gyroscopeObserver.register(this);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        /*gyroscopeObserver.unregister();*/
    }
}
