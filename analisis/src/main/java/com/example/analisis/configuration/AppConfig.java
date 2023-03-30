package com.example.analisis.configuration;


import java.io.IOException;
import java.nio.file.Paths;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
	@Value("${index.path}")
	private String indexPath;

	@Bean
	public String indexPath() {
		return indexPath;
	}

	@Bean
	public Directory indexDirectory(String indexPath) throws
									  IOException {
		return FSDirectory.open(Paths.get(indexPath));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
