package langchain4j.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.time.Duration;

/**
 * Compares a cloud model (OpenAI) against a local model (Ollama/Mistral)
 * answering the same prompt, using LangChain4j's provider-agnostic
 * {@link ChatModel} abstraction.
 *
 * Prerequisites:
 *   - OPENAI_API_KEY environment variable set to a valid OpenAI API key
 *   - Ollama running locally with "mistral" pulled (see LaunchOllama)
 */
public class OpenAiVsOllama {
    public static void main(String[] args) {
        String openAiApiKey = System.getenv("OPENAI_API_KEY");
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            System.err.println("OPENAI_API_KEY is not set - skipping the OpenAI comparison.");
            System.err.println("Set it as an environment variable to include the cloud model.");
        } else {
            ChatModel openAi = OpenAiChatModel.builder()
                    .apiKey(openAiApiKey)
                    .modelName("gpt-5.4-mini")
                    .build();
            ask("OpenAI gpt-5.4-mini (cloud)", openAi);
        }

        ChatModel mistral = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("mistral:latest")
                .timeout(Duration.ofSeconds(120))
                .build();
        ask("Mistral 7B (local)", mistral);
    }

    //Works with Any ChatModel - OpenAI, Ollama, or any other provider
    static void ask(String name, ChatModel model) {
        try {
            long start = System.currentTimeMillis();
            String answer = model.chat("In one sentence, explain Dependency Injection");

            System.out.println("===== " + name + " =====");
            System.out.println(answer);
            System.out.println("(took " + (System.currentTimeMillis() - start) + " ms)\n");
        } catch (Exception e) {
            System.err.println("===== " + name + " =====");
            System.err.println("Request failed: " + e.getMessage() + "\n");
        }
    }
}
