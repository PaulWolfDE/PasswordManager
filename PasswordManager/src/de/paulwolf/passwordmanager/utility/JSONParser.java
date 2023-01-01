package de.paulwolf.passwordmanager.utility;

import de.paulwolf.passwordmanager.Configuration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONParser {

    private static String getAll(Reader r) throws IOException {

        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = r.read()) != -1)
            sb.append((char) cp);
        return sb.toString();
    }

    public static JSONObject getCompatibilityJSON() throws IOException, JSONException {

        InputStream is = new URL("https://paulwolf.de/crypto/passwordmanager/compatibility.json").openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String raw = getAll(br);
        is.close();
        return new JSONObject(raw);
    }

    public static boolean checkRemoteCompatibility(String databaseVersion) {

        try {
            JSONObject compatibility = getCompatibilityJSON();
            JSONObject clientVersion = (JSONObject) compatibility.get(Configuration.VERSION_NUMBER);
            JSONObject foundVersion = (JSONObject) clientVersion.get(databaseVersion);
            return (boolean) foundVersion.get("compatible");
        } catch (JSONException e) {
            return false; // Version unknown
        } catch (IOException e) {
            return false; // Connection problem
        }
    }

    public static boolean isUpToDate() {


        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://api.github.com/repos/PaulWolfDE/PasswordManager/releases/latest");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept", "application/vnd.github+json");
            connection.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");
            connection.setUseCaches(false);

            // When status code isn't OK, compatibility file is being checked
            if (connection.getResponseCode() != 200)
                throw new IOException();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder rawResponse = new StringBuilder();

            while ((line = reader.readLine()) != null)
                rawResponse.append(line);
            reader.close();

            JSONObject response = new JSONObject(rawResponse.toString());

            return response.getString("tag_name").equals(Configuration.VERSION_NUMBER);

        } catch (MalformedURLException ignored) {
        } catch (IOException e) {
            // If connection to GitHub API fails, compatibility file is being checked
            try {
                JSONObject json = getCompatibilityJSON();
                String newestVersion = (String) json.get("newestVersion");
                if (!Configuration.VERSION_NUMBER.equals(newestVersion))
                    return false;
            } catch (Exception e2) {
                return true; // JSON or connection problem
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return true;
    }
}
