package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.eqour.imageparsingrest.serialization.ImageDeserializer;
import ru.eqour.imageparsingrest.serialization.ImageSerializer;

import java.awt.image.BufferedImage;

@Configuration
@SpringBootApplication
public class ImageParsingRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageParsingRestApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();

		module.addSerializer(BufferedImage.class, new ImageSerializer());
		module.addDeserializer(BufferedImage.class, new ImageDeserializer());

		mapper.registerModule(module);
		return mapper;
	}

}
