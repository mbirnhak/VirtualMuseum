package edu.trincoll.VirtualMuseum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This Rest Controller gathers all the functions in the 3 different
 * service classes (image, audio, and text services) and exposes them
 * to allow the frontend to call them.
 */
@RestController
public class MuseumController {
    private final DalleGenerator dalleGenerator;

    @Autowired
    public MuseumController(DalleGenerator dalleGenerator) {
        this.dalleGenerator = dalleGenerator;
    }

    @PostMapping(value = "create/image", consumes = {"application/json"}, produces = {"application/json"})
    public String generateImage(@RequestBody String prompt) {
        return dalleGenerator.generateImage(prompt);
    }
}
