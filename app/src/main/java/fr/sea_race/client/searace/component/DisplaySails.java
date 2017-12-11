package fr.sea_race.client.searace.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import fr.sea_race.client.searace.R;
import fr.sea_race.client.searace.model.Sail;

/**
 * Created by cmeichel on 12/11/17.
 */

public class DisplaySails extends RelativeLayout {

    private TextView label;
    private Spinner spinner;
    private List<Sail> sails;
    private Context context;

    public DisplaySails(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        inflate(getContext(), R.layout.component_display_sails, this);
        label = (TextView)findViewById(R.id.label);
        spinner = (Spinner)findViewById(R.id.value);

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

    public DisplaySails(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplaySails(Context context) {
        this(context, null, 0);
    }

    public void setLabel (String label) {
        this.label.setText(label);
    }

    public void setSails(List<Sail> sails) {
        this.sails = sails;
        ArrayAdapter sailAdapter = new ArrayAdapter(context, R.layout.spinner, this.sails);
        spinner.setAdapter(sailAdapter);
    }

    public Sail getValue() {
        return (Sail)spinner.getSelectedItem();
    }

    public void setValue (String value) {

    }

}
