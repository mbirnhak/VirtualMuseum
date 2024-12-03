import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class VoiceLister {
    public static void main(String[] args) {
        String XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311";
        String urlString = "https://api.elevenlabs.io/v1/voices";

        try {
            // Use URI to create a URL object
            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("xi-api-key", XI_API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Message: " + connection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Log raw JSON response for debugging
                System.out.println("Raw JSON Response: " + response.toString());

                // Parse manually
                String json = response.toString();
                String[] voices = json.split("\\{\"name\":\"");
                for (int i = 1; i < voices.length; i++) {
                    String voice = voices[i];
                    String name = voice.split("\",")[0];
                    String voiceId = voice.split("\"voice_id\":\"")[1].split("\"")[0];
                    System.out.println(name + "; " + voiceId);
                }
            } else {
                System.out.println("Error: " + responseCode + " " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
