package edu.trincoll.VirtualMuseum;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
//		outputFile.delete();
	}

}
