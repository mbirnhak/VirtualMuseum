package edu.trincoll.VirtualMuseum;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.openai.OpenAiImageModelName;
import dev.langchain4j.model.output.Response;

public class DalleGenerator {

    // Method to generate an image given a prompt
    public static String generateImage(String prompt) {
        // Initialize the DALL-E 3 model with API key and model name
        ImageModel model = OpenAiImageModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY")) 
                .modelName(OpenAiImageModelName.DALL_E_3)
                .build();

        try {
            // Generate an image based on the provided prompt
            Response<Image> imageResponse = model.generate(prompt);

            // Output details for debugging or logging
            System.out.println("Revised Prompt: " + imageResponse.content().revisedPrompt());
            System.out.println("Token Usage: " + imageResponse.tokenUsage());

            // Return the URL of the generated image
            return imageResponse.content().url().toString();
        } catch (Exception e) {
            System.err.println("Error generating image: " + e.getMessage());
            return null;
        }
    }

        //testing
    public static void main(String[] args) {
        // Example usage with a sample prompt

        String prompt = "A futuristic cityscape with flying cars and neon lights at night";
        String imageUrl = generateImage(prompt);

        if (imageUrl != null) {
            System.out.println("Generated Image URL: " + imageUrl);
        } else {
            System.out.println("Image generation failed.");
        }
    }
}

