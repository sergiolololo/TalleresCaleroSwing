package com.taller.negocio.impl;

import com.taller.beans.CitaBean;
import com.taller.beans.ClienteBean;
import com.taller.beans.MaterialBean;
import com.taller.beans.VehiculoBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CitaBOImpl extends  ComunImpl {
	private final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();
	
	public CitaBean insertarCita(CitaBean citaBean) {
		try {
			int id;
			if(citaBean.getId() > 0){
				id = citaBean.getId();
			}else{
				id = obtenerSecuencia();
			}
			obtenerStatement().executeUpdate(obtenerQueryInsert(id, citaBean));
			citaBean.setId(id);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally {
			cerrarConexion();
		}
		return citaBean;
	}

	@Override
	protected String obtenerQueryInsert(int id, Object objetoInsert) {
		CitaBean citaBean = (CitaBean) objetoInsert;
        return "INSERT INTO cita VALUES ("
				+ id + ", "
				+ citaBean.getVehiculoBean().getId() + ", "
				+ "'" + citaBean.getDescripcion() + "', "
				+ "'" + citaBean.getFechaString() + "', "
				+ "'12:00:00', "
				+ citaBean.getKilometros() + ", "
				+ citaBean.getTiempo() + ", "
				+ citaBean.getPrecioHora() + ", "
				+ citaBean.getDescuento() + ", "
				+ citaBean.getImporteTiempo() + ", "
				+ citaBean.getImporteTotal() + ", "
				+ citaBean.getPagado() + ", "
				+ "'" + citaBean.getObservaciones() + "', "
				+ citaBean.getImportePagado() + ", "
				+ citaBean.getImportePendiente() + ", "
				+ citaBean.isEsPresupuesto()
				+ ")";
	}

	@Override
	protected String getQueryBuscar(Object objetoBuscar) {
		String consulta = "SELECT aa.*, bb.marca, bb.modelo, cc.id_cliente, cc.nombre, cc.apellido FROM cita aa JOIN vehiculo bb ON bb.id_vehiculo = aa.id_vehiculo JOIN cliente cc ON cc.id_cliente = bb.id_cliente WHERE 1=1";
		String orderBy = " ORDER BY aa.fecha DESC";

		CitaBean citaBean = (CitaBean) objetoBuscar;
		if(citaBean != null) {
			if(citaBean.getId() != 0) {
				consulta += " AND aa.id_cita = " + citaBean.getId();
			}else {
				if(citaBean.getVehiculoBean() != null && citaBean.getVehiculoBean().getId() != 0) {
					consulta += " AND aa.id_vehiculo = " + citaBean.getVehiculoBean().getId();
				}else {
					if(citaBean.getVehiculoBean() != null && citaBean.getVehiculoBean().getClienteBean() != null && citaBean.getVehiculoBean().getClienteBean().getId() != 0 ){
						consulta += " AND cc.id_cliente = " + citaBean.getVehiculoBean().getClienteBean().getId();
					}
				}
				consulta += " AND aa.ES_PRESUPUESTO = " + citaBean.isEsPresupuesto();
			}
		}
		return consulta+orderBy;
	}

	@Override
	protected void rellenarSalidaBuscar(ResultSet resultSet, List<Object> listadoSalida) throws SQLException {
		CitaBean cita = new CitaBean();
		cita.setId(resultSet.getInt("ID_CITA"));
		cita.setDescripcion(resultSet.getString("DESCRIPCION"));
		cita.setFecha(resultSet.getDate("FECHA"));
		cita.setKilometros(resultSet.getInt("KILOMETROS"));
		cita.setTiempo(resultSet.getDouble("TIEMPO"));
		cita.setPrecioHora(resultSet.getDouble("PRECIO_HORA"));
		cita.setDescuento(resultSet.getDouble("DESCUENTO"));
		cita.setImporteTiempo(resultSet.getDouble("IMPORTE_TIEMPO"));
		cita.setImporteTotal(resultSet.getDouble("IMPORTE_TOTAL"));
		cita.setPagado(resultSet.getInt("PAGADO"));
		cita.setObservaciones(resultSet.getString("OBSERVACIONES"));
		cita.setImportePagado(resultSet.getDouble("IMPORTE_PAGADO"));
		cita.setImportePendiente(resultSet.getDouble("FALTA_PAGAR"));
		cita.setEsPresupuesto(resultSet.getBoolean("ES_PRESUPUESTO"));
		cita.setVehiculoBean(new VehiculoBean());
		cita.getVehiculoBean().setId(resultSet.getInt("id_vehiculo"));
		cita.getVehiculoBean().setMarca(resultSet.getString("marca"));
		cita.getVehiculoBean().setModelo(resultSet.getString("modelo"));
		cita.getVehiculoBean().setClienteBean(new ClienteBean());
		cita.getVehiculoBean().getClienteBean().setId(resultSet.getInt("id_cliente"));
		cita.getVehiculoBean().getClienteBean().setNombre(resultSet.getString("nombre"));
		cita.getVehiculoBean().getClienteBean().setApellido(resultSet.getString("apellido"));
		listadoSalida.add(cita);
	}

	@Override
	protected String getQueryUpdate(Object objetoUpdate) {
		CitaBean citaBean = (CitaBean) objetoUpdate;
		return "UPDATE CITA SET ES_PRESUPUESTO = " + citaBean.isEsPresupuesto() + " WHERE ID_CITA = " + citaBean.getId();
	}

	@Override
	protected String obtenerMensajeUpdateOK() {
		return "La cita ha sido actualizada correctamente";
	}

	@Override
	protected String obtenerMensajeUpdateKO() {
		return "Error al actualizar la cita";
	}

	@Override
	protected String getQueryDelete(int id) {
		return "DELETE FROM CITA WHERE ID_CITA=" + id;
	}

	@Override
	protected String obtenerQuerySecuencia() {
		return "SELECT MAX(ID_CITA) FROM CITA";
	}
	
	public List<Object[]> obtenerGananciasPorAnio(boolean esMensual, VehiculoBean vehiculoBean) throws SQLException {

		List<Object[]> listadoSalida = new ArrayList<>();
		ResultSet resultSet = null;
		try {
			String select;
			if(esMensual){
				select = "(YEAR(aa.fecha) * 100) + MONTH(aa.fecha)";
			}else{
				select = "YEAR(aa.fecha)";
			}
			String groupBy = "GROUP BY " + select + "\n" +
					"ORDER BY yyyyMM";

			String consulta = "SELECT " + select + " AS yyyyMM, " +
					"SUM((bb.PC*bb.CANTIDAD) + (bb.PC*bb.CANTIDAD)*bb.IVA/100) as sumaPC, " +
					"SUM(((bb.PVP*bb.CANTIDAD)-(bb.PVP*bb.CANTIDAD*DESCUENTO_CLIENTE/100)) + ((bb.PVP*bb.CANTIDAD)-(bb.PVP*bb.CANTIDAD*DESCUENTO_CLIENTE/100))*bb.IVA/100) as sumaPVP \n" +
					"FROM CITA aa LEFT JOIN CITAHASMATERIAL bb ON bb.ID_CITA = aa.ID_CITA \n" +
					"JOIN VEHICULO cc ON cc.ID_VEHICULO = aa.ID_VEHICULO \n" +
					"JOIN CLIENTE dd ON dd.ID_CLIENTE = cc.ID_CLIENTE \n" +
					"WHERE aa.ES_PRESUPUESTO = false \n";

			if(vehiculoBean != null && vehiculoBean.getClienteBean() != null && vehiculoBean.getClienteBean().getNombre() != null){
				consulta += "AND (dd.NOMBRE || ' ' || dd.APELLIDO) = '" + vehiculoBean.getClienteBean().getNombre() + "' \n";
				//consulta += "AND dd.APELLIDO = '" + vehiculoBean.getClienteBean().getApellido() + "' \n";
			}
			if (vehiculoBean != null && vehiculoBean.getMarca() != null) {
				consulta += "AND (cc.MARCA || ' ' || cc.MODELO) = '" + vehiculoBean.getMarca() + "' \n";
				//consulta += "AND cc.MODELO = '" + vehiculoBean.getModelo() + "' \n";
			}
			consulta += groupBy;
			resultSet = obtenerStatement().executeQuery(consulta);

			while(resultSet.next()) {
				Object[] fila = new Object[7];
				fila[0] = resultSet.getInt("yyyyMM");
				fila[1] = resultSet.getDouble("sumaPC");
				fila[2] = resultSet.getDouble("sumaPVP");
				listadoSalida.add(fila);
			}

			consulta = "SELECT " + select + " AS yyyyMM, " +
					"SUM(aa.IMPORTE_TIEMPO) as importeTiempo, " +
					"SUM(aa.IMPORTE_TOTAL) as importeTotal, " +
					"SUM(aa.IMPORTE_PAGADO) as importePagado, " +
					"SUM(aa.FALTA_PAGAR) as faltaPagar \n" +
					"FROM CITA aa \n" +
					"JOIN VEHICULO bb ON bb.ID_VEHICULO = aa.ID_VEHICULO \n" +
					"JOIN CLIENTE cc ON cc.ID_CLIENTE = bb.ID_CLIENTE \n" +
					"WHERE aa.ES_PRESUPUESTO = false \n";

			if(vehiculoBean != null && vehiculoBean.getClienteBean() != null && vehiculoBean.getClienteBean().getNombre() != null){
				consulta += "AND (cc.NOMBRE || ' ' || cc.APELLIDO) = '" + vehiculoBean.getClienteBean().getNombre() + "' \n";
				//consulta += "AND cc.APELLIDO = '" + vehiculoBean.getClienteBean().getApellido() + "' \n";
			}
			if (vehiculoBean != null && vehiculoBean.getMarca() != null) {
				consulta += "AND (bb.MARCA || ' ' || bb.MODELO) = '" + vehiculoBean.getMarca() + "' \n";
				//consulta += "AND bb.MODELO = '" + vehiculoBean.getModelo() + "' \n";
			}
			consulta += groupBy;
			resultSet = obtenerStatement().executeQuery(consulta);

			int i=0;
			while(resultSet.next()) {
				listadoSalida.get(i)[2] = (Double) listadoSalida.get(i)[2] + resultSet.getDouble("importeTiempo");
				listadoSalida.get(i)[4] = resultSet.getDouble("importeTotal");
				listadoSalida.get(i)[5] = resultSet.getDouble("importePagado");

				double faltaPagar = resultSet.getDouble("faltaPagar");
				listadoSalida.get(i)[6] = faltaPagar < 0 ? 0.00 : faltaPagar;

				listadoSalida.get(i)[3] = (Double) listadoSalida.get(i)[5] - (Double) listadoSalida.get(i)[1];
				i++;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if(resultSet != null && !resultSet.isClosed()){
				resultSet.close();
			}
			cerrarConexion();
		}
		return listadoSalida;
	}

	public CitaBean obtenerDetalleCita(int idCita) {
		CitaBean cita = new CitaBean();
		cita.setId(idCita);
		cita = (CitaBean) buscar(cita).get(0);
		cita = buscarMaterialesDeCita(cita);

		VehiculoBean vehiculoBean = new VehiculoBean();
		vehiculoBean.setId(cita.getVehiculoBean().getId());
		vehiculoBean = (VehiculoBean) vehiculoBO.buscar(vehiculoBean).get(0);
		cita.setVehiculoBean(vehiculoBean);

		return cita;
	}

	public CitaBean buscarMaterialesDeCita(CitaBean citaBean) {
		List<MaterialBean> listadoMateriales = new ArrayList<>();
		ResultSet resultSet;

		try {
			String consulta = "SELECT * FROM CITAHASMATERIAL WHERE id_cita = " + citaBean.getId();
			resultSet = obtenerStatement().executeQuery(consulta);

			double subTotal = 0;
			while(resultSet.next()) {
				MaterialBean materialBean = new MaterialBean();
				materialBean.setId(resultSet.getInt("ID_CITA_HAS_MATERIAL"));
				materialBean.setDescripcion(resultSet.getString("DESCRIPCION"));
				materialBean.setCantidad(resultSet.getDouble("CANTIDAD"));
				materialBean.setPvp(resultSet.getDouble("PVP"));
				materialBean.setPc(resultSet.getDouble("PC"));
				materialBean.setDescuentoCliente(resultSet.getDouble("DESCUENTO_CLIENTE"));
				materialBean.setImporteCliente(resultSet.getDouble("IMPORTE_CLIENTE"));
				materialBean.setIva(resultSet.getDouble("IVA"));
				listadoMateriales.add(materialBean);

				subTotal += materialBean.getImporteCliente();
			}
			citaBean.setMaterialBeanList(listadoMateriales);

			double iva =subTotal*21/100;
			subTotal = round(subTotal);
			iva = round(iva);

			citaBean.setSubtotal(subTotal);
			citaBean.setIva(iva);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			cerrarConexion();
		}
		return citaBean;
	}

	public List<CitaBean> obtenerClientesConRevisionDentroDeUnMes() throws SQLException {
		List<CitaBean> resultado = new ArrayList<>();
		ResultSet resultSet = null;
		try {
			String consulta = "SELECT cc.ID_CLIENTE, cc.NOMBRE, cc.APELLIDO, bb.ID_VEHICULO, bb.MARCA, bb.MODELO, aa.ID_CITA, aa.FECHA \n" +
					"FROM CLIENTE cc \n" +
					"JOIN VEHICULO bb ON bb.ID_CLIENTE = cc.ID_CLIENTE \n" +
					"JOIN CITA aa ON aa.ID_VEHICULO = bb.ID_VEHICULO \n" +
					"WHERE (sysdate - aa.FECHA) >= 335 AND (sysdate - aa.FECHA) <= 366 \n" +
					"AND (upper(aa.DESCRIPCION) LIKE '%REVISION%' OR upper(aa.DESCRIPCION) LIKE '%REVISIÓN%')";
			resultSet = obtenerStatement().executeQuery(consulta);

			while(resultSet.next()) {
				ClienteBean clienteBean = new ClienteBean();
				clienteBean.setId(resultSet.getInt("ID_CLIENTE"));
				clienteBean.setNombre(resultSet.getString("NOMBRE"));
				clienteBean.setApellido(resultSet.getString("APELLIDO"));
				VehiculoBean vehiculoBean = new VehiculoBean();
				vehiculoBean.setId(resultSet.getInt("ID_VEHICULO"));
				vehiculoBean.setMarca(resultSet.getString("MARCA"));
				vehiculoBean.setModelo(resultSet.getString("MODELO"));
				vehiculoBean.setClienteBean(clienteBean);
				CitaBean citaBean = new CitaBean();
				citaBean.setId(resultSet.getInt("ID_CITA"));
				citaBean.setFecha(resultSet.getDate("FECHA"));
				citaBean.setVehiculoBean(vehiculoBean);
				resultado.add(citaBean);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if(resultSet != null && !resultSet.isClosed()){
				resultSet.close();
			}
			cerrarConexion();
		}
		return resultado;
	}

	private double round(double value) {
		long factor = (long) Math.pow(10, 2);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}