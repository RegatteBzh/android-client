package fr.sea_race.client.searace.login;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import fr.sea_race.client.searace.main.MainActivity;
import fr.sea_race.client.searace.R;


public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    private Context currentContext;
    private GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        String googleClientId = getString(R.string.default_web_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(googleClientId)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton googleConnect = (SignInButton)findViewById(R.id.login_google);
        googleConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login_google:
                        signIn();
                        break;
                    // ...
                }
            }
        });

        Button facebookConnect = (Button)findViewById(R.id.login_facebook);
        facebookConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        currentContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        account = GoogleSignIn.getLastSignedInAccount(this);
        //GetGoogleSession(account);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("Token", "Clear session token");
        ServerCheckout.clearToken();
        if (account == null) {
            account = GoogleSignIn.getLastSignedInAccount(this);
        }
        GetGoogleSession(account);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            GetGoogleSession(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login", "signInResult:failed code=" + e.getStatusCode());
            GetGoogleSession(null);
        }
    }

    private void GetGoogleSession(GoogleSignInAccount account) {
        if (account != null && account.getIdToken() != null && !account.getIdToken().isEmpty()) {
            Log.i("Login",  account.getIdToken());

            // perform a post on /auth/google/checkout
            (new AsyncTask<GoogleSignInAccount, Void, Void> () {

                @Override
                protected Void doInBackground(GoogleSignInAccount... account) {
                    if (account.length == 1) {
                        ServerCheckout checkout = new ServerCheckout(account[0]);
                        Log.i("Server token", ServerCheckout.getToken());

                        if (!ServerCheckout.getToken().isEmpty()) {
                            Intent intent = new Intent(currentContext, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                    return null;
                }


            }).execute(account);

        } else {
            if (account == null) {
                Toast.makeText(this, getString(R.string.login_fail),
                    Toast.LENGTH_LONG).show();
                Log.wtf("Login", "Account is null");
            } else {
                Toast.makeText(this, getString(R.string.login_missing_token),
                        Toast.LENGTH_LONG).show();
                Log.wtf("Login", "token: " + account.getIdToken());
            }
        }
    }

}
