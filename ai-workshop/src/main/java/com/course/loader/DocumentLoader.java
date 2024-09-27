package com.course.loader;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import jakarta.annotation.PostConstruct;

@Component
public class DocumentLoader {

	@Value("classpath:doc.txt")
	private Resource resource;
	
	@Value("${spring.ai.vectorstore.qdrant.host}")
	private String host;
	
	@Value("${spring.ai.vectorstore.qdrant.port}")
	private int port;
	
	@Value("${spring.ai.vectorstore.qdrant.api-key}")
	private String apiKey;
	
	@Value("${spring.ai.vectorstore.qdrant.collection-name}")
	private String collectionName;
	
	@Value("${spring.ai.vectorstore.qdrant.use-tls}")
	private boolean useTls;
	
	private final VectorStore vectorStore;
	
	public DocumentLoader(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}
	
	@PostConstruct
	public void init() throws InterruptedException, ExecutionException {
		QdrantClient client =new QdrantClient(QdrantGrpcClient.newBuilder(host, port, useTls).withApiKey(apiKey).build());
		
		Collections.CollectionInfo workshop = client.getCollectionInfoAsync(collectionName).get();
		
		if (workshop.getPointsCount() > 0) {
			return;
		}
		
		var textSpliter = new TokenTextSplitter();
		List<Document> documents = textSpliter.apply(loadText());
		vectorStore.add(documents);
	}

	private List<Document> loadText() {
		TextReader textReader = new TextReader(resource);
		textReader.getCustomMetadata().put("filename", "doc.txt");
		return textReader.get();
	}
	
	
}
