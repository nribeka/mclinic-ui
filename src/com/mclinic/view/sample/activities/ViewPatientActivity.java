package com.mclinic.view.sample.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.burkeware.search.api.Context;
import com.mclinic.api.model.Observation;
import com.mclinic.api.model.Patient;
import com.mclinic.api.service.ObservationService;
import com.mclinic.api.service.PatientService;
import com.mclinic.view.sample.R;
import com.mclinic.view.sample.adapters.ObservationAdapter;
import com.mclinic.view.sample.utilities.Constants;
import com.mclinic.view.sample.utilities.FileUtils;

public class ViewPatientActivity extends ListActivity {

    private Button mActionButton;
    private static Patient mPatient;
    private static String mProviderId;
    private String mSelectedId;
    private ArrayList<String> mForms = new ArrayList<String>();
    private static HashMap<String, String> mInstanceValues = new HashMap<String, String>();
    private static final DateFormat COLLECT_INSTANCE_NAME_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss");

    private ArrayAdapter<Observation> mObservationAdapter;
    private static ArrayList<Observation> mObservations = new ArrayList<Observation>();
    
    private PatientService pService = Context.getInstance(PatientService.class);
    
    private ObservationService obsService = Context.getInstance(ObservationService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_patient);

        if (!FileUtils.storageReady()) {
            showCustomToast(getString(R.string.error_storage));
            finish();
        }

        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        mProviderId = settings.getString(PreferencesActivity.KEY_PROVIDER, "0");

        // TODO Check for invalid patient IDs
        String patientIdStr = getIntent().getStringExtra(Constants.KEY_PATIENT_ID);
        
        mPatient = getPatient(patientIdStr);

        setTitle(getString(R.string.app_name) + " > "
                + getString(R.string.view_patient));

        View patientView = (View) findViewById(R.id.patient_info);
        patientView.setBackgroundResource(R.drawable.search_holder_gradient);

        TextView textView = (TextView) findViewById(R.id.identifier_text);
        if (textView != null)
            textView.setText(mPatient.getIdentifier());

        textView = (TextView) findViewById(R.id.name_text);
        if (textView != null) {
            textView.setText(mPatient.getName());
        }

        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        textView = (TextView) findViewById(R.id.birthdate_text);
        if (textView != null && mPatient.getBirthdate() != null) {
            textView.setText(df.format(mPatient.getBirthdate()));
        }

        ImageView imageView = (ImageView) findViewById(R.id.gender_image);
        if (imageView != null) {
            if (mPatient.getGender().equals("M")) {
                imageView.setImageResource(R.drawable.male);
            } else if (mPatient.getGender().equals("F")) {
                imageView.setImageResource(R.drawable.female);
            }
        }

