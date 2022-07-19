package de.paulwolf.passwordmanager.utility;

import de.paulwolf.passwordmanager.Main;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
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
            JSONObject clientVersion = (JSONObject) compatibility.get(Main.VERSION_NUMBER);
            JSONObject foundVersion = (JSONObject) clientVersion.get(databaseVersion);
            return (boolean) foundVersion.get("compatible");
        } catch (JSONException e) {
            return false; // Version unknown
        } catch (IOException e) {
            return false; // Connection problem
        }
    }

    public static boolean isUpToDate() {

        try {
            JSONObject json = getCompatibilityJSON();
            String newestVersion = (String) json.get("newestVersion");
            if (!Main.VERSION_NUMBER.equals(newestVersion))
                return false;
        } catch (JSONException e) {
            return true; // JSON problem
        } catch (IOException e) {
            return true; // Connection problem
        }
        return true;
    }
}
