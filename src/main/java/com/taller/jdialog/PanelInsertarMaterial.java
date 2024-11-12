package com.taller.jdialog;

import com.taller.beans.MaterialBean;
import com.taller.beans.RecambioBean;
import com.taller.constantes.Constantes;
import com.taller.negocio.impl.MaterialBOImpl;
import com.taller.negocio.impl.RecambioBOImpl;
import com.taller.utils.Java2sAutoTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PanelInsertarMaterial extends JDialog {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private final MaterialBOImpl materialBO = new MaterialBOImpl();
    private final RecambioBOImpl recambioBO = new RecambioBOImpl();

    private final JTextField txtPc, txtDescuento, txtCantidad, txtPv, txtStock;
    private final Java2sAutoTextField txtDescripcion;

    private final List<MaterialBean> listaMateriales;
	
	public PanelInsertarMaterial(JFrame padre, boolean esInsert, Object[] filaMaterial, Integer selectedRow) throws IOException  {

        JDialog thisJdialog = new JDialog(padre, true);
        thisJdialog.setResizable(false);
        thisJdialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        thisJdialog.setTitle(esInsert?"Añadir material":"Modificar material");
        thisJdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        thisJdialog.getContentPane().setLayout(null);
        thisJdialog.setShape(new RoundRectangle2D.Double(0, 0, 683, 258, 30, 30));
        thisJdialog.setSize(683, 258);
        
        JLabel lblApellido = new JLabel("Descripción");
        lblApellido.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblApellido.setBounds(27, 25, 84, 20);
        thisJdialog.getContentPane().add(lblApellido);
        
        listaMateriales = materialBO.obtenerMaterialesParaAutocompletar();
        
        txtDescripcion = new Java2sAutoTextField(List.of(listaMateriales.stream().map(MaterialBean::getDescripcion).toArray(String[]::new)));
        txtDescripcion.addFocusListener(new FocusAdapter() {
        	@Override
        	public void focusLost(FocusEvent e) {
        		String material = txtDescripcion.getText();
                listaMateriales.stream().filter(m -> m.getDescripcion().equals(material)).findFirst().ifPresent(m -> {
                    if(txtPc.getText().isEmpty() && txtPv.getText().isEmpty()){
                        txtPc.setText(String.valueOf(m.getPc()));
                        txtPv.setText(String.valueOf(m.getPvp()));
                    }
                    txtStock.setText(m.getCantidad()!=-1?String.valueOf(m.getCantidad()):"N/D");
                });
        	}
        });
        txtDescripcion.setColumns(10);
        txtDescripcion.setBounds(157, 26, 475, 22);
        thisJdialog.getContentPane().add(txtDescripcion);
        
        JLabel lblLocalidad = new JLabel("PC");
        lblLocalidad.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblLocalidad.setBounds(27, 56, 107, 20);
        thisJdialog.getContentPane().add(lblLocalidad);
        
        txtPc = new JTextField();
        txtPc.setColumns(10);
        txtPc.setBounds(157, 56, 179, 22);
        thisJdialog.getContentPane().add(txtPc);
        
        JLabel lblTelefono = new JLabel("Descuento (%)");
        lblTelefono.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTelefono.setBounds(27, 86, 107, 20);
        thisJdialog.getContentPane().add(lblTelefono);
        
        txtDescuento = new JTextField();
        txtDescuento.setColumns(10);
        txtDescuento.setBounds(157, 86, 179, 22);
        thisJdialog.getContentPane().add(txtDescuento);
        
        JLabel lblEmail = new JLabel("Cantidad");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblEmail.setBounds(359, 86, 80, 20);
        thisJdialog.getContentPane().add(lblEmail);
        
        txtCantidad = new JTextField();
        txtCantidad.setColumns(10);
        txtCantidad.setBounds(453, 86, 179, 22);
        thisJdialog.getContentPane().add(txtCantidad);
        
        JLabel lblDireccion = new JLabel("PV");
        lblDireccion.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblDireccion.setBounds(359, 56, 84, 20);
        thisJdialog.getContentPane().add(lblDireccion);
        
        txtPv = new JTextField();
        txtPv.setColumns(10);
        txtPv.setBounds(453, 56, 179, 22);
        thisJdialog.getContentPane().add(txtPv);

        JButton btnAniadir = new JButton(esInsert?"Añadir":"Actualizar");
        btnAniadir.setBounds(278, 164, 107, 33);
        btnAniadir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAniadir.setForeground(Color.WHITE);
        btnAniadir.setFocusPainted(false);
        btnAniadir.setBackground(new Color(0, 119, 160));
        btnAniadir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filaMaterial!=null && !filaMaterial[0].toString().isEmpty()){
                    aniadirFila((Integer) filaMaterial[0], selectedRow);
                }else{
                    aniadirFila(null, selectedRow);
                }
            }
        });
        thisJdialog.getContentPane().add(btnAniadir);
        
        JLabel lblStock = new JLabel("Stock");
        lblStock.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblStock.setBounds(359, 116, 84, 20);
        thisJdialog.getContentPane().add(lblStock);
        
        txtStock = new JTextField();
        txtStock.setEnabled(false);
        txtStock.setEditable(false);
        txtStock.setColumns(10);
        txtStock.setBounds(453, 117, 179, 22);
        thisJdialog.getContentPane().add(txtStock);

        if(!esInsert){
            cargarCampos(Arrays.asList(txtDescripcion, txtPc, txtPv, txtDescuento, txtCantidad), filaMaterial, selectedRow);
        }

        thisJdialog.setLocationRelativeTo(null);
        thisJdialog.setVisible(true);
	}

    private void cargarCampos(List<JTextField> campos, Object[] filaMaterial, int selectedRow) {
        for(int i=0; i<campos.size(); i++) {
            campos.get(i).setText(filaMaterial[i+1]!=null?filaMaterial[i+1].toString():"");
        }
        if(selectedRow == 0){
            txtPc.setEnabled(false);
            txtDescripcion.setEnabled(false);
        }else {
            listaMateriales.stream().filter(m -> m.getDescripcion().equals(txtDescripcion.getText())).findFirst().ifPresent(m -> {
                txtStock.setText(m.getCantidad()!=-1?String.valueOf(m.getCantidad()):"N/D");
            });
        }
    }

    private void aniadirFila(Integer idMaterial, Integer selectedRow) {
        if(algunTxtVacio(Arrays.asList(txtDescripcion, txtPc, txtDescuento, txtCantidad, txtPv))){
            JOptionPane.showMessageDialog(null, "Debe rellenar todos los campos antes de continuar");
        }else{
            PanelEdicionCita.aniadirFila(txtDescripcion.getText(), txtPc.getText(), txtDescuento.getText(), txtCantidad.getText(), txtPv.getText(), idMaterial, selectedRow);
            double cantidadAniadida = Double.parseDouble(txtCantidad.getText());

            // comprobamos el stock que le queda
            RecambioBean recambioBean = new RecambioBean();
            recambioBean.setNombre(txtDescripcion.getText());
            List<Object> listaRecambios = recambioBO.buscar(recambioBean);
            if(!listaRecambios.isEmpty()){
                recambioBean = (RecambioBean) listaRecambios.get(0);
                double cantidadPendiente = recambioBean.getCantidad();
                if(cantidadPendiente == 0){
                    // Sin stock
                    JOptionPane.showMessageDialog(null, "No queda stock");
                }else{
                    double diferencia = cantidadPendiente-cantidadAniadida;
                    if(diferencia == 0){
                        recambioBean.setCantidad(diferencia);
                        // Acaba de agotar el stock
                        JOptionPane.showMessageDialog(null, "Acaba de agotar el stock");
                    }else if(diferencia < 0){
                        recambioBean.setCantidad(0);
                        // aniadido mas de lo que queda
                        JOptionPane.showMessageDialog(null, "Stock insuficiente");
                    }else if(diferencia == 1){
                        recambioBean.setCantidad(diferencia);
                        JOptionPane.showMessageDialog(null, "¡OJO! Solo queda una unidad");
                    }else{
                        recambioBean.setCantidad(diferencia);
                    }
                    //recambioBO.actualizar(recambioBean);
                }
            }

            vaciarCajasTexto(Arrays.asList(txtDescripcion, txtPc, txtDescuento, txtCantidad, txtPv, txtStock));
            txtDescripcion.requestFocus();
        }
    }

    private void vaciarCajasTexto(List<JTextField> listaTxt){
        for(JTextField txt : listaTxt) {
            txt.setText("");
        }
    }

    private boolean algunTxtVacio(List<JTextField> listaTxt){
        boolean esVacio = false;
        for(JTextField txt : listaTxt) {
            if(txt.getText().equals(Constantes.STRING_VACIO)) {
                esVacio = true;
                break;
            }
        }
        return esVacio;
    }
}