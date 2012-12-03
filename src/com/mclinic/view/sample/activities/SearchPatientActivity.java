package com.mclinic.view.sample.activities;


public class SearchPatientActivity { //extends ListActivity implements DownloadListener {
//
//    private SearchPatientTask mSearchPatientTask;
//
//    private AlertDialog mFakeProgressDialog;
//    private AlertDialog mAlertDialog;
//
//    private static final int DOWNLOAD_PATIENT_DIALOG = 0;
//
//    private static final int DIALOG_ALERT = 0;
//
//    private static final int DIALOG_PATIENT_SELECT = 1;
//
//    public SearchPatientActivity mSearchPatientActivity = this;
//    private int mSelectedId = -1;
//
//    private ArrayList<HashMap<String, String>> mPatients;
//    private HashMap<String, Patient> mListPatients;
//    private HashMap<String, List<Observation>> mListObservation;
//    private ClinicAdapter mPatientAdapter;
//
//    private static final String KEY_PATIENTS = "patients";
//    private static final String KEY_SELECTED = "selected";
//
//    private boolean mAlertDialogShowing;
//    private int mAlertDialogType;
//    private String mAlertMessage;
//
//    private int mPosition;
//    private static final String KEY_ALERT_DIALOG_SHOWING = "alertDialogShowing";
//    private static final String KEY_ALERT_DIALOG_TYPE = "alertDialogType";
//    private static final String KEY_ALERT_MESSAGE = "alertMessage";
//    private static final String KEY_POSITION = "position";
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mPatients = (ArrayList<HashMap<String, String>>) savedInstanceState
//                .getSerializable(KEY_PATIENTS);
//        mSelectedId = savedInstanceState.getInt(KEY_SELECTED);
//        mAlertMessage = savedInstanceState.getString(KEY_ALERT_MESSAGE);
//        mAlertDialogType = savedInstanceState.getInt(KEY_ALERT_DIALOG_TYPE);
//        mAlertDialogShowing = savedInstanceState
//                .getBoolean(KEY_ALERT_DIALOG_SHOWING);
//        mPosition = savedInstanceState.getInt(KEY_POSITION);
//
//        if (mAlertDialogShowing) {
//            switch (mAlertDialogType) {
//                case DIALOG_ALERT:
//                    showAlertDialog(mAlertMessage);
//                    break;
//                case DIALOG_PATIENT_SELECT:
//                    showPatientSelectDialog();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//
//        super.onSaveInstanceState(outState);
//        outState.putSerializable(KEY_PATIENTS, mPatients);
//        outState.putInt(KEY_SELECTED, mSelectedId);
//        outState.putInt(KEY_ALERT_DIALOG_TYPE, mAlertDialogType);
//        outState.putBoolean(KEY_ALERT_DIALOG_SHOWING, mAlertDialogShowing);
//        outState.putString(KEY_ALERT_MESSAGE, mAlertMessage);
//        outState.putInt(KEY_POSITION, mPosition);
//
//    }
//
//    public void onCreate(Bundle icicle) {
//        super.onCreate(icicle);
//
//        setTitle(getString(R.string.app_name) + " > "
//                + getString(R.string.search_patient));
//
//        mSearchPatientTask = (SearchPatientTask) getLastNonConfigurationInstance();
//        if (mSearchPatientTask == null) {
//            Bundle extras = getIntent().getExtras();
//
//            if (extras != null) {
//                String searchText = extras.getString("searchText");
//                if (searchText.length() < 3) {
//                    showAlertDialog("Error: Enter three or more characters before searching.");
//                } else {
//                    //search here
//                    SearchPatient(searchText);
//                }
//            }
//        }
//        return;
//
//    }
//
//    private void SearchPatient(String searchText) {
//        try {
//            mSearchPatientTask = new SearchPatientTask();
//            mSearchPatientTask.setSearchPatientListener(mSearchPatientActivity);
//            //Determine whether to search by name or by identifier
//            if (stringHasDigit(searchText))
//                mSearchPatientTask.execute("", searchText);
//            else
//                mSearchPatientTask.execute(searchText, "");
//
//            showDialog(DOWNLOAD_PATIENT_DIALOG);
//        } catch (ActivityNotFoundException e) {
//            e.printStackTrace();
//            showAlertDialog("Search Error");
//        }
//    }
//
//    private static boolean stringHasDigit(String searchString) {
//        Pattern intsOnly = Pattern.compile("\\d+");
//        Matcher m = intsOnly.matcher(searchString);
//        return m.find();
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view;
//        TextView textView;
//
//        switch (id) {
//            case DOWNLOAD_PATIENT_DIALOG:
//
//                DialogInterface.OnClickListener downloadPatientListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dismissDialog(DOWNLOAD_PATIENT_DIALOG);
//                        mSearchPatientTask.setSearchPatientListener(null);
//                        mSearchPatientTask.cancel(true);
//                        mSearchPatientTask = null;
//                        finish();
//                    }
//                };
//
//                view = layoutInflater.inflate(R.layout.progress_dialog, null);
//                textView = (TextView) view.findViewById(R.id.message);
//                textView.setText("Searching");
//                builder.setView(view);
//                builder.setCancelable(false);
//                builder.setNegativeButton(getString(R.string.cancel),
//                        downloadPatientListener);
//
//                mFakeProgressDialog = builder.create();
//                return mFakeProgressDialog;
//        }
//        return null;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (mSearchPatientTask != null) {
//            mSearchPatientTask.setSearchPatientListener(null);
//            if (mSearchPatientTask.getStatus() == AsyncTask.Status.FINISHED) {
//                mSearchPatientTask.cancel(false);
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss();
//        }
//
//        if (mFakeProgressDialog != null) {
//            try {
//                dismissDialog(DOWNLOAD_PATIENT_DIALOG);
//            } catch (IllegalArgumentException e) {
//
//            }
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void taskComplete(HashMap<String, Object> results) {
//
//        try {
//            dismissDialog(DOWNLOAD_PATIENT_DIALOG);
//
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//
//        if (results.containsKey(Constants.TASK_ERROR)) {
//            showAlertDialog("Error: " + results.get(Constants.TASK_ERROR));
//        } else {
//            if (results.size() > 1) {
//                if (results.get(Constants.TASK_SUCCESS).equals(Constants.TASK_SUCCESS)) {
//                    mListPatients = (HashMap<String, Patient>) results.get(Constants.PATIENTS);
//                    mListObservation = (HashMap<String, List<Observation>>) results.get(Constants.OBSERVATIONS);
//
//                    results.remove(Constants.TASK_ERROR);
//                    results.remove(Constants.TASK_SUCCESS);
//                    results.remove(Constants.PATIENTS);
//                    results.remove(Constants.OBSERVATIONS);
//
//                    if (results.size() > 0) {
//                        mPatients = new ArrayList<HashMap<String, String>>();
//                        HashMap<String, String> sResults = getPatientMap(results);
//
//                        ImmutableSortedMap<String, String> sortedResults = ImmutableSortedMap
//                                .copyOf(sResults,
//                                        Ordering.natural().onResultOf(
//                                                Functions.forMap(sResults)));
//
//                        for (Map.Entry<String, String> entry : sortedResults
//                                .entrySet()) {
//                            HashMap<String, String> patient = new HashMap<String, String>();
//                            String[] s = entry.getValue().split("\\|");
//                            patient.put("r1", s[0]);
//                            patient.put("r2", s[1]);
//                            patient.put("patientId", entry.getKey());
//                            mPatients.add(patient);
//                        }
//
//                        showPatientSelectDialog();
//                    } else {
//                        showCustomToast("No patients Found");
//                        finish();
//                    }
//                } else
//                    Log.e("SearchPatientActivity", "This should never happen");
//            } else {
//                showCustomToast("No patients Found");
//                finish();
//            }
//        }
//    }
//
//    @SuppressWarnings("rawtypes")
//    private HashMap<String, String> getPatientMap(HashMap<String, Object> objMap) {
//        HashMap<String, String> stringMap = new HashMap<String, String>();
//        Iterator<String> it = objMap.keySet().iterator();
//        while (it.hasNext()) {
//            String key = it.next();
//            String val = (String) objMap.get(key);
//            stringMap.put(key, val);
//        }
//        return stringMap;
//    }
//
//
//    private void showPatientSelectDialog() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Select Patient");
//        builder.setPositiveButton(getString(R.string.download_selected),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        int selected = mAlertDialog.getListView()
//                                .getCheckedItemPosition();
//                        if (selected != AdapterView.INVALID_POSITION) {
//                            @SuppressWarnings("rawtypes")
//                            HashMap hm = (HashMap) mAlertDialog.getListView()
//                                    .getItemAtPosition(selected);
//
//                            String patientId;
//                            patientId = hm.get("patientId").toString();
//                            //you have patient here insert into db
//                            insertPatient(patientId);
//
//                        } else {
//                            mAlertDialog.cancel();
//                        }
//
//                        mAlertDialogShowing = false;
//                        mSelectedId = -1;
//                        finish();
//                    }
//                });
//        builder.setNegativeButton(getString(R.string.cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
//                    }
//                });
//
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, mPatients,
//                R.layout.checked_text_view, new String[]{"r1", "r2"},
//                new int[]{R.id.r1, R.id.r2});
//
//        builder.setSingleChoiceItems(simpleAdapter, mSelectedId,
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mSelectedId = which;
//
//                    }
//                });
//
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                mAlertDialogShowing = false;
//                mSelectedId = -1;
//            }
//        });
//        mAlertDialog = builder.create();
//
//        mAlertDialog.show();
//        mAlertDialogShowing = true;
//        mAlertDialogType = DIALOG_PATIENT_SELECT;
//    }
//
//    private void insertPatient(String strPatientId) {
//        Integer patientId;
//        try {
//            patientId = Integer.parseInt(strPatientId);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        mPatientAdapter = new ClinicAdapter();
//        mPatientAdapter.open();
//        Cursor c = mPatientAdapter.fetchPatient(patientId);
//
//        if (c != null && c.getCount() > 0) { //Patient already exists
//            showCustomToast("Patient already exists");
//        } else {
//            Patient pat = mListPatients.get(strPatientId);
//            if (pat != null) {
//                mPatientAdapter.createPatient(pat);
//                insertObservations(strPatientId);
//            }
//        }
//        c.close();
//        mPatientAdapter.close();
//    }
//
//    private void insertObservations(String strPatientId) {
//        List<Observation> lstObs = (List<Observation>) mListObservation.get(strPatientId);
//        if (lstObs != null && lstObs.size() > 0) {
//            for (Observation observation : lstObs) {
//                mPatientAdapter.createObservation(observation);
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (mAlertDialog != null && !mAlertDialog.isShowing()) {
//            if (mAlertDialogShowing) {
//                switch (mAlertDialogType) {
//                    case DIALOG_ALERT:
//                        showAlertDialog(mAlertMessage);
//                        break;
//                    case DIALOG_PATIENT_SELECT:
//                        showPatientSelectDialog();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//
//        if (mSearchPatientTask != null) {
//            mSearchPatientTask.setSearchPatientListener(this);
//            if (mSearchPatientTask.getStatus() == AsyncTask.Status.FINISHED) {
//                dismissDialog(DOWNLOAD_PATIENT_DIALOG);
//            } else if (mSearchPatientTask.getStatus() == AsyncTask.Status.RUNNING) {
//                showDialog(DOWNLOAD_PATIENT_DIALOG);
//            }
//        }
//
//        refreshListView();
//
//    }
//
//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        if (mSearchPatientTask != null
//                && mSearchPatientTask.getStatus() != AsyncTask.Status.FINISHED) {
//            return mSearchPatientTask;
//        }
//        return null;
//    }
//
//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//    }
//
//    private void refreshListView() {
//    }
//
//    private void showAlertDialog(String message) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.alert_dialog_message, null);
//        TextView tv = (TextView) view.findViewById(R.id.alertMessage);
//        tv.setText(message);
//        builder.setView(view);
//
//        builder.setCancelable(true).setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mAlertDialogShowing = false;
//                        finish();
//                    }
//                });
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                mAlertDialogShowing = false;
//
//            }
//        });
//        mAlertDialog = builder.create();
//
//        mAlertDialog.show();
//        mAlertDialogShowing = true;
//        mAlertMessage = message;
//        mAlertDialogType = DIALOG_ALERT;
//    }
//
//    @Override
//    public void taskComplete(String result) {
//    }
//
//    @Override
//    public void progressUpdate(String message, int progress, int max) {
//    }
//
//    @Override
//    public void progressUpdate(String progress) {
//    }
//
//    private void showCustomToast(String message) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.toast_view, null);
//
//        // set the text in the view
//        TextView tv = (TextView) view.findViewById(R.id.message);
//        tv.setText(message);
//
//        Toast t = new Toast(this);
//        t.setView(view);
//        t.setDuration(Toast.LENGTH_SHORT);
//        t.setGravity(Gravity.CENTER, 0, 0);
//        t.show();
//    }
}
