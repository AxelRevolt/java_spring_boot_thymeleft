package com.gestionale.aziendale.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.gestionale.aziendale.model.Account;
import com.gestionale.aziendale.model.Ruolo;
import com.gestionale.aziendale.model.Utente;
import com.gestionale.aziendale.repository.RepoJson;

@Component
public class Tool {
	
	@Autowired
	@Lazy
	private RepoJson repoJson; // il lazy annotation dice a spring di non istrazniare a priori tale variabile perchè sarebbe ricorsiva
	// siccome c'è un autowired anche in RepoJson di Tool
	
	public ArrayList<Account> arrayToList(Account[] arrToList) {
		ArrayList<Account> array_list = new ArrayList<Account>();
		Collections.addAll(array_list, arrToList);
		return array_list;
	}
	
	public Account macthAccount(Account accountTry, List<Account> accountRepoList) {
		
		for(Account a : accountRepoList) {
			if(a.getUserName().equals(accountTry.getUserName()) && a.getPassword().equals(accountTry.getPassword())) {
				//accountTry = a; // set account to try log to real account found 
				return a;
			}
		}
		
		return null;
	}
	
	public File pathFileJson() {
		
		System.out.println("start create or recuvera file json storgae");
		
	     // Ottieni la directory di lavoro corrente (dove si trova il JAR)
        String currentWorkingDirectory = System.getProperty("user.dir"); // get current path on run jar on windows or mac

		File pathFileJson = new File(currentWorkingDirectory + File.separator + "test.json"); // for craeet seperator for windows or mac 

		try {

			if (!pathFileJson.exists()) {
				pathFileJson.createNewFile();
				try {
					Account account = new Account(0, "admin", "admin@admin", "admin", new Ruolo("admin"), new Utente("", "", "", 0));
					
					repoJson.writeJsonListRecords(account, pathFileJson);
				} catch (Exception e) {
					System.err.println("error on craete file and insert accound Admin");
					e.printStackTrace();
				}
			} else {
				System.out.println("file already exists on : " + currentWorkingDirectory);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("end create or recuvera file json storage");
		
		return pathFileJson;
	}

}
