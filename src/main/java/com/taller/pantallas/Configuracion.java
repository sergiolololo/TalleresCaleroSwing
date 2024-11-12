package com.taller.pantallas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

public class Configuracion extends JDialog {

	@Serial
	private static final long serialVersionUID = 1L;

	public Configuracion(JFrame padre, boolean modal) throws IOException {
		super(padre,modal);
		setResizable(false);
		getContentPane().setFont(new Font("Arial", Font.PLAIN, 12));
		setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		setTitle("Administración");
		setShape(new RoundRectangle2D.Double(0, 0, 674, 189, 20, 20));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 674, 189);
		getContentPane().setLayout(null);
		
		JButton btnClientes = new JButton("Clientes");
		btnClientes.addActionListener(e -> new ListadoClientes(padre,true));
		btnClientes.setBounds(10, 30, 168, 60);
		btnClientes.setFont(new Font("Arial", Font.BOLD, 16));
		btnClientes.setHorizontalAlignment(SwingConstants.LEFT);
		btnClientes.setBorderPainted(false);
		btnClientes.setContentAreaFilled(false);
		btnClientes.setFocusPainted(false);

		BufferedImage master1 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/cliente2.png")));
		Image scaled1 = master1.getScaledInstance(60, 63, java.awt.Image.SCALE_SMOOTH);
		btnClientes.setIcon(new ImageIcon(scaled1));

		btnClientes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClientes.setToolTipText("Administración de clientes");
		getContentPane().add(btnClientes);
		
		JButton btnVehiculos = new JButton("Vehículos");
		btnVehiculos.setFont(new Font("Arial", Font.BOLD, 16));

		BufferedImage master = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/coche2.png")));
		Image scaled = master.getScaledInstance(70, 70, java.awt.Image.SCALE_SMOOTH);
		btnVehiculos.setIcon(new ImageIcon(scaled));

		btnVehiculos.addActionListener(e -> new ListadoVehiculos(padre,true));
		btnVehiculos.setToolTipText("Gestión de vehículos");
		btnVehiculos.setFocusPainted(false);
		btnVehiculos.setContentAreaFilled(false);
		btnVehiculos.setBorderPainted(false);		
		btnVehiculos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnVehiculos.setBounds(215, 30, 182, 60);
		getContentPane().add(btnVehiculos);

		JButton recambios = new JButton("Recambios");
		
		BufferedImage master4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/recambios.png")));
		Image scaled4 = master4.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		recambios.setIcon(new ImageIcon(scaled4));
		
		recambios.setToolTipText("Recambios");
		recambios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		recambios.setOpaque(true);
		recambios.setHorizontalAlignment(SwingConstants.LEFT);
		recambios.setForeground(Color.BLACK);
		recambios.setFont(new Font("Arial", Font.BOLD, 16));
		recambios.setFocusPainted(false);
		recambios.setContentAreaFilled(false);
		recambios.setBorderPainted(false);
		recambios.setBounds(434, 30, 216, 60);
		recambios.addActionListener(e -> /*JOptionPane.showMessageDialog(null, "Se desbloquea con el nivel 100 del pase de batalla")*/ new ListadoRecambios(padre,true));
		getContentPane().add(recambios);

	    // Se visualiza la ventana.	
	    setLocationRelativeTo(null);
	    setVisible(true);
	}
}