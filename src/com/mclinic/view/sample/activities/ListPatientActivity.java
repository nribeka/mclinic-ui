package com.mclinic.view.sample.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.burkeware.search.api.Context;
import com.mclinic.api.model.Cohort;
import com.mclinic.api.model.Patient;
import com.mclinic.api.module.MclinicAPIModule;
import com.mclinic.api.service.AdministrativeService;
import com.mclinic.api.service.CohortService;
import com.mclinic.api.service.PatientService;
import com.mclinic.view.sample.R;
import com.mclinic.view.sample.adapters.PatientAdapter;
import com.mclinic.view.sample.listeners.DownloadListener;
import com.mclinic.view.sample.tasks.DownloadCohortTask;
import com.mclinic.view.sample.tasks.DownloadPatientTask;
import com.mclinic.view.sample.tasks.DownloadTask;
import com.mclinic.view.sample.utilities.Constants;
import com.mclinic.view.sample.utilities.FileUtils;

public class ListPatientActivity extends ListActivity implements DownloadListener{

    // Menu ID's
    private static final int MENU_PREFERENCES = Menu.FIRST;
    private static final int MENU_GET_FORMS = Menu.FIRST + 1;

    // Request codes
    public static final int DOWNLOAD_PATIENT = 1;
    public static final int BARCODE_CAPTURE = 2;
    public static final int SEARCH_PATIENT = 3;
    
	private final static int COHORT_DIALOG = 1;
	private final static int COHORTS_PROGRESS_DIALOG = 2;
	private final static int PATIENTS_PROGRESS_DIALOG = 3;
	
	private AlertDialog mCohortDialog;
	private ProgressDialog mProgressDialog;
	
	private DownloadTask mDownloadTask;
	
	private Integer cohortPos;
	
	private ArrayList<Cohort> mCohorts = new ArrayList<Cohort>();


    private static final String DOWNLOAD_PATIENT_CANCELED_KEY = "downloadPatientCanceled";

    private ImageButton mDownloadButton;
    private ImageButton mSearchButton;
    private ImageButton mBarcodeButton;
    private Button mNewPatientButton;
    private Button mFilledFormsButton;
    private EditText mSearchText;
    private TextWatcher mFilterTextWatcher;

    private ArrayAdapter<Patient> mPatientAdapter;
    private ArrayList<Patient> mPatients = new ArrayList<Patient>();
    private boolean mDownloadPatientCanceled = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(DOWNLOAD_PATIENT_CANCELED_KEY)) {
                mDownloadPatientCanceled = savedInstanceState.getBoolean(DOWNLOAD_PATIENT_CANCELED_KEY);
            }
        }

        setContentView(R.layout.list_patients);

        if (!FileUtils.storageReady()) {
            showCustomToast(getString(R.string.error_storage));
            finish();
        }

        mFilterTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (mPatientAdapter != null) {
                    mPatientAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        };

        mSearchText = (EditText) findViewById(R.id.search_text);
        mSearchText.addTextChangedListener(mFilterTextWatcher);

        mBarcodeButton = (ImageButton) findViewById(R.id.barcode_button);
        mBarcodeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent("com.google.zxing.client.android.SCAN");
                try {
                    startActivityForResult(i, BARCODE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.barcode_error), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
            }
        });

        mNewPatientButton = (Button) findViewById(R.id.create_patient);
        mNewPatientButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), CreatePatientActivity.class);
                startActivity(in);
            }
        });

        mFilledFormsButton = (Button) findViewById(R.id.filled_forms);
        mFilledFormsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent iu = new Intent(getApplicationContext(), InstanceListActivity.class);
                startActivity(iu);
            }
        });

        mDownloadButton = (ImageButton) findViewById(R.id.download_patients);
        mDownloadButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                Intent id = new Intent(getApplicationContext(), DownloadPatientActivity.class);
