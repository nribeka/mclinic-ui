package com.mclinic.view.sample.activities;


/**
 * @author Samuel Mbugua
 */
public class InstanceListActivity { //extends ListActivity {
//
//    private static final String BUNDLE_SELECTED_ITEMS_KEY = "selected_items";
//    private static final String BUNDLE_TOGGLED_KEY = "toggled";
//
//    private static final int INSTANCE_UPLOADER = 0;
//    private static final int FLOAT_MENU_EDIT_INSTANCE = 654321;
//
//    private Button mActionButton;
//    private Button mToggleButton;
//
//    private ClinicAdapter mCla;
//    private SimpleCursorAdapter mInstances;
//    private ArrayList<Long> mSelected = new ArrayList<Long>();
//    private boolean mRestored = false;
//    private boolean mToggled = false;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.filled_forms_list);
//
//        mActionButton = (Button) findViewById(R.id.upload_button);
//        mActionButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                if (mSelected.size() > 0) {
//                    // items selected
//                    uploadSelectedFiles();
//                    refreshData();
//                    mToggled = false;
//                } else {
//                    // no items selected
//                    Toast.makeText(getApplicationContext(), getString(R.string.no_select_error),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });
//
//        mToggleButton = (Button) findViewById(R.id.toggle_button);
//        mToggleButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // toggle selections of items to all or none
//                ListView ls = getListView();
//                mToggled = !mToggled;
//                // remove all items from selected list
//                mSelected.clear();
//                for (int pos = 0; pos < ls.getCount(); pos++) {
//                    ls.setItemChecked(pos, mToggled);
//                    // add all items if mToggled sets to select all
//                    if (mToggled)
//                        mSelected.add(ls.getItemIdAtPosition(pos));
//                }
//                mActionButton.setEnabled(!(mSelected.size() == 0));
//            }
//        });
//    }
//
//    /**
//     * Retrieves instance information from {@link FileDbAdapter}, composes and displays each row.
//     */
//    private void refreshView() {
//        if (mCla == null) {
//            mCla = new ClinicAdapter();
//            mCla.open();
//        }
//
//        //delete all orphan instances
//        mCla.removeOrphanInstances(getApplicationContext());
//        // get all un-submitted instances
//        Cursor c = mCla.fetchFormInstancesByStatus(ClinicAdapter.STATUS_UNSUBMITTED);
//        startManagingCursor(c);
//
//        String[] data = new String[]{
//                ClinicAdapter.KEY_FORMINSTANCE_DISPLAY, ClinicAdapter.KEY_FORMINSTANCE_STATUS
//        };
//        int[] view = new int[]{
//                R.id.text1, R.id.text2
//        };
//
//        // render total instance view
//        mInstances =
//                new SimpleCursorAdapter(this, R.layout.two_item_multiple_choice, c, data, view);
//        setListAdapter(mInstances);
//        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        getListView().setItemsCanFocus(false);
//
//        //register list adapter for context menu
//        registerForContextMenu(getListView());
//        mActionButton.setEnabled(!(mSelected.size() == 0));
//
//        // set title
//        setTitle(getString(R.string.app_name) + " > " + getString(R.string.filled_forms));
//
//        // if current activity is being reinitialized due to changing orientation restore all check
//        // marks for ones selected
//        if (mRestored) {
//            ListView ls = getListView();
//            for (long id : mSelected) {
//                for (int pos = 0; pos < ls.getCount(); pos++) {
//                    if (id == ls.getItemIdAtPosition(pos)) {
//                        ls.setItemChecked(pos, true);
//                        break;
//                    }
//                }
//            }
//            mRestored = false;
//        }
//    }
//
//    private void uploadSelectedFiles() {
//        ArrayList<String> selectedInstances = new ArrayList<String>();
//
//        Cursor c = null;
//
//        for (int i = 0; i < mSelected.size(); i++) {
//            c = mCla.fetchFormInstance(mSelected.get(i));
//            startManagingCursor(c);
//            String s = c.getString(c.getColumnIndex(ClinicAdapter.KEY_PATH));
//            selectedInstances.add(s);
//        }
//
//        // bundle intent with upload files
//        Intent i = new Intent(this, InstanceUploaderActivity.class);
//        i.putExtra(ClinicAdapter.KEY_INSTANCES, selectedInstances);
//        startActivityForResult(i, INSTANCE_UPLOADER);
//    }
//
//    private void refreshData() {
//        if (!mRestored) {
//            mSelected.clear();
//        }
//        refreshView();
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(0, FLOAT_MENU_EDIT_INSTANCE, 0, getString(R.string.edit_form));
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
//        Cursor c = (Cursor) getListAdapter().getItem(info.position);
//
//        // register into content provider
//        ContentValues insertValues = new ContentValues();
//        insertValues.put("displayName", c.getString(c.getColumnIndex(ClinicAdapter.KEY_FORMINSTANCE_DISPLAY)));
//        insertValues.put("instanceFilePath", c.getString(c.getColumnIndex(ClinicAdapter.KEY_PATH)));
//        String jrFormId = c.getString(c.getColumnIndex(ClinicAdapter.KEY_FORM_ID));
//        insertValues.put("jrFormId", jrFormId);
//        Uri insertResult = App.getApp().getContentResolver()
//                .insert(InstanceColumns.CONTENT_URI, insertValues);
//
//        launchFormEntry(Integer.valueOf(insertResult.getLastPathSegment()), jrFormId);
//
//        c.close();
//
//        return super.onContextItemSelected(item);
//    }
//
//    private void launchFormEntry(int instanceId, String strJrFormId) {
//
//        String jrFormId = null;
//        int formId = -1;
//        try {
//            Cursor mCursor = App.getApp().getContentResolver()
//                    .query(FormsColumns.CONTENT_URI, null, null, null, null);
//            mCursor.moveToPosition(-1);
//            while (mCursor.moveToNext()) {
//
//                int id = mCursor.getInt(mCursor.getColumnIndex(FormsColumns._ID));
//                jrFormId = mCursor.getString(mCursor.getColumnIndex(FormsColumns.JR_FORM_ID));
//
//                if (strJrFormId.equalsIgnoreCase(jrFormId)) {
//                    formId = id;
//                    break;
//                }
//            }
//            if (mCursor != null)
//                mCursor.close();
//
//            if (formId != -1) {
//                Uri formUri = ContentUris.withAppendedId(FormsColumns.CONTENT_URI, formId);
//
//                if (instanceId != -1) {
//                    Intent intent = new Intent();
//                    intent.setComponent(new ComponentName(
//                            "org.odk.collect.android",
//                            "org.odk.collect.android.activities.FormEntryActivity"));
//                    intent.setAction(Intent.ACTION_EDIT);
//                    intent.putExtra("newform", false);
//                    intent.setData(Uri.parse(InstanceColumns.CONTENT_URI + "/" + instanceId));
//                    startActivity(intent);
//                } else {
//                    startActivity(new Intent(Intent.ACTION_EDIT, formUri));
//                }
//            } else {
//                Toast t = Toast.makeText(getApplicationContext(), getString(R.string.error_form_find), Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//                t.show();
//            }
//
//        } catch (ActivityNotFoundException e) {
//            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.error_odk_collect), Toast.LENGTH_LONG);
//            t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//            t.show();
//        }
//    }
//
//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//        // get row id from db
//        Cursor c = (Cursor) getListAdapter().getItem(position);
//        long k = c.getLong(c.getColumnIndex(ClinicAdapter.KEY_ID));
//
//        // add/remove from selected list
//        if (mSelected.contains(k))
//            mSelected.remove(k);
//        else
//            mSelected.add(k);
//
//        mActionButton.setEnabled(!(mSelected.size() == 0));
//    }
//
//    @Override
//    protected void onDestroy() {
//        try {
//            if (mCla != null) {
//                mCla.close();
//                mCla = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            super.onDestroy();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        refreshData();
//        super.onResume();
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        long[] selectedArray = savedInstanceState.getLongArray(BUNDLE_SELECTED_ITEMS_KEY);
//        for (int i = 0; i < selectedArray.length; i++)
//            mSelected.add(selectedArray[i]);
//        mToggled = savedInstanceState.getBoolean(BUNDLE_TOGGLED_KEY);
//        mRestored = true;
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        long[] selectedArray = new long[mSelected.size()];
//        for (int i = 0; i < mSelected.size(); i++)
//            selectedArray[i] = mSelected.get(i);
//        outState.putLongArray(BUNDLE_SELECTED_ITEMS_KEY, selectedArray);
//        outState.putBoolean(BUNDLE_TOGGLED_KEY, mToggled);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (resultCode == RESULT_CANCELED)
//            return;
//        switch (requestCode) {
//            // returns with a form path, start entry
//            case INSTANCE_UPLOADER:
//                if (intent.getBooleanExtra(ClinicAdapter.KEY_INSTANCES, false)) {
//                    refreshData();
//                    if (mInstances.isEmpty())
//                        finish();
//                }
//                break;
//            default:
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, intent);
//    }
}