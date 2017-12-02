package fr.sea_race.client.searace.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by cyrille on 02/12/17.
 */

public class ApiRequest {

    public static String baseUrl = "https://sea-race.fr";
    private static String apiPrefix = "/api";
    public static String token = "";

    public static void setToken(String authToken) {
        token = authToken;
    }

    public static URL buildUrl(String route, Map<String, String> query) throws MalformedURLException {
        String urlStr = baseUrl + apiPrefix + (route.charAt(0) == '/' ? "" : "/") + route;

        // Query params replacement
        Pattern pattern = Pattern.compile(":([^/]*)");
        StringBuffer output = new StringBuffer();
        Matcher matcher = pattern.matcher(urlStr);
        while (matcher.find()) {
            String key = matcher.group(1);
            if (query.containsKey((key))) {
                matcher.appendReplacement(output, query.get(key));
            } else {
                matcher.appendReplacement(output, "{" + key + "}");
            }
        }
        matcher.appendTail(output);

        return  new URL(output.toString());
    }

    private static JSONObject getResponse(HttpsURLConnection conn) throws BadRequestException {
        int responseCode = 0;
        try {
            responseCode = conn.getResponseCode();

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
                return new JSONObject(response.toString());
            } else {
                throw new BadRequestException(responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException(-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject get(String route, Map<String, String> query) throws BadRequestException, IOException {

        URL url = buildUrl(route, query);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Set method and headers
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", token);

        return getResponse(conn);

    }

    public static JSONObject bodyQuery(String route, Map<String, String> query, JSONObject data, String verb) throws BadRequestException, IOException {
        URL url = buildUrl(route, query);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Set method and headers
        conn.setRequestMethod(verb);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", token);

        // Set body data
        conn.setDoOutput(true);
        OutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(data.toString().getBytes());
        wr.flush();
        wr.close();

        return getResponse(conn);
    }

    public static JSONObject post(String route, Map<String, String> query, JSONObject data) throws BadRequestException, IOException {
        return bodyQuery(route, query, data, "POST");
    }

    public static JSONObject put(String route, Map<String, String> query, JSONObject data) throws BadRequestException, IOException {
        return bodyQuery(route, query, data, "PUT");
    }

    public static JSONObject patch(String route, Map<String, String> query, JSONObject data) throws BadRequestException, IOException {
        return bodyQuery(route, query, data, "PATCH");
    }

    public static void delete(String route, Map<String, String> query) throws BadRequestException, IOException {

        URL url = buildUrl(route, query);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        // Set method and headers
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", token);

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new BadRequestException(responseCode);
        }


    }
}
