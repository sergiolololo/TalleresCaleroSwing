package com.taller.negocio.impl;

import com.taller.beans.RecambioBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RecambioBOImpl extends ComunImpl {

	@Override
	protected String obtenerQueryInsert(int id, Object objetoInsert) {
		RecambioBean recambioBean = (RecambioBean) objetoInsert;
		return "INSERT INTO recambio VALUES (" + id + ", '" + recambioBean.getReferencia() + "', '"
				+ recambioBean.getNombre() + "', " + recambioBean.getCantidad() + ", " + recambioBean.getPc() + ", " + recambioBean.getPv() + ");";
	}

	@Override
	protected void rellenarSalidaBuscar(ResultSet resultSet, List<Object> listadoRecambios) throws SQLException {
		RecambioBean recambio = new RecambioBean();
		recambio.setId(resultSet.getInt("id_Recambio"));
		recambio.setReferencia(resultSet.getString("referencia"));
		recambio.setNombre(resultSet.getString("nombre"));
		recambio.setCantidad(resultSet.getDouble("cantidad"));
		recambio.setPc(resultSet.getDouble("pc"));
		recambio.setPv(resultSet.getDouble("pv"));
		listadoRecambios.add(recambio);
	}

	@Override
	protected String obtenerMensajeInsertOK() {
		return "El recambio ha sido creado correctamente";
	}

	@Override
	protected String obtenerMensajeInsertKO() {
		return "El recambio que intenta insertar ya existe";
	}

	protected String obtenerQuerySecuencia() {
		return "SELECT MAX(ID_RECAMBIO) FROM RECAMBIO";
	}

	@Override
	protected String getQueryBuscar(Object objectoBuscar) {
		RecambioBean recambioBean = (RecambioBean) objectoBuscar;
		String consulta = "SELECT * FROM recambio WHERE 1=1";
		if(recambioBean != null) {
			if(recambioBean.getId() != 0) {
				consulta += " AND id_recambio = " + recambioBean.getId();
			}
			if(recambioBean.getReferencia() != null) {
				consulta += " AND UPPER(referencia) LIKE '%" + recambioBean.getReferencia().toUpperCase() + "%'";
			}
			if(recambioBean.getNombre() != null) {
				consulta += " AND nombre = '" + recambioBean.getNombre() + "'";
			}
		}
		return consulta;
	}

	@Override
	protected String obtenerMensajeUpdateOK() {
		return "El recambio ha sido modificado correctamente";
	}

	@Override
	protected String obtenerMensajeUpdateKO() {
		return "Error al modificar recambio.";
	}

	@Override
	protected String getQueryUpdate(Object objetoUpdate) {
		RecambioBean recambioBean = (RecambioBean) objetoUpdate;

		String query = "UPDATE recambio SET cantidad=" + recambioBean.getCantidad();
		String queryWhere = " WHERE ID_RECAMBIO=" + recambioBean.getId();

		if(recambioBean.getReferencia() != null) {
			query += ", referencia='" + recambioBean.getReferencia() + "'";
		}
		if(recambioBean.getNombre() != null) {
			query += ", nombre='" + recambioBean.getNombre() + "'";
		}
		if(recambioBean.getPc() != 0) {
			query += ", pc=" + recambioBean.getPc();
		}
		if(recambioBean.getPv() != 0) {
			query += ", pv=" + recambioBean.getPv();
		}
		return query + queryWhere;
	}

	@Override
	protected String getQueryDelete(int id) {
		return "DELETE FROM RECAMBIO WHERE ID_RECAMBIO=" + id;
	}
}
