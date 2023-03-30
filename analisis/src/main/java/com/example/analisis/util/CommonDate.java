package com.example.analisis.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CommonDate {
		
	public static Date getCurrentDate() {
		   LocalDateTime now = LocalDateTime.now();
		   return Date
				      .from(now.atZone(ZoneId.systemDefault())
				      .toInstant());
	}

}
