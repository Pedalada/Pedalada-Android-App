package pedalada.pedalapp;
// https://developer.android.com/training/basics/data-storage/files.html

import android.content.Context;

import com.opencsv.CSVWriter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/** Helper class to handle data in internal memory of smartphone */
public final class LocalFiles {

    // http://stackoverflow.com/questions/27772011/how-to-export-data-to-csv-file-in-android

    // read into an ArrayList by: https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
    ArrayList<Double[]> readIntoArray(String filename, Context context) {

        File file = new File(context.getFilesDir(), filename);
        BufferedReader br = null;
        String line = "";
        String csvSeparator = ",";
        Double[] position = new Double[2];
        ArrayList<Double[]> result = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(file)); // fortunately takes a File object too :D
            while ((line = br.readLine()) != null) {
                // use the defined separator
                String[] ride = line.split(csvSeparator);
                position[0] = Double.parseDouble(ride[0]);
                position[1] = Double.parseDouble(ride[1]);
                result.add(position);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // internally
    void saveToCSV(Context context, String filename, String data) {
        CSVWriter csvw;
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteLastFile(Context context, String filename) {
        context.deleteFile(filename); // where filename should be sth like "Pedalada_10
    }

}
