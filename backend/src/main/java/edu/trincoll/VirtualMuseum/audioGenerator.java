package edu.trincoll.VirtualMuseum;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;

public class audioGenerator {

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

    public static void main(String[] args) {
        String XI_API_KEY = System.getenv("XI_API_KEY"); // Your API key
        String VOICE_ID = "CwhRBWXzGAHq8TQ4Fs17"; // Voice ID
        String TEXT_TO_SPEAK = "testing the audio part of our project"; // Text to convert to speech
        String OUTPUT_PATH = "src/main/resources/output1.mp3"; // Path to save the output audio file

        VoiceSettings voiceSettings = new VoiceSettings(0.5, 0.8, 0.0, true);
        // Construct the payload with all required fields
        Payload payload = new Payload(TEXT_TO_SPEAK,
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
        Gson gson = new Gson();
        String payloadJson = gson.toJson(payload);

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
                byte[] input = payloadJson.getBytes(StandardCharsets.UTF_8);
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
                } catch(Error e) {
                    System.out.println("Error outputting stream to file: " + e);
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
