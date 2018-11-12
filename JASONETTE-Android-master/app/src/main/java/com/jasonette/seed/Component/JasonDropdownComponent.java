package com.jasonette.seed.Component;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.Helper.JasonHelper;

import org.json.JSONObject;

public class JasonDropdownComponent {
    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {

        if(view == null)
        {
            return new Spinner(context);
        }
        else {
            try {
                view = JasonComponent.build(view, component, parent, context);
                final Spinner sSpinner = ((Spinner) view);
                String[] items = new String[]{"1", "2", "three"};
                if(component.has("name")){
                    Log.i("Name--->", component.getString("name"));

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
                    sSpinner.setAdapter(adapter);

                    if(((JasonViewActivity) context).model.var.has(component.getString("name"))){
                        Log.i("Dropdown", "Inside dropdown");

                    }
                    else {

                    }
                }

//                JSONObject style = JasonHelper.style(component, context);
//                String type = component.getString("type");

                return view;
            } catch (Exception e) {
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                return new View(context);
            }
        }
    }

}
