package com.mclinic.view.sample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.mclinic.view.sample.R;

public class TwoTextItemCheckView extends RelativeLayout implements Checkable {

    public TwoTextItemCheckView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public TwoTextItemCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public TwoTextItemCheckView(Context context) {
        super(context);
    }


    @Override
	public boolean isChecked() {
        CheckBox c = (CheckBox) findViewById(R.id.twolinecheckbox);
        return c.isChecked();
    }


    @Override
	public void setChecked(boolean checked) {
        CheckBox c = (CheckBox) findViewById(R.id.twolinecheckbox);
        c.setChecked(checked);
    }


    @Override
	public void toggle() {
        CheckBox c = (CheckBox) findViewById(R.id.twolinecheckbox);
        c.setChecked(!c.isChecked());
    }

}
