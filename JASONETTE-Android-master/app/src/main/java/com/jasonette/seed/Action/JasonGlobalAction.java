package com.jasonette.seed.Action;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.Launcher.Launcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;


public class JasonGlobalAction {



    public void reset(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) throws IOException {

        /********************

         The following resets a global variable named "db".
         When a variable is reset, the key itself gets destroyed, so when you check ('db' in $global), it will return false

         {
             "type": "$global.reset",
             "options": {
                 "items": ["db"]
             }
         }

         ********************/




        try {
            SharedPreferences pref = context.getSharedPreferences("global", 0);
            SharedPreferences.Editor editor = pref.edit();
//


            JSONObject options = action.getJSONObject("options");
            if(options.has("items")){
                JSONArray items = options.getJSONArray("items");
                for (int i=0; i<items.length(); i++) {
                    String item = items.getString(i);
                    editor.remove(item);
                    ((Launcher)context.getApplicationContext()).resetGlobal(item);
                }
                editor.commit();
            }

            // Execute next
            JasonHelper.next("success", action, ((Launcher)context.getApplicationContext()).getGlobal(), event, context);

        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void set(final JSONObject action, final JSONObject data, final JSONObject event, final Context context) {

        String TAG = "global";
        /********************

         The following sets a global variable named "db".

         {
             "type": "$global.set",
             "options": {
                 "db": ["a", "b", "c", "d"]
             }
         }

         Once set, you can access them through template expressions from ANYWHERE within the app, like this:

         {
             "items": {
                 "{{#each $global.db}}": {
                     "type": "label",
                     "text": "{{this}}"
                 }
             }
         }

         ********************/



        try {
            SharedPreferences pref = context.getSharedPreferences("global", 0);
            SharedPreferences.Editor editor = pref.edit();

            JSONObject options = action.getJSONObject("options");
            JSONObject jo = new JSONObject();
            JSONObject jsonObj = new JSONObject();

            //File processing
            File myFile = new File( Environment.getExternalStorageDirectory() , "DT/json/field.json" );
            FileWriter writer  = new FileWriter(myFile.getAbsoluteFile() , true);
            JsonParser jsonParser = new JsonParser();

            //Putting the contents of JSON file into a JSONObject using JSONParser


            //ReaDING FROM file
//            BufferedReader br = new BufferedReader(new FileReader(myFile));
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append( line );
//                sb.append( "\n" );
//                line = br.readLine();
//            }


            Iterator<String> keysIterator = options.keys();
            String section_name = (String) options.get("section_id");
            JSONObject finalObj = new JSONObject(  );
            JsonArray jsonArray = new JsonArray();
            JsonObject student2 = new JsonObject();
            JsonObject student = new JsonObject();
            JsonElement obj = jsonParser.parse(new FileReader(myFile));
            Log.d(TAG, "set: object " + obj);
            if(obj.isJsonNull()) {

            }
            else {
                student = (JsonObject) obj;
            }
            Log.d( TAG, "value from json file" + student.get("example") );

            while (keysIterator.hasNext()) {

                String key = (String) keysIterator.next();
                Object val = options.get(key);

                if(!key.equals("section_id") ) {
                    editor.putString( key, val.toString() );
                    ((Launcher) context.getApplicationContext()).setGlobal( key, val );
                    student2.addProperty( key,  val.toString() );
                }

            }
            student.add( section_name ,student2 );
            FileWriter file = new FileWriter(myFile);
            file.write(student.toString());
            file.flush();
            file.close();

            //Writing to file
//            MediaScannerConnection.scanFile(context , new String[]{myFile.getAbsolutePath() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//            if(myFile.exists()) {
//                writer.write( jsonObj.toString() );
//                writer.flush();// this lines takes whatever is stored in the BufferedWriter's internal buffer
//                writer.close();
//            }


            Log.d( "global", "jsonObject" + jo  + "myAnswer  "  + " ansother one ---->"  );
            editor.commit();

            // Execute next
            JasonHelper.next("success", action, ((Launcher)context.getApplicationContext()).getGlobal(), event, context);

        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
}
