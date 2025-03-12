package com.example.firstTry;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.naming.spi.ObjectFactory;

@SpringBootApplication
public class FirstTryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstTryApplication.class, args);
	}

}
