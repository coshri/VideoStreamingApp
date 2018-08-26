
import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Iterator;
import java.util.Map;



    public class HttpTools {

        public static String GET(String url) throws Exception {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                return response.toString();
            } else {
                return "";
            }
        }

        public static String POST(String url, ContentValues params) throws IOException {

            //Convert Map<String,Object> into key=value&key=value pairs.
            StringBuilder postData = new StringBuilder();
            for (String param : params.keySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param, "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(params.get(param)), "UTF-8"));
            }

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            // For POST only - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(postData.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
            // For POST only - END

            int responseCode = con.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else return "";
        }


        public static ContentValues JsonToContentValues(JSONObject jsonObject) throws JSONException {
            ContentValues result = new ContentValues();
            Iterator<?> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = jsonObject.get(key);
                result.put(key, value.toString());
            }
            return result;
        }
    }

