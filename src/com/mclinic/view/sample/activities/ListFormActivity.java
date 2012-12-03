package com.mclinic.view.sample.activities;


public class ListFormActivity { //extends ListActivity implements InstanceLoaderListener {

//    public static final String tag = "ListFormActivity";
//
//    // Request codes
//    public static final int DOWNLOAD_FORM = 1;
//    public static final int COLLECT_FORM = 2;
//    private static final int FL_MENU_DELETE_FORM = 6543210;
//
//    private static final int PROGRESS_DIALOG = 1;
//
//    private Button mActionButton;
//
//    private ArrayAdapter<Form> mFormAdapter;
//    private ArrayList<Form> mForms = new ArrayList<Form>();
//
//    private AlertDialog mAlertDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.form_list);
//
//        if (!FileUtils.storageReady()) {
//            showCustomToast(getString(R.string.error_storage));
//            finish();
//        }
//
//        setTitle(getString(R.string.app_name) + " > " + getString(R.string.openmrs_forms));
//
//        mActionButton = (Button) findViewById(R.id.refresh_forms);
//        mActionButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent id = new Intent(getApplicationContext(), DownloadFormActivity.class);
//                startActivityForResult(id, DOWNLOAD_FORM);
//            }
//        });
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
//    private void refreshView() {
//
//        mFormAdapter = new ArrayAdapter<Form>(this,
//                android.R.layout.simple_list_item_1, mForms);
//        setListAdapter(mFormAdapter);
//
//        //register list adapter for context menu
//        registerForContextMenu(getListView());
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, FL_MENU_DELETE_FORM, 0, getString(R.string.delete_form));
//        menu.setHeaderTitle(getString(R.string.action_menu));
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info;
//        try {
//            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        } catch (ClassCastException e) {
//            e.printStackTrace();
//            return false;
//        }
//        Form f = (Form) getListAdapter().getItem(info.position);
//        if (f != null) {
//            ClinicAdapter ca = new ClinicAdapter();
//            ca.open();
//            if (ca.deleteForm(f.getFormId())) {
//                FileUtils.deleteFile(f.getPath());
//                getDownloadedForms();
//                showCustomToast(getString(R.string.delete_form_successful, f.getName()));
//            } else
//                showCustomToast(getString(R.string.error_delete_form));
//            ca.close();
//        }
//        return super.onContextItemSelected(item);
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
//        getDownloadedForms();
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void loadingComplete(String result) {
//        dismissDialog(PROGRESS_DIALOG);
//    }
//
//    @Override
//    public Object onRetainNonConfigurationInstance() {
//        return null;
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
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
