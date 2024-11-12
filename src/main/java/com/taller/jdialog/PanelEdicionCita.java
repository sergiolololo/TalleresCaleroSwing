package com.taller.jdialog;

import com.taller.beans.VehiculoBean;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Objects;


public class PanelEdicionCita extends PanelEdicionCitaPresupuestoComun {
	public PanelEdicionCita(JFrame padre, Object[] filaCita, boolean esNueva, VehiculoBean vehiculoPasar, WindowListener windowListener, boolean esPresupuesto) throws IOException {
		super(padre, filaCita, esNueva, vehiculoPasar, windowListener, esPresupuesto);
	}

	@Override
	protected void crearBotonImagen(boolean esNueva, Object[] filaCita) {
		JLabel lblNewLabel_2 = new JLabel("Indique el estado del pago");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setBounds(1304, 275, 273, 20);
		thisJdialog.getContentPane().add(lblNewLabel_2);
		
		btnImagen = new JButton("No pagado");
		btnImagen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/nopagado2.png"))));
		btnImagen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnImagen.getText().equals("Pagado")) {
					btnImagen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/nopagado2.png"))));
					btnImagen.setText("No pagado");
				}else if(btnImagen.getText().equals("No pagado")) {
					btnImagen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pagadoparte.png"))));
					btnImagen.setText("Pagado parcial");
				}else {
					btnImagen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pagado2.png"))));
					btnImagen.setText("Pagado");
				}
			}
		});

		if(!esNueva){
			int pagado = (int) filaCita[14];
			if(pagado == 1){
				btnImagen.setText("Pagado");
				btnImagen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pagado2.png"))));
			}else if(pagado == 2){
				btnImagen.setText("Pagado parcial");
				btnImagen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pagadoparte.png"))));
			}
		}

		btnImagen.setOpaque(true);
		btnImagen.setHorizontalAlignment(SwingConstants.RIGHT);
		btnImagen.setForeground(Color.BLACK);
		btnImagen.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnImagen.setFocusPainted(false);
		btnImagen.setContentAreaFilled(false);
		btnImagen.setBorderPainted(false);
		btnImagen.setBounds(1362, 306, 215, 64);
		thisJdialog.getContentPane().add(btnImagen);
	}
	
	@Override
	protected void crearBotonesPagadoYFaltaPagar() {
		JLabel lblNewLabel_4 = new JLabel("Pagado");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setBounds(697, 100, 55, 23);
		thisJdialog.getContentPane().add(lblNewLabel_4);

        JLabel lblNewLabel_7 = new JLabel("Falta por pagar");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_7.setBounds(884, 100, 130, 23);
		thisJdialog.getContentPane().add(lblNewLabel_7);
		
		txtImportePagado = new JTextField();
		txtImportePagado.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtImportePagado.setBackground(new Color(152, 205, 129));
		txtImportePagado.setForeground(Color.BLACK);
		txtImportePagado.setBounds(763, 102, 96, 19);
		txtImportePagado.setColumns(10);
		txtImportePagado.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				actualizarFaltaPagar();
			}
		});
		thisJdialog.getContentPane().add(txtImportePagado);
		
		txtFaltaPagar = new JTextField();
		txtFaltaPagar.setFont(new Font("Tahoma", Font.BOLD, 14));
		txtFaltaPagar.setEditable(false);
		txtFaltaPagar.setForeground(Color.BLACK);
		txtFaltaPagar.setBackground(new Color(214, 152, 152));
		txtFaltaPagar.setBounds(1013, 102, 96, 19);
		thisJdialog.getContentPane().add(txtFaltaPagar);
		txtFaltaPagar.setColumns(10);
	}
}