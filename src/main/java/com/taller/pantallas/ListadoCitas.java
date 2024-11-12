package com.taller.pantallas;

import com.taller.beans.CitaBean;
import com.taller.jdialog.PanelEdicionCita;
import com.taller.jdialog.PanelListadoVehiculos;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

public class ListadoCitas extends ComunPantalla {
	public ListadoCitas(JFrame padre, boolean modal) {
		super(padre, modal);
	}

	@Override
	protected String getNombrePantalla() {
		return "Administración de citas";
	}

	@Override
	protected String[] getNombreColumnas() {
		return new String[]{
				"#", "Descripción", "Fecha", "Km", "Tiempo(h)", "Precio/hora", "Descuento", "Importe/tiempo", "Importe total", "Pagado", "Vehículo", "Cliente", "idVehiculo", "idCliente", "PagadoInt", "Observaciones", "IMPORTE_PAGADO"};
	}

	@Override
	protected void setearAnchoColumnas() {
		tabla.getColumnModel().getColumn(0).setPreferredWidth(20);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(70);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(50);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(60);
		tabla.getColumnModel().getColumn(5).setPreferredWidth(70);
		tabla.getColumnModel().getColumn(6).setPreferredWidth(70); //DESCUENTO
		tabla.getColumnModel().getColumn(7).setPreferredWidth(75);
		tabla.getColumnModel().getColumn(8).setPreferredWidth(70);
		tabla.getColumnModel().getColumn(9).setPreferredWidth(50);

		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(16));
		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(12));
		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(12));
		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(12));
		tabla.getColumnModel().removeColumn(tabla.getColumnModel().getColumn(0));
	}

	@Override
	protected void configurarBotonNuevo(JFrame padre) {
		btnNuevo = new JButton("Nueva cita");
		btnNuevo.addActionListener(e -> new PanelListadoVehiculos(padre, crearWindowsListener(), false));
		btnNuevo.setToolTipText("Nueva cita");
		btnNuevo.setBounds(424, 30, 159, 33);
	}

	@Override
	protected void configurarBotonEditar(JFrame padre) {
		btnEditar = new JButton("Ver / editar cita");
		btnEditar.setToolTipText("Editar cita");
		btnEditar.setBounds(636, 30, 159, 33);
		btnEditar.addActionListener(e -> {
			try {
				Object[] fila = new Object[tabla.getModel().getColumnCount()];
				for(int i = 0; i< tabla.getModel().getColumnCount(); i++){
					fila[i] = tabla.getModel().getValueAt(tabla.getSelectedRow(), i);
				}
				new PanelEdicionCita(padre, fila, false, null, crearWindowsListener(), false);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

	@Override
	protected void configurarBotonEliminar() {
		btnEliminar = new JButton("Eliminar cita");
		btnEliminar.setToolTipText("Eliminar cita");
		btnEliminar.setBounds(849, 30, 159, 33);
	}

	@Override
	protected String getMensajeEliminar() {
		return "Se va a elimnar la cita. ¿Está seguro de que desea continuar?";
	}

	@Override
	protected Object[] aniadirFilaCargarTabla(Object object) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		CitaBean cita = (CitaBean) object;
		Object[] fila = new Object[17];
		fila[0] = cita.getId();
		fila[1] = cita.getDescripcion();
		fila[2] = cita.getFecha()!=null?sdf.format(cita.getFecha()):"";
		fila[3] = cita.getKilometros();
		fila[4] = cita.getTiempo();
		fila[5] = cita.getPrecioHora();
		fila[6] = cita.getDescuento();
		fila[7] = cita.getImporteTiempo();
		fila[8] = cita.getImporteTotal();

		ImageIcon imagen;
		if(cita.getPagado()==0){
			imagen = new ImageIcon(Objects.requireNonNull(ListadoCitas.class.getClassLoader().getResource("images/nopagado.png")));
		}else if(cita.getPagado()==1){
			imagen = new ImageIcon(Objects.requireNonNull(ListadoCitas.class.getClassLoader().getResource("images/pagado.png")));
		}else {
			imagen = new ImageIcon(Objects.requireNonNull(ListadoCitas.class.getClassLoader().getResource("images/warning.png")));
		}

		fila[9] = imagen;
		fila[10] = cita.getVehiculoBean().getMarca() + " " + cita.getVehiculoBean().getModelo();
		fila[11] = cita.getVehiculoBean().getClienteBean().getNombre() + " " + cita.getVehiculoBean().getClienteBean().getApellido();
		fila[12] = cita.getVehiculoBean().getId();
		fila[13] = cita.getVehiculoBean().getClienteBean().getId();
		fila[14] = cita.getPagado();
		fila[15] = cita.getObservaciones();
		fila[16] = cita.getImportePagado();

		return fila;
	}

	@Override
	protected Object[] aniadirFilaNoModelCargarTabla(Object object) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		CitaBean cita = (CitaBean) object;
		Object[] fila = new Object[12];
		fila[0] = cita.getDescripcion();
		fila[1] = cita.getFecha()!=null?sdf.format(cita.getFecha()):"";
		fila[2] = cita.getKilometros();
		fila[3] = cita.getTiempo();
		fila[4] = cita.getPrecioHora();
		fila[5] = cita.getDescuento();
		fila[6] = cita.getImporteTiempo();
		fila[7] = cita.getImporteTotal();

		ImageIcon imagen;
		if(cita.getPagado()==0){
			imagen = new ImageIcon(Objects.requireNonNull(ListadoCitas.class.getClassLoader().getResource("images/nopagado.png")));
		}else if(cita.getPagado()==1){
			imagen = new ImageIcon(Objects.requireNonNull(ListadoCitas.class.getClassLoader().getResource("images/pagado.png")));
		}else {
			imagen = new ImageIcon(Objects.requireNonNull(ListadoCitas.class.getClassLoader().getResource("images/warning.png")));
		}

		fila[8] = imagen;
		fila[9] = cita.getVehiculoBean().getMarca() + " " + cita.getVehiculoBean().getModelo();
		fila[10] = cita.getVehiculoBean().getClienteBean().getNombre() + " " + cita.getVehiculoBean().getClienteBean().getApellido();
		fila[11] = cita.getObservaciones();

		return fila;
	}

	@Override
	protected List<Object> obtenerListado() {
		return citaBO.buscar(new CitaBean());
	}

	@Override
	protected void eliminarPorId(int id) {
		materialBO.borrar(id);
		citaBO.borrar(id);
	}

	@Override
	protected void configurarBounds(){
		jDialog.setShape(new RoundRectangle2D.Double(0, 0, 1468, 678, 30, 30));
		jDialog.setBounds(100, 100, 1468, 678);
		scrollPane.setBounds(10, 83, 1434, 536);
		btnNuevo.setBounds(424, 40, 159, 33);
		btnEditar.setBounds(636, 40, 159, 33);
		btnEliminar.setBounds(849, 40, 159, 33);
	}

	@Override
	protected void llamarCargarTabla() {
		if(PanelEdicionCita.insertado){
			cargarTabla();
		}
	}
}