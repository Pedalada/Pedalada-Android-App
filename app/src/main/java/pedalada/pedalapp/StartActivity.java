package pedalada.pedalapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    static public String userID;

    private Button startButton;
    private TextView loginInfo;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.startscreen);

        callbackManager = CallbackManager.Factory.create();

        // UI
        loginButton = (LoginButton) findViewById(R.id.login_button);
        startButton = (Button) findViewById(R.id.startButton);
        loginInfo = (TextView) findViewById(R.id.loginInfo);

        // handle facebook
        setFacebookLogin(loginButton, callbackManager);
    }

    // credit: http://code.tutsplus.com/tutorials/quick-tip-add-facebook-login-to-your-android-app--cms-23837
    private void setFacebookLogin(LoginButton lb, CallbackManager cm) {
        lb.setReadPermissions(Arrays.asList("user_birthday"));
        lb.registerCallback(cm, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                userID = loginResult.getAccessToken().getUserId();
                loginInfo.setText("Bem vindo!\n" + "User ID: " + userID);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login cancelado.", Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login falhou.", Toast.LENGTH_SHORT);
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startTrip(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);

        Context context = getApplicationContext();
        CharSequence text = "A começar gravação e a 'aquecer' o gps\nBoa pedalada!";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
