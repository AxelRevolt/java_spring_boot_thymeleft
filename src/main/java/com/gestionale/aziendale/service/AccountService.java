package com.gestionale.aziendale.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionale.aziendale.factory.AccountFactory;
import com.gestionale.aziendale.model.Account;
import com.gestionale.aziendale.repository.RepoJson;
import com.gestionale.aziendale.tool.Tool;

@Service
public class AccountService {

	@Autowired
	private Tool tool;

	@Autowired
	private RepoJson repoJson;
	
	@Autowired
	private AccountFactory accountFactory;

	public Account tryLogin(Account account, List<Account> accountRepoList) {
		return repoJson.searchAccount(account, accountRepoList);
	}

	public void register(Account account) {
		try {
			repoJson.writeJsonListRecords(account, tool.pathFileJson());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delete(Account account) {
		try {
			repoJson.removeObjFromJson(account, tool.pathFileJson());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Account convert(String id, String userName, String password, String ruolo, 
			String nome, String cognome, String eta, String cf) {
		return accountFactory.convert(id, userName, password, ruolo, nome, cognome, eta, cf);
	}
	
	public void modify(Account account) {
		try {
			repoJson.modifyObjFromJson(account, tool.pathFileJson());
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public List<Account> getListAccount(File file) {
		try {
			return repoJson.readProductionFromJson(file);
		} catch (FileNotFoundException e) {
			System.err.println("file not found");
			e.printStackTrace();
		}
		return null;
	}

}
