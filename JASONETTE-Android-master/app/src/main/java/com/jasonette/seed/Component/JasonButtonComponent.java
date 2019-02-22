package com.jasonette.seed.Component;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.Launcher.Launcher;
import com.jasonette.seed.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Timestamp;

public class JasonButtonComponent{
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) throws JSONException {
        if(component.has("url")){
            // image button
            view = JasonImageComponent.build(view, component, parent, context);
        }
        else if(component.has("text")){
            // label button

            view = JasonLabelComponent.build(view, component, parent, context);
            //view.setContentDescription("hf_use_text");
            if(component.has("contentDescription")){
                view.setContentDescription(component.getString("contentDescription"));
            }
            else
            {
                view.setContentDescription("hf_use_text");
            }
//            view.setBackgroundResource( R.drawable.rounded_corners);


            try {

                if(component.has("save")) {
//                    if (component.getString("save").equalsIgnoreCase("true")) {
//                        if (component.has("filename")) {
//                            File myFile = new File( Environment.getExternalStorageDirectory() , "DGX/json/field.json" );
//                            FileWriter writer  = new FileWriter(myFile.getAbsoluteFile() , true);
//                            JsonParser jsonParser = new JsonParser();
//                            JsonObject student ;
//                            String filename = component.getString("filename");
//                            File jsonFile = new File( Environment.getExternalStorageDirectory()   ,"/DGX/json/" + filename + "_" + new Timestamp(System.currentTimeMillis()) + ".json");
//
//                            MediaScannerConnection.scanFile(context, new String[]{jsonFile.getAbsolutePath() }, null,
//                                    new MediaScannerConnection.OnScanCompletedListener() {
//                                        public void onScanCompleted(String path, Uri uri) {
//                                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                                            Log.i("ExternalStorage", "-> uri=" + uri);
//                                        }
//                                    });
//                            if(!jsonFile.exists()) {
//                                jsonFile.createNewFile();
//                            }
//
//                            JsonElement obj = jsonParser.parse(new FileReader(myFile));
//                            if(obj.isJsonNull()) {
//
//                            }
//                            else {
//                                student = (JsonObject) obj;
//                                Log.d("button", "build: " + student);
//                                FileWriter file = new FileWriter(jsonFile);
//                                file.write(String.valueOf(student));
//
//                            }
//                            if(myFile.delete())
//                            {
//                                Log.d("button", "build: deleted");
//                            }
//
//                        }
//                    }
                }

                JSONObject style = component.getJSONObject("style");


                /*******
                 * ALIGN : By default align center
                 ******/

                // Default is center
                int g = Gravity.CENTER;
                if(style.has("margin")){
                    ((TextView) view).setGravity(Gravity.LEFT);

                    LayoutParams params = new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 20, 0, 0);

                    ((TextView) view).setLayoutParams(params);
                }

                if (style.has("align")) {
                    String align = style.getString("align");
                    if (align.equalsIgnoreCase("center")) {
                        g = g | Gravity.CENTER_HORIZONTAL;
                        ((TextView) view).setGravity(Gravity.CENTER_HORIZONTAL);
                        LayoutParams params = new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 0, 0, 0);
                        ((TextView) view).setLayoutParams(params);


                    } else if (align.equalsIgnoreCase("right")) {
                        g = g | Gravity.RIGHT;
                        ((TextView) view).setGravity(Gravity.RIGHT);
                    } else if (align.equalsIgnoreCase("left")) {
                        g = g | Gravity.LEFT;
                    }
                    else if (align.equalsIgnoreCase("middle")) {
                        g = g | Gravity.CENTER;
                        ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        ((TextView) view).setGravity(Gravity.LEFT);

                        LayoutParams params = new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(0, 30, 0, 0);
                        ((TextView) view).setLayoutParams(params);


                    }

                    if (align.equalsIgnoreCase("top")) {
                        g = g | Gravity.TOP;
                    } else if (align.equalsIgnoreCase("bottom")) {
                        g = g | Gravity.BOTTOM;
                    } else {
                        g = g | Gravity.CENTER_VERTICAL;
                    }
                }
                ((TextView)view).setGravity(g);



                /*******
                 * Padding: By default padding is 15
                 ******/
                // override each padding value only if it's not specified

                int padding_top = -1;
                int padding_left = -1;
                int padding_bottom = -1;
                int padding_right = -1;
                int margin_top =0;
                int margin_btm =0;
                if(style.has("padding")){
                    padding_top = (int)JasonHelper.pixels(context, style.getString("padding_top"), "horizontal");
                    padding_left = padding_top;
                    padding_right = padding_top;
                    padding_bottom = padding_top;
                }
                if(style.has("padding_top")){
                    padding_top = (int)JasonHelper.pixels(context, style.getString("padding_top"), "vertical");
                }
                if(style.has("padding_left")){
                    padding_left = (int)JasonHelper.pixels(context, style.getString("padding_left"), "horizontal");
                }
                if(style.has("padding_bottom")){
                    padding_bottom = (int)JasonHelper.pixels(context, style.getString("padding_bottom"), "vertical");
                }
                if(style.has("padding_right")){
                    padding_right = (int)JasonHelper.pixels(context, style.getString("padding_right"), "horizontal");
                }

                // if not specified, default is 15
                if(padding_top < 0){
                    padding_top = 15;
                }
                if(padding_left < 0){
                    padding_left = 15;
                }
                if(padding_bottom < 0){
                    padding_bottom = 15;
                }
                if(padding_right < 0){
                    padding_right = 15;
                }


                view.setPadding(padding_left, padding_top, padding_right, padding_bottom);
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
            }

        } else {
            // shouldn't happen
            if (view == null) {
                return new View(context);
            } else {
                return view;
            }
        }
        JasonComponent.addListener(view, context);
        return view;
    }
}
