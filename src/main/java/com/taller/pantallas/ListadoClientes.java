package com.taller.pantallas;

import com.taller.beans.ClienteBean;
import com.taller.jdialog.PanelInsertarCliente;

import javax.swing.*;
import java.io.IOException;
import java.util.List;


public class ListadoClientes extends ComunPantalla {
	public ListadoClientes(JFrame padre, boolean modal) {
		super(padre, modal);
	}

	@Override
	protected String getNombrePantalla() {
		return "Administración de clientes";
	}

	@Override
	protected String[] getNombreColumnas(){
		return new String[]{"#", "Nombre", "Apellido", "Localidad", "Teléfono", "DNI"};
	}

	@Override
	protected void setearAnchoColumnas(){
		tabla.getColumnModel().getColumn(0).setMaxWidth(60);
		tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(60);

		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));
	}

	@Override
	protected void configurarBotonNuevo(JFrame padre) {
		btnNuevo = new JButton("Nuevo cliente");
		btnNuevo.setToolTipText("Nuevo cliente");
		btnNuevo.setBounds(348, 20, 159, 33);
		btnNuevo.addActionListener(e -> {
			try {
				new PanelInsertarCliente(padre, true, null, crearWindowsListener());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	@Override
	protected void configurarBotonEditar(JFrame padre) {
		btnEditar = new JButton("Editar cliente");
		btnEditar.setToolTipText("Editar cliente");
		btnEditar.setBounds(527, 20, 159, 33);
		btnEditar.addActionListener(e -> {
			abrirPanelEdicion(padre);
		});
	}

	@Override
	protected void configurarBotonEliminar() {
		btnEliminar = new JButton("Eliminar cliente");
		btnEliminar.setToolTipText("Eliminar cliente");
		btnEliminar.setBounds(702, 20, 159, 33);
	}

	@Override
	protected void abrirPanelEdicion(JFrame padre) {
		try {
			Object[] fila = new Object[tabla.getModel().getColumnCount()];
			for(int i = 0; i< tabla.getModel().getColumnCount(); i++){
				fila[i] = tabla.getModel().getValueAt(tabla.getSelectedRow(), i);
			}
			new PanelInsertarCliente(padre, false, fila, crearWindowsListener());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected String getMensajeEliminar() {
		return "Se va a elimnar el cliente. ¿Está seguro de que desea continuar?";
	}

	@Override
	protected Object[] aniadirFilaCargarTabla(Object object){
		ClienteBean clienteBean = (ClienteBean) object;
		Object[] fila = new Object[6];
		fila[0] = clienteBean.getId();
		fila[1] = clienteBean.getNombre();
		fila[2] = clienteBean.getApellido();
		fila[3] = clienteBean.getLocalidad();
		fila[4] = clienteBean.getTelefono();
		fila[5] = clienteBean.getDni();
		return fila;
	}

	@Override
	protected Object[] aniadirFilaNoModelCargarTabla(Object object){
		ClienteBean clienteBean = (ClienteBean) object;
		Object[] fila = new Object[5];
		fila[0] = clienteBean.getNombre();
		fila[1] = clienteBean.getApellido();
		fila[2] = clienteBean.getLocalidad();
		fila[3] = clienteBean.getTelefono();
		fila[4] = clienteBean.getDni();
		return fila;
	}

	@Override
	protected List<Object> obtenerListado() {
		return clienteBO.buscar(new ClienteBean());
	}

	@Override
	protected void eliminarPorId(int id){
		clienteBO.borrar((Integer) tabla.getModel().getValueAt(tabla.getSelectedRow(), 0));
	}

	@Override
	protected void llamarCargarTabla() {
		if(PanelInsertarCliente.insertado){
			cargarTabla();
		}
	}
}