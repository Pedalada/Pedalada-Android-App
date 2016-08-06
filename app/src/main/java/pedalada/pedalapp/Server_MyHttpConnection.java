package pedalada.pedalapp;


import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/** Uses Android recommended HttpURLConnection */
public class Server_MyHttpConnection {

    // TODO figure out the god-damned url...
    private URL url;

    public void post(JSONObject obj) {

        byte[] bits = obj.toString().getBytes();

        HttpURLConnection urlConnection;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(bits);

            // re-consult the guide...

        } catch (IOException e) {
            Log.d("IOEXCEPTION", e.getMessage());
        }

    }




}
