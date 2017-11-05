package hitch_frontend.hitch.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import hitch_frontend.hitch.R;

/**
 * Dialog where user can create a new account.
 */
public class CreateAccountDialog extends DialogFragment {

    // keys used for storing data in bundle
    private static final String USERNAME_KEY = "USERNAME", FIRSTNAME_KEY = "FIRSTNAME",
        LASTNAME_KEY = "LASTNAME", PASSWORD_KEY = "PASSWORD";

    private EditText userNameEntry, firstNameEntry, lastNameEntry, passwordEntry;

    // interface for receiving dialog callbacks
    public interface CreateAccountDialogListener {
        void onConfirm(CreateAccountDialog dialogFragment, String userName, String firstName,
                       String lastName, String password);
        void onCancel(CreateAccountDialog dialogFragment);
    }

    // listener that receives callbacks
    private CreateAccountDialogListener mListener;

    // returns a new instance of the fragment with bundle that has the values given by toEdit.
    // Will populate the dialog with data from toEdit.
    public static CreateAccountDialog newInstance() {
        return new CreateAccountDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request window without title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override // ensures activity implements LogDialogListener
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CreateAccountDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CreateAccountDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_account, container);
    }

    // called when user clicks button to confirm. Calls mListener's onConfirm with user entries.
    // expects listener to validate and potentially close the dialog.
    public void onConfirmed(View view) {
        mListener.onConfirm(this,
                userNameEntry.getText().toString(),
                firstNameEntry.getText().toString(),
                lastNameEntry.getText().toString(),
                passwordEntry.getText().toString());
    }
}
