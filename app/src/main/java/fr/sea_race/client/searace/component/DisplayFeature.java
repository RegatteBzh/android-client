package fr.sea_race.client.searace.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.sea_race.client.searace.R;

/**
 * Created by cyrille on 08/12/17.
 */

public class DisplayFeature extends RelativeLayout {

    private TextView label;
    private TextView value;

    public DisplayFeature(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        inflate(getContext(), R.layout.component_display_feature, this);
        label = (TextView)findViewById(R.id.label);
        value = (TextView)findViewById(R.id.value);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DisplayFeature,
                0, 0);

        try {
            setLabel(a.getString(R.styleable.DisplayFeature_label));
        } finally {
            a.recycle();
        }

    }

    public DisplayFeature(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplayFeature(Context context) {
        this(context, null, 0);
    }

    public void setLabel (String label) {
        this.label.setText(label);
    }

    public void setValue (String value) {
        this.value.setText(value);
    }

}
