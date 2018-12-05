package com.jasonette.seed.Action;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.Launcher.Launcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        try{
            File jsonFile = new File(Environment.getExternalStorageDirectory()   ,"/DT/file1.txt");
            Log.d( "global", "file path " + jsonFile);

            if(!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
//            FileWriter file = new FileWriter( jsonFile);
            JSONObject obj = new JSONObject();
            obj.put("Name", "crunchify.com");
            obj.put("Author", "App Shah");

            JSONObject jsonObject = new JSONObject();

            // Add the values to the jsonObject
            jsonObject.put("Name", "www.javainterviewpoint.com");
            jsonObject.put("Age", "999");

            // Create a new JSONArray object
            JSONArray jsonArray = new JSONArray();

            // Add values to the jsonArray
            jsonArray.put( 0,"India" );
            jsonArray.put( 1,"China " );
            jsonArray.put( 2,"Aus" );



            // Add the jsoArray to jsonObject
            jsonObject.put("Countries", jsonArray);


            // Create a new FileWriter object
            FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory() + "/DT/file1.txt" );
            // Writting the jsonObject into sample.json
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
//            file.write( String.valueOf( obj ));
//            file.close();

        } catch (JSONException e) {
            Log.d( "global", "jsonerror" +  e.getMessage() );
            e.printStackTrace();
        } catch (IOException e) {
            Log.d( "global", "jsonerror" +  e.getMessage() );
            e.printStackTrace();
        }

        try {
            SharedPreferences pref = context.getSharedPreferences("global", 0);
            SharedPreferences.Editor editor = pref.edit();

            JSONObject options = action.getJSONObject("options");

            JSONObject jo = new JSONObject();

            Iterator<String> keysIterator = options.keys();
            while (keysIterator.hasNext()) {
                String key = (String) keysIterator.next();
                Object val = options.get(key);
                if(jo.has( key ))
                {
                    if(jo.get(key) != "")
                    {
                        jo.remove( key );
                        jo.put( key,val );
                    }

                }
                else{
                    jo.put( key,val );
                }
                Log.d( "global", "jsonObject" + jo );
                    Log.d( "global", "set: "+ val );
                editor.putString(key, val.toString());
                ((Launcher)context.getApplicationContext()).setGlobal(key, val);
            }
            editor.commit();

            // Execute next
            JasonHelper.next("success", action, ((Launcher)context.getApplicationContext()).getGlobal(), event, context);

        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
}
