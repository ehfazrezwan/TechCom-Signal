package org.thoughtcrime.securesms;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.thoughtcrime.securesms.components.ContactFilterToolbar;
import org.thoughtcrime.securesms.contacts.ContactsCursorLoader;
import org.thoughtcrime.securesms.logging.Log;
import org.thoughtcrime.securesms.util.DirectoryHelper;
import org.thoughtcrime.securesms.util.DynamicLanguage;
import org.thoughtcrime.securesms.util.DynamicNoActionBarTheme;
import org.thoughtcrime.securesms.util.DynamicTheme;
import org.thoughtcrime.securesms.util.TextSecurePreferences;
import org.thoughtcrime.securesms.util.ViewUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class TabbedFragmentContacts extends Fragment
                                    implements SwipeRefreshLayout.OnRefreshListener,
                                               ContactSelectionListFragment.OnContactSelectedListener
{
    private static final String TAG = TabbedFragmentContacts.class.getSimpleName();

    private final DynamicTheme dynamicTheme    = new DynamicNoActionBarTheme();
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    protected ContactSelectionListFragment contactsFragment;

    private ContactFilterToolbar toolbar;

    public TabbedFragmentContacts() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dynamicTheme.onCreate(getActivity());
        dynamicLanguage.onCreate(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (!getActivity().getIntent().hasExtra(ContactSelectionListFragment.DISPLAY_MODE)) {
            int displayMode = TextSecurePreferences.isSmsEnabled(getContext()) ? ContactsCursorLoader.DisplayMode.FLAG_ALL
                                : ContactsCursorLoader.DisplayMode.FLAG_PUSH | ContactsCursorLoader.DisplayMode.FLAG_GROUPS;
            getActivity().getIntent().putExtra(ContactSelectionListFragment.DISPLAY_MODE, displayMode);
        }
//        initializeToolbar();
//        initializeResources();
//        initializeSearch();

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tabbed_fragment_contacts, container, false);
        this.toolbar = view.findViewById(R.id.contactFragmentToolbar);
        System.out.println("Toolbar: " + this.toolbar);

        initializeToolbar();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeResources();
        initializeSearch();

    }

    @Override
    public void onResume() {
        super.onResume();
        dynamicTheme.onResume(getActivity());
        dynamicLanguage.onResume(getActivity());
    }

    protected ContactFilterToolbar getToolbar() {
        return toolbar;
    }

    private void initializeToolbar() {
//        this.toolbar = ViewUtil.findById(getActivity(), R.id.contactFragmentToolbar);
//
//        System.out.println( "Toolbar" + this.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(this.toolbar);
//
        assert ((AppCompatActivity)getActivity()).getSupportActionBar() != null;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(null);
    }

    private void initializeResources() {
        contactsFragment = (ContactSelectionListFragment) getChildFragmentManager().findFragmentById(R.id.contact_selection_list_fragment);
        contactsFragment.setOnContactSelectedListener(this);
        System.out.println("Init resources " + contactsFragment);
        contactsFragment.setOnRefreshListener(this);
    }

    private void initializeSearch() {
        toolbar.setOnFilterChangedListener(filter -> contactsFragment.setQueryFilter(filter));
    }

    @Override
    public void onRefresh() {
        new TabbedFragmentContacts.RefreshDirectoryTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getContext());
    }

    @Override
    public void onContactSelected(String number) {}

    @Override
    public void onContactDeselected(String number) {}

    private static class RefreshDirectoryTask extends AsyncTask<Context, Void, Void> {

        private final WeakReference<TabbedFragmentContacts> activity;

        private RefreshDirectoryTask(TabbedFragmentContacts activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Context... params) {

            try {
                DirectoryHelper.refreshDirectory(params[0], true);
            } catch (IOException e) {
                Log.w(TAG, e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            TabbedFragmentContacts activity = this.activity.get();

            if (activity != null && !activity.isRemoving()) {
                activity.toolbar.clear();
                activity.contactsFragment.resetQueryFilter();
            }
        }
    }


}
