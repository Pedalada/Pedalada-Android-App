package pedalada.pedalapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;

/** Uses Android recommended HttpURLConnection */
public class Server_PostJSON extends AsyncTask<JSONObject, Void, String> {

    private static String requestBinURL = "http://requestb.in/1n8qs1c1";
    private static URL url;
    private static HttpURLConnection connection;

    public static void setUp() { // just sets the URL url
        try {
            url = new URL(requestBinURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(JSONObject... jason) {

        if (jason.length == 0) {
            Log.d("NOOB", "THERE IS NO JSON");
            return null;
        }

        JSONObject json = jason[0];


        try {

            // set and connect
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000 /* milliseconds */);
            connection.connect();

            // for the JSON
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();


            // for the string version
            // OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            // writer.write(json);
            // writer.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("writenn", connection.getResponseMessage());
            } else {
                Log.d("Fodeu", connection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Yo mama so fat she could send swell our way";
    }
}