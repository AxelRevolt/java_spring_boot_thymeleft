package com.gestionale.aziendale.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gestionale.aziendale.model.Account;
import com.gestionale.aziendale.tool.Tool;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

@Service
public class RepoJson { // la service di solito la si usa per chiamare qualche servizio esterno 
	// siccome io sono tutto in locale tericoamente non sarebbe servita ma giusto per rimanere in tema e per allenarmi ho
	// strutturato questo applicativo come quelli su cui lavoro per l'azienda

	@Autowired
	private Tool tool;

	public Account searchAccount(Account accountTry, List<Account> accountRepoList) {

		Account accountFound = tool.macthAccount(accountTry, accountRepoList);

		if (null != accountFound) {
			return accountFound;
		}

		return null;

	}

	public ArrayList<Account> readProductionFromJson(File pathFile) throws FileNotFoundException {

//		System.err.println("hello , readProductionFromJson");

		Account[] arrProduct = null;
		ArrayList<Account> listProduction = new ArrayList<Account>();
		JsonReader reader = null;

		FileReader fileReader = new FileReader(pathFile);

		try {

			BufferedReader br = new BufferedReader(fileReader);
			if (br.readLine() == null) {
				System.out.println("error file not have data");
			} else {

				reader = new JsonReader(new FileReader(pathFile));
				arrProduct = new Gson().fromJson(reader, Account[].class);

			}

			if (null != arrProduct) {
				listProduction = tool.arrayToList(arrProduct);
			}

			return listProduction;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
			}
		}
		return listProduction;
	}

