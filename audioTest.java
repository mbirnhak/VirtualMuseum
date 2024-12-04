import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class audioTest {

    // Constants for API details
    private static final String XI_API_KEY = "sk_8671dfe6a49e38639a819a5679e91b49635e07f9a0d76311";
    private static final String VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17";

    public static void main(String[] args) {
        String textToSpeak = "testing the audio part of our project"; // Text to convert to speech
        String outputPath = "output2.mp3"; // Path to save the output audio file

        try {
            // Send the text to the ElevenLabs API and get audio data
            byte[] audioData = sendTextToSpeechAPI(textToSpeak);

            // Save the audio data to a file
            saveAudioToFile(audioData, outputPath);

            System.out.println("Audio saved successfully.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Sends text to the ElevenLabs Text-to-Speech API and retrieves the audio data.
     *
     * @param text The text to be converted to speech.
     * @return A byte array containing the audio data.
     * @throws IOException If an I/O error occurs.
     */
    public static byte[] sendTextToSpeechAPI(String text) throws IOException {
        String payload = "{"
                + "\"text\": \"" + text + "\","
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

        String urlString = "https://api.elevenlabs.io/v1/text-to-speech/" + VOICE_ID + "/stream";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("xi-api-key", XI_API_KEY);

            // Send payload
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read audio data
                InputStream in = new BufferedInputStream(connection.getInputStream());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                return byteArrayOutputStream.toByteArray();
            } else {
                // Handle errors
                throw new IOException("Error response: " + responseCode + " " + connection.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            throw new IOException("Invalid URL: " + e.getMessage());
        }
    }

    /**
     * Saves audio data to a file.
     *
     * @param audioData  A byte array containing the audio data.
     * @param outputPath The path to save the output audio file.
     * @throws IOException If an I/O error occurs.
     */
    public static void saveAudioToFile(byte[] audioData, String outputPath) throws IOException {
        if (audioData == null || audioData.length == 0) {
            throw new IOException("No audio data received.");
        }

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(audioData);
        }
    }
}
