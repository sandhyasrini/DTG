package com.jasonette.seed.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONObject;


public class JasonSwitchComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if(view == null) {
            return new Switch(context);
        } else {
            try {
                view = JasonComponent.build(view, component, parent, context);
                final Switch aSwitch = ((Switch) view);

                Boolean checked = false;
                Boolean isClickable = true;
//                float scaleX = 10;
//                float scaleY = 10;
                float text_size;

                if(component.has("name")){
                    if(((JasonViewActivity) context).model.var.has(component.getString("name"))){
                        checked = ((JasonViewActivity) context).model.var.getBoolean(component.getString("name"));
                    } else {
                        if(component.has("value")){
                            String name = component.getString( "value" );
//                            checked = component.getBoolean("value");
                            checked = Boolean.valueOf( name );
                        }
                    }

                    //Setting content description. realwear uses this as voice tag
                    if (component.has("contentDescription")) {
                        Log.i("Content Description--->", component.getString("contentDescription"));
                        ((Switch) view).setContentDescription(component.getString("contentDescription"));
                    }
                }

                if(component.has("switch_text")){
                    aSwitch.setText(component.getString("switch_text"));
                }

                //Custom field- TO make Switch readonly if isClickable id false.
                if(component.has("isClickable")){
                    isClickable = component.getBoolean("isClickable");
                }

                final JSONObject style = JasonHelper.style(component, context);

                aSwitch.setChecked(checked);
                aSwitch.setClickable(isClickable);

                int bgColor = Color.parseColor( "#ffffff" );
                if(style.has( "background" )){
                    bgColor = JasonHelper.parse_color(style.getString("background"));
                }
                if(style.has("border")){
                    int color = JasonHelper.parse_color(style.getString("border"));
                    GradientDrawable gd = new GradientDrawable();
                    gd.setShape(GradientDrawable.RECTANGLE);
                    gd.setColor( bgColor );
                    gd.setStroke(1,  color);
                    gd.setBounds(1, 1, 1, 1);
                    view.setBackground(gd);
                }
                else {
                    GradientDrawable gd = new GradientDrawable();
                    gd.setShape(GradientDrawable.RECTANGLE);
                    gd.setColor( bgColor );
                    view.setBackground(gd);
                }

                if(style.has("textsize")){
                    text_size = Float.parseFloat(style.getString("textsize"));
                    aSwitch.setTextSize(text_size);
                }

//                if (style.has("scalex"))
//                    scaleX = Float.parseFloat (style.getString("scalex"));
//
//                if (style.has("scaley"))
//                    scaleY = Float.parseFloat(style.getString("scaley"));
//
//                aSwitch.setScaleX(scaleX);
//                aSwitch.setScaleY(scaleY);


                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        onChange(aSwitch, isChecked, style, context);
                    }
                });
                changeColor(aSwitch, checked, style);
                view.requestLayout();
                return view;
            } catch (Exception e){
                return new View(context);
            }
        }
    }

    public static void onChange(Switch view, boolean isChecked, JSONObject style, Context root_context) {
        changeColor(view, isChecked, style);
        JSONObject component = (JSONObject)view.getTag();
        try {
            ((JasonViewActivity) root_context).model.var.put(component.getString("name"), view.isChecked());
            if (component.has("action")) {
                JSONObject action = component.getJSONObject("action");
                ((JasonViewActivity) root_context).call(action.toString(), new JSONObject().toString(), "{}", view.getContext());
            }
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
    public static void changeColor(Switch s, boolean isChecked, JSONObject style) {
        try {
            if(isChecked) {
                int color;
                if (style.has("color")) {
                    color = JasonHelper.parse_color(style.getString("color"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        s.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        s.getTrackDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    s.getThumbDrawable().clearColorFilter();
                    s.getTrackDrawable().clearColorFilter();
                }
            } else {
                int color;
                if (style.has("color:disabled")) {
                    color = JasonHelper.parse_color(style.getString("color:disabled"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        s.getThumbDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                        s.getTrackDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                } else {
                    s.getThumbDrawable().clearColorFilter();
                    s.getTrackDrawable().clearColorFilter();
                }
            }
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
}


//Sample JSON object:
//
//  {
//          "type": "switch",
//          "name": "light",
//          "value": "false",
//          "isClickable" : "true",
//          "action": {
//          "type": "$util.toast",
//          "options": {
//          "text" :"{{$get.light}}",
//          "type": "warning"
//                }
//          }
//  }