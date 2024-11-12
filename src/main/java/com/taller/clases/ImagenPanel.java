package com.taller.clases;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagenPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImageIcon imagen;
	String nombre;
	
	public ImagenPanel(String nombre) {
		this.nombre = nombre;
	}


	public void paint(Graphics g) {
		Dimension tamanio=getSize();
		imagen =new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(nombre)));
		g.drawImage(imagen.getImage(),0,0,tamanio.width,tamanio.height,null);
		setOpaque(false);
		super.paint(g);
	}
	
}
