package com.taller.jdialog;

import com.taller.beans.VehiculoBean;
import com.taller.negocio.impl.VehiculoBOImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PanelListadoVehiculos extends JDialog {
	@Serial
	private static final long serialVersionUID = 1L;
	private static final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();
	private static List<Object[]> listaVehiculos;
	private static List<Object> listaVehiculosDao;
	private static String filtroTabla = "";
	private static int columnaFiltrada = -1;
	private static JTable tablaItemsPorta;
	private JButton btnSeleccionar;

	public PanelListadoVehiculos(JFrame padre, WindowListener windowListener, boolean esPresupuesto) {
		super(padre,true);
		setResizable(false);
		setTitle("Administración de vehículos");
		setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		setShape(new RoundRectangle2D.Double(0, 0, 1444, 675, 30, 30));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1281, 591);
		getContentPane().setLayout(null);

		tablaItemsPorta = new JTable() {
			public String getToolTipText(MouseEvent e) {
				String tip = null;
				Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				int colIndex = columnAtPoint(p);

				try {
					tip = getValueAt(rowIndex, colIndex)!=null?getValueAt(rowIndex, colIndex).toString():null;
				} catch (RuntimeException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				return tip;
			}
		};
		tablaItemsPorta.getSelectionModel().addListSelectionListener(arg0 -> {
			if (!arg0.getValueIsAdjusting()) {
				btnSeleccionar.setEnabled(tablaItemsPorta.getSelectedRow() >= 0);
			}
		});
		tablaItemsPorta.setModel(new DefaultTableModel(new Object[0][], new String[]{
				"", "Marca", "Modelo", "Año", "Matrícula", "VIN", "Cilindrada", "Caballos", "Tipo", "Combustible", "Cliente", "IdCliente"}){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		cargarTabla();

		tablaItemsPorta.getColumnModel().getColumn(0).setPreferredWidth(40);
		tablaItemsPorta.getColumnModel().getColumn(1).setPreferredWidth(80);
		tablaItemsPorta.getColumnModel().getColumn(2).setPreferredWidth(110);
		tablaItemsPorta.getColumnModel().getColumn(3).setPreferredWidth(50);
		tablaItemsPorta.getColumnModel().getColumn(4).setPreferredWidth(60);
		tablaItemsPorta.getColumnModel().getColumn(4).setPreferredWidth(110);
		tablaItemsPorta.getColumnModel().getColumn(11).setPreferredWidth(0);
		tablaItemsPorta.getColumnModel().getColumn(11).setMaxWidth(0);
		tablaItemsPorta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaItemsPorta.setBounds(10, 82, 919, 330);
		tablaItemsPorta.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int columnaFiltrando = tablaItemsPorta.columnAtPoint(e.getPoint());
				String nombreColumna = tablaItemsPorta.getColumnName(columnaFiltrando);
				String filtro = columnaFiltrando==columnaFiltrada?filtroTabla: "";
				String filtrando = JOptionPane.showInputDialog("Filtrar columna " + nombreColumna, filtro);
				if(filtrando != null){
					cambiarNombreHeader(-1, true);
					filtroTabla = filtrando;
					columnaFiltrada = columnaFiltrando;
					filtrarTabla(columnaFiltrando, filtroTabla);
					if(!filtroTabla.isEmpty()){
						cambiarNombreHeader(columnaFiltrando, false);
					}
				}
			}
		});
		tablaItemsPorta.getColumnModel().removeColumn(tablaItemsPorta.getColumnModel().getColumn(11));

		JScrollPane scrollPaneItemsPorta = new JScrollPane();
		scrollPaneItemsPorta.setViewportBorder(null);
		scrollPaneItemsPorta.setBounds(10, 62, 1247, 482);
		scrollPaneItemsPorta.setViewportView(tablaItemsPorta);
		getContentPane().add(scrollPaneItemsPorta);

		btnSeleccionar = new JButton("Selecionar");
		btnSeleccionar.addActionListener(e -> {
            if(tablaItemsPorta.getSelectedRow() < 0){
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fila antes de continuar");
            }else{
                setVisible(false);
                dispose();
                try {
                    int idVehiculo = (int) tablaItemsPorta.getModel().getValueAt(tablaItemsPorta.getSelectedRow(), 0);
                    VehiculoBean vehiculoPasar = null;
                    for(Object object: listaVehiculosDao){
                        VehiculoBean vehiculo = (VehiculoBean) object;
                        if(idVehiculo == vehiculo.getId()){
                            vehiculoPasar = vehiculo;
                        }
                    }
					if(esPresupuesto){
						new PanelEdicionPresupuesto(padre, null, true, vehiculoPasar, windowListener, true);
					}else{
						new PanelEdicionCita(padre, null, true, vehiculoPasar, windowListener, false);
					}
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
		btnSeleccionar.setEnabled(true);
		btnSeleccionar.setToolTipText("Nuevo vehículo");
		btnSeleccionar.setForeground(Color.WHITE);
		btnSeleccionar.setFocusPainted(false);
		btnSeleccionar.setBackground(new Color(0, 119, 160));
		btnSeleccionar.setBounds(1121, 20, 136, 33);
		btnSeleccionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		getContentPane().add(btnSeleccionar);

		JLabel lblInfo_1 = new JLabel("Es necesario seleccionar un vehículo");
		lblInfo_1.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/INFO.png"))));
		lblInfo_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblInfo_1.setBounds(10, 25, 311, 20);
		lblInfo_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(lblInfo_1);

		// Se visualiza la ventana.
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void cargarTabla() {
		limpiarTabla();
		listaVehiculos = new ArrayList<>();
		listaVehiculosDao = vehiculoBO.buscar(new VehiculoBean());
		for (Object object : listaVehiculosDao) {
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

			((DefaultTableModel) tablaItemsPorta.getModel()).addRow(fila);
			listaVehiculos.add(fila);
		}
	}
	private static void limpiarTabla(){
		int numeroFilas = tablaItemsPorta.getModel().getRowCount();
		for(int i=0; i<numeroFilas; i++) {
			((DefaultTableModel) tablaItemsPorta.getModel()).removeRow(0);
		}
	}

	private void filtrarTabla(int columnaFiltrando, String filtrando){
		limpiarTabla();
		for(Object[] fila: listaVehiculos){
			if(fila[columnaFiltrando].toString().toUpperCase().contains(filtrando.toUpperCase())){
				((DefaultTableModel) tablaItemsPorta.getModel()).addRow(fila);
			}
		}
		tablaItemsPorta.repaint();
		tablaItemsPorta.getTableHeader().repaint();
	}

	private void cambiarNombreHeader(int columna, boolean resetearTodas){
		if(resetearTodas){
			int columnas = tablaItemsPorta.getTableHeader().getColumnModel().getColumnCount();
			for(int i=0; i<columnas; i++){
				String valorHeader = tablaItemsPorta.getTableHeader().getColumnModel().getColumn(i).getHeaderValue().toString().replace(" *", "");
				tablaItemsPorta.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(valorHeader);
			}
		}else{
			String valorHeader = tablaItemsPorta.getTableHeader().getColumnModel().getColumn(columna).getHeaderValue().toString();
			valorHeader = valorHeader + " *";
			tablaItemsPorta.getTableHeader().getColumnModel().getColumn(columna).setHeaderValue(valorHeader);
		}
	}
}