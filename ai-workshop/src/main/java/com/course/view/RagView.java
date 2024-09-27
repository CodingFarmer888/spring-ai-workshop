package com.course.view;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/rag")
public class RagView extends VerticalLayout{

	private final ChatClient chatClient;
	
	private final String template = """
			你會根據 DOCUMENTS: 下的文件《約會大作戰》語錄回答使用者的問題: {documents}
			""";
	
	public RagView(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		this.chatClient = chatClientBuilder.build();
		var messageList = new VerticalLayout();
		var messageInput = new MessageInput();
		
		messageInput.addSubmitListener(e -> {
			messageList.add(new Paragraph("You: " + e.getValue()));
			var listOfSimilarDocuments = vectorStore.similaritySearch(e.getValue());
			
			var documents = listOfSimilarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));
			
			var systemMessgae = new SystemPromptTemplate(this.template).createMessage(Map.of("documents", documents));
			
			var userMessage = new UserMessage(e.getValue());
			var prompt = new Prompt(List.of(systemMessgae, userMessage));
			
			messageList.add(new Paragraph("AI: " + this.chatClient.prompt(prompt).call().content()));
		});
		
		add(messageList, messageInput);
	}
}
