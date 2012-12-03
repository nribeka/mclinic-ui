package com.mclinic.view.sample.activities;


/**
 * @author Samuel Mbugua
 */
public class CreatePatientActivity { //extends ListActivity implements InstanceLoaderListener {

//    public static final String tag = "ListFormActivity";
//
//    // Request codes
//    public static final int DOWNLOAD_FORM = 1;
//    public static final int COLLECT_FORM = 2;
//
//    private static final int PROGRESS_DIALOG = 1;
//
//    private static final String SELECTED_FORM_ID_KEY = "selectedFormId";
//    private static final DateFormat COLLECT_INSTANCE_NAME_DATE_FORMAT = new SimpleDateFormat(
//            "yyyy-MM-dd_HH-mm-ss");
//
//    private String mPatientName;
//    private String mFamilyName;
//    private String mMiddleName;
//    private String mGivenName;
//    private static String mProviderId;
//
//    private ArrayAdapter<Form> mFormAdapter;
//    private ArrayList<Form> mForms = new ArrayList<Form>();
//
//    private Integer mSelectedFormId = null;
//    private EditText mPatientNameET;
//
//    private AlertDialog mAlertDialog;
//    private ProgressDialog mProgressDialog;
//    private static Element mFormNode = null;
//
//
//    private InstanceLoaderTask mInstanceLoaderTask;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(SELECTED_FORM_ID_KEY)) {
//                mSelectedFormId = savedInstanceState
//                        .getInt(SELECTED_FORM_ID_KEY);
//            }
//        }
//
//        setContentView(R.layout.create_patient);
//
//        if (!FileUtils.storageReady()) {
//            showCustomToast(getString(R.string.error_storage));
//            finish();
//        }
//
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        mProviderId = settings.getString(PreferencesActivity.KEY_PROVIDER, "0");
//
//        mPatientNameET = (EditText) findViewById(R.id.new_patient);
//
//        setTitle(getString(R.string.app_name) + " > " + getString(R.string.new_patient));
//
//        Object data = getLastNonConfigurationInstance();
//        if (data instanceof InstanceLoaderTask)
//            mInstanceLoaderTask = (InstanceLoaderTask) data;
//    }
//
//    private void getDownloadedForms() {
//
//        ClinicAdapter ca = new ClinicAdapter();
//
//        ca.open();
//        Cursor c = ca.fetchAllForms();
//
//        if (c != null && c.getCount() >= 0) {
//
//            mForms.clear();
//
//            int formIdIndex = c.getColumnIndex(ClinicAdapter.KEY_FORM_ID);
//            int nameIndex = c.getColumnIndex(ClinicAdapter.KEY_NAME);
//            int pathIndex = c.getColumnIndex(ClinicAdapter.KEY_PATH);
//
//            Form f;
//            if (c.getCount() > 0) {
//                do {
//                    if (!c.isNull(pathIndex)) {
//                        f = new Form();
//                        f.setFormId(c.getInt(formIdIndex));
//                        f.setPath(c.getString(pathIndex));
//                        f.setName(c.getString(nameIndex));
//                        mForms.add(f);
//                    }
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
//    }
//
//    private Form getForm(Integer formId) {
//        Form f = null;
//        ClinicAdapter ca = new ClinicAdapter();
//
//        ca.open();
//        Cursor c = ca.fetchForm(formId);
//
//        if (c != null && c.getCount() > 0) {
//            int nameIndex = c.getColumnIndex(ClinicAdapter.KEY_NAME);
//            int pathIndex = c.getColumnIndex(ClinicAdapter.KEY_PATH);
//
//            f = new Form();
//            f.setFormId(formId);
//            f.setPath(c.getString(pathIndex));
//            f.setName(c.getString(nameIndex));
//        }
//
//        if (c != null)
//            c.close();
//        ca.close();
//
//        return f;
//    }
//
//    private void refreshView() {
//
//        mFormAdapter = new ArrayAdapter<Form>(this,
//                android.R.layout.simple_list_item_1, mForms);
//        setListAdapter(mFormAdapter);
//    }
//
//    private int createFormInstance(String formPath, String jrFormId) {
//
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
//        instanceName = instanceName + "_new_"
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
//            insertValues.put("displayName", mPatientName + " (new)");
//            insertValues.put("instanceFilePath", instanceFilePath);
//            insertValues.put("jrFormId", jrFormId);
//            Uri insertResult = App.getApp().getContentResolver()
//                    .insert(InstanceColumns.CONTENT_URI, insertValues);
//
//            // insert to clinic
//            // Save form instance to db
//            FormInstance fi = new FormInstance();
//            fi.setPatientId(0);
//            fi.setFormId(Integer.parseInt(jrFormId));
//            fi.setPath(instanceFilePath);
//            fi.setStatus(ClinicAdapter.STATUS_UNSUBMITTED);
//
//            ClinicAdapter ca = new ClinicAdapter();
//            ca.open();
//            ca.createFormInstance(fi, mPatientName + " (new)");
//            ca.close();
//
//            return Integer.valueOf(insertResult.getLastPathSegment());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return -1;
//
//    }
//
//    private static void findFormNode(Element element) {
//
//        // loop through all the children of this element
//        for (int i = 0; i < element.getChildCount(); i++) {
//
//            Element childElement = element.getElement(i);
//            if (childElement != null) {
//
//                String childName = childElement.getName();
//                if (childName.equalsIgnoreCase("form"))
//                    mFormNode = childElement;
//
//                if (childElement.getChildCount() > 0)
//                    findFormNode(childElement);
//            }
//        }
//
//    }
//
//    private void traverseInstanceNodes(Element element) {
//
//        // loop through all the children of this element
//        for (int i = 0; i < element.getChildCount(); i++) {
//
//            Element childElement = element.getElement(i);
//            if (childElement != null) {
//
//                String childName = childElement.getName();
//
//                if (childName.equalsIgnoreCase("patient.family_name")) {
//                    childElement.clear();
//                    childElement.addChild(0, org.kxml2.kdom.Node.TEXT, mFamilyName);
//                }
//                if (childName.equalsIgnoreCase("patient.given_name")) {
//                    childElement.clear();
//                    childElement.addChild(0, org.kxml2.kdom.Node.TEXT, mGivenName);
//                }
//                if (childName.equalsIgnoreCase("patient.middle_name") && mMiddleName != null && mMiddleName.length() > 0) {
//                    childElement.clear();
//                    childElement.addChild(0, org.kxml2.kdom.Node.TEXT, mMiddleName);
//                }
//
//                // provider id
//                if (childName.equalsIgnoreCase("encounter.provider_id")) {
//                    childElement.clear();
//                    childElement.addChild(0, org.kxml2.kdom.Node.TEXT, mProviderId.toString());
//                }
//
//                // value node
//                if (childName.equalsIgnoreCase("date") || childName.equalsIgnoreCase("time") || childName.equalsIgnoreCase("value")) {
//                    // remove xsi:null
//                    childElement.clear();
//                }
//
//                if (childElement.getChildCount() > 0)
//                    traverseInstanceNodes(childElement);
//            }
//        }
//    }
//
//    private void launchFormEntry(String formPath, String instancePath) {
//
//        String jrFormId = null;
//        int formId = -1;
//        try {
//            Cursor mCursor = App.getApp().getContentResolver()
//                    .query(FormsColumns.CONTENT_URI, null, null, null, null);
//            mCursor.moveToPosition(-1);
//            while (mCursor.moveToNext()) {
//
//                int id = mCursor.getInt(mCursor
//                        .getColumnIndex(FormsColumns._ID));
//                String filePath = mCursor.getString(mCursor
//                        .getColumnIndex(FormsColumns.FORM_FILE_PATH));
//                jrFormId = mCursor.getString(mCursor
//                        .getColumnIndex(FormsColumns.JR_FORM_ID));
//
//                if (formPath.equalsIgnoreCase(filePath)) {
//                    formId = id;
//                    break;
//                }
//            }
//            if (mCursor != null)
//                mCursor.close();
//
//            if (formId != -1) {
//                Uri formUri = ContentUris.withAppendedId(
//                        FormsColumns.CONTENT_URI, formId);
//
//                int instanceId = createFormInstance(formPath, jrFormId);
//
//                if (instanceId != -1) {
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName(
//                            "org.odk.collect.android",
//                            "org.odk.collect.android.activities.FormEntryActivity"));
//                    intent.setAction(Intent.ACTION_EDIT);
//                    intent.setData(Uri.parse(InstanceColumns.CONTENT_URI + "/"
//                            + instanceId));
//
//                    startActivityForResult(intent, COLLECT_FORM);
//
//                } else
//                    startActivityForResult(new Intent(Intent.ACTION_EDIT, formUri), COLLECT_FORM);
//            } else
//                showCustomToast(getString(R.string.error_form_find));
//        } catch (ActivityNotFoundException e) {
//            showCustomToast(getString(R.string.error_odk_collect));
//        } catch (Exception e) {
//            showCustomToast(getString(R.string.error_initialize_odk_collect));
//        }
//    }
//
//    @Override
//    protected void onListItemClick(ListView listView, View view, int position,
//                                   long id) {
//        // Get selected form
//        Form f = (Form) getListAdapter().getItem(position);
//        mSelectedFormId = f.getFormId();
//
//        mPatientName = mPatientNameET.getText().toString();
//
//        if (mPatientName == null || mPatientName.length() < 2 || !mPatientName.contains(" "))
//            showCustomToast(getString(R.string.invalid_name));
//        else {
//
//            //split names and get new patient names
//            String[] patientNames = mPatientName.split(" ");
//            mGivenName = patientNames[0];
//            if (patientNames.length == 2)
//                mFamilyName = patientNames[1];
//            else if (patientNames.length == 3) {
//                mMiddleName = patientNames[1];
//                mFamilyName = patientNames[2];
//            } else {
//                showCustomToast(getString(R.string.invalid_name));
//                return;
//            }
//            launchFormEntry(f.getPath(), null);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        finish();
//        super.onActivityResult(requestCode, resultCode, intent);
//
//    }
//
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case PROGRESS_DIALOG:
//                mProgressDialog = new ProgressDialog(this);
//                DialogInterface.OnClickListener loadingButtonListener = new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        mInstanceLoaderTask.cancel(true);
//                    }
//                };
//                mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
//                mProgressDialog.setTitle("Loading Instance");
//                mProgressDialog.setMessage(getString(R.string.please_wait));
//                mProgressDialog.setIndeterminate(true);
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.setButton(getString(R.string.cancel),
//                        loadingButtonListener);
//                return mProgressDialog;
//        }
//        return null;
//    }
//
//    private void dismissDialogs() {
//        if (mAlertDialog != null && mAlertDialog.isShowing()) {
//            mAlertDialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        dismissDialogs();
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        if (mInstanceLoaderTask != null) {
//            mInstanceLoaderTask.setInstanceLoaderListener(this);
//        }
//
//        getDownloadedForms();
//
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        if (mInstanceLoaderTask != null) {
//            mInstanceLoaderTask.setInstanceLoaderListener(null);
//            if (mInstanceLoaderTask.getStatus() == AsyncTask.Status.FINISHED) {
//                // Allow saving to finish
//                mInstanceLoaderTask.cancel(false);
//            }
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public void loadingComplete(String result) {
//        dismissDialog(PROGRESS_DIALOG);
//        if (result != null && mSelectedFormId != null) {
//            Form f = getForm(mSelectedFormId);
//            String formPath = f.getPath();
//            String instancePath = result;
//            launchFormEntry(formPath, instancePath);
//        }
//    }
//
//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        if (mInstanceLoaderTask != null
//                && mInstanceLoaderTask.getStatus() != AsyncTask.Status.FINISHED)
//            return mInstanceLoaderTask;
//
//        return null;
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if (mSelectedFormId != null)
//            outState.putInt(SELECTED_FORM_ID_KEY, mSelectedFormId);
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
//        t.setDuration(Toast.LENGTH_LONG);
//        t.setGravity(Gravity.CENTER, 0, 0);
//        t.show();
//    }
}
