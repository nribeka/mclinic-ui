package com.mclinic.view.sample.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.mclinic.view.sample.R;

public class CheckedTextView extends LinearLayout implements Checkable {

	public CheckedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public boolean isChecked() {
		RadioButton r = (RadioButton) findViewById(R.id.radiobutton);
		return r.isChecked();
	}

	@Override
	public void setChecked(boolean checked) {
		RadioButton r = (RadioButton) findViewById(R.id.radiobutton);
		r.setChecked(checked);
	}

	@Override
	public void toggle() {
		RadioButton r = (RadioButton) findViewById(R.id.radiobutton);
		r.setChecked(!r.isChecked());
	}
}