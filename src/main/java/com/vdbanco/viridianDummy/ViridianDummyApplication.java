package com.vdbanco.viridianDummy;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJSONDoc
public class ViridianDummyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViridianDummyApplication.class, args);
	}
}
