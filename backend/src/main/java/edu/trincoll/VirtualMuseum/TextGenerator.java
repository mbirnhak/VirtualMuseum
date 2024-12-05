package edu.trincoll.VirtualMuseum;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.output.Response;
import org.springframework.stereotype.Service;

@Service
public class TextGenerator {
    private final String apiKey = System.getenv("OPENAI_API_KEY");

    private final ChatLanguageModel chatModel = OpenAiChatModel.builder()
            .apiKey(apiKey)
            .modelName(OpenAiChatModelName.GPT_4_O_MINI)
            .build();

    String visionChat(String imageUrl) {
        Response<AiMessage> response = chatModel.generate(
                UserMessage.from(
                        ImageContent.from(imageUrl),
                        TextContent.from("What do you see?")
                )
        );
        return response.content().text();
    }
}
