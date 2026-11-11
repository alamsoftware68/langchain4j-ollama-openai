# LangChain4j + Ollama (and OpenAI) Demo

Small Java project showing how [LangChain4j](https://docs.langchain4j.dev/) lets you swap LLM
providers — a local model via [Ollama](https://ollama.com/) and a cloud model via OpenAI — behind
the same `ChatModel` interface, without changing your application code.

## What this demonstrates

- Calling a local LLM (Mistral, via Ollama) with LangChain4j
- The two shapes of the `ChatModel.chat(...)` API:
  - `chat(String)` → quick one-off prompt, returns a plain `String`
  - `chat(ChatMessage...)` → system + user messages, returns a structured `ChatResponse`
    with the reply plus token-usage metadata
- Provider-agnostic code: the same `ask(String, ChatModel)` method works unchanged whether
  it's handed an OpenAI model or an Ollama model

## Tech stack

- Java 21
- Maven
- [LangChain4j](https://github.com/langchain4j/langchain4j) `1.23.0`
- [Ollama](https://ollama.com/) (local inference)
- OpenAI API (optional, cloud comparison)

## Project structure

```
src/main/java/langchain4j/ollama/
├── LaunchOllama.java      # Basic Ollama call: plain-string chat + message-based chat
└── OpenAiVsOllama.java    # Same prompt sent to OpenAI (cloud) and Ollama (local) side by side
```

## Prerequisites

1. **Java 21** and **Maven** installed.
2. **Ollama** installed and running locally, with the `mistral` model pulled:
   ```bash
   ollama pull mistral
   ollama serve
   ```
3. *(Optional, for `OpenAiVsOllama`)* An OpenAI API key, exported as an environment variable:
   ```bash
   # macOS/Linux
   export OPENAI_API_KEY=sk-...

   # Windows PowerShell
   $env:OPENAI_API_KEY = "sk-..."
   ```
   If this isn't set, `OpenAiVsOllama` skips the OpenAI call and only runs the local model.

## Running

```bash
mvn compile

# Basic Ollama demo (string chat vs. message-based chat)
mvn exec:java -Dexec.mainClass="langchain4j.ollama.LaunchOllama"

# Compare OpenAI (cloud) vs. Ollama (local) on the same prompt
mvn exec:java -Dexec.mainClass="langchain4j.ollama.OpenAiVsOllama"
```

Or run either class's `main` method directly from your IDE.

## Sample output

```
In one sentence answer what is agentic ai
Agentic AI refers to AI systems that can autonomously plan and take actions...
(took 842 ms)

*******************************
Method overloading lets a class have multiple methods with the same name but
different parameter lists...
(took 1203 ms, input tokens: 34, output tokens: 58)
```

## Notes

- Never commit API keys. `OPENAI_API_KEY` is read from the environment, never hardcoded.
- `.idea/` is gitignored; keep IDE run configurations (which can contain env vars) out of
  version control.
