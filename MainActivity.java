package www.seekerslab.com.seekeschat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framlayout, new PeopleFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.mainactivity_bottomnavigationview);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_people:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framlayout, new PeopleFragment()).commit();
                        return true;

                    case R.id.action_chat:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framlayout, new ChatFragment()).commit();
                        return true;
                    case R.id.action_account:
                        getFragmentManager().beginTransaction().replace(R.id.mainactivity_framlayout, new AccountFragment()).commit();
                        return true;
                }
                return false;
            }
        });
        passPushTokenToServer();

    }

    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();

        Map<String, Object> map = new HashMap<>();
        map.put("pushToken", token);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        EventBus.getInstance().post(ActivityResultEvent.create(requestCode, resultCode, data));
    }
}
