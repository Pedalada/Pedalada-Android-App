package pedalada.pedalapp;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Server_Ekat_ProcessBitmapTask extends AsyncTask<Bitmap, Void, String> {
    private final String LOG_TAG = Server_Ekat_ProcessBitmapTask.class.getSimpleName();
    private String upLoadServerUri = null;
    private HttpURLConnection urlConnection = null;
    private String boundary = "*****";
    private String crlf = "\r\n";
    private String twoHyphens = "--";
    private String attachmentName = null;
    private String attachmentFileName = null;

//    TextView uiUpdate = (TextView) findViewById(R.id.serverResponseTextView);
//    private ProgressDialog Dialog = new ProgressDialog(CameraActivity.this);

    private static final int HTTP_STATUS_OKAY = 200;
    private static final int HTTP_STATUS_BAD_REQUEST = 400;
    //SuLX: 192.168.1.246:5000
    //Olivais: 192.168.1.81:5000
    //Rokas:192.168.56.1
    // Tempelhof airport : 172.21.56.187
    private static final String SERVER_URL = "";

    @Override
    protected void onPreExecute() {
        // NOTE: You can call UI Element here.
        //UI Element
//        Dialog.setMessage("Processing image..");
//        Dialog.show();
    }

    @Override
    protected void onPostExecute(String string) {
//        Dialog.hide();
//        Dialog.dismiss();
//        if (response != null){
//            // display the response from the server
//            uiUpdate.setText(response);
//        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response.toString();
    }

    @Override
    protected String doInBackground(Bitmap... params) {

        if (params.length == 0) {
            return null;
        }

        // return the amount of added sugar
        String jsonSugar = "empty";

        // process image; convert to a BASE64 String
        Bitmap bitmap = params[0];
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();

        // ba1 contains the String of the image to be sent
        String ba1 = Base64.encodeToString(ba,Base64.NO_WRAP);

        // Base64.DEFAULT seems to insert a new line; replaced with NO_WRAP
        Log.v(LOG_TAG, "Image String: " + ba1);

        // send image string to server
        try {

            JSONArray response = new JSONArray();

            // IPv4 of Ekaterina's computer
            URL url = new URL(SERVER_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
            urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

            // DO NOT USE setChunkedStreamingMode: interrupts stream of POST
            //urlConnection.setChunkedStreamingMode(ba1.length());
            urlConnection.connect();

            // start content wrapper
            DataOutputStream request = new DataOutputStream(urlConnection.getOutputStream());
            request.writeBytes("Content-Type: multipart/form-data;boundary=" + this.boundary);
            request.writeBytes("Content-Disposition: form-data;" + this.crlf);
            request.writeBytes(this.crlf);

            //attach image string to request
            request.writeBytes(ba1);

            //end wrapper
            request.writeBytes(this.crlf);
            request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);

            //close output stream
            request.flush();
            request.close();

            // receive response from web service
            int responseCode = urlConnection.getResponseCode();
            Log.v(LOG_TAG, "Received response code: " + responseCode);
            if(responseCode == HTTP_STATUS_OKAY){
                InputStream is = urlConnection.getInputStream();
                String contentAsString = readStream(is);
                JSONObject jsonResponse = new JSONObject(contentAsString);
                jsonSugar = jsonResponse.getString("sugar");
                Log.v(LOG_TAG, "Received data: " + contentAsString + ", " + jsonSugar);
            } else {
                if (responseCode == HTTP_STATUS_BAD_REQUEST) {
                    Log.v(LOG_TAG, "Received POST 400: bad request.");
                }
            }

            urlConnection.disconnect();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in http connection " + e.toString());
        }

        return jsonSugar;
    }
}
