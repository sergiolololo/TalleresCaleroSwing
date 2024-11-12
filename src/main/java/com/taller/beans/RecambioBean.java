package com.taller.beans;

public class RecambioBean {
	
	private int id;
	private String referencia;
	private String nombre;
	private double cantidad;
	private double pc;
	private double pv;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getPc() {
		return pc;
	}
	public void setPc(double pc) {
		this.pc = pc;
	}
	public double getPv() {
		return pv;
	}
	public void setPv(double pv) {
		this.pv = pv;
	}
}
