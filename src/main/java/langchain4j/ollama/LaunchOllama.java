package langchain4j.ollama;

import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.time.Duration;

/**
 * Demonstrates calling a local Ollama model via LangChain4j, using both the
 * simple String-in/String-out overload and the message-based overload that
 * returns a full {@link ChatResponse} (with token usage).
 *
 * Prerequisite: Ollama must be running locally with the model pulled, e.g.
 *   ollama pull mistral
 *   ollama serve
 */
public class LaunchOllama {
    public static void main(String[] args) {
        ChatModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("mistral:latest")    //the name shown by "ollama list"
                .temperature(0.3)
                .timeout(Duration.ofSeconds(120))  //local models can be slow, the first call also loads the model
                .build();

        try {
            long start = System.currentTimeMillis();

            String answer = model.chat("In one sentence answer what is agentic ai");
            System.out.println(answer);
            System.out.println("(took " + (System.currentTimeMillis() - start) + " ms)\n");

            System.out.println("*******************************");

            start = System.currentTimeMillis();

            ChatResponse response = model.chat(
                    SystemMessage.from("You are a Java trainer. Answer in 3 short lines with a small example."),
                    UserMessage.from("What is method overloading"));

            System.out.println(response.aiMessage().text());
            System.out.println("(took " + (System.currentTimeMillis() - start) + " ms, "
                    + "input tokens: " + response.tokenUsage().inputTokenCount() + ", "
                    + "output tokens: " + response.tokenUsage().outputTokenCount() + ")");
        } catch (Exception e) {
            System.err.println("Could not reach Ollama at http://localhost:11434.");
            System.err.println("Make sure Ollama is installed and running, and that the model is pulled:");
            System.err.println("  ollama pull mistral");
            System.err.println("  ollama serve");
            System.err.println("Underlying error: " + e.getMessage());
        }
    }
}
