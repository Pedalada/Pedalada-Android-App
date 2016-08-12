package pedalada.pedalapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class Server_MyIonConnection {

    // server stuff
    private static String flaskIP = "http://192.168.123.0/";
    // 127.0.0.1

    public Server_MyIonConnection() {}

    public void post(Context context, JsonObject jsonObject) {
        Ion.with(context) // returns the context the view is currently running in
                .load(flaskIP)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e != null) {
                            Log.d("EXCEPTION", e.getMessage());
                        } else if (result != null) {
                            Log.d("SUCCESS", result.toString());
                        }
                    }
                });
    }


//    public JsonObject getJson(Context context) {
//        Ion.with(context)
//                .load(flaskIP)
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        // do stuff with the result or error
//                    }
//                });
//    }


}
