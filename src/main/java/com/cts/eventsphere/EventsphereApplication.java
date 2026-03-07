package com.cts.eventsphere;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class EventsphereApplication {
	public static void main(String[] args) {
        log.info("EventSphere backend started");
		SpringApplication.run(EventsphereApplication.class, args);
	}

}