//	public ArrayList<Account> readProductionFromJson(File pathFile) throws FileNotFoundException {
//	    try (Reader reader = new FileReader(pathFile)) {
//	        Gson gson = new Gson();
//	        Type accountListType = new TypeToken<ArrayList<Account>>() {}.getType();
//	        return gson.fromJson(reader, accountListType);
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	    }
//	    return new ArrayList<>(); // Restituisci una lista vuota in caso di errori
//	}

	public boolean writeJsonListRecords(Account account, File file) throws IOException {

		System.out.println("Account in arrivo : " + account.toString());

		JsonArray jsonArray = new JsonArray();
		ArrayList<Account> oldAccountList = new ArrayList<Account>();
//		ArrayList<String> listLinkOld = new ArrayList<String>();
//		ArrayList<String> listLinkNew = new ArrayList<String>();

		int sizeOldList = 0;

		FileReader fileReader = new FileReader(file);
		FileWriter fileWriter = null;

		JsonObject newObject = null;
		JsonObject objPermesso = null; // obj json for new model
		JsonObject objUtente = null; // obj json for new model

		if (file.exists()) {
			try {
				System.out.println("file exist");
				BufferedReader br = new BufferedReader(fileReader);
				if (br.readLine() == null) {
					System.out.println("file is empty");
				} else {
					oldAccountList = readProductionFromJson(file);
					sizeOldList = oldAccountList.size();

				}
			} catch (Exception e) {
				System.err.println("error on writeJsonListRecords");
				e.printStackTrace();
			}
		}

		if (oldAccountList.size() != 0) { //
			System.out.println("Start for old list write");
			for (int i = 0; i < sizeOldList; i++) {// add old link before write file complete
				if (oldAccountList.get(i).getId() == null || oldAccountList.get(i).getId() == 0) {
					oldAccountList.get(i).setId(1);
				} else if (i!= 0){
					oldAccountList.get(i).setId(oldAccountList.get(i-1).getId() + 1);
				}
				setObjJson(jsonArray, newObject, objPermesso, objUtente, oldAccountList.get(i));
			}
			account.setId(oldAccountList.size() + 1);
			setObjJson(jsonArray, newObject, objPermesso, objUtente, account); // add new obj in case of register

		} else { // first write
			account.setId(1);
			setObjJson(jsonArray, newObject, objPermesso, objUtente, account);
		}

		try {
			System.out.println("tentativo di scrittura del file json");
			fileWriter = new FileWriter(file);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(jsonArray, fileWriter);
		} catch (Exception e) {
			return false;
		} finally {
			fileWriter.close();
		}

		System.out.println("Fine scrittura file json : " + account.toString());

		return true;

	}

	public boolean removeObjFromJson(Account account, File file) throws IOException {

		System.out.println("removeObjFromJson account in arrivo : " + account.toString());

		JsonArray jsonArray = new JsonArray();
		ArrayList<Account> oldAccountList = new ArrayList<Account>();

		int sizeOldList = 0;

		FileReader fileReader = new FileReader(file);
		FileWriter fileWriter = null;

		JsonObject newObject = null;
		JsonObject objPermesso = null; // obj json for new model
		JsonObject objUtente = null; // obj json for new model

		if (file.exists()) {
			try {
				System.out.println("file exist");
				BufferedReader br = new BufferedReader(fileReader);
				if (br.readLine() == null) {
					System.out.println("file is empty");
				} else {
					oldAccountList = readProductionFromJson(file);
					sizeOldList = oldAccountList.size();

				}
			} catch (Exception e) {
				System.err.println("error on writeJsonListRecords");
				e.printStackTrace();
			}
		}

		for (Account a : oldAccountList) {
			if (account.getId() == a.getId()) {
				oldAccountList.remove(a);// remove obj
				break;
			}
		}

		for (Account a : oldAccountList) {
			setObjJson(jsonArray, newObject, objPermesso, objUtente, a);
		}

		try {
			System.out.println("tentativo di scrittura del file json");
			fileWriter = new FileWriter(file);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(jsonArray, fileWriter);
		} catch (Exception e) {
			return false;
		} finally {
			fileWriter.close();
		}

		System.out.println("Fine scrittura file json : " + account.toString());

		return true;

	}

	public boolean modifyObjFromJson(Account modifiedAccount, File file) throws IOException {

		System.out.println("modifyObjFromJson account in arrivo : " + modifiedAccount.toString());

		JsonArray jsonArray = new JsonArray();
		ArrayList<Account> oldAccountList = new ArrayList<Account>();

		int sizeOldList = 0;

		FileReader fileReader = new FileReader(file);
		FileWriter fileWriter = null;

		JsonObject newObject = null;
		JsonObject objPermesso = null;
		JsonObject objUtente = null;

		if (file.exists()) {
			try {
				System.out.println("file exist");
				BufferedReader br = new BufferedReader(fileReader);
				if (br.readLine() == null) {
					System.out.println("file is empty");
				} else {
					oldAccountList = readProductionFromJson(file);
					sizeOldList = oldAccountList.size();
				}
			} catch (Exception e) {
				System.err.println("error on writeJsonListRecords");
				e.printStackTrace();
			}
		}

		for (Account a : oldAccountList) {
			if (modifiedAccount.getId() == a.getId()) {
				// Modifica l'oggetto anziché rimuoverlo
				a.setEmail(modifiedAccount.getEmail());
				a.setUserName(modifiedAccount.getUserName());
				a.setPassword(modifiedAccount.getPassword());
				a.setRuolo(modifiedAccount.getRuolo());
				a.setUtente(modifiedAccount.getUtente());
			}
		}

		for (Account a : oldAccountList) {
			setObjJson(jsonArray, newObject, objPermesso, objUtente, a);
		}

		try {
			System.out.println("tentativo di scrittura del file json");
			fileWriter = new FileWriter(file);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(jsonArray, fileWriter);
		} catch (Exception e) {
			return false;
		} finally {
			if (fileWriter != null) {
				fileWriter.close();
			}
		}

		System.out.println("Fine scrittura file json : " + modifiedAccount.toString());

		return true;
	}

	private void setObjJson(JsonArray jsonArray, JsonObject newObject, JsonObject objPermesso, JsonObject objUtente,
			Account account) {

		newObject = new JsonObject();

		newObject.addProperty("id", account.getId());
		newObject.addProperty("email", account.getEmail());
		newObject.addProperty("userName", account.getUserName());
		newObject.addProperty("password", account.getPassword());

		objPermesso = new JsonObject();

		objPermesso.addProperty("ruolo", account.getRuolo().getRuolo());// set paramiter for new obj json

		objUtente = new JsonObject();

		objUtente.addProperty("nome", account.getUtente().getNome());
		objUtente.addProperty("cognome", account.getUtente().getCognome());
		objUtente.addProperty("cf", account.getUtente().getCf());
		objUtente.addProperty("eta", account.getUtente().getEta());

		newObject.add("ruolo", objPermesso);

		newObject.add("utente", objUtente);

		jsonArray.add(newObject);// add obj main json into json array
	}

}
