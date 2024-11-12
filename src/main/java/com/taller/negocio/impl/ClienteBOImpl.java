package com.taller.negocio.impl;

import com.taller.beans.ClienteBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClienteBOImpl extends ComunImpl {

	@Override
	protected String obtenerQueryInsert(int id, Object objetoInsert) {
		ClienteBean clienteBean = (ClienteBean) objetoInsert;
		return "INSERT INTO cliente VALUES (" + id + ", '" + clienteBean.getNombre() + "', '" + clienteBean.getApellido() + "', '"
				+ clienteBean.getLocalidad() + "', '" + clienteBean.getTelefono() + "');";
	}

	@Override
	protected void rellenarSalidaBuscar(ResultSet resultSet, List<Object> listadoClientes) throws SQLException {
		ClienteBean cliente = new ClienteBean();
		cliente.setId(resultSet.getInt("id_cliente"));
		cliente.setNombre(resultSet.getString("nombre"));
		cliente.setApellido(resultSet.getString("apellido"));
		cliente.setLocalidad(resultSet.getString("localidad"));
		cliente.setTelefono(resultSet.getLong("telefono"));
		listadoClientes.add(cliente);
	}

	@Override
	protected String obtenerMensajeInsertOK() {
		return "El cliente ha sido creado correctamente";
	}

	@Override
	protected String obtenerMensajeInsertKO() {
		return "El cliente que intenta insertar ya existe";
	}

	protected String obtenerQuerySecuencia() {
		return "SELECT MAX(ID_CLIENTE) FROM CLIENTE";
	}

	@Override
	protected String getQueryBuscar(Object objectoBuscar) {
		ClienteBean clienteBean = (ClienteBean) objectoBuscar;
		String consulta = "SELECT * FROM cliente WHERE 1=1";
		if(clienteBean != null) {
			if(clienteBean.getId() != 0) {
				consulta += " AND id_cliente = " + clienteBean.getId();
			}
			if(clienteBean.getNombre() != null) {
				consulta += " AND UPPER(nombre) LIKE '%" + clienteBean.getNombre().toUpperCase() + "%'";
			}
			if(clienteBean.getApellido() != null) {
				consulta += " AND UPPER(apellido) LIKE '%" + clienteBean.getApellido().toUpperCase() + "%'";
			}
			if(clienteBean.getLocalidad() != null) {
				consulta += " AND UPPER(localidad) LIKE '%" + clienteBean.getLocalidad().toUpperCase() + "%'";
			}
			if(clienteBean.getTelefono() != null) {
				consulta += " AND telefono = " + clienteBean.getTelefono();
			}
		}
		consulta += " ORDER BY nombre";
		return consulta;
	}

	@Override
	protected String obtenerMensajeUpdateOK() {
		return "El cliente ha sido modificado correctamente";
	}

	@Override
	protected String obtenerMensajeUpdateKO() {
		return "Error al modificar cliente.";
	}

	@Override
	protected String getQueryUpdate(Object objetoUpdate) {
		ClienteBean clienteBean = (ClienteBean) objetoUpdate;
		return "UPDATE cliente SET nombre='" + clienteBean.getNombre() + "', apellido='" + clienteBean.getApellido()
				+ "', localidad='" + clienteBean.getLocalidad() + "', telefono=" + clienteBean.getTelefono() + " WHERE ID_CLIENTE=" + clienteBean.getId();
	}

	@Override
	protected String getQueryDelete(int id) {
		return "DELETE FROM CLIENTE WHERE ID_CLIENTE=" + id;
	}
}
