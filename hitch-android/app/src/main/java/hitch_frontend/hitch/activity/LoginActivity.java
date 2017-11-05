package hitch_frontend.hitch.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hitch_frontend.hitch.R;
import hitch_frontend.hitch.fragment.CreateAccountDialog;

/**
 * Handles user log-in. User credentials would be stored in PREFERENCES_FILE_KEY once user has
 * already logged in. This allows us to attempt to validate the user on apps start. This will send
 * us to the scheduling activity directly.
 */

public class LoginActivity extends Activity implements CreateAccountDialog.CreateAccountDialogListener {

    // file key where username, password is stored
    private static final String PREFERENCES_FILE_KEY = "com.hitch-frontend.PASSWORD_KEY";
    // keys under which username, password are stored in preferences
    private static final String USERNAME_KEY = "USERNAME", PASSWORD_KEY = "PASSWORD";

    // handle to SharedPreferences. MUST BE INITIALIZED USING init()!
    private static SharedPreferences userData;

    private EditText usernameEntry, passwordEntry;
    private Button passwordBtn, createAccountBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // attempt to log user in from credentials stored in SharedPreferences
        Log.d("LoginActivity", "Retrieving Credentials");
        String[] credentials = retrieveCredentials();
//        String[] credentials = new String[0];
        if (credentials.length == 2 && validateLogin(credentials[0], credentials[1])) {
            // user validated from saved credentials
            launchScheduler();
        } else {
            // set-up login screen
            setContentView(R.layout.login_layout);

            // init stuff from view
            usernameEntry = (EditText) findViewById(R.id.username_entry);
            passwordEntry = (EditText) findViewById(R.id.password_entry);
            passwordBtn = (Button) findViewById(R.id.password_submit);
            createAccountBtn = (Button) findViewById(R.id.create_account_btn);
        }
    }

    // attempts to retrieve username, password from SharedPreferences.
    // returns empty String[] if not found
    public String[] retrieveCredentials() {
        SharedPreferences data = getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE);

        String user_name = data.contains(USERNAME_KEY) ? data.getString(USERNAME_KEY, "") : "";
        String password = data.contains(PASSWORD_KEY) ? data.getString(PASSWORD_KEY, "") : "";

        Log.d("LoginActivity", "Retrieving Credentials " + user_name + ", " + password);

        // return 2-element array of username, password if keys contained in context
        if (user_name.isEmpty() && password.isEmpty()) {
            return new String[0];
        } else {
            return new String[] {
                    data.getString(USERNAME_KEY, ""),
                    data.getString(PASSWORD_KEY, "")
            };
        }
    }

    // writes username, password to SharedPreferences
    public void writeCredentials(String username, String password) {
        Log.d("LoginActivity", "Writing Credentials " + username + ", " + password);
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(USERNAME_KEY, username);
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
    }

    // attempts to validate the given username and password, returns result
    public boolean validateLogin(String username, String password) {
        Log.d("LoginActivity", "Validating Login (" + username + ", " + password + ")");
        return true;
    }

    // handle user pressing button to submit password
    public void onPasswordSubmitted(View view) {
        String username = usernameEntry.getText().toString();
        String password = passwordEntry.getText().toString();
        Log.d("LoginActivity", "onPasswordSubmitted (" + username + ", " + password + ")");
        // try to validate user-entered name and password
        if (validateLogin(username, password)) {
            Log.d("LoginActivity", "Validated Login");
            // save credentials to SharedPreferences
            writeCredentials(usernameEntry.getText().toString(), passwordEntry.getText().toString());
            launchScheduler();
        } else {
            Log.d("LoginActivity", "Invalid Username/Password");
        }
    }

    // handle user pressing button to launch CreateDialog
    public void onCreateDialog(View view) {
        Log.d("LoginActivity", "onCreateDialog()");
        launchCreateDialog();
    }

    // launches create an account dialog
    public void launchCreateDialog() {
        Log.d("LoginActivity", "Launching Create Dialog");
        CreateAccountDialog dialog = CreateAccountDialog.newInstance();
        dialog.show(getFragmentManager(), "Create an Account");
    }

    // launches scheduling activity
    public void launchScheduler() {
        Log.d("LoginActivity", "Launching Scheduler");
        startActivity(new Intent(this, SchedulerActivity.class));
    }

    @Override
    public void onConfirm(CreateAccountDialog dialogFragment, String userName, String firstName, String lastName, String password) {
        Log.d("LoginActivity", "Confirmed " + userName + "," + firstName + "," + lastName + "," + password);
    }

    @Override
    public void onCancel(CreateAccountDialog dialogFragment) {
        Log.d("LoginActivity", "Cancel Requested");
    }
}
