package com.gestionale.aziendale.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.gestionale.aziendale.model.Account;
import com.gestionale.aziendale.model.ERuolo;
import com.gestionale.aziendale.model.Ruolo;
import com.gestionale.aziendale.model.Utente;
import com.gestionale.aziendale.service.AccountService;
import com.gestionale.aziendale.tool.Tool;

@Controller
public class ControllerMain {

	private final Logger log = LoggerFactory.getLogger(ControllerMain.class); 
	// per i progetti lavorativi si usano i log ogni log va identificato per la classe in cui si trova col metodo qui usato

	@Autowired // autowired possiamo definirlo auto injection in partica non istaziamo manulamnete la classe che ci occorre 
	// una volta che spring vede che chiamiamo il metodo x che al suo interno usa tool allora prepara la variabile istanziandola 
	//( tool = new Tool())  cosi che la variabile non sia null in modo da poterla usare , immagino che lo si faccia per risparmiare memoria
	private Tool tool;

	@Autowired
	private AccountService accountService;

	///////////////////// index\\\\\\\\\\\\\\\\\\\\\\\\
	@GetMapping(value = { "/", "/index" }) // questo è una mappattura di un metodo esposto chiamabile tramite il metodo get di html js ecc. 
	public String index(Model model) { // qui sul metodo possiamo aggiungere degli argomenti che devono essere inviati per chiamare tale metodo esposto
		// il model qui sopra si usa per inviare qualcosa alla prossima pagina richimata quando si chiama questo end point
		// in pratica quanto starta applicativo prima di far visualizzare la pagina iniziale , inviamo un obj account vuoto 
		// in modo da usarlo poi nell'eventuale login o register , questa è una problematica che ho riscontrato con thymilef
		model.addAttribute("account", new Account(null,"", "", "", new Ruolo(""), new Utente(null, null, null, 0)));
		return "index";
	}

	///////////////////// Login\\\\\\\\\\\\\\\\\\\\\\\\\

	@PostMapping("/login")
	public String login(@ModelAttribute("account") Account account, Model model) { // @ModelAttribute lo usaimo per aspettarci sulla chiamata un 
		// obj di tipo Account si usano th:object="${account}" in un form poi ogni input deve avere il th:value pari 
		// tipo di string o cmq tipo di dato da inviare come accoun.id accoun.ruolo.ruolo ecc.
		log.info("ciao login : {}", account.toString());
		List<Account> repoAccountList = accountService.getListAccount(tool.pathFileJson());
		Account accountTryLogin = accountService.tryLogin(account, repoAccountList);
		if (null != accountTryLogin) {
			System.out.println("passed");
			if (ERuolo.ADMIN.name().equalsIgnoreCase(accountTryLogin.getRuolo().getRuolo())) {
				model.addAttribute("accounts", repoAccountList);
				return "dashBoardAdmin";
			} else {
				model.addAttribute("account", accountTryLogin);
				return "dashBoard";
			}
		} else {
			System.out.println("not passed");
			return "accessDenided";
		}
	}

	//////////////////// register\\\\\\\\\\\\\\\\\\\\\\

	@PostMapping("/register")
	public String register(@ModelAttribute("account") Account account, Model model) {
		log.info("ciao register : {}", account.toString());
		accountService.register(account);
		return "index";
	}

	//////////////////// modify\\\\\\\\\\\\\\\\\\\\\\\\
	@PostMapping(value = "/delete")
	public String delete(@RequestParam String id, Model model) {
		System.out.println("call /delete");
		accountService.delete(new Account(Integer.parseInt(id), null, null, null, null, null)); // remove from list obj in json
		List<Account> repoAccountList = accountService.getListAccount(tool.pathFileJson()); // re-load list of json
		model.addAttribute("accounts", repoAccountList); // seend list to html
		return "dashBoardAdmin"; // load page
	}
	
	@PostMapping(value = "/toModify")
	public String toModify(@RequestParam("id") String id,
			@RequestParam("userName") String userName, 
			@RequestParam("email") String email,
			@RequestParam("ruolo") String ruolo, 
			@RequestParam("nome")String nome,
			@RequestParam("cognome") String cognome,
			@RequestParam("cf") String cf,
			@RequestParam("eta") String eta,
			Model model) { // TODO vedere sesi riesce ad essere generici nell'ivio di user or mail
		System.out.println("call /modify");
		System.out.println("parametri " + id + ", " + userName + email + ruolo + nome + cognome
				+ eta );
		Account account = accountService.convert(id, userName, email, ruolo, nome, cognome, eta, cf);// convert string to obj Account and start modify
		model.addAttribute("account", account); // seend list to html
		return "modifica";
	}
	
	@PostMapping(value = "/saveModify")
	public String saveModify(/*@RequestParam("id") String id,
			@RequestParam("userName") String userName, 
			@RequestParam("email") String email,
			@RequestParam("ruolo") String ruolo, 
			@RequestParam("nome")String nome,
			@RequestParam("cognome") String cognome,
			@RequestParam("eta") String eta,
			@RequestParam("cf") String cf,*/
			@ModelAttribute("account") Account account,
			Model model) {
		System.out.println("save toString : " + account.toString());
//		Account account = account;//accountService.convert(id, userName, email, ruolo, nome, cognome, eta, cf);// convert string to obj Account and start modifya
		accountService.modify(account);
		List<Account> repoAccountList = accountService.getListAccount(tool.pathFileJson()); // re-load list of json
		model.addAttribute("accounts", repoAccountList); // seend list to html
		return "dashBoardAdmin"; // load page
	}

	/////////////////// close\\\\\\\\\\\\\\\\\\\\\\\\\\
	@GetMapping(value = "/closeApp")
	public void exitCloseapp() {
		System.out.println("call close app");
		System.exit(0);
	}

//	@PreDestroy
//	public void onExit() {
//		// Operazioni di pulizia o chiusura prima della terminazione
//		System.out.println("Application is shutting down...");
//	}

	/////////////////// error\\\\\\\\\\\\\\\\\\\\\\\\\\

	@ExceptionHandler(Exception.class) // questo endpoint è bellissimo cattura tutte le eventuali eccezioni le prende 
	// con obj model le assegnamo ad un attribuito nella pagina html da richiamare per poi stampara errore nella pagina html cosi che 
	// si veda ad html errore anche se non è un errore completo ti da già un idea dell'eventuale errore 
	public String handleNumberFormatException(Exception ex, Model model) {
		System.out.println("error : " + ex.getMessage() + "\n" + ex.toString());
		model.addAttribute("error", ex.getMessage() + "\n" + ex.toString());
		ex.printStackTrace();
		return "error";
	}

}
