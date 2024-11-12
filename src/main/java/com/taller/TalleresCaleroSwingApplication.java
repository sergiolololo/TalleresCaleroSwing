package com.taller;

import com.taller.conexion.Conexion;
import com.taller.utils.GoogleDrive;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@PropertySources({
    @PropertySource("classpath:url.properties"),
})
public class TalleresCaleroSwingApplication {

	/**
	 * Launch the application.
     */
	public static void main(String[] args) {
		
		ApplicationContext context = new SpringApplicationBuilder(TalleresCaleroSwingApplication.class).headless(false).run(args);

	    String[] names = context.getBeanDefinitionNames();
	    for(String name : names){
            System.out.println("-----------------");
            System.out.println(name);
        }
	}
}