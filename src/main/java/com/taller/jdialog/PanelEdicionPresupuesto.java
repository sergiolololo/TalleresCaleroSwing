package com.taller.jdialog;

import com.taller.beans.CitaBean;
import com.taller.beans.VehiculoBean;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


public class PanelEdicionPresupuesto extends PanelEdicionCitaPresupuestoComun {
	public PanelEdicionPresupuesto(JFrame padre, Object[] filaCita, boolean esNueva, VehiculoBean vehiculoPasar, WindowListener windowListener, boolean esPresupuesto) throws IOException {
		super(padre, filaCita, esNueva, vehiculoPasar, windowListener, esPresupuesto);
	}

	@Override
	protected void crearBotonImagen(boolean esNueva, Object[] filaCita) throws IOException {
		JButton btnMoverACitas = new JButton("Mover a citas");
		btnMoverACitas.setEnabled(!esNueva);
		
		BufferedImage master2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/citas2.png")));
		Image scaled2 = master2.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH);
		btnMoverACitas.setIcon(new ImageIcon(scaled2));
		btnMoverACitas.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea mover este presupuesto a citas?", "Mover a citas", JOptionPane.YES_NO_OPTION);
            if(opcion == 0){
                CitaBean citaBean = new CitaBean();
				citaBean.setId(Integer.parseInt(lblNumeroFactura.getText()));
				citaBean.setEsPresupuesto(false);
                citaBO.actualizar(citaBean);

				JOptionPane.showMessageDialog(null, "Presupuesto movido a citas correctamente", "Mover a citas", JOptionPane.INFORMATION_MESSAGE);
				thisJdialog.dispose();
				thisJdialog.setVisible(false);
            }
        });
		btnMoverACitas.setToolTipText("Mover a citas");
		btnMoverACitas.setOpaque(true);
		btnMoverACitas.setHorizontalAlignment(SwingConstants.LEFT);
		btnMoverACitas.setForeground(Color.BLACK);
		btnMoverACitas.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnMoverACitas.setFocusPainted(false);
		btnMoverACitas.setContentAreaFilled(false);
		btnMoverACitas.setBorderPainted(false);
		btnMoverACitas.setBounds(1394, 305, 230, 64);
		thisJdialog.getContentPane().add(btnMoverACitas);
	}
}