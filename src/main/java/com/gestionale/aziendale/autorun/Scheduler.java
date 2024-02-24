package com.gestionale.aziendale.autorun;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.gestionale.aziendale.tool.Tool;

@Component
public class Scheduler implements ApplicationListener<ApplicationReadyEvent> {
	// implementando questa classe si può decidere un attività primaria subito dopo lo start 
	// ed è auto run ovvero il secondo dopo lo start del server tomcat parte il codice nel metodo 
	// onApplicationEvent che se vedi è un esempio di override
	// le iterfacce vanno implemetate e se ne posso implemetare anche 10 mentre le classi si posso estentere ma solo 
	// una classe alla volta si può estendere 
	
	private final Logger log = LoggerFactory.getLogger(Scheduler.class);
	
	@Autowired
	private Tool tool;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		
		log.info("I am first action on start up the application :P");
		
		tool.pathFileJson(); // on start try to create file json blank
		
		String os = System.getProperty("os.name").toLowerCase();

        try {
        	 if (os.contains("win")) {
                 // Sistema operativo Windows
                 log.info("Sistema operativo: Windows");
                 Runtime.getRuntime().exec("cmd /c start http://localhost:8080/index");
             } else if (os.contains("nix") || os.contains("nux")) {
                 // Sistema operativo basato su Unix (Linux)
                 log.info("Sistema operativo: Unix (Linux)");
                 Runtime.getRuntime().exec("xdg-open http://localhost:8080/index");
             }else if (os.contains("mac")) {
                 // Sistema operativo macOS
                 log.info("Sistema operativo: MacOS");
                 Runtime.getRuntime().exec("open http://localhost:8080/index");
             } else {
                 // Altro sistema operativo non supportato
                 log.error("Sistema operativo non supportato");
             }
        	 
        	 // codice fatto da chat gpt ma utilissimo apre il browser su mac e windows sulla pagina localhost dove questo
        	 // applicativo si appogia 
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
