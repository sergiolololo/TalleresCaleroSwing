package com.taller.beans;

public class ErrorBean {

	private boolean existeError;
	private String mensaje;
	private Integer id;
	
	/**
	 * getters and setters
	 * @return
	 */
	public boolean isExisteError() {
		return existeError;
	}
	public void setExisteError(boolean existeError) {
		this.existeError = existeError;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
}
