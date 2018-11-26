package com.jasonette.seed.Component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.jasonette.seed.Helper.JasonHelper;
import org.json.JSONObject;

public class JasonLabelComponent {


    public static View build(View view, final JSONObject component, final JSONObject parent, final Context context) {
        int bgColor;
        int borderWidth;
        int cornerWidth;

        if(view == null){
            return new TextView(context);
        } else {
            try {
                ((TextView)view).setText(component.getString("text"));
                JasonComponent.build(view, component, parent, context);

                view.setContentDescription("hf_use_text");

                String type;
                JSONObject style = JasonHelper.style(component, context);
                type = component.getString("type");

                if (style.has("color")) {
                    int color = JasonHelper.parse_color(style.getString("color"));
                    ((TextView)view).setTextColor(color);
                }
                if(style.has( "background" )){
                    bgColor = JasonHelper.parse_color(style.getString("background"));
                }

                else {
                    bgColor =  Color.parseColor( "#ffffff" );
                }

                if(style.has("border_width")){
                    borderWidth = Integer.parseInt(style.getString("border_width"));
                }
                else {
                    borderWidth= 1;
                }
                if(style.has("corner_width")){
                    cornerWidth = Integer.parseInt( (style.getString( "corner_width" )) );

                }
                else {
                    cornerWidth = 0;
                }
                if(style.has("border")){

                    int color = JasonHelper.parse_color(style.getString("border"));
                    GradientDrawable gd = new GradientDrawable();
                    gd.setShape(GradientDrawable.RECTANGLE);
                    gd.setGradientType( GradientDrawable.LINEAR_GRADIENT );
                    gd.setColor( bgColor  ); // Changes this drawbale to use a single color instead of a gradient
                    gd.setStroke(borderWidth,  color);
                    gd.setCornerRadius( cornerWidth );
                    gd.setBounds(2, 2, 2, 2);
                    view.setBackground(gd);


                }
                if(style.has("corner_width")){

                    GradientDrawable gd = new GradientDrawable();
                    gd.setShape(GradientDrawable.RECTANGLE);
                    gd.setGradientType( GradientDrawable.LINEAR_GRADIENT );
                    gd.setColor( bgColor  ); // Changes this drawbale to use a single color instead of a gradient
                    gd.setCornerRadius( cornerWidth );
                    gd.setBounds(2, 2, 2, 2);
                    view.setBackground(gd);


                }





                JasonHelper.setTextViewFont(((TextView)view), style, context);

                int g = 0;
                if (style.has("align")) {
                    String align = style.getString("align");
                    if (align.equalsIgnoreCase("center")) {
                        g = g | Gravity.CENTER_HORIZONTAL;
                        ((TextView) view).setGravity(Gravity.CENTER_HORIZONTAL);
                    } else if (align.equalsIgnoreCase("right")) {
                        g = g | Gravity.RIGHT;
                        ((TextView) view).setGravity(Gravity.RIGHT);
                    } else if (align.equalsIgnoreCase("left")) {
                        g = g | Gravity.LEFT;
                    }

                    if (align.equalsIgnoreCase("top")) {
                        g = g | Gravity.TOP;
                    } else if (align.equalsIgnoreCase("bottom")) {
                        g = g | Gravity.BOTTOM;
                    } else {
                        g = g | Gravity.CENTER_VERTICAL;
                    }
                } else {
                    g = Gravity.CENTER_VERTICAL;
                }

                ((TextView)view).setGravity(g);


                if (style.has("size")) {
                    ((TextView)view).setTextSize(Float.parseFloat(style.getString("size")));
                }

                ((TextView)view).setHorizontallyScrolling(false);

                JasonComponent.addListener(view, context);

                view.requestLayout();
                return view;

            } catch (Exception e){
                Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
                return new View(context);
            }
        }
    }
}
