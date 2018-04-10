package www.seekerslab.com.seekeschat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ImageView profile;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String seekersChatBackground = mFirebaseRemoteConfig.getString("SeekersChat_background");

        getWindow().setStatusBarColor(Color.parseColor(seekersChatBackground));

        profile = findViewById(R.id.signupactivity_imageview_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,  PICK_FROM_ALBUM);
            }
        });

        email = findViewById(R.id.signupactivity_edittext_email);
        name = findViewById(R.id.signupactivity_edittext_name);
        password = findViewById(R.id.signupactivity_edittext_password);
        signup = findViewById(R.id.signupactivity_button_signup);

        signup.setBackgroundColor(Color.parseColor(seekersChatBackground));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setEnabled(false);
                if(email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null || imageUri == null){
                    signup.setEnabled(true);
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String  uid = task.getResult().getUser().getUid();
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name.getText().toString()).build();

                        task.getResult().getUser().updateProfile(userProfileChangeRequest);

                        FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                String imageUrl = task.getResult().getDownloadUrl().toString();

                                User user = new User();
                                user.userName = name.getText().toString();
                                user.profileImageUrl = imageUrl;
                                user.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        signup.setEnabled(true);
                                        SignupActivity.this.finish();
                                    }
                                });


                            }
                        });

                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            Glide.with(this).load(data.getData()).centerCrop().into(profile);
            imageUri = data.getData();
        }
    }
}
