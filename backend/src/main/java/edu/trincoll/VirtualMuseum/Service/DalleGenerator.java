package edu.trincoll.VirtualMuseum.Service;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import dev.langchain4j.model.output.Response;
import org.springframework.stereotype.Service;

@Service
public class DalleGenerator {
    public DalleGenerator() {
    }

    // Method to generate an image given a prompt
    public String generateImage(String prompt) {
        // Initialize the DALL-E 3 model with API key and model name
        ImageModel model = OpenAiImageModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(OpenAiImageModelName.DALL_E_3)
                .build();

        try {
            // Generate an image based on the provided prompt
            Response<Image> imageResponse = model.generate(prompt);

            // Return the URL of the generated image
            return imageResponse.content().url().toString();
        } catch (Exception e) {
            System.err.println("Error generating image: " + e.getMessage());
            return null;
        }
    }
}