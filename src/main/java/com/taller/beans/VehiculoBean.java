package com.taller.beans;

public class VehiculoBean {
	
	private int id;
	private String vin;
	private String marca;
	private String modelo;
	private String caballos;
	private String matricula;
	private String anio;
	private String combustible;
	private String centimetros;
	private String tipo;
	private ClienteBean clienteBean;
	
	/**
	 * getters and setters
	 * @return
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getCaballos() {
		return caballos;
	}
	public void setCaballos(String caballos) {
		this.caballos = caballos;
	}
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getCombustible() {
		return combustible;
	}
	public void setCombustible(String combustible) {
		this.combustible = combustible;
	}
	public String getCentimetros() {
		return centimetros;
	}
	public void setCentimetros(String centimetros) {
		this.centimetros = centimetros;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public ClienteBean getClienteBean() {
		return clienteBean;
	}
	public void setClienteBean(ClienteBean clienteBean) {
		this.clienteBean = clienteBean;
	}
}