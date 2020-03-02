package com.paxees.gcsuser;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Paxees on 10/13/2018.
 */

public class FontImprima  {
    Context context;
    public FontImprima(Context context, EditText editText)
    {
        Typeface imprima = Typeface.createFromAsset(context.getAssets(), "fonts/contm.ttf");
        editText.setTypeface(imprima);
    }
    public FontImprima(Context context, Button button)
    {
        Typeface imprima = Typeface.createFromAsset(context.getAssets(), "fonts/contm.ttf");
        button.setTypeface(imprima);
    }
    public FontImprima(Context context, TextView textView)
    {
        Typeface imprima = Typeface.createFromAsset(context.getAssets(), "fonts/contm.ttf");
        textView.setTypeface(imprima);
    }
}
