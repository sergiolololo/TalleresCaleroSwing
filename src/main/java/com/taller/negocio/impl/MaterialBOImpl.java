package com.taller.negocio.impl;

import com.taller.beans.ErrorBean;
import com.taller.beans.MaterialBean;
import com.taller.beans.RecambioBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialBOImpl extends ComunImpl {

	public ErrorBean insertarMateriales(List<MaterialBean> materialBeanList, boolean esPresupuesto) {
		ErrorBean errorBean = new ErrorBean();
		errorBean.setMensaje(obtenerMensajeInsertOK(esPresupuesto));
		errorBean.setExisteError(false);

		try {
			for(MaterialBean materialBean : materialBeanList){
				int id = obtenerSecuencia();
				obtenerStatement().executeUpdate(obtenerQueryInsert(id, materialBean));
			}
		} catch (SQLException e) {
			errorBean.setMensaje(obtenerMensajeInsertKO());
			errorBean.setExisteError(true);
		}finally {
			cerrarConexion();
		}
		return errorBean;
	}

	private String obtenerMensajeInsertOK(boolean esPresupuesto) {
		return esPresupuesto?"El presupuesto ha sido creado correctamente":"La cita ha sido creada correctamente";
	}

	@Override
	protected String obtenerQueryInsert(int id, Object objetoInsert) {
		MaterialBean materialBean = (MaterialBean) objetoInsert;
		return "INSERT INTO CITAHASMATERIAL VALUES(" + id + ","
				+ materialBean.getCitaBean().getId() + ",'"
				+ materialBean.getDescripcion() + "',"
				+ materialBean.getCantidad() + ","
				+ materialBean.getPvp()
				+ ",0.00,"
				+ materialBean.getPc() + ","
				+ materialBean.getDescuentoCliente() + ","
				+ materialBean.getImporteCliente()
				+ ",21)";
	}

	public List<MaterialBean> obtenerMaterialesParaAutocompletar() {
		List<MaterialBean> listadoSalida = new ArrayList<MaterialBean>();
		ResultSet resultSet;
		try {
			String consulta = "(SELECT AA.DESCRIPCION AS NOMBRE, AA.PC, AA.PVP AS PV, -1 AS CANTIDAD\n" +
					"FROM CITAHASMATERIAL AA \n" +
					"WHERE AA.ID_CITA_HAS_MATERIAL = (SELECT MAX(BB.ID_CITA_HAS_MATERIAL) FROM CITAHASMATERIAL BB WHERE BB.DESCRIPCION = AA.DESCRIPCION) \n" +
					"AND NOT EXISTS(\n" +
					"SELECT 0\n" +
					"FROM RECAMBIO CC \n" +
					"WHERE CC.NOMBRE = AA.DESCRIPCION)) \n" +
					"UNION \n" +
					"(SELECT NOMBRE, PC, PV, CANTIDAD\n" +
					"FROM RECAMBIO)";

			resultSet = obtenerStatement().executeQuery(consulta);

			while(resultSet.next()) {
				MaterialBean materialBean = new MaterialBean();
				materialBean.setDescripcion(resultSet.getString("NOMBRE"));
				materialBean.setPc(resultSet.getDouble("PC"));
				materialBean.setPvp(resultSet.getDouble("PV"));
				materialBean.setCantidad(resultSet.getDouble("CANTIDAD"));
				listadoSalida.add(materialBean);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			cerrarConexion();
		}
		return listadoSalida;
	}

	public List<RecambioBean> obtenerRecambios() {
		List<RecambioBean> listadoSalida = new ArrayList<RecambioBean>();
		ResultSet resultSet;
		try {
			String consulta = "SELECT ID_RECAMBIO, NOMBRE, CANTIDAD \n" +
					"FROM RECAMBIO";
			resultSet = obtenerStatement().executeQuery(consulta);

			while(resultSet.next()) {
				RecambioBean recambioBean = new RecambioBean();
				recambioBean.setId(resultSet.getInt("ID_RECAMBIO"));
				recambioBean.setNombre(resultSet.getString("NOMBRE"));
				recambioBean.setCantidad(resultSet.getDouble("CANTIDAD"));
				listadoSalida.add(recambioBean);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			cerrarConexion();
		}
		return listadoSalida;
	}

	@Override
	protected String obtenerMensajeInsertOK() {
		return "La cita ha sido creada correctamente";
	}

	@Override
	protected String obtenerMensajeInsertKO() {
		return "Error al insertar la cita";
	}

	@Override
	protected String getQueryDelete(int idCita) {
		return "DELETE FROM CITAHASMATERIAL WHERE ID_CITA=" + idCita;
	}

	@Override
	protected String obtenerQuerySecuencia() {
		return "SELECT MAX(ID_CITA_HAS_MATERIAL) FROM CITAHASMATERIAL";
	}
}
