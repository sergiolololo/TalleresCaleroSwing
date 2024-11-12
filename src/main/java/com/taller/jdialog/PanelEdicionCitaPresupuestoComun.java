package com.taller.jdialog;

import com.taller.beans.*;
import com.taller.negocio.impl.CitaBOImpl;
import com.taller.negocio.impl.MaterialBOImpl;
import com.taller.negocio.impl.RecambioBOImpl;
import com.taller.renderer.CellRenderer;
import com.taller.renderer.DateLabelFormatter;
import com.taller.utils.Utilidades;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;


public abstract class PanelEdicionCitaPresupuestoComun extends JDialog {
	@Serial
	private static final long serialVersionUID = 1L;
	protected final JDialog thisJdialog;

	protected static final CitaBOImpl citaBO = new CitaBOImpl();
	private static final MaterialBOImpl materialBO = new MaterialBOImpl();
	private static final RecambioBOImpl recambioBO = new RecambioBOImpl();

	public static JTable tablaMateriales;
	private JButton btnEliminar, btnEditar;
	private static JButton btnRehacer, btnDeshacer;
    protected JButton btnImagen;
	private final JLabel lblNombre;
	private final JLabel lblApellido;
	private final JLabel lblTelefono;
	private final JLabel lblMarca;
	private final JLabel lblMatricula;
	private final JLabel lblVin;
	private final JLabel lblAnio;
	protected final JLabel lblNumeroFactura;
	private final JTextArea txtObservaciones;
    private final JTextField txtKm;
	private final UtilDateModel model;
    private final JTextField txtDescripcion;
    protected static JTextField txtImportePagado;
	protected static JTextField txtFaltaPagar;
	public static boolean insertado = false;
	private static List<List<Object[]>> cacheTablaDeshacer;
	private static List<List<Object[]>> cacheTablaRehacer;

	private CitaBean datosCita;

	public PanelEdicionCitaPresupuestoComun(JFrame padre, Object[] filaCita, boolean esNueva, VehiculoBean vehiculoPasar, WindowListener windowListener, boolean esPresupuesto) throws IOException {
		thisJdialog = new JDialog(padre, true);
		thisJdialog.setResizable(false);
		thisJdialog.setTitle("Edición de cita/presupuesto");
		thisJdialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		thisJdialog.setShape(new RoundRectangle2D.Double(0, 0, 1848, 994, 30, 30));
		thisJdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		thisJdialog.setSize(1848, 994);
		thisJdialog.getContentPane().setLayout(null);

		tablaMateriales = new JTable() {
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

		tablaMateriales.getSelectionModel().addListSelectionListener(arg0 -> {
			if (!arg0.getValueIsAdjusting()) {
				if(tablaMateriales.getSelectedRow() < tablaMateriales.getRowCount()-3) {
					btnEditar.setEnabled(true);
                    btnEliminar.setEnabled(tablaMateriales.getSelectedRow() > 0);
				}else {
					btnEliminar.setEnabled(false);
                    btnEditar.setEnabled(false);
				}
				if(tablaMateriales.getSelectedRow() < 0){
					btnEditar.setEnabled(false);
					btnEliminar.setEnabled(false);
				}
			}
		});
		tablaMateriales.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table =(JTable) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					if(btnEditar.isEnabled()){
						try {
							Object[] fila = new Object[8];
							for(int i = 0; i< tablaMateriales.getModel().getColumnCount(); i++){
								fila[i] = tablaMateriales.getModel().getValueAt(tablaMateriales.getSelectedRow(), i);
							}
							new PanelInsertarMaterial(padre, false, fila, tablaMateriales.getSelectedRow());
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
			}
		});

		tablaMateriales.setModel(new DefaultTableModel(new Object[0][], new String[]{
				"", "Descripción", "PC", "PV", "Descuento (%)", "Cantidad", "Importe"}){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
			@Override
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		});
		tablaMateriales.getColumnModel().removeColumn(tablaMateriales.getColumnModel().getColumn(0));
		/*tablaMateriales.getColumnModel().getColumn(6).setPreferredWidth(50);
		tablaMateriales.getColumnModel().getColumn(6).setMaxWidth(50);*/
		tablaMateriales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaMateriales.setBounds(10, 82, 919, 330);

		JScrollPane scrollPaneItemsPorta = new JScrollPane();
		scrollPaneItemsPorta.setViewportBorder(null);
		scrollPaneItemsPorta.setBounds(26, 380, 1786, 390);
		scrollPaneItemsPorta.setViewportView(tablaMateriales);
		thisJdialog.getContentPane().add(scrollPaneItemsPorta);
		
		this.btnImagen = new JButton();
		txtFaltaPagar = new JTextField();
		txtImportePagado = new JTextField();
		
		txtObservaciones = new JTextArea();
		JScrollPane aaa = new JScrollPane();
		aaa.setViewportBorder(null);
		aaa.setBounds(26, 819, 1786, 48);
		aaa.setViewportView(txtObservaciones);
		thisJdialog.getContentPane().add(aaa);
		
		JLabel lblNewLabel = new JLabel("Observaciones:");
		lblNewLabel.setBounds(26, 796, 159, 13);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		thisJdialog.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBounds(26, 22, 294, 101);
		panel.setBorder(new EtchedBorder(1, null, null));
		thisJdialog.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Datos del cliente:");
		lblNewLabel_1.setBounds(10, 10, 129, 13);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel.add(lblNewLabel_1);
		
		lblNombre = new JLabel("Nombre: ?");
		lblNombre.setBounds(10, 33, 274, 13);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(lblNombre);
		
		lblApellido = new JLabel("Apellido: ?");
		lblApellido.setBounds(10, 56, 274, 13);
		lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(lblApellido);
		
		lblTelefono = new JLabel("Teléfono: ?");
		lblTelefono.setBounds(10, 79, 274, 13);
		lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(lblTelefono);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK);
		separator.setBounds(26, 175, 1786, 7);
		thisJdialog.getContentPane().add(separator);
		
