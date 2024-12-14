package edu.trincoll.VirtualMuseum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * This Rest Controller gathers all the functions in the 3 different
 * service classes (image, audio, and text services) and exposes them
 * to allow the frontend to call them.
 */
@RestController
public class MuseumController {
    private final DalleGenerator dalleGenerator;
    private final TextGenerator textGenerator;
    private final AudioGenerator audioGenerator;

    @Autowired
    public MuseumController(DalleGenerator dalleGenerator, TextGenerator textGenerator, AudioGenerator audioGenerator) {
        this.dalleGenerator = dalleGenerator;
        this.textGenerator = textGenerator;
        this.audioGenerator = audioGenerator;

    }

    @PostMapping(value = "create/image", consumes = {"application/json"}, produces = {"application/json"})
    public Map<String, String> generateImageAndDescriptionAndAudio(@RequestBody String prompt) {
        String imageUrl = dalleGenerator.generateImage(prompt);
        String description = textGenerator.visionChat(imageUrl);
        byte[] audio = null;
        String audioBase64 = null;

        try {
            // Send the text to the ElevenLabs API and get audio data
            audio = audioGenerator.generateAudio(description);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Encode the audio to Base64 if it's not null
        if (audio != null) {
            audioBase64 = Base64.getEncoder().encodeToString(audio);
        }

        Map<String, String> map = new HashMap<>();
        map.put("imageUrl", imageUrl);
        map.put("description", description);
        if (audioBase64 != null) {
            map.put("audioBase64", audioBase64);
        }

        return map;
    }


}
