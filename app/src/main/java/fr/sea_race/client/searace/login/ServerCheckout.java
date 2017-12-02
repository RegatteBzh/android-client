package fr.sea_race.client.searace.login;

import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cyrille on 02/12/17.
 */

public class ServerCheckout {

    private String baseUrl = "https://sea-race.fr";
    private String googleAuthCheckoutUrl = "/auth/google/checkout";
    private GoogleSignInAccount googleAccount;
    private static String serverAuthToken = "";

    public ServerCheckout (GoogleSignInAccount account) {
        googleAccount = account;
        SendGoogleCheckout();
    }

    public static String getToken() {
        return serverAuthToken;
    }

    public static void clearToken() {
        serverAuthToken = "";
    }

    private String createGooglePostData(String jwtToken) {
        JSONObject result = new JSONObject();
        try {
            result.put("idToken", jwtToken);
        } catch (JSONException e) {
                e.printStackTrace();
        }
        return result.toString();
    }

    private void SendGoogleCheckout () {
        String postData = createGooglePostData(googleAccount.getIdToken());
        Log.i("POST data", postData);
        try {

            Log.i("HTTP", "Opening connexion to " + baseUrl + googleAuthCheckoutUrl);
            URL url = new URL(baseUrl + googleAuthCheckoutUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // Set method and headers
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            Log.i("HTTP","Prepare body");
            byte[] post = postData.getBytes("UTF-8");

            Log.i("HTTP",postData);

            // Set body data
            conn.setDoOutput(true);
            OutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(post);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();

            // Read response
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            if (responseCode == 200 && !response.toString().isEmpty()) {
                JSONObject jsonObj = new JSONObject(response.toString());

                serverAuthToken = jsonObj.getString("token");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