		JLabel lblNewLabel_5 = new JLabel("Número de factura");
		lblNewLabel_5.setBounds(36, 133, 254, 13);
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		thisJdialog.getContentPane().add(lblNewLabel_5);
		
		lblNumeroFactura = new JLabel("");
		lblNumeroFactura.setBounds(36, 150, 179, 13);
		lblNumeroFactura.setFont(new Font("Tahoma", Font.BOLD, 13));
		thisJdialog.getContentPane().add(lblNumeroFactura);
		
		JLabel lblNewLabel_6 = new JLabel("Fecha de factura");
		lblNewLabel_6.setBounds(1579, 111, 179, 13);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		thisJdialog.getContentPane().add(lblNewLabel_6);
		
		model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.setBounds(1579, 133, 158, 20);
		thisJdialog.getContentPane().add(datePicker);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(26, 200, 294, 149);
		panel_1.setBorder(new EtchedBorder(1, null, null));
		thisJdialog.getContentPane().add(panel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Datos del vehículo:");
		lblNewLabel_1_1.setBounds(10, 10, 187, 13);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(lblNewLabel_1_1);
		
		lblMarca = new JLabel("");
		lblMarca.setBounds(10, 33, 274, 13);
		lblMarca.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(lblMarca);
		
		lblMatricula = new JLabel("Matrícula: ?");
		lblMatricula.setBounds(10, 56, 274, 13);
		lblMatricula.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(lblMatricula);
		
		lblVin = new JLabel("VIN: ?");
		lblVin.setBounds(10, 79, 274, 13);
		lblVin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(lblVin);
		
		lblAnio = new JLabel("Año: ?");
		lblAnio.setBounds(10, 100, 274, 13);
		lblAnio.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(lblAnio);
		
		JLabel lblKm = new JLabel("Kilómetros:");
		lblKm.setBounds(10, 123, 78, 13);
		lblKm.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(lblKm);

		txtKm = new JTextField();
		txtKm.setBounds(82, 121, 96, 19);
		panel_1.add(txtKm);
		txtKm.setColumns(10);
		
		JButton btnAniadir = new JButton("Añadir");
		btnAniadir.setToolTipText("Añadir material");
		btnAniadir.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/aniadir.png"))));
		btnAniadir.setOpaque(true);
		btnAniadir.setHorizontalAlignment(SwingConstants.LEFT);
		btnAniadir.setForeground(Color.BLACK);
		btnAniadir.setFont(new Font("Arial", Font.BOLD, 10));
		btnAniadir.setFocusPainted(false);
		btnAniadir.setContentAreaFilled(false);
		btnAniadir.setBorderPainted(false);
		btnAniadir.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnAniadir.setBounds(620, 306, 135, 64);
		btnAniadir.addActionListener(e -> {
            try {
                new PanelInsertarMaterial(padre, true, null, null);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
		thisJdialog.getContentPane().add(btnAniadir);
		
		btnEditar = new JButton("Editar");
		btnEditar.setEnabled(false);
		btnEditar.setToolTipText("Editar material");
		btnEditar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/editar.png"))));
		btnEditar.setOpaque(true);
		btnEditar.setHorizontalAlignment(SwingConstants.LEFT);
		btnEditar.setForeground(Color.BLACK);
		btnEditar.setFont(new Font("Arial", Font.BOLD, 10));
		btnEditar.setFocusPainted(false);
		btnEditar.setContentAreaFilled(false);
		btnEditar.setBorderPainted(false);
		btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEditar.setBounds(765, 306, 135, 64);
		btnEditar.addActionListener(e -> {
            try {
                Object[] fila = new Object[tablaMateriales.getModel().getColumnCount()];
                for(int i = 0; i< tablaMateriales.getModel().getColumnCount(); i++){
                    fila[i] = tablaMateriales.getModel().getValueAt(tablaMateriales.getSelectedRow(), i);
                }
                new PanelInsertarMaterial(padre, false, fila, tablaMateriales.getSelectedRow());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
		thisJdialog.getContentPane().add(btnEditar);
		
		btnEliminar = new JButton("Eliminar");
		btnEliminar.setEnabled(false);
		btnEliminar.setToolTipText("Eliminar material");
		btnEliminar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/borrar.png"))));
		btnEliminar.setOpaque(true);
		btnEliminar.setHorizontalAlignment(SwingConstants.LEFT);
		btnEliminar.setForeground(Color.BLACK);
		btnEliminar.setFont(new Font("Arial", Font.BOLD, 10));
		btnEliminar.setFocusPainted(false);
		btnEliminar.setContentAreaFilled(false);
		btnEliminar.setBorderPainted(false);
		btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEliminar.setBounds(910, 306, 135, 64);
		btnEliminar.addActionListener(e -> eliminarMaterial());
		thisJdialog.getContentPane().add(btnEliminar);

        JButton btnFactura = new JButton("Factura");
		btnFactura.setToolTipText("Generar factura");
		btnFactura.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/factura.png"))));
		btnFactura.setOpaque(true);
		btnFactura.setHorizontalAlignment(SwingConstants.LEFT);
		btnFactura.setForeground(Color.BLACK);
		btnFactura.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnFactura.setFocusPainted(false);
		btnFactura.setContentAreaFilled(false);
		btnFactura.setBorderPainted(false);
		btnFactura.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnFactura.setBounds(879, 877, 230, 64);
		btnFactura.addActionListener(e -> {
			try {
				generarPlantillaFactura();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
		thisJdialog.getContentPane().add(btnFactura);
		
		JButton btnGuardar = new JButton("Guardar cambios");
		btnGuardar.setToolTipText("Guardar cambios");
		btnGuardar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/guardar.png"))));
		btnGuardar.setOpaque(true);
		btnGuardar.setHorizontalAlignment(SwingConstants.LEFT);
		btnGuardar.setForeground(Color.BLACK);
		btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnGuardar.setFocusPainted(false);
		btnGuardar.setContentAreaFilled(false);
		btnGuardar.setBorderPainted(false);
		btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnGuardar.setBounds(1107, 306, 230, 64);
		btnGuardar.addActionListener(e -> {
            CitaBean citaBean = guardarCita(esNueva, vehiculoPasar, filaCita, esPresupuesto);
            guardarMateriales(citaBean, esPresupuesto);
            insertado = true;

            thisJdialog.setVisible(false);
            thisJdialog.dispose();
        });
		thisJdialog.getContentPane().add(btnGuardar);
		
		crearBotonImagen(esNueva, filaCita);
		
		txtDescripcion = new JTextField();
		txtDescripcion.setBounds(418, 42, 1005, 19);
		thisJdialog.getContentPane().add(txtDescripcion);
		txtDescripcion.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Descripción:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_3.setBounds(418, 22, 135, 13);
		thisJdialog.getContentPane().add(lblNewLabel_3);

		crearBotonesPagadoYFaltaPagar();
		
		btnRehacer = new JButton("");
		btnRehacer.setToolTipText("Rehacer");
		btnRehacer.setEnabled(false);
		btnRehacer.setBounds(1777, 335, 35, 35);
		btnRehacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aniadirFilaCache(cacheTablaDeshacer, btnDeshacer);
				recargarTablaDesdeCache(cacheTablaRehacer);
				if(cacheTablaRehacer.isEmpty()){
					btnRehacer.setEnabled(false);
				}
			}
		});
		
		BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource("images/rehacer.png"));
		ImageIcon imagenDeshacer = new ImageIcon(image);
		imagenDeshacer.setImage(imagenDeshacer.getImage().getScaledInstance( 30, 30,  Image.SCALE_SMOOTH ));
		int offset = btnRehacer.getInsets().left;
		btnRehacer.setIcon(resizeIcon(imagenDeshacer, btnRehacer.getWidth() - offset, btnRehacer.getHeight() - offset));
		thisJdialog.getContentPane().add(btnRehacer);
		
		btnDeshacer = new JButton("");
		btnDeshacer.setToolTipText("Deshacer");
		btnDeshacer.setEnabled(false);
		btnDeshacer.setBounds(1728, 335, 35, 35);
		btnDeshacer.addActionListener(new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  aniadirFilaCache(cacheTablaRehacer, btnRehacer);
				  recargarTablaDesdeCache(cacheTablaDeshacer);
				  if(cacheTablaDeshacer.isEmpty()){
					  btnDeshacer.setEnabled(false);
				  }
			  }
		});

		image = ImageIO.read(getClass().getClassLoader().getResource("images/deshacer.png"));
		imagenDeshacer = new ImageIcon(image);
		imagenDeshacer.setImage(imagenDeshacer.getImage().getScaledInstance( 30, 30,  Image.SCALE_SMOOTH ));
		offset = btnDeshacer.getInsets().left;
		btnDeshacer.setIcon(resizeIcon(imagenDeshacer, btnDeshacer.getWidth() - offset, btnDeshacer.getHeight() - offset));
		
		thisJdialog.getContentPane().add(btnDeshacer);

		cargarTabla(filaCita, esNueva, vehiculoPasar);

		thisJdialog.addWindowListener(windowListener);

		// Se visualiza la ventana.
		thisJdialog.setLocationRelativeTo(null);
		thisJdialog.setVisible(true);
	}

	private void recargarTablaDesdeCache(List<List<Object[]>> cacheTablaDeshacer) {
		for(int i=tablaMateriales.getRowCount(); i>0; i--){
			((DefaultTableModel) tablaMateriales.getModel()).removeRow(i-1);
		}
		for(Object[] fila : cacheTablaDeshacer.get(cacheTablaDeshacer.size()-1)){
			((DefaultTableModel) tablaMateriales.getModel()).addRow(fila);
		}
		cacheTablaDeshacer.remove(cacheTablaDeshacer.size()-1);
	}

	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
	    Image img = icon.getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  Image.SCALE_SMOOTH);
	    return new ImageIcon(resizedImage);
	}

	private void generarPlantillaFactura() throws IOException {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Libro de Excel 97-2003 (*.xlsx)", "xlsx");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(new File(txtDescripcion.getText()));

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File dirDestino = new File(chooser.getCurrentDirectory().getAbsolutePath() + "\\" + chooser.getSelectedFile().getName() + ".xlsx");
			FileOutputStream fileOut = new FileOutputStream(dirDestino.getAbsolutePath());
			Workbook workbook = generarFactura();
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			JOptionPane.showMessageDialog(null, "Factura generada correctamente");
		}
	}

