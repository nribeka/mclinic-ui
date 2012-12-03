package com.mclinic.view.sample.activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.mclinic.api.model.Observation;
import com.mclinic.api.model.Patient;
import com.mclinic.api.service.PatientService;
import com.mclinic.view.sample.R;
import com.mclinic.view.sample.adapters.EncounterAdapter;
import com.mclinic.view.sample.utilities.Constants;
import com.mclinic.view.sample.utilities.FileUtils;

public class ObservationTimelineActivity extends ListActivity {

    private Patient mPatient;
    private String mObservationFieldName;

    private ArrayAdapter<Observation> mEncounterAdapter;
    private ArrayList<Observation> mEncounters = new ArrayList<Observation>();
    
    @Inject
    private PatientService pService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.observation_timeline);

        if (!FileUtils.storageReady()) {
            showCustomToast(getString(R.string.error_storage));
            finish();
        }

        // TODO Check for invalid patient IDs
        String patientIdStr = getIntent().getStringExtra(Constants.KEY_PATIENT_ID);
        mPatient = getPatient(patientIdStr);

        mObservationFieldName = getIntent().getStringExtra(Constants.KEY_OBSERVATION_FIELD_NAME);

        setTitle(getString(R.string.app_name) + " > "
                + getString(R.string.view_observation));

        TextView textView = (TextView) findViewById(R.id.title_text);
        if (textView != null) {
            textView.setText(mObservationFieldName);
        }
    }

    private Patient getPatient(String patientUUID) {
        return pService.getPatientByUUID(patientUUID);
    }

    private void getObservations(Patient patient, String fieldName) {

//        ClinicAdapter ca = new ClinicAdapter();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        ca.open();
//        Cursor c = ca.fetchPatientObservation(patientId, fieldName);
//
//        if (c != null && c.getCount() >= 0) {
//
//            mEncounters.clear();
//
//            int valueTextIndex = c.getColumnIndex(ClinicAdapter.KEY_VALUE_TEXT);
//            int valueIntIndex = c.getColumnIndex(ClinicAdapter.KEY_VALUE_INT);
//            int valueDateIndex = c.getColumnIndex(ClinicAdapter.KEY_VALUE_DATE);
//            int valueNumericIndex = c.getColumnIndex(ClinicAdapter.KEY_VALUE_NUMERIC);
//            int encounterDateIndex = c.getColumnIndex(ClinicAdapter.KEY_ENCOUNTER_DATE);
//            int dataTypeIndex = c.getColumnIndex(ClinicAdapter.KEY_DATA_TYPE);
//
//            Observation obs;
//            do {
//                obs = new Observation();
//                obs.setFieldName(fieldName);
//                try {
//                    obs.setEncounterDate(df.parse(c
//                            .getString(encounterDateIndex)));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                int dataType = c.getInt(dataTypeIndex);
//                obs.setDataType((byte) dataType);
//                switch (dataType) {
//                    case Constants.TYPE_INT:
//                        obs.setValueInt(c.getInt(valueIntIndex));
//                        break;
//                    case Constants.TYPE_FLOAT:
//                        obs.setValueNumeric(c.getFloat(valueNumericIndex));
//                        break;
//                    case Constants.TYPE_DATE:
//                        try {
//                            obs.setValueDate(df.parse(c
//                                    .getString(valueDateIndex)));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    default:
//                        obs.setValueText(c.getString(valueTextIndex));
//                }
//
//                mEncounters.add(obs);
//
//            } while (c.moveToNext());
//        }
//
//        refreshView();
//
//        if (c != null) {
//            c.close();
//        }
//        ca.close();
    }

    private void refreshView() {

        mEncounterAdapter = new EncounterAdapter(this, R.layout.encounter_list_item,
                mEncounters);
        setListAdapter(mEncounterAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPatient != null && mObservationFieldName != null) {
            getObservations(mPatient, mObservationFieldName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void showCustomToast(String message) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_view, null);

        // set the text in the view
        TextView tv = (TextView) view.findViewById(R.id.message);
        tv.setText(message);

        Toast t = new Toast(this);
        t.setView(view);
        t.setDuration(Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}
