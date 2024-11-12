package com.taller.beans;

import com.taller.utils.Utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CitaBean {

	private int id;
	private Date fecha;
	private String descripcion;
	private Integer kilometros;
	private double tiempo;
	private double precioHora;
	private double descuento;
	private double importeTiempo;
	private double importeTotal;
	private int pagado;
	private double subtotal;
	private double iva;
	private String observaciones;
	private double importePagado;
	private double importePendiente;
	boolean esPresupuesto;
	private List<MaterialBean> materialBeanList;
	private VehiculoBean vehiculoBean;

	public CitaBean() {
	}
	public CitaBean(boolean esPresupuesto) {
		this.esPresupuesto = esPresupuesto;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getKilometros() {
		return kilometros;
	}
	public void setKilometros(Integer kilometros) {
		this.kilometros = kilometros;
	}
	public double getTiempo() {
		return tiempo;
	}
	public void setTiempo(double tiempo) {
		this.tiempo = tiempo;
	}
	public double getPrecioHora() {
		return precioHora;
	}
	public void setPrecioHora(double precioHora) {
		this.precioHora = precioHora;
	}
	public double getDescuento() {
		return descuento;
	}
	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}
	public double getImporteTiempo() {
		return importeTiempo;
	}
	public void setImporteTiempo(double importeTiempo) {
		this.importeTiempo = importeTiempo;
	}
	public double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public int getPagado() {
		return pagado;
	}
	public void setPagado(int pagado) {
		this.pagado = pagado;
	}
	public List<MaterialBean> getMaterialBeanList() {
		return materialBeanList !=null? materialBeanList : new ArrayList<>();
	}
	public void setMaterialBeanList(List<MaterialBean> materialBeanList) {
		this.materialBeanList = materialBeanList;
	}
	public VehiculoBean getVehiculoBean() {
		return vehiculoBean;
	}
	public void setVehiculoBean(VehiculoBean vehiculoBean) {
		this.vehiculoBean = vehiculoBean;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public double getIva() {
		return iva;
	}
	public void setIva(double iva) {
		this.iva = iva;
	}
	public String getFechaString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(getFecha());
	}
	public String getFechaFormateada() {
		return Utilidades.formatDate(getFecha());
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public double getImportePagado() {
		return importePagado;
	}
	public void setImportePagado(double importePagado) {
		this.importePagado = importePagado;
	}
	public double getImportePendiente() {
		return importePendiente;
	}
	public void setImportePendiente(double importePendiente) {
		this.importePendiente = importePendiente;
	}
	public boolean isEsPresupuesto() {
		return esPresupuesto;
	}
	public void setEsPresupuesto(boolean esPresupuesto) {
		this.esPresupuesto = esPresupuesto;
	}
}