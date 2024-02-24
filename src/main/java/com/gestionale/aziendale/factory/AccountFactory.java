package com.gestionale.aziendale.factory;

import org.springframework.stereotype.Component;

import com.gestionale.aziendale.model.Account;
import com.gestionale.aziendale.model.Ruolo;
import com.gestionale.aziendale.model.Utente;

@Component
public class AccountFactory { // i component sono classi che si occupano di lavorazioni dati posso essere chiamate sempre con autowired
	
	public Account convert(String id, String userName, String email, String ruolo, 
			String nome, String cognome, String eta, String cf) {
		return new Account(Integer.parseInt(id), userName, email, null, 
				new Ruolo(ruolo), new Utente(nome, cognome, cf, Integer.parseInt(eta)));
	}

}
