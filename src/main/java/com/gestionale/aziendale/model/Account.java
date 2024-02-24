package com.gestionale.aziendale.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "userName", "email", "password", "ruolo", "utente" })
public class Account {

	// il json annoattion lo si usa per la mappatura del file json , il costruttore è necessario con tutti i field in questo cosa in
	//modo che gson di google possa convertire il file json in obj e viceversa 
	
	private Integer id;

	private String userName;

	private String email;

	private String password;

	private Ruolo ruolo;

	private Utente utente;

	public Account(Integer id, String userName, String email, String password, Ruolo ruolo, Utente utente) {
		this.id = id;
		this.userName = userName != null ? userName : "";
		this.email = email != null ? email : "";
		this.password = password != null ? password : "";
		this.ruolo = ruolo != null ? ruolo : new Ruolo("");
		this.utente = utente != null ? utente : new Utente("", "", "", 0);
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Ruolo getRuolo() {
		return ruolo != null ? ruolo : new Ruolo("");
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public Utente getUtente() {
		return utente != null ? utente : new Utente("", "", "", 0);
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", userName=" + userName + ", email=" + email + ", password=" + password
				+ ", ruolo=" + ruolo + ", utente=" + utente + "]";
	}

}
