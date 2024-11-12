package com.taller.beans;

public class MaterialBean {

	private int id;
	private String descripcion;
	private double cantidad;
	private double pvp;
	private double pc;
	private double descuentoCliente;
	private double importeCliente;
	private double iva;
	private CitaBean citaBean;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getCantidad() {
		return cantidad;
	}
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	public double getPvp() {
		return pvp;
	}
	public void setPvp(double pvp) {
		this.pvp = pvp;
	}
	public double getPc() {
		return pc;
	}
	public void setPc(double pc) {
		this.pc = pc;
	}
	public double getDescuentoCliente() {
		return descuentoCliente;
	}
	public void setDescuentoCliente(double descuentoCliente) {
		this.descuentoCliente = descuentoCliente;
	}
	public double getImporteCliente() {
		return importeCliente;
	}
	public void setImporteCliente(double importeCliente) {
		this.importeCliente = importeCliente;
	}
	public double getIva() {
		return iva;
	}
	public void setIva(double iva) {
		this.iva = iva;
	}
	public CitaBean getCitaBean() {
		return citaBean;
	}
	public void setCitaBean(CitaBean citaBean) {
		this.citaBean = citaBean;
	}
}