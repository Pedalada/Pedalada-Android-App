package pedalada.pedalapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class Server_MyIonConnection {

    private String url = "";

    public Server_MyIonConnection() {}

    public void post(Context context, JsonObject jsonObject) {
        Ion.with(context) // returns the context the view is currently running in
                .load(url)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        while (result == null) {
                            if (result != null) {
                                Log.d("RESULT", result.toString());}
                            if (e != null) {Log.d("EXCEPTION", e.getMessage());}
                        }

                    }
                });
    }

    public JsonObject read() {
        return new JsonObject();
    }


}
