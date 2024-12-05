package edu.trincoll.VirtualMuseum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertFalse;

@SpringBootTest
class VirtualMuseumApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testGenerateAudio() {
		// Arrange
		AudioGenerator audioGenerator = new AudioGenerator();
		String outputPath = "src/main/resources/output1.mp3"; // Path where audio should be generated

		// Act
		audioGenerator.generateAudio();

		// Assert
		File outputFile = new File(outputPath);
		assertTrue(outputFile.exists(), "The audio file should be generated at the specified path.");

		// Clean up (optional, if you don't want to keep the generated file)
		outputFile.delete();
	}

	@Test
	public void testGenerateImage() {
		// Arrange
		DalleGenerator dalleGenerator = new DalleGenerator();
		String prompt = "I hope this works"; // Path where audio should be generated

		// Act
		String imageUrl = dalleGenerator.generateImage(prompt);

		// Assert
        assertTrue(imageUrl.startsWith("http"), "The image URL should start with 'http'");
		assertTrue(imageUrl.contains("dalle"), "The image URL should contain 'dalle' or relevant reference");
	}

	@Test
	public void testVisionChat() {
		// Arrange
		TextGenerator textGenerator = new TextGenerator();

		// Act
		String responseText = textGenerator.visionChat();

		// Assert
        assertFalse("The response text should not be empty", responseText.isEmpty());
		assertTrue(responseText.contains("see") || responseText.contains("image"), "The response text should contain some description");
	}

}
