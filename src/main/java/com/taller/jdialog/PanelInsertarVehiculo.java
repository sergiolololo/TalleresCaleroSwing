package com.taller.jdialog;

import com.taller.beans.ClienteBean;
import com.taller.beans.ErrorBean;
import com.taller.beans.VehiculoBean;
import com.taller.constantes.Constantes;
import com.taller.negocio.impl.ClienteBOImpl;
import com.taller.negocio.impl.VehiculoBOImpl;
import com.taller.utils.JComboBoxValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.List;

public class PanelInsertarVehiculo extends JDialog {
    private final JDialog thisJdialog;
	private final ClienteBOImpl clienteBO = new ClienteBOImpl();
    private final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();
	private final JTextField txtMarca, txtModelo, txtAnio, txtMatricula, txtVin, txtCilindrada, txtCaballos, txtTipo, txtCombustible;
	private DefaultComboBoxModel modelCmbCliente;
	private final JComboBox<String> cmbCliente;

    public static boolean insertado = false;

	public PanelInsertarVehiculo(JFrame padre, boolean esInsert, Object[] filaVehiculo, WindowListener windowListener) {

        thisJdialog = new JDialog(padre, true);
        thisJdialog.setResizable(false);
        thisJdialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        thisJdialog.setTitle(esInsert?"Añadir vehículo":"Modificar vehículo");
        thisJdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        thisJdialog.getContentPane().setLayout(null);
        thisJdialog.setShape(new RoundRectangle2D.Double(0, 0, 683, 284, 30, 30));
        thisJdialog.setSize(683, 284);
        
        inicializarModelCombos();
        
        JLabel lblNewLabel = new JLabel("Marca");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(27, 26, 80, 20);
        thisJdialog.getContentPane().add(lblNewLabel);
        
        txtMarca = new JTextField();
        txtMarca.setColumns(10);
        txtMarca.setBounds(132, 26, 179, 22);
        thisJdialog.getContentPane().add(txtMarca);
        
        JLabel lblApellido = new JLabel("Modelo");
        lblApellido.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblApellido.setBounds(359, 26, 84, 20);
        thisJdialog.getContentPane().add(lblApellido);
        
        txtModelo = new JTextField();
        txtModelo.setColumns(10);
        txtModelo.setBounds(453, 26, 179, 22);
        thisJdialog.getContentPane().add(txtModelo);
        
        JLabel lblLocalidad = new JLabel("Año");
        lblLocalidad.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblLocalidad.setBounds(27, 56, 84, 20);
        thisJdialog.getContentPane().add(lblLocalidad);
        
        txtAnio = new JTextField();
        txtAnio.setColumns(10);
        txtAnio.setBounds(132, 56, 179, 22);
        thisJdialog.getContentPane().add(txtAnio);
        
        JLabel lblTelefono = new JLabel("Matricula");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTelefono.setBounds(359, 56, 84, 20);
        thisJdialog.getContentPane().add(lblTelefono);
        
        txtMatricula = new JTextField();
        txtMatricula.setColumns(10);
        txtMatricula.setBounds(453, 56, 179, 22);
        thisJdialog.getContentPane().add(txtMatricula);
        
        JLabel lblEmail = new JLabel("VIN");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblEmail.setBounds(27, 86, 80, 20);
        thisJdialog.getContentPane().add(lblEmail);
        
        txtVin = new JTextField();
        txtVin.setColumns(10);
        txtVin.setBounds(132, 86, 179, 22);
        thisJdialog.getContentPane().add(txtVin);
        
        JLabel lblDani = new JLabel("Cilindrada");
        lblDani.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblDani.setBounds(359, 86, 80, 20);
        thisJdialog.getContentPane().add(lblDani);
        
        txtCilindrada = new JTextField();
        txtCilindrada.setColumns(10);
        txtCilindrada.setBounds(453, 86, 179, 22);
        thisJdialog.getContentPane().add(txtCilindrada);
        
        JLabel lblDireccion = new JLabel("Caballos");
        lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblDireccion.setBounds(27, 115, 80, 20);
        thisJdialog.getContentPane().add(lblDireccion);
        
        txtCaballos = new JTextField();
        txtCaballos.setColumns(10);
        txtCaballos.setBounds(132, 116, 179, 22);
        thisJdialog.getContentPane().add(txtCaballos);
        
        JLabel lbl1 = new JLabel("Tipo");
        lbl1.setFont(new Font("Tahoma", Font.BOLD, 13));
        lbl1.setBounds(359, 115, 80, 20);
        thisJdialog.getContentPane().add(lbl1);

        txtTipo = new JTextField();
        txtTipo.setColumns(10);
        txtTipo.setBounds(453, 116, 179, 22);
        thisJdialog.getContentPane().add(txtTipo);
        
        JLabel lbl2 = new JLabel("Combustible");
        lbl2.setFont(new Font("Tahoma", Font.BOLD, 13));
        lbl2.setBounds(27, 144, 85, 20);
        thisJdialog.getContentPane().add(lbl2);
        
        txtCombustible = new JTextField();
        txtCombustible.setColumns(10);
        txtCombustible.setBounds(132, 145, 179, 22);
        thisJdialog.getContentPane().add(txtCombustible);
        
        JLabel lbl3 = new JLabel("Ciente");
        lbl3.setFont(new Font("Tahoma", Font.BOLD, 13));
        lbl3.setBounds(359, 144, 80, 20);
        thisJdialog.getContentPane().add(lbl3);

        cmbCliente = new JComboBox<>();
        cmbCliente.setBounds(453, 145, 179, 22);
        cmbCliente.setModel(modelCmbCliente);
        cmbCliente.setName("cmbApp");
        thisJdialog.getContentPane().add(cmbCliente);

        JButton btnAniadir = new JButton(esInsert?"AÑADIR":"ACTUALIZAR");
        btnAniadir.setBounds(281, 195, 107, 33);
        btnAniadir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAniadir.setForeground(Color.WHITE);
        btnAniadir.setFocusPainted(false);
        btnAniadir.setBackground(new Color(0, 119, 160));
        btnAniadir.addActionListener(e -> aniadirFila(esInsert, filaVehiculo!=null?(Integer) filaVehiculo[0]:null));
        thisJdialog.getContentPane().add(btnAniadir);

        if(!esInsert){
            cargarCampos(Arrays.asList(txtMarca, txtModelo, txtAnio, txtMatricula, txtVin, txtCilindrada, txtCaballos, txtTipo, txtCombustible), filaVehiculo);
        }

        thisJdialog.addWindowListener(windowListener);

        thisJdialog.setLocationRelativeTo(null);
        thisJdialog.setVisible(true);
	}

