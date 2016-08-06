package pedalada.pedalapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Utils extends AppCompatActivity {

    // http://stackoverflow.com/questions/30818538/converting-json-object-with-bitmaps
    static String getStringFromBitmap(Bitmap bitmapPicture) {
         // This function converts Bitmap picture to a string which can be JSONified.
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    static Bitmap getBitmapFromString(String jsonString) {
        // This function converts the String back to Bitmap
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    // bullshiiieeeettt
    // http://stackoverflow.com/questions/4966188/how-to-post-data-in-android-to-server-in-json-format
//    public static String putDataToServer(String url, JSONObject returnedJObject) throws Throwable {
//
//        HttpPost request = new HttpPost(url);
//        JSONStringer json = new JSONStringer();
//        StringBuilder sb=new StringBuilder();
//
//        if (returnedJObject!=null) {
//            Iterator<String> itKeys = returnedJObject.keys();
//            if(itKeys.hasNext())
//                json.object();
//            while (itKeys.hasNext()) {
//                String k=itKeys.next();
//                json.key(k).value(returnedJObject.get(k));
//                Log.e("keys "+k,"value "+returnedJObject.get(k).toString());
//            }
//        }
//        json.endObject();
//
//        StringEntity entity = new StringEntity(json.toString());
//        entity.setContentType("application/json;charset=UTF-8");
//        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
//        request.setHeader("Accept", "application/json");
//        request.setEntity(entity);
//
//        HttpResponse response =null;
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//
//        HttpConnectionParams.setSoTimeout(httpClient.getParams(), SyncStateContract.Constants.ANDROID_CONNECTION_TIMEOUT*1000);
//        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), SyncStateContract.Constants.ANDROID_CONNECTION_TIMEOUT*1000);
//        try{
//            response = httpClient.execute(request);
//        }
//        catch(SocketException se) {
//            Log.e("SocketException", se+"");
//            throw se;
//        }
//
//        InputStream in = response.getEntity().getContent();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//        String line = null;
//        while((line = reader.readLine()) != null){
//            sb.append(line);
//        }
//        return sb.toString();
//    }

    static String hhmmss(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

    }

    /** Obtains distance in meters between two coordinates.
     * Source: http://www.ridgesolutions.ie/index.php/2013/11/14/algorithm-to-calculate-speed-from-two-gps-latitude-and-longitude-points-and-time-difference/ */
    static double getDistance(double lat1, double lon1, double lat2, double lon2) {

        // Convert degrees to radians
        lat1 = lat1 * Math.PI / 180.0;
        lon1 = lon1 * Math.PI / 180.0;

        lat2 = lat2 * Math.PI / 180.0;
        lon2 = lon2 * Math.PI / 180.0;

        // radius of earth in metres
        double r = 6378100;

        // P
        double rho1 = r * Math.cos(lat1);
        double z1 = r * Math.sin(lat1);
        double x1 = rho1 * Math.cos(lon1);
        double y1 = rho1 * Math.sin(lon1);

        // Q
        double rho2 = r * Math.cos(lat2);
        double z2 = r * Math.sin(lat2);
        double x2 = rho2 * Math.cos(lon2);
        double y2 = rho2 * Math.sin(lon2);

        // Dot product
        double dot = (x1 * x2 + y1 * y2 + z1 * z2);
        double cos_theta = dot / (r * r);

        double theta = Math.acos(cos_theta);

        // Distance in Metres
        return r * theta;
    }
}
