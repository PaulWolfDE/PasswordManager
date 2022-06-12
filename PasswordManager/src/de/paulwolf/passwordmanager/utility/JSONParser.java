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

        InputStream is = new URL("http://paulwolf.de/crypto/passwordmanager/compatibility.json").openStream();
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

    public static void printJSON() {

        JSONObject root = new JSONObject();
        JSONObject jo1 = new JSONObject();

        JSONObject compatible = new JSONObject();
        compatible.put("compatible", true);
        JSONObject incompatible = new JSONObject();
        incompatible.put("compatible", false);

        jo1.put("1.3.2", compatible);
        jo1.put("1.3.3", compatible);
        jo1.put("1.3.4", compatible);
        jo1.put("1.3.5", compatible);
        jo1.put("1.3.6", compatible);
        jo1.put("1.3.7", compatible);
        jo1.put("1.3.8", compatible);
        jo1.put("1.3.9", compatible);
        jo1.put("1.4.0", compatible);
        jo1.put("1.4.1", compatible);
        jo1.put("1.4.2", compatible);
        jo1.put("1.4.3", compatible);
        root.put("1.4.3", jo1);

        System.out.println(root.toString(4));
    }
}
