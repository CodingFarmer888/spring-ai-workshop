package com.course.view;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/image")
public class ImageView extends VerticalLayout {
	
	public ImageView(OpenAiImageModel imageClient) {
		var messageList = new VerticalLayout();
		var messageInput = new MessageInput();
		
		messageInput.addSubmitListener(e -> {
			messageList.add(new Paragraph("You: " + e.getValue()));
			
			ImageResponse response = imageClient.call(
					new ImagePrompt(e.getValue(),
							OpenAiImageOptions.builder().withModel("dall-e-2").withWidth(1024).withHeight(1024).build()));
			
			messageList.add(new Image(response.getResult().getOutput().getUrl(),"Gen Image"));
		});
		
		add(messageList, messageInput);
	}

}
