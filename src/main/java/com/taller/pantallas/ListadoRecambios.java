package com.taller.pantallas;

import com.taller.beans.RecambioBean;
import com.taller.jdialog.PanelInsertarRecambio;

import javax.swing.*;
import java.io.IOException;
import java.util.List;


public class ListadoRecambios extends ComunPantalla {
	public ListadoRecambios(JFrame padre, boolean modal) {
		super(padre, modal);
	}

	@Override
	protected String getNombrePantalla() {
		return "Administración de recambios";
	}

	@Override
	protected String[] getNombreColumnas(){
		return new String[]{"#", "Referencia", "Nombre", "PC", "PV", "Cantidad"};
	}

	@Override
	protected void setearAnchoColumnas(){
		tabla.getColumnModel().getColumn(0).setMaxWidth(60);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(110);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(60);

		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));
	}

	@Override
	protected void configurarBotonNuevo(JFrame padre) {
		btnNuevo = new JButton("Nuevo recambio");
		btnNuevo.setToolTipText("Nuevo recambio");
		btnNuevo.setBounds(348, 20, 159, 33);
		btnNuevo.addActionListener(e -> {
			try {
				new PanelInsertarRecambio(padre, true, null, crearWindowsListener());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	@Override
	protected void configurarBotonEditar(JFrame padre) {
		btnEditar = new JButton("Editar recambio");
		btnEditar.setToolTipText("Editar recambio");
		btnEditar.setBounds(527, 20, 159, 33);
		btnEditar.addActionListener(e -> {
			try {
				Object[] fila = new Object[tabla.getModel().getColumnCount()];
				for(int i = 0; i< tabla.getModel().getColumnCount(); i++){
					fila[i] = tabla.getModel().getValueAt(tabla.getSelectedRow(), i);
				}
				new PanelInsertarRecambio(padre, false, fila, crearWindowsListener());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	@Override
	protected void configurarBotonEliminar() {
		btnEliminar = new JButton("Eliminar recambio");
		btnEliminar.setToolTipText("Eliminar recambio");
		btnEliminar.setBounds(702, 20, 159, 33);
	}

	@Override
	protected String getMensajeEliminar() {
		return "Se va a elimnar el recambio. ¿Está seguro de que desea continuar?";
	}

	@Override
	protected Object[] aniadirFilaCargarTabla(Object object){
		RecambioBean recambioBean = (RecambioBean) object;
		Object[] fila = new Object[6];
		fila[0] = recambioBean.getId();
		fila[1] = recambioBean.getReferencia();
		fila[2] = recambioBean.getNombre();
		fila[3] = recambioBean.getPc();
		fila[4] = recambioBean.getPv();
		fila[5] = recambioBean.getCantidad();
		return fila;
	}

	@Override
	protected Object[] aniadirFilaNoModelCargarTabla(Object object){
		RecambioBean recambioBean = (RecambioBean) object;
		Object[] fila = new Object[5];
		fila[0] = recambioBean.getReferencia();
		fila[1] = recambioBean.getNombre();
		fila[2] = recambioBean.getPc();
		fila[3] = recambioBean.getPv();
		fila[4] = recambioBean.getCantidad();
		return fila;
	}

	@Override
	protected List<Object> obtenerListado() {
		return recambioBO.buscar(new RecambioBean());
	}

	@Override
	protected void eliminarPorId(int id){
		recambioBO.borrar((Integer) tabla.getModel().getValueAt(tabla.getSelectedRow(), 0));
	}

	@Override
	protected void llamarCargarTabla() {
		if(PanelInsertarRecambio.insertado){
			cargarTabla();
		}
	}
}