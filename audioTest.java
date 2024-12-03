import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class audioTest {
    public static void main(String[] args) {
        String XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311"; // Your API key
        String VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17"; // Voice ID
        String TEXT_TO_SPEAK = "testing the audio part of our project"; // Text to convert to speech
        String OUTPUT_PATH = "output1.mp3"; // Path to save the output audio file

        // Construct the payload with all required fields
        String payload = "{"
            + "\"text\": \"" + TEXT_TO_SPEAK + "\","
            + "\"model_id\": \"eleven_multilingual_v2\","
            + "\"voice_settings\": {"
            + "  \"stability\": 0.5,"
            + "  \"similarity_boost\": 0.8,"
            + "  \"style\": 0.0,"
            + "  \"use_speaker_boost\": true"
            + "},"
            + "\"pronunciation_dictionary_locators\": [],"
            + "\"seed\": null,"
            + "\"previous_text\": null,"
            + "\"next_text\": null,"
            + "\"previous_request_ids\": [],"
            + "\"next_request_ids\": [],"
            + "\"use_pvc_as_ivc\": false,"
            + "\"apply_text_normalization\": \"auto\""
            + "}";

        // Construct the URI and handle possible URISyntaxException
        try {
            String urlString = "https://api.elevenlabs.io/v1/text-to-speech/" + VOICE_ID + "/stream";
            URI uri = new URI(urlString);  // Convert string to URI
            URL url = uri.toURL();  // Convert URI to URL

            // Create a connection and set properties
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("xi-api-key", XI_API_KEY);

            // Send the payload
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // If response is OK, read and save the audio
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                
                // Check if any audio data is received
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                byte[] audioData = byteArrayOutputStream.toByteArray();

                // Check the length of the received data
                if (audioData.length == 0) {
                    System.out.println("No audio data received.");
                    return;
                }

                // Save the audio to the output file
                try (FileOutputStream fos = new FileOutputStream(OUTPUT_PATH)) {
                    fos.write(audioData);
                }

                System.out.println("Audio saved successfully.");
            } else {
                // Read and print the error response
                InputStream errorStream = connection.getErrorStream();
                if (errorStream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                        String line;
                        StringBuilder errorResponse = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            errorResponse.append(line);
                        }
                        System.out.println("Error Response: " + errorResponse.toString());
                    }
                } else {
                    System.out.println("Error response: " + connection.getResponseMessage());
                }
            }
        } catch (URISyntaxException e) {
            System.out.println("Invalid URI syntax: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error occurred: " + e.getMessage());
        }
    }
}
