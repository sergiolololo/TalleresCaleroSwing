package com.taller.jdialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;

public class PanelAcceso extends JDialog {
    private final JDialog thisJdialog;
    private final JPasswordField txtPassword;
    public static boolean acceso = false;

	public PanelAcceso(JFrame padre, WindowListener windowListener) {
        thisJdialog = new JDialog(padre, true);
        thisJdialog.setResizable(false);
        thisJdialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        thisJdialog.setTitle("Acceso usuario");
        thisJdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        thisJdialog.getContentPane().setLayout(null);
        thisJdialog.setShape(new RoundRectangle2D.Double(0, 0, 358, 200, 30, 30));
        thisJdialog.setSize(358, 171);
        
        JLabel lblApellido = new JLabel("Contraseña");
        lblApellido.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblApellido.setBounds(29, 25, 84, 20);
        thisJdialog.getContentPane().add(lblApellido);
        
        txtPassword = new JPasswordField();
        txtPassword.setColumns(10);
        txtPassword.setBounds(134, 27, 179, 22);
        thisJdialog.getContentPane().add(txtPassword);

        JButton btnAniadir = new JButton("Acceder");
        btnAniadir.setBounds(118, 77, 107, 33);
        btnAniadir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAniadir.setForeground(Color.WHITE);
        btnAniadir.setFocusPainted(false);
        btnAniadir.setBackground(new Color(0, 119, 160));
        btnAniadir.addActionListener(e -> {
            if(String.valueOf(txtPassword.getPassword()).equals("8031")){
                acceso = true;
                thisJdialog.setVisible(false);
                thisJdialog.dispose();
            }else{
                JOptionPane.showMessageDialog(thisJdialog, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        thisJdialog.getContentPane().add(btnAniadir);

        thisJdialog.addWindowListener(windowListener);

        thisJdialog.setLocationRelativeTo(null);
        thisJdialog.setVisible(true);
	}
}