	private Workbook generarFactura() {
		XSSFWorkbook workbook = null;
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("plantillaFactura.xlsx");
			workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheetAt(0);

			sheet.getRow(2).getCell(0).setCellValue(sheet.getRow(2).getCell(0).getStringCellValue().
					replace("?", datosCita.getVehiculoBean().getClienteBean().getNombre() + " " + datosCita.getVehiculoBean().getClienteBean().getApellido()));
			sheet.getRow(3).getCell(0).setCellValue(sheet.getRow(3).getCell(0).getStringCellValue().
					replace("?", datosCita.getVehiculoBean().getClienteBean().getLocalidad()!=null?datosCita.getVehiculoBean().getClienteBean().getLocalidad():""));
			sheet.getRow(4).getCell(0).setCellValue(sheet.getRow(4).getCell(0).getStringCellValue().
					replace("?", datosCita.getVehiculoBean().getClienteBean().getTelefono()!=null?datosCita.getVehiculoBean().getClienteBean().getTelefono().toString():""));
			sheet.getRow(5).getCell(0).setCellValue(sheet.getRow(5).getCell(0).getStringCellValue().
					replace("?", datosCita.getVehiculoBean().getMarca() + " " + datosCita.getVehiculoBean().getModelo()));
			sheet.getRow(6).getCell(0).setCellValue(sheet.getRow(6).getCell(0).getStringCellValue().
					replace("?", datosCita.getVehiculoBean().getMatricula()));

			sheet.getRow(7).getCell(0).setCellValue(sheet.getRow(7).getCell(0).getStringCellValue().
					replace("?", txtKm.getText()));

			sheet.getRow(3).getCell(4).setCellValue(sheet.getRow(3).getCell(4).getStringCellValue().
					replace("?", Utilidades.formatDate(model.getValue())));

			sheet.getRow(4).getCell(4).setCellValue(sheet.getRow(4).getCell(4).getStringCellValue().
					replace("?", lblNumeroFactura.getText()));
			sheet.getRow(6).getCell(6).setCellValue(Double.parseDouble(txtImportePagado.getText()));
			sheet.getRow(6).getCell(8).setCellValue(txtFaltaPagar.getText().replace(".", ","));

			sheet.getRow(12).getCell(1).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 5).toString()));
			sheet.getRow(12).getCell(2).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 3).toString()));
			sheet.getRow(12).getCell(4).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 4).toString()));
			sheet.getRow(12).getCell(6).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 6).toString()));

			for(int i=1 ; i<tablaMateriales.getRowCount()-3; i++){
				System.out.println(i);
				if(sheet.getRow(17+i).getCell(0) == null)sheet.getRow(17+i).createCell(0);
				sheet.getRow(17+i).getCell(0).setCellValue(tablaMateriales.getModel().getValueAt(i, 1).toString());
				if(sheet.getRow(17+i).getCell(1) == null)sheet.getRow(17+i).createCell(1);
				sheet.getRow(17+i).getCell(1).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 5).toString()));
				if(sheet.getRow(17+i).getCell(2) == null)sheet.getRow(17+i).createCell(2);
				sheet.getRow(17+i).getCell(2).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 3).toString()));
				if(sheet.getRow(17+i).getCell(4) == null)sheet.getRow(17+i).createCell(4);
				sheet.getRow(17+i).getCell(4).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 4).toString()));

				if(sheet.getRow(17+i).getCell(6) == null)sheet.getRow(17+i).createCell(6);
				sheet.getRow(17+i).getCell(6).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 6).toString()));

				/*sheet.getRow(i).getCell(4).setCellValue(tablaMateriales.getModel().getValueAt(i, 5).toString());
				sheet.getRow(i).getCell(5).setCellValue(tablaMateriales.getModel().getValueAt(i, 6).toString());
				sheet.getRow(i).getCell(6).setCellValue(tablaMateriales.getModel().getValueAt(i, 7).toString());*/
			}
			String valor = tablaMateriales.getModel().getValueAt(tablaMateriales.getRowCount()-2, 5).toString();
			valor = valor.substring(valor.lastIndexOf(" ")+1);
			sheet.getRow(38).getCell(4).setCellValue(valor);

			sheet.getRow(38).getCell(6).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(tablaMateriales.getRowCount()-2, 6).toString()));
			sheet.getRow(39).getCell(6).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(tablaMateriales.getRowCount()-3, 6).toString()));

			sheet.getRow(40).getCell(6).setCellValue(Double.parseDouble(tablaMateriales.getModel().getValueAt(tablaMateriales.getRowCount()-1, 6).toString()));

			/*int rowCount = tabla.getModel().getRowCount();
			for (int i=0; i<rowCount; i++) {
				if(!tabla.getModel().getValueAt(i, 0).toString().equals("---")) {
					int rowTotal = sheet.getLastRowNum()>0?sheet.getLastRowNum()+1:sheet.getPhysicalNumberOfRows();
					XSSFRow fila = sheet.createRow(rowTotal);
					fila.createCell(1).setCellValue(tabla.getModel().getValueAt(i, 0).toString());
					fila.createCell(2).setCellValue(tabla.getModel().getValueAt(i, 1).toString());
					fila.createCell(3).setCellValue(tabla.getModel().getValueAt(i, 2).toString());
				}
			}*/
			is.close();
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
		return workbook;
	}

	static void actualizarFaltaPagar() {
		String texto = txtImportePagado.getText();
		double total = Double.parseDouble(tablaMateriales.getModel().getValueAt(tablaMateriales.getRowCount()-1, 6).toString());
		//total = (double) (Math.round(total * 100) / 100);

		if(texto.isEmpty()){
			txtFaltaPagar.setText(String.format("%.2f", total).replace(",", "."));
		}else if(!texto.endsWith(".")){
			double pagado = Double.parseDouble(texto);
			double falta = total - pagado;
			txtFaltaPagar.setText(String.format("%.2f", falta).replace(",", "."));
		}else{
			double falta = total - Double.parseDouble(texto.substring(0, texto.length() - 1));
			txtFaltaPagar.setText(String.format("%.2f", falta).replace(",", "."));
		}
	}

	private void guardarMateriales(CitaBean citaBean, boolean esPresupuesto){
		List<MaterialBean> materialBeanList = new ArrayList<>();

		citaBean = citaBO.obtenerDetalleCita(citaBean.getId());

		List<RecambioBean> listaRecambios = materialBO.obtenerRecambios();
		List<MaterialBean> listaMaterialesEnBBDD = citaBean.getMaterialBeanList();

		// borramos los materiales asociados a la cita
		materialBO.borrar(citaBean.getId());

		listaMaterialesEnBBDD.forEach(materialBBDD -> {
			listaRecambios.stream().filter(recambio -> recambio.getNombre().equals(materialBBDD.getDescripcion())).findFirst().ifPresent(recambio -> {
				recambio.setCantidad(recambio.getCantidad() + materialBBDD.getCantidad());
				recambioBO.actualizar(recambio);
			});
		});

		for(int i = 1; i< tablaMateriales.getRowCount()-3; i++){
			MaterialBean materialBean = new MaterialBean();
			materialBean.setId(tablaMateriales.getModel().getValueAt(i, 0).toString().isEmpty()?0: Integer.parseInt(tablaMateriales.getModel().getValueAt(i, 0).toString()));
			materialBean.setDescripcion(tablaMateriales.getModel().getValueAt(i, 1).toString());
			materialBean.setPc(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 2).toString()));
			materialBean.setPvp(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 3).toString()));
			materialBean.setDescuentoCliente(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 4).toString()));
			materialBean.setCantidad(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 5).toString()));
			materialBean.setImporteCliente(Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 6).toString()));
			materialBean.setCitaBean(citaBean);
			materialBeanList.add(materialBean);

			listaRecambios.stream().filter(recambio -> recambio.getNombre().equals(materialBean.getDescripcion())).findFirst().ifPresent(recambio -> {
				recambio.setCantidad(recambio.getCantidad() - materialBean.getCantidad());
				recambioBO.actualizar(recambio);
			});
		}

		ErrorBean errorBean = materialBO.insertarMateriales(materialBeanList, esPresupuesto);
		JOptionPane.showMessageDialog(null, errorBean.getMensaje());
	}

	private CitaBean guardarCita(boolean esNueva, VehiculoBean vehiculoPasar, Object[] filaCita, boolean esPresupuesto) {
		CitaBean citaBean = new CitaBean();
		citaBean.setId(Integer.parseInt(lblNumeroFactura.getText()));
		if(!esNueva){
			citaBean.setVehiculoBean(new VehiculoBean());
			citaBean.getVehiculoBean().setId((Integer) (esPresupuesto?filaCita[11]:filaCita[12]));
			citaBO.borrar(citaBean.getId());
		}else{
			citaBean.setVehiculoBean(vehiculoPasar);
		}
		citaBean.setDescripcion(txtDescripcion.getText());
		citaBean.setImportePagado(!txtImportePagado.getText().isEmpty()?Double.parseDouble(txtImportePagado.getText()):0);
		citaBean.setImportePendiente(!txtFaltaPagar.getText().isEmpty()?Double.parseDouble(txtFaltaPagar.getText()):0);
		citaBean.setFecha(model.getValue());
		citaBean.setKilometros(Integer.valueOf(txtKm.getText()));
		citaBean.setTiempo(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 5).toString()));
		citaBean.setPrecioHora(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 3).toString()));
		citaBean.setDescuento(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 4).toString()));
		citaBean.setImporteTiempo(Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 6).toString()));
		citaBean.setImporteTotal(Double.parseDouble(tablaMateriales.getModel().getValueAt(tablaMateriales.getRowCount()-1, 6).toString()));
		citaBean.setEsPresupuesto(esPresupuesto);

		int pagado = 0;
		if(!esPresupuesto) {
			if(btnImagen.getText().equals("Pagado")){
				pagado = 1;
			}else if(btnImagen.getText().equals("Pagado parcial")){
				pagado = 2;
			}
		}
		citaBean.setPagado(pagado);
		citaBean.setObservaciones(txtObservaciones.getText());
		citaBean = citaBO.insertarCita(citaBean);

		return citaBean;
	}

	private void eliminarMaterial() {
		int opcion = JOptionPane.showConfirmDialog(this, "Se va a elimnar el material. ¿Está seguro de que desea continuar?", "", JOptionPane.YES_NO_OPTION);
		if(opcion == 0){
			((DefaultTableModel) tablaMateriales.getModel()).removeRow(tablaMateriales.getSelectedRow());
			actualizarTotales();
		}
	}

	public void cargarTabla(Object[] filaCita, boolean esNueva, VehiculoBean vehiculoPasar) {
		cacheTablaDeshacer = new ArrayList<>();
		cacheTablaRehacer = new ArrayList<>();

		datosCita = new CitaBean();
		if(!esNueva){
			int idCita = (int) filaCita[0];
			datosCita = citaBO.obtenerDetalleCita(idCita);
		}else{
			datosCita.setVehiculoBean(vehiculoPasar);
		}

		crearFila("", "Mano de obra", "N/A", !esNueva?datosCita.getPrecioHora():30.0, !esNueva?datosCita.getDescuento():0.0,
				!esNueva?datosCita.getTiempo():0.0, !esNueva?datosCita.getImporteTiempo():0.0);

		for (MaterialBean reparacion : datosCita.getMaterialBeanList()) {
			crearFila(reparacion.getId(), reparacion.getDescripcion(), reparacion.getPc(), reparacion.getPvp(), reparacion.getDescuentoCliente(),
					reparacion.getCantidad(), reparacion.getImporteCliente());
		}
		crearFila("", "", "", "", "", "Total materiales", !esNueva?datosCita.getSubtotal():0.0);
		crearFila("", "", "", "", "", "IVA materiales - 21%", !esNueva?datosCita.getIva():0.0);
		crearFila("", "", "", "", "", "Total", !esNueva?datosCita.getImporteTotal():0.0);

		for(int i = 0; i< tablaMateriales.getColumnCount(); i++){
			tablaMateriales.getColumnModel().getColumn(i).setCellRenderer(new CellRenderer());
		}

		lblNombre.setText(lblNombre.getText().replace("?", !esNueva?datosCita.getVehiculoBean().getClienteBean().getNombre():vehiculoPasar.getClienteBean().getNombre()));
		lblApellido.setText(lblApellido.getText().replace("?", !esNueva?datosCita.getVehiculoBean().getClienteBean().getApellido():vehiculoPasar.getClienteBean().getApellido()));
		lblTelefono.setText(lblTelefono.getText().replace("?", !esNueva?datosCita.getVehiculoBean().getClienteBean().getTelefono().toString():vehiculoPasar.getClienteBean().getTelefono().toString()));

		lblMarca.setText((!esNueva?datosCita.getVehiculoBean().getMarca():vehiculoPasar.getMarca()) + " " + (!esNueva?datosCita.getVehiculoBean().getModelo():vehiculoPasar.getModelo()));
		lblMatricula.setText(lblMatricula.getText().replace("?", !esNueva?datosCita.getVehiculoBean().getMatricula():vehiculoPasar.getMatricula()));
		lblVin.setText(lblVin.getText().replace("?", !esNueva?datosCita.getVehiculoBean().getVin():vehiculoPasar.getVin()));
		lblAnio.setText(lblAnio.getText().replace("?", !esNueva?datosCita.getVehiculoBean().getAnio():vehiculoPasar.getAnio()));
		txtKm.setText(!esNueva?datosCita.getKilometros().toString():"0");
		txtDescripcion.setText(!esNueva?datosCita.getDescripcion():"");
		txtObservaciones.setText(!esNueva?datosCita.getObservaciones():"");
		txtImportePagado.setText(!esNueva? String.valueOf(datosCita.getImportePagado()) :"0.0");

		double faltaPagar = 0.0;
		if(!esNueva){
            faltaPagar = datosCita.getImporteTotal() - datosCita.getImportePagado();
        }
		//txtFaltaPagar.setText(String.format("%.2f", faltaPagar).replace(",", "."));
		txtFaltaPagar.setText(!esNueva? String.valueOf(datosCita.getImportePendiente()) :"0.0");

		int numeroSecuencia = !esNueva?datosCita.getId():1;
		if(esNueva){
			numeroSecuencia = citaBO.obtenerSecuencia();
		}
		lblNumeroFactura.setText(String.valueOf(numeroSecuencia));

		Calendar cal = Calendar.getInstance();
		cal.setTime(!esNueva?datosCita.getFecha():new Date());
		model.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		model.setSelected(true);
	}

	private void crearFila(Object valor1, Object valor2, Object valor3, Object valor4, Object valor5, Object valor6, Object valor7){
		Object[] fila2 = new Object[tablaMateriales.getModel().getColumnCount()];
		fila2[0] = (valor1 instanceof Double)?String.format("%.2f", valor1).replace(",", "."):valor1;
		fila2[1] = (valor2 instanceof Double)?String.format("%.2f", valor2).replace(",", "."):valor2;
		fila2[2] = (valor3 instanceof Double)?String.format("%.2f", valor3).replace(",", "."):valor3;
		fila2[3] = (valor4 instanceof Double)?String.format("%.2f", valor4).replace(",", "."):valor4;
		fila2[4] = (valor5 instanceof Double)?String.format("%.2f", valor5).replace(",", "."):valor5;
		fila2[5] = (valor6 instanceof Double)?String.format("%.2f", valor6).replace(",", "."):valor6;
		fila2[6] = (valor7 instanceof Double)?String.format("%.2f", valor7).replace(",", "."):valor7;
		((DefaultTableModel) tablaMateriales.getModel()).addRow(fila2);
	}

	public static void aniadirFila(String txtDescripcion, String txtPc, String txtDescuento, String txtCantidad, String txtPv, Integer idMaterial, Integer selectedRow){

		aniadirFilaCache(cacheTablaDeshacer, btnDeshacer);
		cacheTablaRehacer = new ArrayList<>();

		String formateoPc = !txtPc.equals("N/A")?String.format("%.2f", Double.parseDouble(txtPc)).replace(",", "."):txtPc;
		double pv = Double.parseDouble(txtPv);
		double cantidad = Double.parseDouble(txtCantidad);
		double descuento = Double.parseDouble(txtDescuento);
		double importe = (pv*cantidad) - (pv*cantidad*descuento/100);

		Object[] fila = new Object[tablaMateriales.getModel().getColumnCount()];
        fila[0] = idMaterial!=null?idMaterial:"";
        fila[1] = txtDescripcion;
        fila[2] = formateoPc;
        fila[3] = String.format("%.2f", pv).replace(",", ".");
        fila[4] = String.format("%.2f", descuento).replace(",", ".");
        fila[5] = String.format("%.2f", cantidad).replace(",", ".");
        fila[6] = String.format("%.2f", importe).replace(",", ".");

		if(selectedRow == null){
			// buscamos si ya se encuentra ese material en la tabla, para modificarlo
			boolean encontrado = false;
			for(int i=0; i<tablaMateriales.getRowCount(); i++){
				if(tablaMateriales.getModel().getValueAt(i, 1).equals(fila[1]) && tablaMateriales.getModel().getValueAt(i, 2).equals(fila[2]) &&
						tablaMateriales.getModel().getValueAt(i, 3).equals(fila[3]) && tablaMateriales.getModel().getValueAt(i, 4).equals(fila[4])){

					double cantidadTabla = Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 5).toString().replace(",", "."));
					double sumaCantidad = cantidadTabla + cantidad;

					tablaMateriales.getModel().setValueAt(String.format("%.2f", sumaCantidad).replace(",", "."), i, 5);
					encontrado = true;
				}
			}
			if(!encontrado){
				((DefaultTableModel) tablaMateriales.getModel()).insertRow(tablaMateriales.getRowCount()-3, fila);
			}
		}else{
			((DefaultTableModel) tablaMateriales.getModel()).removeRow(selectedRow);
			((DefaultTableModel) tablaMateriales.getModel()).insertRow(selectedRow, fila);
		}
		actualizarTotales();

		btnDeshacer.setEnabled(true);
		btnRehacer.setEnabled(false);
	}

	private static void aniadirFilaCache(List<List<Object[]>> cacheTabla, JButton button) {
		List<Object[]> filaCache = new ArrayList<>();
		for(int i=0; i<tablaMateriales.getRowCount(); i++){
			Object[] fila = new Object[tablaMateriales.getModel().getColumnCount()];
			for(int j=0; j<tablaMateriales.getModel().getColumnCount(); j++){
				fila[j] = tablaMateriales.getModel().getValueAt(i, j);
			}
			filaCache.add(fila);
		}
		cacheTabla.add(filaCache);
		button.setEnabled(true);
	}

	private static void actualizarTotales(){
		double subtotal = 0;
		for (int i = 1; i< tablaMateriales.getRowCount()-3; i++) {
			double importe = Double.parseDouble(tablaMateriales.getModel().getValueAt(i, 6).toString());
			subtotal += importe;
		}
		String valor = String.format("%.2f", subtotal).replace(",", ".");
		tablaMateriales.getModel().setValueAt(valor, tablaMateriales.getRowCount()-3, 6);

		var iva = subtotal*21/100;
		valor = String.format("%.2f", iva).replace(",", ".");
		tablaMateriales.getModel().setValueAt(valor, tablaMateriales.getRowCount()-2, 6);

		double importeHoras = Double.parseDouble(tablaMateriales.getModel().getValueAt(0, 6).toString());
		var total = subtotal+iva+importeHoras;
		valor = String.format("%.2f", total).replace(",", ".");
		tablaMateriales.getModel().setValueAt(valor, tablaMateriales.getRowCount()-1, 6);

		actualizarFaltaPagar();
	}
	
	protected abstract void crearBotonImagen(boolean esNueva, Object[] filaCita) throws IOException;
	
	protected void crearBotonesPagadoYFaltaPagar() {
	}
}