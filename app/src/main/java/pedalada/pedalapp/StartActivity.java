package pedalada.pedalapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class StartActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

        setContentView(R.layout.startscreen);
        button = (Button) findViewById(R.id.startButton);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.startscreen, container, false);
//        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email");
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // set the other guy visible
//            }
//
//            @Override
//            public void onCancel() {
//                // do noth
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                // re-initialize activity
//            }
//        });
//
//
//
//    }

    public void startTrip(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);

        Context context = getApplicationContext();
        CharSequence text = "A começar gravação e a 'aquecer' o gps\nBoa pedalada!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // what happens when returning to this activity?

}