        mActionButton = (Button) findViewById(R.id.fill_forms);
        mActionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                getDownloadedForms();
                if (mForms.size() > 0) {
                    createFillFormDialog();
                } else {
                    showCustomToast(getString(R.string.no_form_to_fill));
                }
            }
        });
    }

    private void getDownloadedForms() {

//        ClinicAdapter ca = new ClinicAdapter();
//        ca.open();
//        Cursor c = ca.fetchAllForms();
//
//        if (c != null && c.getCount() >= 0) {
//            mForms.clear();
//            int formIdIndex = c.getColumnIndex(ClinicAdapter.KEY_FORM_ID);
//            int nameIndex = c.getColumnIndex(ClinicAdapter.KEY_NAME);
//            int pathIndex = c.getColumnIndex(ClinicAdapter.KEY_PATH);
//
//            if (c.getCount() > 0) {
//                do {
//                    if (!c.isNull(pathIndex))
//                        mForms.add(c.getString(nameIndex) + " ("
//                                + c.getInt(formIdIndex) + ")");
//                } while (c.moveToNext());
//            }
//        }
//
//        refreshView();
//
//        if (c != null)
//            c.close();
//
//        ca.close();
    	refreshView();
    }


    private static void prepareInstanceValues() {

        for (int i = 0; i < mObservations.size(); i++) {
            Observation o = mObservations.get(i);
            mInstanceValues.put(o.getFieldName(), o.toString());
        }
    }

    private static int createFormInstance(String formPath, String jrFormId) {

//        // reading the form
//        Document doc = new Document();
//        KXmlParser formParser = new KXmlParser();
//        try {
//            formParser.setInput(new FileReader(formPath));
//            doc.parse(formParser);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        findFormNode(doc.getRootElement());
//        if (mFormNode != null) {
//            prepareInstanceValues();
//            traverseInstanceNodes(mFormNode);
//        } else {
//            return -1;
//        }
//
//        // writing the instance file
//        File formFile = new File(formPath);
//        String formFileName = formFile.getName();
//        String instanceName = "";
//        if (formFileName.endsWith(".xml")) {
//            instanceName = formFileName.substring(0, formFileName.length() - 4);
//        } else {
//            instanceName = jrFormId;
//        }
//        instanceName = instanceName + "_"
//                + COLLECT_INSTANCE_NAME_DATE_FORMAT.format(new Date());
//
//        String instancePath = FileUtils.INSTANCES_PATH + instanceName;
//        (new File(instancePath)).mkdirs();
//        String instanceFilePath = instancePath + "/" + instanceName + ".xml";
//        File instanceFile = new File(instanceFilePath);
//
//        KXmlSerializer instanceSerializer = new KXmlSerializer();
//        try {
//            instanceFile.createNewFile();
//            FileWriter instanceWriter = new FileWriter(instanceFile);
//            instanceSerializer.setOutput(instanceWriter);
//            mFormNode.write(instanceSerializer);
//            instanceSerializer.endDocument();
//            instanceSerializer.flush();
//            instanceWriter.close();
//
//            // register into content provider
//            ContentValues insertValues = new ContentValues();
//            insertValues.put("displayName", mPatient.getGivenName() + " "
//                    + mPatient.getFamilyName());
//            insertValues.put("instanceFilePath", instanceFilePath);
//            insertValues.put("jrFormId", jrFormId);
//            Uri insertResult = App.getApp().getContentResolver()
//                    .insert(InstanceColumns.CONTENT_URI, insertValues);
//
//            // insert to clinic
//            // Save form instance to db
//            FormInstance fi = new FormInstance();
//            fi.setPatientId(mPatient.getPatientId());
//            fi.setFormId(Integer.parseInt(jrFormId));
//            fi.setPath(instanceFilePath);
//            fi.setStatus(ClinicAdapter.STATUS_UNSUBMITTED);
//
//            ClinicAdapter ca = new ClinicAdapter();
//            ca.open();
//            ca.createFormInstance(fi, mPatient.getGivenName() + " "
//                    + mPatient.getFamilyName());
//            ca.close();
//
//            return Integer.valueOf(insertResult.getLastPathSegment());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return -1;
    }


    private void launchFormEntry(String jrFormId) {

//        String formPath = null;
//        int id = -1;
//        try {
//            Cursor mCursor = App.getApp().getContentResolver()
//                    .query(FormsColumns.CONTENT_URI, null, null, null, null);
//            mCursor.moveToPosition(-1);
//            while (mCursor.moveToNext()) {
//
//                int dbid = mCursor.getInt(mCursor.getColumnIndex(FormsColumns._ID));
//                String dbjrFormId = mCursor.getString(mCursor.getColumnIndex(FormsColumns.JR_FORM_ID));
//                formPath = mCursor.getString(mCursor.getColumnIndex(FormsColumns.FORM_FILE_PATH));
//
//                if (jrFormId.equalsIgnoreCase(dbjrFormId)) {
//                    id = dbid;
//                    break;
//                }
//            }
//            if (mCursor != null) {
//                mCursor.close();
//            }
//
//            if (id != -1) {
//
//                // create instance
//                int instanceId = createFormInstance(formPath, jrFormId);
//                if (instanceId != -1) {
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName(
//                            "org.odk.collect.android",
//                            "org.odk.collect.android.activities.FormEntryActivity"));
//                    intent.setAction(Intent.ACTION_EDIT);
//                    intent.setData(Uri.parse(InstanceColumns.CONTENT_URI + "/" + instanceId));
//
//                    startActivity(intent);
//                } else {
//                    Uri formUri = ContentUris.withAppendedId(FormsColumns.CONTENT_URI, id);
//                    startActivity(new Intent(Intent.ACTION_EDIT, formUri));
//                }
//            } else
//                showCustomToast(getString(R.string.error_form_load));
//        } catch (ActivityNotFoundException e) {
//            showCustomToast(getString(R.string.error_odk_collect));
//        } catch (NullPointerException e) {
//            showCustomToast(getString(R.string.error_initialize_odk_collect));
//        }
    }

    private void createFillFormDialog() {

        final CharSequence[] items = mForms.toArray(new CharSequence[mForms.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a form to fill");

        builder.setSingleChoiceItems(items, -1, null);
        builder.setPositiveButton("Fill Form",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // extract form id
                        int i = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if (i != -1) {
                            Pattern p = Pattern.compile("\\([0-9]+\\)$");
                            Matcher m = p.matcher(items[i]);
                            while (m.find()) {
                                mSelectedId = m.group(0).substring(1, m.group(0).length() - 1);
                            }
                            dialog.dismiss();
                            launchFormEntry(mSelectedId);
                        } else {
                            dialog.dismiss();
                        }

                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private Patient getPatient(String patientUUID) {
        return pService.getPatientByUUID(patientUUID);
    }

    private void getAllObservations(Patient patient) {
        mObservations.clear();
        List<Observation> observations = obsService.getAllObservations(patient);
        for (Observation observation : observations) {
        	mObservations.add(observation);
		}
    }

    // TODO on long press, graph
    // TODO if you have only one value, don't display next level
    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {

        if (mPatient != null) {
            // Get selected observation
            Observation obs = (Observation) getListAdapter().getItem(position);

            Intent ip;
            int dataType = obs.getDataType();
            if (dataType == Constants.TYPE_INT || dataType == Constants.TYPE_FLOAT) {
                ip = new Intent(getApplicationContext(), ObservationChartActivity.class);
                ip.putExtra(Constants.KEY_PATIENT_ID, mPatient.getUuid());
                ip.putExtra(Constants.KEY_OBSERVATION_FIELD_NAME, obs.getFieldName());
                startActivity(ip);
            } else {
                ip = new Intent(getApplicationContext(), ObservationTimelineActivity.class);
                ip.putExtra(Constants.KEY_PATIENT_ID, mPatient.getUuid());
                ip.putExtra(Constants.KEY_OBSERVATION_FIELD_NAME, obs.getFieldName());
                startActivity(ip);
            }
        }
    }

    private void refreshView() {

        mObservationAdapter = new ObservationAdapter(this,
                R.layout.observation_list_item, mObservations);
        setListAdapter(mObservationAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPatient != null) {
            getAllObservations(mPatient);
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
        LayoutInflater inflater = (LayoutInflater) getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
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