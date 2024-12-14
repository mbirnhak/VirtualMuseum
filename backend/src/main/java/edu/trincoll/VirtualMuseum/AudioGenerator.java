package edu.trincoll.VirtualMuseum;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class AudioGenerator {
    private final String XI_API_KEY = System.getenv("XI_API_KEY"); // Your API key

    // Java record for VoiceSettings JSON data
    public record VoiceSettings(
            double stability,
            double similarityBoost,
            double style,
            boolean useSpeakerBoost
    ) {}

    // Java record for Payload JSON data
    public record Payload(
            String text,
            String modelId,
            VoiceSettings voiceSettings,
            Object pronunciationDictionaryLocators,
            Object seed,
            Object previousText,
            Object nextText,
            Object previousRequestIds,
            Object nextRequestIds,
            boolean usePvcAsIvc,
            String applyTextNormalization
    ) {}

    public void generateAndSaveAudio() {
        String textToSpeak = "testing the audio part of our project"; // Text to convert to speech
        String outputPath = "src/main/resources/output1.mp3"; // Path to save the output audio file

        try {
            // Send the text to the ElevenLabs API and get audio data
            byte[] audioData = generateAudio(textToSpeak);

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
     * @param textToSpeak The text to be converted to speech.
     * @return A byte array containing the audio data.
     * @throws IOException If an I/O error occurs.
     */
    public byte[] generateAudio(String textToSpeak) throws IOException {
        String VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17"; // Voice ID
        Payload payload = getPayload(textToSpeak);
        Gson gson = new Gson();
        String payloadJson = gson.toJson(payload);

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
                byte[] input = payloadJson.getBytes(StandardCharsets.UTF_8);
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

    private static Payload getPayload(String textToSpeak) {
        VoiceSettings voiceSettings = new VoiceSettings(0.5, 0.8, 0.0, true);
        // Construct the payload with all required fields
        return new Payload(textToSpeak,
                "eleven_multilingual_v2",
                voiceSettings,
                "[]",
                null,
                null,
                null,
                "[]",
                "[]",
                false,
                "auto"
        );
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