package com.taller.pantallas;

import com.taller.beans.VehiculoBean;
import com.taller.jdialog.PanelInsertarVehiculo;

import javax.swing.*;
import java.util.List;


public class ListadoVehiculos extends ComunPantalla {
	public ListadoVehiculos(JFrame padre, boolean modal) {
		super(padre, modal);
	}

	@Override
	protected String getNombrePantalla() {
		return "Administración de vehículos";
	}

	@Override
	protected String[] getNombreColumnas() {
		return new String[]{
				"#", "Marca", "Modelo", "Año", "Matrícula", "VIN", "Cilindrada", "Caballos", "Tipo", "Combustible", "Cliente", "IdCliente"};
	}

	@Override
	protected void setearAnchoColumnas() {
		tabla.getColumnModel().getColumn(0).setMaxWidth(60);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(110);
		/*tabla.getColumnModel().getColumn(11).setPreferredWidth(0);
		tabla.getColumnModel().getColumn(11).setMaxWidth(0);*/
		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(11));
		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));
	}

	@Override
	protected void configurarBotonNuevo(JFrame padre) {
		btnNuevo = new JButton("Nuevo vehículo");
		btnNuevo.addActionListener(e -> new PanelInsertarVehiculo(padre, true, null, crearWindowsListener()));
		btnNuevo.setToolTipText("Nuevo vehículo");
		btnNuevo.setBounds(348, 20, 159, 33);
	}

	@Override
	protected void configurarBotonEditar(JFrame padre) {
		btnEditar = new JButton("Editar vehículo");
		btnEditar.setToolTipText("Editar vehículo");
		btnEditar.addActionListener(e -> {
			Object[] fila = new Object[tabla.getModel().getColumnCount()];
			for(int i = 0; i< tabla.getModel().getColumnCount(); i++){
				fila[i] = tabla.getModel().getValueAt(tabla.getSelectedRow(), i);
			}
			new PanelInsertarVehiculo(padre, false, fila, crearWindowsListener());
		});
		btnEditar.setBounds(527, 20, 159, 33);
	}

	@Override
	protected void configurarBotonEliminar() {
		btnEliminar = new JButton("Eliminar vehículo");
		btnEliminar.setToolTipText("Eliminar vehículo");
		btnEliminar.setBounds(702, 20, 159, 33);
	}

	@Override
	protected String getMensajeEliminar() {
		return "Se va a elimnar el vehículo. ¿Está seguro de que desea continuar?";
	}

	@Override
	protected Object[] aniadirFilaCargarTabla(Object object) {
		VehiculoBean vehiculo = (VehiculoBean) object;
		Object[] fila = new Object[12];
		fila[0] = vehiculo.getId();
		fila[1] = vehiculo.getMarca();
		fila[2] = vehiculo.getModelo();
		fila[3] = vehiculo.getAnio();
		fila[4] = vehiculo.getMatricula();
		fila[5] = vehiculo.getVin();
		fila[6] = vehiculo.getCentimetros();
		fila[7] = vehiculo.getCaballos();
		fila[8] = vehiculo.getTipo();
		fila[9] = vehiculo.getCombustible();
		if(vehiculo.getClienteBean()!= null && vehiculo.getClienteBean().getNombre()!= null && vehiculo.getClienteBean().getApellido()!= null) {
			fila[10] = vehiculo.getClienteBean().getNombre() + " " + vehiculo.getClienteBean().getApellido();
		}else{
			fila[10] = "";
		}
		fila[11] = vehiculo.getClienteBean().getId();
		return fila;
	}

	@Override
	protected Object[] aniadirFilaNoModelCargarTabla(Object object) {
		VehiculoBean vehiculo = (VehiculoBean) object;
		Object[] fila = new Object[10];
		fila[0] = vehiculo.getMarca();
		fila[1] = vehiculo.getModelo();
		fila[2] = vehiculo.getAnio();
		fila[3] = vehiculo.getMatricula();
		fila[4] = vehiculo.getVin();
		fila[5] = vehiculo.getCentimetros();
		fila[6] = vehiculo.getCaballos();
		fila[7] = vehiculo.getTipo();
		fila[8] = vehiculo.getCombustible();
		if(vehiculo.getClienteBean()!= null && vehiculo.getClienteBean().getNombre()!= null && vehiculo.getClienteBean().getApellido()!= null) {
			fila[9] = vehiculo.getClienteBean().getNombre() + " " + vehiculo.getClienteBean().getApellido();
		}else{
			fila[9] = "";
		}
		return fila;
	}

	@Override
	protected List<Object> obtenerListado() {
		return vehiculoBO.buscar(new VehiculoBean());
	}

	@Override
	protected void eliminarPorId(int id) {
		vehiculoBO.borrar((Integer) tabla.getModel().getValueAt(tabla.getSelectedRow(), 0));
	}

	@Override
	protected void llamarCargarTabla() {
		if(PanelInsertarVehiculo.insertado){
			cargarTabla();
		}
	}
}