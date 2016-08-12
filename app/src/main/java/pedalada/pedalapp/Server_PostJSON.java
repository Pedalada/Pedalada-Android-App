package pedalada.pedalapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/** Uses Android recommended HttpURLConnection */
public class Server_PostJSON extends AsyncTask<String, Void, String> {


    // browserflask <- "http://127.0.0.1/"
    // pyflask <- "http://10.0.2.2/"
    // IPv4 <- "http://192.168.123.0/"
    private static String IPv4 = "http://192.168.123.0/";
    private static URL url;
    private static HttpURLConnection connection;

    public static void setUp() { // just sets the URL url
        try {
            url = new URL(IPv4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(String... jasony) { // where strings == JSONObject.toString()

        if (jasony.length == 0) {
            Log.d("NOOB", "THERE IS NO JSON");
            return null;
        }

        String json = jasony[0];


        try {

            // set and connect
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000 /* milliseconds */);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json);
            writer.close();

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