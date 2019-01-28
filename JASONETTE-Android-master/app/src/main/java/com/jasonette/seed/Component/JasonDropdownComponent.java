package com.jasonette.seed.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;
import com.jasonette.seed.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class JasonDropdownComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        if (view == null) {
            return new Spinner(context);
        } else {
            try {
                view = JasonComponent.build(view, component, parent, context);
                final Spinner sSpinner = ((Spinner) view);
                sSpinner.setBackgroundColor(Color.parseColor("#ffffff"));
                sSpinner.setMinimumWidth(30);
                sSpinner.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                sSpinner.setPadding(0, 0, 0, 0);
//                sSpinner.setBackgroundResource( R.drawable.bg );


                if (component.has("name")) {
                    Log.i("Name--->", component.getString("name"));
                    String[] items;
                    //Log.i("Content Description--->", component.getString("contentDescription"));

                    int getSize;

                    //Setting Style
                    final int color;
                    final float size;
                    final int padding;
                    final int bgColor;
                    JSONObject style = JasonHelper.style(component, context);
                    if (style.has("color"))
                        color = JasonHelper.parse_color(style.getString("color"));
                    else
                        color = Color.parseColor("#111111");

                    if (style.has("background"))
                        bgColor = JasonHelper.parse_color(style.getString("background"));
                    else
                        bgColor = 0;


                    if (style.has("size"))
                        size = Float.parseFloat(style.getString("size"));
                    else
                        size = 15;

                    if (style.has("padding"))
                        padding = Integer.parseInt(style.getString("padding"));
                    else
                        padding = 0;


                    //Setting content description. realwear uses this as voice tag
                    if (component.has("contentDescription")) {
                        Log.i("Content Description--->", component.getString("contentDescription"));
                        ((Spinner) view).setContentDescription(component.getString("contentDescription"));
                    }
                    if (style.has("border")) {

                        int bordercolor = JasonHelper.parse_color(style.getString("border"));
                        GradientDrawable gd = new GradientDrawable();
                        gd.setShape(GradientDrawable.RECTANGLE);
                        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                        gd.setColor(bgColor); // Changes this drawbale to use a single color instead of a gradient
                        gd.setStroke(1, bordercolor);
                        gd.setBounds(1, 1, 1, 1);
                        ((Spinner) view).setBackground(gd);
                    }

                    int defaultSelPos = 0;
                    boolean isDefaultVal = true;
                    String selectedItem;


                    if (component.has("value")) {
                        Log.i("Value--->", component.getString("value"));
                        try {
                            isDefaultVal = false;
                            //defaultSelPos = Integer.parseInt(component.getString("value"));
                            JSONArray arrOfElements = component.getJSONArray("options");
                            for (int i = 0; i < arrOfElements.length(); i++) {
                                JSONObject optionObj = arrOfElements.getJSONObject(i);
                                if(optionObj.getString("value").trim().equals(component.getString("value").trim()))
                                {
                                    defaultSelPos = i;
                                    break;
                                }
                            }
                        } catch (NumberFormatException e) {
                            isDefaultVal = true;
                            Log.e("Error", component.getString("value") + "is not a valid integer number.");
                        }
                    }

                    //Create spinner (Dropdown) and map dropdown values.
                    if (component.has("options")) {
                        JSONArray arrOfOptions = component.getJSONArray("options");
                        items = new String[arrOfOptions.length()];
                        for (int i = 0; i < arrOfOptions.length(); i++) {
                            JSONObject optionObj = arrOfOptions.getJSONObject(i);
                            //Log.i("Dropdown option--->" + i, optionObj.getString("value"));
                            items[i] = optionObj.getString("value");

                            if (optionObj.has("defaultSelected")) {
                                //Log.i("defaultSelected -->", optionObj.getString("defaultSelected"));
                                if (optionObj.getString("defaultSelected").trim().equals("true")) {
                                    // Log.i("defaultSelected True", optionObj.getString("defaultSelected"));
                                    //Set default value only if there is no value for value property.
                                    if (isDefaultVal) {
                                        defaultSelPos = i;
                                    }
                                }
                            }
                        }


                        try {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items) {
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getView(position, convertView, parent);
                                    ((TextView) v).setTextSize(size);
                                    ((TextView) v).setTextColor(color);
                                    return v;
                                }

                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = super.getDropDownView(position, convertView, parent);
                                    ((TextView) v).setTextSize(size);
                                    ((TextView) v).setTextColor(color);
                                    ((TextView) v).setCompoundDrawablePadding(padding);
                                    ((TextView) v).setBackgroundColor(bgColor);
                                    return v;
                                }
                        };

//                            adapter.setDropDownViewResource(R.layout.custom);
                            sSpinner.setAdapter(adapter);
                            if (style.has("height") && style.has("width")) {
                                int  height = (int) JasonHelper.pixels(context, style.getString("height"), "horizontal");
                                int  width = (int) JasonHelper.pixels(context, style.getString("width"), "horizontal");

                                sSpinner.setLayoutParams(new LinearLayout.LayoutParams(width, height));

                            } else if (style.has("height")) {
                                int  height = (int) JasonHelper.pixels(context, style.getString("height"), "horizontal");

                                sSpinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, height));

                            } else if (style.has("width")) {
                              int  width = (int) JasonHelper.pixels(context, style.getString("width"), "horizontal");

                                sSpinner.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));

                            }

                            sSpinner.setSelection(defaultSelPos);

                            sSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    itemSelected(sSpinner, i, context);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    //adapterView.setSelection(defaultSelPos);
                                }
                            });


                        } catch (Exception e) {
                            Log.d("dropdown", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                        }
                    }
                }
                view.requestLayout();
                return view;
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                return new View(context);
            }
        }
    }

    public static void itemSelected(Spinner view, int i, Context root_context) {
        view.setSelection(i);
        JSONObject component = (JSONObject) view.getTag();
        try {
            ((JasonViewActivity) root_context).model.var.put(component.getString("name"), view.getSelectedItem());
            if (component.has("action")) {
                JSONObject action = component.getJSONObject("action");
                ((JasonViewActivity) root_context).call(action.toString(), new JSONObject().toString(), "{}", view.getContext());
            }
        } catch (Exception e) {
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }
    }
}

//------------------------Sample Dropdown JSON object-------------------------------
//  {
//          "type":"dropdown",
//          "name":"shift",
//          "value":"{{$global.dropdownval}}",
//          "contentDescription":"shift",
//          "options":[
//          {
//          "value":"Value 1",
//          "defaultSelected":"false"
//          },
//          {
//          "value":"Value 2",
//          "defaultSelected":"false"
//          },
//          {
//          "value":"Value 3",
//          "defaultSelected":"true"
//          }
//          ],
//          "style":{
//          "size":"20",
//          "color":"rgb(200,130,0)",
//          "padding":"5"
//          },
//          "action":{
//          "type":"$util.toast",
//          "options":{
//          "text":"{{$global.dropdownval}}",
//          "type":"warning"
//          }
//          }
//  }

