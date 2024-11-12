package com.taller.jdialog;

import com.taller.beans.ClienteBean;
import com.taller.beans.ErrorBean;
import com.taller.constantes.Constantes;
import com.taller.negocio.impl.ClienteBOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PanelInsertarCliente extends JDialog {
    private final JDialog thisJdialog;
	private final ClienteBOImpl clienteBO = new ClienteBOImpl();
    private final JTextField txtNombre, txtApellido, txtLocalidad, txtTelefono;
    public static boolean insertado = false;
	
	public PanelInsertarCliente(JFrame padre, boolean esInsert, Object[] filaCliente, WindowListener windowListener) throws IOException  {
        thisJdialog = new JDialog(padre, true);
        thisJdialog.setResizable(false);
        thisJdialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        thisJdialog.setTitle(esInsert?"Añadir cliente":"Modificar cliente");
        thisJdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        thisJdialog.getContentPane().setLayout(null);
        thisJdialog.setShape(new RoundRectangle2D.Double(0, 0, 683, 198, 30, 30));
        thisJdialog.setSize(683, 198);
        
        JLabel lblNewLabel = new JLabel("Nombre");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(27, 26, 80, 20);
        thisJdialog.getContentPane().add(lblNewLabel);
        
        txtNombre = new JTextField();
        txtNombre.setColumns(10);
        txtNombre.setBounds(132, 26, 179, 22);
        thisJdialog.getContentPane().add(txtNombre);
        
        JLabel lblApellido = new JLabel("Apellido");
        lblApellido.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblApellido.setBounds(359, 26, 84, 20);
        thisJdialog.getContentPane().add(lblApellido);
        
        txtApellido = new JTextField();
        txtApellido.setColumns(10);
        txtApellido.setBounds(453, 26, 179, 22);
        thisJdialog.getContentPane().add(txtApellido);
        
        JLabel lblLocalidad = new JLabel("Localidad");
        lblLocalidad.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblLocalidad.setBounds(27, 56, 84, 20);
        thisJdialog.getContentPane().add(lblLocalidad);
        
        txtLocalidad = new JTextField();
        txtLocalidad.setColumns(10);
        txtLocalidad.setBounds(132, 56, 179, 22);
        thisJdialog.getContentPane().add(txtLocalidad);
        
        JLabel lblTelefono = new JLabel("Telefono");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTelefono.setBounds(359, 56, 80, 20);
        thisJdialog.getContentPane().add(lblTelefono);
        
        txtTelefono = new JTextField();
        txtTelefono.setColumns(10);
        txtTelefono.setBounds(453, 57, 179, 22);
        thisJdialog.getContentPane().add(txtTelefono);

        JButton btnAniadir = new JButton(esInsert?"AÑADIR":"ACTUALIZAR");
        btnAniadir.setBounds(279, 105, 107, 33);
        btnAniadir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAniadir.setForeground(Color.WHITE);
        btnAniadir.setFocusPainted(false);
        btnAniadir.setBackground(new Color(0, 119, 160));
        btnAniadir.addActionListener(e -> aniadirFila(esInsert, filaCliente!=null?(Integer) filaCliente[0]:null));
        thisJdialog.getContentPane().add(btnAniadir);

        if(!esInsert){
            cargarCampos(Arrays.asList(txtNombre, txtApellido, txtLocalidad, txtTelefono), filaCliente);
        }

        thisJdialog.addWindowListener(windowListener);

        thisJdialog.setLocationRelativeTo(null);
        thisJdialog.setVisible(true);
	}

    private void cargarCampos(List<JTextField> campos, Object[] filaCliente) {
        for(int i=0; i<4; i++) {
            campos.get(i).setText(filaCliente[i+1]!=null?filaCliente[i+1].toString():"");
        }
    }

    private void aniadirFila(boolean esInsert, Integer idCliente) {
        if(algunTxtVacio(Arrays.asList(txtNombre, txtApellido, txtLocalidad, txtTelefono))){
            JOptionPane.showMessageDialog(null, "Debe rellenar todos los campos antes de continuar");
        }else{
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String localidad = txtLocalidad.getText();
            String telefono = txtTelefono.getText();

            int opcion = JOptionPane.showConfirmDialog(null, "Se va a crear el registro. ¿Está seguro de que quiere continuar?", Constantes.STRING_VACIO, JOptionPane.YES_NO_OPTION);
            if(opcion == 0) {
                ClienteBean cliente = new ClienteBean();
                cliente.setNombre(nombre);
                cliente.setApellido(apellido);
                cliente.setLocalidad(localidad);
                cliente.setTelefono(Long.parseLong(telefono));

                ErrorBean errorBean;
                if(esInsert){
                    errorBean = clienteBO.insertar(cliente);
                }else{
                    cliente.setId(idCliente);
                    errorBean = clienteBO.actualizar(cliente);
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
}