package com.example.aaron.loginwithfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    private TextView userUID;
    private TextView userName;
    private TextView userEmail;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = ((TextView) findViewById(R.id.userName));
        userEmail = ((TextView) findViewById(R.id.userEmail));
        userUID = ((TextView) findViewById(R.id.userUID));
        image = ((ImageView) findViewById(R.id.imageView));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Uri imgUri = auth.getCurrentUser().getPhotoUrl();
            //Log.d("URI", imgUri.toString());
            if(imgUri != null) {
                try {
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri));
                    image.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                image.setImageURI(Uri.parse("android.resource://com.example.aaron.loginwithfirebase/drawable/com_facebook_profile_picture_blank_square"));
            }
            userName.setText(auth.getCurrentUser().getDisplayName());
            userEmail.setText(auth.getCurrentUser().getEmail());
            userUID.setText(auth.getCurrentUser().getUid());


        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(
                                    AuthUI.EMAIL_PROVIDER,
                                    AuthUI.GOOGLE_PROVIDER
                                    //AuthUI.FACEBOOK_PROVIDER
                            )
                            .build(),
                    RC_SIGN_IN);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, MainActivity.class));
                //Toast.makeText(this, "YES   :)",Toast.LENGTH_LONG).show();
                finish();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
                //Toast.makeText(this, "Not signed in",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void onLogout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

}
