package sahilguptalive.com.androiddemos.contacts;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import sahilguptalive.com.androiddemos.R;

public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mContactAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Step-1 Create activity layout containing a list view. Also create a view for showing contact info.
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        //Step-2 Initialize adapter
        setupContactAdapter();
        //Step-3 Initialize list view
        setupListView(view);
        //Step-7 Initialize the loader
        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    //Step-2
    private void setupContactAdapter() {
        //Step-2a Initialize from column names, which specify the data we need to show on UI
        //, these must also be part of projection columns.
        final String[] FROM_COLUMNS = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY
                , ContactsContract.CommonDataKinds.Phone.NUMBER
                , ContactsContract.CommonDataKinds.Phone._ID
        };
        //Step-2b Initialize to view ids, which specify the view to which corresponding data to be mapped
        //Make sure that you specify view only in order of "from columns". Since "from columns" data would be mapped to views specified.
        //View can only be of type 'TextView' and number of view should be less than or equal to from columns.
        final int[] TO_VIEW_ID = {R.id.contact_view_name, R.id.contact_view_phone_num, R.id.contact_view_id};
        //Step-2c Initialize Contact adapter
        mContactAdapter
                = new SimpleCursorAdapter(getActivity(), R.layout.contact_view, null, FROM_COLUMNS, TO_VIEW_ID);
    }

    //Step-3
    private void setupListView(View rootView) {
        ListView contactListView = (ListView) rootView.findViewById(R.id.contacts_recycler_view);
        contactListView.setAdapter(mContactAdapter);
    }

    //Step-4
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Step-4a Initialize projection to include all column names which you want to include in the cursor response.
        //Projection array must include FROM_COLUMNS values, otherwise exception would be thrown.
        final String[] projection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        return new CursorLoader(getActivity()
                , ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                , projection, null, null, null);
    }

    //Step-5
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mContactAdapter.swapCursor(data);
    }

    //Step-6
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mContactAdapter.swapCursor(null);
    }
}