    private void cargarCampos(List<JTextField> campos, Object[] filaVehiculo) {
        for(int i=0; i<campos.size(); i++) {
            campos.get(i).setText(filaVehiculo[i+1].toString());
        }
        for(int i=0; i<modelCmbCliente.getSize(); i++) {
            if(((JComboBoxValue)modelCmbCliente.getElementAt(i)).getId() == (int)filaVehiculo[11]){
                cmbCliente.setSelectedItem((modelCmbCliente.getElementAt(i)));
            }
        }
    }

    private void aniadirFila(boolean esInsert, Integer idVehiculo) {
        if(algunTxtVacio(Arrays.asList(txtMarca, txtModelo, txtAnio, txtMatricula, txtVin, txtCilindrada, txtCaballos, txtTipo, txtCombustible))) {
            JOptionPane.showMessageDialog(null, "Debe rellenar todos los campos antes de continuar");
        }else {
            String marca = txtMarca.getText();
            String modelo = txtModelo.getText();
            String anio = txtAnio.getText();
            String matricula = txtMatricula.getText();
            String vin = txtVin.getText();
            String cilindrada = txtCilindrada.getText();
            String caballos = txtCaballos.getText();
            String tipo = txtTipo.getText();
            String combustible = txtCombustible.getText();
            int idCliente = ((JComboBoxValue)cmbCliente.getSelectedItem()).getId();

            int opcion = JOptionPane.showConfirmDialog(null, "Se va a crear el registro. ¿Está seguro de que quiere continuar?", Constantes.STRING_VACIO, JOptionPane.YES_NO_OPTION);
            if(opcion == 0) {
                VehiculoBean vehiculo = new VehiculoBean();
                vehiculo.setMarca(marca);
                vehiculo.setModelo(modelo);
                vehiculo.setAnio(anio);
                vehiculo.setMatricula(matricula);
                vehiculo.setVin(vin);
                vehiculo.setCentimetros(cilindrada);
                vehiculo.setCaballos(caballos);
                vehiculo.setTipo(tipo);
                vehiculo.setCombustible(combustible);
                vehiculo.setClienteBean(new ClienteBean());
                vehiculo.getClienteBean().setId(idCliente);

                ErrorBean errorBean;
                if(esInsert){
                    errorBean = vehiculoBO.insertar(vehiculo);
                }else{
                    vehiculo.setId(idVehiculo);
                    errorBean = vehiculoBO.actualizar(vehiculo);
                }

                if(errorBean.isExisteError()) {
                    JOptionPane.showMessageDialog(null, errorBean.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(null, errorBean.getMensaje(), "Info", JOptionPane.INFORMATION_MESSAGE);
                    insertado = true;
                    thisJdialog.setVisible(false);
                    thisJdialog.dispose();
                }
            }
        }
    }

    private boolean algunTxtVacio(List<JTextField> listaTxt){
        for(JTextField txt : listaTxt) {
            if(txt.getText().equals(Constantes.STRING_VACIO)) {
                return true;
            }
        }
        return false;
    }
    
    private void inicializarModelCombos() {
		modelCmbCliente = new DefaultComboBoxModel();

        List<Object> clientes = clienteBO.buscar(new ClienteBean());
        clientes.forEach(cliente -> modelCmbCliente.addElement(new JComboBoxValue(((ClienteBean)cliente).getId(), ((ClienteBean)cliente).getNombre() + " " + ((ClienteBean)cliente).getApellido() )));
	}
}