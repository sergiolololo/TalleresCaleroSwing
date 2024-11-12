package com.taller.jdialog;

import com.taller.beans.ErrorBean;
import com.taller.beans.RecambioBean;
import com.taller.constantes.Constantes;
import com.taller.negocio.impl.RecambioBOImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PanelInsertarRecambio extends JDialog {
    private final JDialog thisJdialog;
	private final RecambioBOImpl recambioBO = new RecambioBOImpl();
    private final JTextField txtReferencia, txtNombre, txtCantidad, txtPc, txtPv;
    public static boolean insertado = false;
	
	public PanelInsertarRecambio(JFrame padre, boolean esInsert, Object[] fila, WindowListener windowListener) throws IOException  {
        thisJdialog = new JDialog(padre, true);
        thisJdialog.setResizable(false);
        thisJdialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        thisJdialog.setTitle(esInsert?"Añadir recambio":"Modificar recambio");
        thisJdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        thisJdialog.getContentPane().setLayout(null);
        thisJdialog.setShape(new RoundRectangle2D.Double(0, 0, 683, 235, 30, 30));
        thisJdialog.setSize(683, 235);
        
        JLabel lblNewLabel = new JLabel("Referencia");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel.setBounds(27, 26, 80, 20);
        thisJdialog.getContentPane().add(lblNewLabel);
        
        txtReferencia = new JTextField();
        txtReferencia.setColumns(10);
        txtReferencia.setBounds(132, 26, 500, 22);
        thisJdialog.getContentPane().add(txtReferencia);
        
        JLabel lblTelefono = new JLabel("Nombre");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTelefono.setBounds(27, 56, 80, 20);
        thisJdialog.getContentPane().add(lblTelefono);
        
        txtNombre = new JTextField();
        txtNombre.setColumns(10);
        txtNombre.setBounds(131, 57, 179, 22);
        thisJdialog.getContentPane().add(txtNombre);

        JButton btnAniadir = new JButton(esInsert?"AÑADIR":"ACTUALIZAR");
        btnAniadir.setBounds(277, 138, 107, 33);
        btnAniadir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAniadir.setForeground(Color.WHITE);
        btnAniadir.setFocusPainted(false);
        btnAniadir.setBackground(new Color(0, 119, 160));
        btnAniadir.addActionListener(e -> aniadirFila(esInsert, fila!=null?(Integer) fila[0]:null));
        thisJdialog.getContentPane().add(btnAniadir);
        
        JLabel lblCantidad = new JLabel("Cantidad");
        lblCantidad.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblCantidad.setBounds(355, 57, 84, 20);
        thisJdialog.getContentPane().add(lblCantidad);
        
        txtCantidad = new JTextField();
        txtCantidad.setColumns(10);
        txtCantidad.setBounds(453, 57, 179, 22);
        thisJdialog.getContentPane().add(txtCantidad);
        
        JLabel lblPrecio = new JLabel("PC");
        lblPrecio.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblPrecio.setBounds(27, 86, 80, 20);
        thisJdialog.getContentPane().add(lblPrecio);
        
        txtPc = new JTextField();
        txtPc.setColumns(10);
        txtPc.setBounds(131, 87, 179, 22);
        thisJdialog.getContentPane().add(txtPc);
        
        JLabel lblPv = new JLabel("PV");
        lblPv.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblPv.setBounds(355, 87, 80, 20);
        thisJdialog.getContentPane().add(lblPv);
        
        txtPv = new JTextField();
        txtPv.setColumns(10);
        txtPv.setBounds(453, 87, 179, 22);
        thisJdialog.getContentPane().add(txtPv);

        if(!esInsert){
            cargarCampos(Arrays.asList(txtReferencia, txtNombre, txtPc, txtPv, txtCantidad), fila);
        }

        thisJdialog.addWindowListener(windowListener);

        thisJdialog.setLocationRelativeTo(null);
        thisJdialog.setVisible(true);
	}

    private void cargarCampos(List<JTextField> campos, Object[] fila) {
        for(int i=0; i<campos.size()-3; i++) {
            campos.get(i).setText(fila[i+1]!=null?fila[i+1].toString():"");
        }
        // campos decimales
        for(int i=campos.size()-3; i<campos.size(); i++) {
            Object aa = fila[i+1];
            campos.get(i).setText(aa!=null?aa.toString():"");
        }
    }

    private void aniadirFila(boolean esInsert, Integer id) {
        if(algunTxtVacio(Arrays.asList(txtReferencia, txtNombre, txtCantidad, txtPc, txtPv))){
            JOptionPane.showMessageDialog(null, "Debe rellenar todos los campos antes de continuar");
        }else{
            String referencia = txtReferencia.getText();
            String nombre = txtNombre.getText();
            double cantidad = Double.parseDouble(txtCantidad.getText());
            double pc = Double.parseDouble(txtPc.getText());
            double pv = Double.parseDouble(txtPv.getText());

            int opcion = JOptionPane.showConfirmDialog(null, "Se va a crear el registro. ¿Está seguro de que quiere continuar?", Constantes.STRING_VACIO, JOptionPane.YES_NO_OPTION);
            if(opcion == 0) {
                RecambioBean recambio = new RecambioBean();
                recambio.setReferencia(referencia);
                recambio.setNombre(nombre);
                recambio.setCantidad(cantidad);
                recambio.setPc(pc);
                recambio.setPv(pv);

                ErrorBean errorBean;
                if(esInsert){
                    errorBean = recambioBO.insertar(recambio);
                }else{
                    recambio.setId(id);
                    errorBean = recambioBO.actualizar(recambio);
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