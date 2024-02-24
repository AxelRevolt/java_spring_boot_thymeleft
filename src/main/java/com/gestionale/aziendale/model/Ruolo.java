package com.gestionale.aziendale.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "ruolo" })
public class Ruolo {

	private String ruolo;
	
	public Ruolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	@Override
	public String toString() {
		return "Ruolo [ruolo=" + ruolo + "]";
	}

}