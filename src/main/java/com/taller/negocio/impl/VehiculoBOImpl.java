package com.taller.negocio.impl;

import com.taller.beans.ClienteBean;
import com.taller.beans.VehiculoBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VehiculoBOImpl extends ComunImpl {

	@Override
	protected String obtenerQueryInsert(int id, Object objetoInsert) {
		VehiculoBean vehiculoBean = (VehiculoBean) objetoInsert;
		return "INSERT INTO vehiculo VALUES (" + id + ", '" +
				vehiculoBean.getMarca() + "', '" + vehiculoBean.getModelo() + "', " + vehiculoBean.getAnio() + ", '"  + vehiculoBean.getMatricula() + "', '"
				+ vehiculoBean.getVin() + "', " + vehiculoBean.getCentimetros() + ", " + vehiculoBean.getCaballos() + ", '" + vehiculoBean.getTipo() + "', '"
				+ vehiculoBean.getCombustible() + "', " + vehiculoBean.getClienteBean().getId() + ")";
	}

	@Override
	protected String obtenerMensajeInsertOK() {
		return "El vehiculo ha sido creado correctamente";
	}

	@Override
	protected String obtenerMensajeInsertKO() {
		return "El vehiculo que intenta insertar ya existe";
	}

	@Override
	protected String getQueryBuscar(Object objetoBuscar) {
		VehiculoBean vehiculoBean = (VehiculoBean) objetoBuscar;
		String consulta = "SELECT aa.*, bb.nombre,bb.apellido, bb.telefono, bb.localidad FROM vehiculo aa LEFT JOIN cliente bb ON bb.ID_CLIENTE = aa.ID_CLIENTE WHERE 1=1";
		if(vehiculoBean != null) {
			if(vehiculoBean.getId() != 0) {
				consulta += " AND aa.ID_VEHICULO = " + vehiculoBean.getId();
			}
			if(vehiculoBean.getVin() != null) {
				consulta += " AND UPPER(aa.vin) LIKE '%" + vehiculoBean.getVin().toUpperCase() + "%'";
			}
			if(vehiculoBean.getMarca() != null) {
				consulta += " AND UPPER(aa.marca) LIKE '%" + vehiculoBean.getMarca().toUpperCase() + "%'";
			}
			if(vehiculoBean.getModelo() != null) {
				consulta += " AND UPPER(aa.modelo) LIKE '%" + vehiculoBean.getModelo().toUpperCase() + "%'";
			}
			if(vehiculoBean.getCaballos() != null) {
				consulta += " AND aa.caballos = " + vehiculoBean.getCaballos().toUpperCase();
			}
			if(vehiculoBean.getMatricula() != null) {
				consulta += " AND UPPER(aa.matricula) LIKE '%" + vehiculoBean.getMatricula().toUpperCase() + "%'";
			}
			if(vehiculoBean.getAnio() != null) {
				consulta += " AND aa.anio = " + vehiculoBean.getAnio().toUpperCase() ;
			}
			if(vehiculoBean.getCombustible() != null) {
				consulta += " AND UPPER(aa.combustible) LIKE '%" + vehiculoBean.getCombustible().toUpperCase() + "%'";
			}
			if(vehiculoBean.getCentimetros() != null) {
				consulta += " AND aa.cilindrada = " + vehiculoBean.getCentimetros().toUpperCase();
			}
			if(vehiculoBean.getTipo() != null) {
				consulta += " AND UPPER(aa.tipo) LIKE '%" + vehiculoBean.getTipo().toUpperCase() + "%'";
			}
		}
		return consulta;
	}

	@Override
	protected void rellenarSalidaBuscar(ResultSet resultSet, List<Object> listadoSalida) throws SQLException {
		VehiculoBean vehiculo = new VehiculoBean();
		vehiculo.setId(resultSet.getInt("id_vehiculo"));
		vehiculo.setMarca(resultSet.getString("marca"));
		vehiculo.setModelo(resultSet.getString("modelo"));
		vehiculo.setAnio(resultSet.getString("anio"));
		vehiculo.setMatricula(resultSet.getString("matricula"));
		vehiculo.setVin(resultSet.getString("vin"));
		vehiculo.setCentimetros(resultSet.getString("cilindrada"));
		vehiculo.setCaballos(resultSet.getString("caballos"));
		vehiculo.setTipo(resultSet.getString("tipo"));
		vehiculo.setCombustible(resultSet.getString("combustible"));
		vehiculo.setClienteBean(new ClienteBean());
		vehiculo.getClienteBean().setId(resultSet.getInt("id_cliente"));
		vehiculo.getClienteBean().setNombre(resultSet.getString("nombre"));
		vehiculo.getClienteBean().setApellido(resultSet.getString("apellido"));
		vehiculo.getClienteBean().setTelefono(resultSet.getLong("telefono"));
		vehiculo.getClienteBean().setLocalidad(resultSet.getString("localidad"));

		listadoSalida.add(vehiculo);
	}

	@Override
	protected String getQueryUpdate(Object objetoUpdate) {
		VehiculoBean vehiculoBean = (VehiculoBean) objetoUpdate;
		return "UPDATE vehiculo SET MARCA = '" + vehiculoBean.getMarca() + "', MODELO = '" + vehiculoBean.getModelo()
				+ "', ANIO = " + vehiculoBean.getAnio() + ", MATRICULA = '" + vehiculoBean.getMatricula()
				+ "', VIN = '" + vehiculoBean.getVin() + "', CILINDRADA = " + vehiculoBean.getCentimetros() + ", CABALLOS = " + vehiculoBean.getCaballos()
				+ ", TIPO = '" + vehiculoBean.getTipo() + "', COMBUSTIBLE = '" + vehiculoBean.getCombustible() +"', ID_CLIENTE = " + vehiculoBean.getClienteBean().getId() + " WHERE ID_VEHICULO = " + vehiculoBean.getId();
	}

	@Override
	protected String obtenerMensajeUpdateOK() {
		return "El vehiculo ha sido modificado correctamente.";
	}

	@Override
	protected String obtenerMensajeUpdateKO() {
		return "Error al modificar vehículo.";
	}

	@Override
	protected String getQueryDelete(int id) {
		return "DELETE FROM VEHICULO WHERE ID_VEHICULO=" + id;
	}

	@Override
	protected String obtenerQuerySecuencia() {
		return "SELECT MAX(ID_VEHICULO) FROM VEHICULO";
	}
}