//                startActivityForResult(id, DOWNLOAD_PATIENT);
            }
        });

        mDownloadButton.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                downloadCohorts();
                return true;
            }
        });

        mSearchButton = (ImageButton) findViewById(R.id.search_patient);
        mSearchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent is = new Intent(getApplicationContext(), SearchPatientActivity.class);
                is.putExtra("searchText", mSearchText.getText().toString());
                startActivityForResult(is, SEARCH_PATIENT);

                //and hide that keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
            }
        });

    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {
        // Get selected patient
        Patient p = (Patient) getListAdapter().getItem(position);
        String patientIdStr = p.getUuid();

        Intent ip = new Intent(getApplicationContext(),ViewPatientActivity.class);
        ip.putExtra(Constants.KEY_PATIENT_ID, patientIdStr);
        startActivity(ip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_PREFERENCES, 0, getString(R.string.server_preferences))
                .setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, MENU_GET_FORMS, 0, getString(R.string.openmrs_forms))
                .setIcon(R.drawable.openmrs);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PREFERENCES:
                Intent ip = new Intent(getApplicationContext(), PreferencesActivity.class);
                startActivity(ip);
                return true;
            case MENU_GET_FORMS:
                Intent in = new Intent(getApplicationContext(), ListFormActivity.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SEARCH_PATIENT) {
            TextKeyListener.clear(mSearchText.getText());
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            if (requestCode == DOWNLOAD_PATIENT) {
                mDownloadPatientCanceled = true;
            }
            return;
        }

        if (requestCode == BARCODE_CAPTURE && intent != null) {
            String sb = intent.getStringExtra("SCAN_RESULT");
            if (sb != null && sb.length() > 0) {
                mSearchText.setText(sb);
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }

    private void getPatients() {
    	
        PatientService pService =Context.getInstance(PatientService.class);
        
    	List<Patient> patients = pService.getAllPatients();
    	mPatients.clear();
    	for (Patient patient : patients) {
    		mPatients.add(patient);
        }

        Collections.sort(mPatients, new Comparator<Patient>() {
            public int compare(Patient p1, Patient p2) {
                return p1.getName().compareTo(p2.getName());
            }
        });

        refreshView();
    }

    private void refreshView() {
        mPatientAdapter = new PatientAdapter(this, R.layout.patient_list_item, mPatients);
        setListAdapter(mPatientAdapter);
        setTitle(getString(R.string.app_name) + " > " + getString(R.string.list_patients)
                + " (" + mPatients.size() + ")");
    }

    @Override
    protected void onDestroy() {
//        if (mUploadFormTask != null) {
//            mUploadFormTask.setUploadListener(null);
//            if (mUploadFormTask.getStatus() == AsyncTask.Status.FINISHED) {
//                mUploadFormTask.cancel(true);
//            }
//        }

        super.onDestroy();
        mSearchText.removeTextChangedListener(mFilterTextWatcher);
    }

    @Override
    protected void onResume() {
//        if (mUploadFormTask != null) {
//            mUploadFormTask.setUploadListener(this);
//        }
        super.onResume();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String server = settings.getString(
		        PreferencesActivity.KEY_SERVER, getString(R.string.default_server));
		String username = settings.getString(
		        PreferencesActivity.KEY_USERNAME, getString(R.string.default_username));
		String password = settings.getString(
		        PreferencesActivity.KEY_PASSWORD, getString(R.string.default_password));

		MclinicAPIModule apiModule = new MclinicAPIModule("/mnt/sdcard/lucene" ,"uuid");
		apiModule.setServer(server);
		apiModule.setUsername(username);
		apiModule.setPassword(password);
		Context.initialize(apiModule);
		AdministrativeService adminService= Context.getInstance(AdministrativeService.class);
		adminService.initializeDB(new File("/mnt/sdcard/j2l"));

		getPatients();
		mSearchText.setText(mSearchText.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(DOWNLOAD_PATIENT_CANCELED_KEY, mDownloadPatientCanceled);
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
    
    private void downloadCohorts() {
				
		// setup dialog and upload task
		showDialog(COHORTS_PROGRESS_DIALOG);
		mDownloadTask = new DownloadCohortTask();
		mDownloadTask.setDownloadListener(this);
		mDownloadTask.execute();
	}
    
    private void downloadPatients(String cohortUUID) {
		
		// setup dialog and upload task
		showDialog(PATIENTS_PROGRESS_DIALOG);
		
		System.out.println("dowloading" + cohortUUID);
		mDownloadTask = new DownloadPatientTask();
		mDownloadTask.setDownloadListener(this);
		mDownloadTask.execute(cohortUUID);
	}
    
    private class CohortDialogListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (which >= 0)
				cohortPos=which;
	        switch (which) {
	            case DialogInterface.BUTTON_NEUTRAL: // refresh
	                downloadCohorts();
	                break;
	            case DialogInterface.BUTTON_NEGATIVE: // cancel
	                setResult(RESULT_CANCELED);
	                dialog.dismiss();
	                break;
	            case DialogInterface.BUTTON_POSITIVE: // download
	            	System.out.println(which);
	            	System.out.println(mCohorts.size());
	                downloadPatients(mCohorts.get(cohortPos).getUuid());
	                break;
	        }
	}

	@Override
	public void onCancel(DialogInterface dialog) {
	    setResult(RESULT_CANCELED);
	    finish();
	}
}
    
	private AlertDialog createCohortDialog() {
		
		CohortDialogListener listener = new CohortDialogListener();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.select_cohort));

		if (!mCohorts.isEmpty()) {
			
			int selectedCohortIndex = -1;
			String[] cohortNames = new String[mCohorts.size()];
			for (int i = 0; i < mCohorts.size(); i++) {
				Cohort c = mCohorts.get(i);
				cohortNames[i] = c.getName();
			}
			builder.setSingleChoiceItems(cohortNames, selectedCohortIndex, listener);
			builder.setPositiveButton(getString(R.string.download), listener);
		} else {
			builder.setMessage(getString(R.string.no_cohort));
		}
		builder.setNeutralButton(getString(R.string.refresh), listener);
		builder.setNegativeButton(getString(R.string.cancel), listener);
		builder.setOnCancelListener(listener);

		return builder.create();
	}
    
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == COHORT_DIALOG) {
			mCohortDialog = createCohortDialog();
			return mCohortDialog;
		} else if (id == COHORTS_PROGRESS_DIALOG || id == PATIENTS_PROGRESS_DIALOG) {
			mProgressDialog = createDownloadDialog();
			return mProgressDialog;
		}

		return null;
	}
	
	private ProgressDialog createDownloadDialog() {
		
		ProgressDialog dialog = new ProgressDialog(this);
		DialogInterface.OnClickListener loadingButtonListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mDownloadTask.setDownloadListener(null);
				setResult(RESULT_CANCELED);
				finish();
			}
		};
		dialog.setTitle(getString(R.string.downloading));
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setIndeterminate(false);
		dialog.setCancelable(false);
		dialog.setButton(getString(R.string.cancel_download),loadingButtonListener);
		
		return dialog;
	}
	
	private void getCohorts() {

		CohortService cService = Context.getInstance(CohortService.class);
		List<Cohort> cohorts = cService.getAllCohorts();
		if (cohorts==null)
			System.out.println("couldnt fetch cohorts");
		else {
			mCohorts.clear();
			for (Cohort cohort : cohorts) {
				mCohorts.add(cohort);
			}
		}
	}
	
	@Override
	public void taskComplete(String result) {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
		
		if (result != null) {
			showCustomToast("Error: " + result);
			getCohorts();
			showDialog(COHORT_DIALOG);
		} else {
			if (mDownloadTask instanceof DownloadCohortTask) {
				mDownloadTask = null;
				getCohorts();
				showDialog(COHORT_DIALOG);
			} else {
				mDownloadTask = null;
				getPatients();
			}
		}
		mDownloadTask = null;
	}

	@Override
	public void taskComplete(HashMap<String, Object> result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void progressUpdate(String message, int progress, int max) {
		// TODO Auto-generated method stub
	}

	@Override
	public void progressUpdate(String progress) {
		// TODO Auto-generated method stub
	}
}