package com.example.analisis.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class UtilDate {
		
	public Date getCurrentDate() {
		   LocalDateTime now = LocalDateTime.now();
		   return Date
				      .from(now.atZone(ZoneId.systemDefault())
				      .toInstant());
	}

}
