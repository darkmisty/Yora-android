package socialapp.com.socialapp.Activities;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import socialapp.com.socialapp.AppViews.MainNavDrawer;
import socialapp.com.socialapp.Fragments.ContactsFragment;
import socialapp.com.socialapp.Fragments.PendingContactRequestsFragment;
import socialapp.com.socialapp.R;

public class ContactsActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemSelectedListener {


    private ObjectAnimator currentAnimator;
    private ArrayAdapter<ContactsSpinnerItem> adapter;


    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_contacts);
        setNavDrawer(new MainNavDrawer(this));

        adapter = new ArrayAdapter<>(this, R.layout.list_item_toolbar_spinnner);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        adapter.add(new ContactsSpinnerItem("Contacts", getResources().getColor(R.color.colorPrimary), ContactsFragment.class));
        adapter.add(new ContactsSpinnerItem(
                "Pending Contact Request",
//                getResources().getColor(R.color.contacts_pending_contact_request),
                ContextCompat.getColor(this, R.color.contacts_pending_contact_request),
                PendingContactRequestsFragment.class));


        Spinner spinner = (Spinner) findViewById(R.id.activity_contacts_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        getSupportActionBar().setTitle(null);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ContactsSpinnerItem item = adapter.getItem(position);

        if (item == null)
            return;

        if (currentAnimator != null)
            currentAnimator.end();

        int currentColor = ((ColorDrawable) toolbar.getBackground()).getColor();

        currentAnimator = ObjectAnimator.ofObject(toolbar, "backgroundColor", new ArgbEvaluator(), currentColor, item.getColor())
                .setDuration(250);

        currentAnimator.start();

        Fragment fragment;
        try {
            fragment = (Fragment) item.getFragment().newInstance();
        } catch (Exception e) {
            Log.e("ContactActivity", "Could not instantiate fragment " + item.getFragment().getName(), e);
            return;
        }

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .replace(R.id.activity_contacts_container, fragment)
                .commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class ContactsSpinnerItem {
        private final String tittle;
        private final int color;
        private Class fragment;

        public ContactsSpinnerItem(String tittle, int color, Class fragment) {
            this.tittle = tittle;
            this.color = color;
            this.fragment = fragment;
        }

        public String getTittle() {
            return tittle;
        }

        public int getColor() {
            return color;
        }

        public Class getFragment() {
            return fragment;
        }

        public void setFragment(Class fragment) {
            this.fragment = fragment;
        }


        @Override
        public String toString() {
            String a = getTittle();
            return a;
        }
    }
}
