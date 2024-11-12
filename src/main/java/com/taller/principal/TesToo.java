package com.taller.principal;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.taller.beans.ClienteBean;
import com.taller.beans.VehiculoBean;
import com.taller.conexion.Conexion;
import com.taller.constantes.Constantes;
import com.taller.jdialog.PanelAcceso;
import com.taller.negocio.impl.ClienteBOImpl;
import com.taller.negocio.impl.VehiculoBOImpl;
import com.taller.pantallas.*;
import com.taller.utils.GoogleDrive;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import com.taller.clases.ImagenPanel;

@Service
public class TesToo extends JFrame {
	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	private static JFrame ventanaPrincipal;
    private static int x;
	private static int y;

	private final ClienteBOImpl clienteBO = new ClienteBOImpl();
	private final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();
	
	private JButton btnPresupuestos, btnFinanzas, btnCitas, btnProv/*, recambios*/;

	/**
	 * Create the application.
	 */
	public TesToo() throws IOException, GeneralSecurityException {

		ventanaPrincipal = new JFrame();

		ventanaPrincipal.setIconImage(Toolkit.getDefaultToolkit().getImage(Objects.requireNonNull(getClass().getClassLoader().getResource("images/logo1.png"))));
		ventanaPrincipal.setResizable(false);
	    ventanaPrincipal.setUndecorated(true); 
	    ventanaPrincipal.setShape(new RoundRectangle2D.Double(0, 0, 900, 553, 20, 20));	
		ventanaPrincipal.setTitle("Talleres Calero");
		ventanaPrincipal.setBounds(100, 100, 900, 553);
		ventanaPrincipal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ventanaPrincipal.setFont(new Font("Lucida Console", Font.PLAIN, 10));
		ventanaPrincipal.getContentPane().setLayout(null);
		ventanaPrincipal.addMouseMotionListener(new MouseMotionAdapter() {
	           public void mouseDragged(MouseEvent e) {
	               Point p = MouseInfo.getPointerInfo().getLocation();
	               ventanaPrincipal.setLocation(p.x - TesToo.x, p.y - TesToo.y);
	            }
	         });
	         ventanaPrincipal.addMouseListener(new MouseAdapter() {
	            public void mouseClicked(MouseEvent e) {
	            	TesToo.x = e.getX();
	            	TesToo.y = e.getY();
	            }
	         });

		//contentPanePpal = new JPanel();
		//contentPanePpal.setLayout(null);
		
		ImagenPanel imageP = new ImagenPanel("images/Fondo_APP.jpg");
		//ImagenPanel imageP = new ImagenPanel("/images/pexels-fwstudio-164005.jpg");
		imageP.setBounds(0, 0, 900, 600);
		imageP.setLayout(null);
		ventanaPrincipal.getContentPane().add(imageP);
		
		JLabel lblCerrar = new JLabel("X");
		lblCerrar.setToolTipText("Cerrar la aplicación?");
		
		lblCerrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(PanelAcceso.acceso){
					Conexion.cerrarConexion();
					int opcion = JOptionPane.showConfirmDialog(null, "¿Desea subir la BBDD a Google Drive? Se sobreescribirá la que haya", "", JOptionPane.YES_NO_OPTION);
					if(opcion == 0){
						try {
							GoogleDrive.uploadFile();
						} catch (IOException | GeneralSecurityException ex) {
							throw new RuntimeException(ex);
						}
					}
				}
                System.exit(0);
			}
		});

		lblCerrar.setForeground(Color.BLACK);
		lblCerrar.setHorizontalAlignment(SwingConstants.CENTER);
		lblCerrar.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblCerrar.setCursor(new Cursor(Constantes.CURSOR_HAND));
		lblCerrar.setBounds(858, 11, 32, 40);
		imageP.add(lblCerrar);
		
		JLabel lblMinimizar = new JLabel("_");
		lblMinimizar.setToolTipText("Minimizar la aplicaci\u00F3n?");
		lblMinimizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ventanaPrincipal.setState(Frame.ICONIFIED);
			}
			
		});
		lblMinimizar.setHorizontalAlignment(SwingConstants.CENTER);
		lblMinimizar.setForeground(Color.BLACK);
		lblMinimizar.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblMinimizar.setCursor(new Cursor(Constantes.CURSOR_HAND));
		lblMinimizar.setBounds(826, 11, 32, 40);
		imageP.add(lblMinimizar);
		
		JLabel lblTitulo = new JLabel("Talleres Calero - Quiero tu dinero");
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitulo.setForeground(Color.BLACK);
		lblTitulo.setFont(new Font("Trebuchet MS", Font.PLAIN, 22));
		lblTitulo.setBounds(68, 11, 367, 40);
		imageP.add(lblTitulo);
		
		JLabel lblIcono = new JLabel("_");

		BufferedImage master = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/logo1.png")));
		Image scaled = master.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		//ventanaPrincipal.setIconImage(new ImageIcon(scaled).getImage());

		lblIcono.setIcon(new ImageIcon(scaled));

		lblIcono.setToolTipText("");
		lblIcono.setForeground(new Color(220, 127, 22));
		lblIcono.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblIcono.setBounds(10, 7, 48, 48);
		imageP.add(lblIcono);
		
		
//		JButton btnBbDD = new JButton("Base de datos");
//		btnBbDD.setHorizontalAlignment(SwingConstants.LEFT);
//		btnBbDD.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				new ConfigurationFile(ventanaPrincipal, true);
//			}
//		});
//
//		btnBbDD.setToolTipText("Configuraci\u00F3n de las conexiones a Base de datos Oracle");
//		
//		
//		
//		
//		btnBbDD.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/BBDD.png"))));
//		
//		btnBbDD.setFont(new Font("Arial", Font.BOLD, 10));
//		btnBbDD.setForeground(Color.BLACK);
//		btnBbDD.setBounds(10, 504, 175, 64);
//		btnBbDD.setOpaque(true);
//		btnBbDD.setContentAreaFilled(false);
//		btnBbDD.setFocusPainted(false);
//		btnBbDD.setBorderPainted(false);	
//		btnBbDD.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnBbDD);
		
//		JButton btnCierreVenta = new JButton("Completar venta");
//		btnCierreVenta.setHorizontalAlignment(SwingConstants.LEFT);
//
//		btnCierreVenta.setToolTipText("Ayuda en las pantallas de Telco");
//		btnCierreVenta.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Venta.png"))));
//		btnCierreVenta.setFont(new Font("Arial", Font.BOLD, 10));
//		btnCierreVenta.setForeground(Color.BLACK);
//		btnCierreVenta.setBounds(10, 429, 175, 64);
//		btnCierreVenta.setOpaque(true);
//		btnCierreVenta.setContentAreaFilled(false);
//		btnCierreVenta.setFocusPainted(false);
//		btnCierreVenta.setBorderPainted(false);	
//		btnCierreVenta.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnCierreVenta);
//
//		JButton btnGenDNI = new JButton("Generar DNI");
//		btnGenDNI.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				textFieldDNI.setVisible(true);
//				textFieldDNI.setText(null);
//				//textFieldDNI.setText(GeneraDNI.generaNifFinal(null));
//				StringSelection stringSelection = new StringSelection (textFieldDNI.getText());
//				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
//				clpbrd.setContents (stringSelection, null);
//			}
//		});
//		btnGenDNI.setHorizontalAlignment(SwingConstants.LEFT);
//
//		btnGenDNI.setToolTipText("Generaci�n autom�tica de DNI");
//		btnGenDNI.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Dni.png"))));
//		btnGenDNI.setFont(new Font("Arial", Font.BOLD, 10));
//		btnGenDNI.setForeground(Color.BLACK);
//		btnGenDNI.setBounds(405, 504, 151, 64);
//		btnGenDNI.setOpaque(true);
//		btnGenDNI.setContentAreaFilled(false);
//		btnGenDNI.setFocusPainted(false);
//		btnGenDNI.setBorderPainted(false);	
//		btnGenDNI.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnGenDNI);
		
		
		btnCitas = new JButton("Citas");
		btnCitas.setEnabled(false);
		btnCitas.addActionListener(e -> new ListadoCitas(ventanaPrincipal,true));
		btnCitas.setHorizontalAlignment(SwingConstants.LEFT);
		btnCitas.setToolTipText("Citas de clientes");
		BufferedImage master2 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/citas2.png")));
		Image scaled2 = master2.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH);
		btnCitas.setIcon(new ImageIcon(scaled2));
		btnCitas.setFont(new Font("Arial", Font.BOLD, 16));
		btnCitas.setForeground(Color.BLACK);
		btnCitas.setBounds(486, 157, 274, 64);
		btnCitas.setOpaque(true);
		btnCitas.setContentAreaFilled(false);
		btnCitas.setFocusPainted(false);
		btnCitas.setBorderPainted(false);
		btnCitas.setCursor(new Cursor(Constantes.CURSOR_HAND));
		imageP.add(btnCitas);
		
//		JButton btnPortaDonanFija = new JButton("Portabilidad donante fija");
//		btnPortaDonanFija.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new PortabilidadDonanteFija(ventanaPrincipal, true);
//			}
//		});
//		
//		btnPortaDonanFija.setHorizontalAlignment(SwingConstants.LEFT);
//
//		btnPortaDonanFija.setToolTipText("Avanzar eventos para la portabilidad donante fija");
//		btnPortaDonanFija.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Porta_donante_fija.png"))));
//		btnPortaDonanFija.setFont(new Font("Arial", Font.BOLD, 10));
//		btnPortaDonanFija.setForeground(Color.BLACK);
//		btnPortaDonanFija.setBounds(376, 70, 236, 64);
//		btnPortaDonanFija.setOpaque(true);
//		btnPortaDonanFija.setContentAreaFilled(false);
//		btnPortaDonanFija.setFocusPainted(false);
//		btnPortaDonanFija.setBorderPainted(false);	
//		btnPortaDonanFija.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnPortaDonanFija);

//		JButton btnEventoFutr = new JButton("Fuerza de trabajo");
//		btnEventoFutr.setHorizontalAlignment(SwingConstants.LEFT);
//
//		btnEventoFutr.setToolTipText("Mo\u00F3dulo de acceso para tareas de Fuerza de trabajo.");
//		btnEventoFutr.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Futr.png"))));
//		btnEventoFutr.setFont(new Font("Arial", Font.BOLD, 10));
//		btnEventoFutr.setForeground(Color.BLACK);
//		btnEventoFutr.setBounds(10, 62, 195, 58);
//		btnEventoFutr.setOpaque(true);
//		btnEventoFutr.setContentAreaFilled(false);
//		btnEventoFutr.setFocusPainted(false);
//		btnEventoFutr.setBorderPainted(false);	
//		btnEventoFutr.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnEventoFutr);

//		JButton btnConfUrl = new JButton("URL Web");
//		btnConfUrl.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//		btnConfUrl.setHorizontalAlignment(SwingConstants.LEFT);
//
//		btnConfUrl.setToolTipText("Configuraci�n de las URL para entornos TELCO");
//		btnConfUrl.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/URL.png"))));
//		btnConfUrl.setFont(new Font("Arial", Font.BOLD, 10));
//		btnConfUrl.setForeground(Color.BLACK);
//		btnConfUrl.setBounds(690, 430, 168, 64);
//		btnConfUrl.setOpaque(true);
//		btnConfUrl.setContentAreaFilled(false);
//		btnConfUrl.setFocusPainted(false);
//		btnConfUrl.setBorderPainted(false);	
//		btnConfUrl.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnConfUrl);
//
//		JButton btnConfEndPoints = new JButton("Rutas EndPoints");
//		btnConfEndPoints.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new ConfigurationOsrService(ventanaPrincipal, true);
//			}
//		});
//		btnConfEndPoints.setHorizontalAlignment(SwingConstants.RIGHT);
//
//		btnConfEndPoints.setToolTipText("Configuraci�n de los endpoints de los servicios TELCO");
//		btnConfEndPoints.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Red.png"))));
//		btnConfEndPoints.setFont(new Font("Arial", Font.BOLD, 10));
//		btnConfEndPoints.setForeground(Color.BLACK);
//		btnConfEndPoints.setBounds(673, 505, 185, 63);
//		btnConfEndPoints.setOpaque(true);
//		btnConfEndPoints.setContentAreaFilled(false);
//		btnConfEndPoints.setFocusPainted(false);
//		btnConfEndPoints.setBorderPainted(false);	
//		btnConfEndPoints.setCursor(new Cursor(Constantes.CURSOR_HAND));
//		imageP.add(btnConfEndPoints);
		
//		JButton btnCont = new JButton("Contrataci\u00F3n");
//		btnCont.setHorizontalAlignment(SwingConstants.LEFT);
//		btnCont.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Cont.png"))));
//		btnCont.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new UtilidadesCONT(ventanaPrincipal, true);
//			}
//		});
//		btnCont.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		btnCont.setToolTipText("Utilidades Contrataci\u00F3n");
//		btnCont.setOpaque(true);
//		btnCont.setForeground(Color.BLACK);
//		btnCont.setFont(new Font("Arial", Font.BOLD, 10));
//		btnCont.setFocusPainted(false);
//		btnCont.setContentAreaFilled(false);
//		btnCont.setBorderPainted(false);
//		btnCont.setBounds(10, 131, 195, 58);
//		imageP.add(btnCont);
		
		btnFinanzas = new JButton("Finanzas");
		btnFinanzas.setEnabled(false);
		btnFinanzas.setHorizontalAlignment(SwingConstants.LEFT);
		BufferedImage master3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/finanzas2.png")));
		Image scaled3 = master3.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
		btnFinanzas.setIcon(new ImageIcon(scaled3));
		btnFinanzas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    new GraficoFinanzas(ventanaPrincipal);
                } catch (IOException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
				/*GraficoFinanzas demo = new GraficoFinanzas( "Mobile Sales" );
				demo.setSize( 560 , 367 );
				demo.setVisible( true );*/
				//JOptionPane.showMessageDialog(null, "Aún no disponible. Espera a la siguiente temporada");
			}
		});
		btnFinanzas.setToolTipText("Flujo de gastos y benefecios");
		btnFinanzas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnFinanzas.setOpaque(true);
		btnFinanzas.setForeground(Color.BLACK);
		btnFinanzas.setFont(new Font("Arial", Font.BOLD, 16));
		btnFinanzas.setFocusPainted(false);
		btnFinanzas.setContentAreaFilled(false);
		btnFinanzas.setBorderPainted(false);
		btnFinanzas.setBounds(179, 347, 245, 64);
		imageP.add(btnFinanzas);
//		
//		JButton btnUsuarios = new JButton("Usuarios Telco");
//		btnUsuarios.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new ConfigurationUsers(ventanaPrincipal, true);
//			}			
//		});
//		btnUsuarios.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Users.png"))));
//		btnUsuarios.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		btnUsuarios.setToolTipText("Gesti�n usuarios Telco");
//		btnUsuarios.setOpaque(true);
//		btnUsuarios.setHorizontalAlignment(SwingConstants.LEFT);
//		btnUsuarios.setForeground(Color.BLACK);
//		btnUsuarios.setFont(new Font("Arial", Font.BOLD, 10));
//		btnUsuarios.setFocusPainted(false);
//		btnUsuarios.setContentAreaFilled(false);
//		btnUsuarios.setBorderPainted(false);
//		btnUsuarios.setBounds(195, 504, 175, 64);
//		imageP.add(btnUsuarios);
		
		btnProv = new JButton("Administración");
		btnProv.setEnabled(false);

		BufferedImage master4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/admin2.png")));
		Image scaled4 = master4.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);

		btnProv.setIcon(new ImageIcon(scaled4));

		btnProv.setToolTipText("Administración de clientes y vehículos");
		btnProv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnProv.setOpaque(true);
		btnProv.setHorizontalAlignment(SwingConstants.LEFT);
		btnProv.setForeground(Color.BLACK);
		btnProv.setFont(new Font("Arial", Font.BOLD, 16));
		btnProv.setFocusPainted(false);
		btnProv.setContentAreaFilled(false);
		btnProv.setBorderPainted(false);
		btnProv.setBounds(179, 151, 274, 78);
		btnProv.addActionListener(e -> {
            try {
                new Configuracion(ventanaPrincipal, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
		imageP.add(btnProv);
		
//		JButton btnParque = new JButton("Revision parque");
//		
//		btnParque.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/Parque.png"))));
//		btnParque.setToolTipText("M\u00F3dulo revisi\u00F3n de parque");
//		btnParque.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		btnParque.setOpaque(true);
//		btnParque.setHorizontalAlignment(SwingConstants.LEFT);
//		btnParque.setForeground(Color.BLACK);
//		btnParque.setFont(new Font("Arial", Font.BOLD, 10));
//		btnParque.setFocusPainted(false);
//		btnParque.setContentAreaFilled(false);
//		btnParque.setBorderPainted(false);
//		btnParque.setBounds(195, 432, 175, 58);
//		imageP.add(btnParque);
		
//		JButton btnGter = new JButton("Gestion de Terceros");
//
//		btnGter.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/GTER.png"))));
//		btnGter.setToolTipText("Utilidades de Gesti\u00F3n de Terceros");
//		btnGter.setOpaque(true);
//		btnGter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//		btnGter.setHorizontalAlignment(SwingConstants.LEFT);
//		btnGter.setForeground(Color.BLACK);
//		btnGter.setFont(new Font("Arial", Font.BOLD, 10));
//		btnGter.setFocusPainted(false);
//		btnGter.setContentAreaFilled(false);
//		btnGter.setBorderPainted(false);
//		btnGter.setBounds(10, 269, 195, 58);
//		imageP.add(btnGter);
		
		JLabel lblVersin = new JLabel("Versión 1.0 Rev.1");
		lblVersin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersin.setForeground(Color.BLACK);
		lblVersin.setFont(new Font("Trebuchet MS", Font.PLAIN, 9));
		lblVersin.setBounds(449, 11, 367, 40);
		imageP.add(lblVersin);
		
		btnPresupuestos = new JButton("Presupuestos");
		btnPresupuestos.setEnabled(false);
		
		master4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/presupuestos.png")));
		scaled4 = master4.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		btnPresupuestos.setIcon(new ImageIcon(scaled4));
		
		btnPresupuestos.setToolTipText("Presupuestos de clientes");
		btnPresupuestos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPresupuestos.setOpaque(true);
		btnPresupuestos.setHorizontalAlignment(SwingConstants.LEFT);
		btnPresupuestos.setForeground(Color.BLACK);
		btnPresupuestos.setFont(new Font("Arial", Font.BOLD, 16));
		btnPresupuestos.setFocusPainted(false);
		btnPresupuestos.setContentAreaFilled(false);
		btnPresupuestos.setBorderPainted(false);
		btnPresupuestos.setBounds(486, 347, 274, 84);
		btnPresupuestos.addActionListener(e -> /*JOptionPane.showMessageDialog(null, "Se desbloquea con el nivel 100 del pase de batalla")*/ new ListadoPresupuestos(ventanaPrincipal,true));
		imageP.add(btnPresupuestos);
		
		/*recambios = new JButton("Recambios");
		recambios.setEnabled(false);
		
		master4 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("images/recambios.png")));
		scaled4 = master4.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
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
		recambios.setBounds(368, 418, 274, 84);
		recambios.addActionListener(e -> *//*JOptionPane.showMessageDialog(null, "Se desbloquea con el nivel 100 del pase de batalla")*//* new ListadoRecambios(ventanaPrincipal,true));
		imageP.add(recambios);*/
		

		
		
		/*
		 * Acciones sobre los botones
		 */
//		btnEventoFutr.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new UtilidadesFUTR(ventanaPrincipal, true);
//			}
//		});
//
//		btnConfUrl.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new URLTelco(ventanaPrincipal, true);
//			}
//		});
//		
//		btnParque.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new RevisionParque(ventanaPrincipal, true);
//			}
//		});
//		
//		btnGter.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//new UtilidadesGTER(ventanaPrincipal, true);
//			}
//		});
		
		/*
		 * Ponemos el LookAndFeel
		 */
	   try {
		   UIManager.setLookAndFeel(new AluminiumLookAndFeel());
	   } catch (UnsupportedLookAndFeelException e1) {
		   System.out.println("No se ha podido establecer el diseño visual del programa");
	   }

		UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.computeIfAbsent("Table.alternateRowColor", k -> new Color(240, 240, 240));
		
		// Se visualiza la ventana.
		ventanaPrincipal.setLocationRelativeTo(null);
		ventanaPrincipal.setVisible(true);

		new PanelAcceso(ventanaPrincipal, crearWindowsListener());
	}

	private void importarClientes(Sheet sheet){
		try{
			for(int i=1; i<sheet.getPhysicalNumberOfRows(); i++){
				Row row = sheet.getRow(i);
				if(row.getCell(0)==null || row.getCell(0).getStringCellValue().isEmpty()){
					break;
				}
				String nombre = row.getCell(1).getStringCellValue();
				String apellido = row.getCell(2).getStringCellValue();
				String localidad = row.getCell(3).getStringCellValue();
				String telefono = row.getCell(4)!=null?row.getCell(4).getStringCellValue():"";
				telefono = telefono.replaceAll(" ","");

				ClienteBean cliente = new ClienteBean();
				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setLocalidad(localidad);
				cliente.setTelefono(!telefono.isEmpty() ?Long.parseLong(telefono):0L);

				try {
					clienteBO.insertar(cliente);
				}catch (Exception e1){
					System.out.println("Error al insertar el cliente. Nombre: " + nombre + " Apellido: " + apellido + " Localidad: " + localidad + " Telefono: " + telefono);
				}
			}
		}catch (Exception e){
			System.out.println("Error al insertar el cliente. Nombre: " + " Apellido: " + " Localidad: " + " Telefono: ");
		}
	}

	private void importarVehiculos(Sheet sheet){
		try{
			boolean empezar = false;
			for(int i=41; i<84; i++){
			Row row = sheet.getRow(i);
				VehiculoBean vehiculo = new VehiculoBean();

				int id = (int)row.getCell(0).getNumericCellValue();
				if(id == 41){
					System.out.println("Empezamos a leer los vehiculos");
				}
				vehiculo.setMarca(getValorCelda(row, 1));
				vehiculo.setModelo(getValorCelda(row, 2));
				vehiculo.setAnio(!getValorCelda(row, 3).isEmpty()?getValorCelda(row, 3):"0");
				vehiculo.setMatricula(getValorCelda(row, 4));
				vehiculo.setVin(getValorCelda(row, 5));
				vehiculo.setCentimetros(!getValorCelda(row, 6).isEmpty()?getValorCelda(row, 6):"0");
				vehiculo.setCaballos(!getValorCelda(row, 7).isEmpty()?getValorCelda(row, 7):"0");
				vehiculo.setTipo(getValorCelda(row, 8));
				vehiculo.setCombustible(getValorCelda(row, 9));
				vehiculo.setClienteBean(new ClienteBean());
				vehiculo.getClienteBean().setId((int)row.getCell(10).getNumericCellValue());

				try {
					vehiculoBO.insertar(vehiculo);
				}catch (Exception e1){
					System.out.println("Error al insertar el vehiculo. Marca: " + vehiculo.getMarca() + " Modelo: " + vehiculo.getModelo() + " Matricula: " + vehiculo.getMatricula() + " VIN: " + vehiculo.getVin());
				}
			}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private String getValorCelda(Row row, int column) {
		String valorCelda = null;
		if(row.getCell(column)!=null&&row.getCell(column).getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
			double valorNumerico = row.getCell(column).getNumericCellValue();
			valorCelda = String.valueOf((int) valorNumerico);
		}else {
			valorCelda = row.getCell(column)!=null?row.getCell(column).getStringCellValue():"";
		}
		return valorCelda;
	}

	private void habilitarCampos(List<JButton> list){
		list.forEach(button -> button.setEnabled(true));
	}

	protected WindowListener crearWindowsListener(){
		return new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}
			@Override
			public void windowClosing(WindowEvent e) {
			}
			@Override
			public void windowClosed(WindowEvent e) {
				if(PanelAcceso.acceso){
					habilitarCampos(Arrays.asList(btnPresupuestos, btnFinanzas, btnCitas, btnProv/*, recambios*/));
					int opcion = JOptionPane.showConfirmDialog(null, "¿Desea descargar la BBDD de Google Drive? Se sobreescribirá la BBDD local", "", JOptionPane.YES_NO_OPTION);
					if(opcion == 0){
						try {
							GoogleDrive.downloadFile();
						} catch (GeneralSecurityException | IOException ex) {
							try{
								File carpetaTokens = new File("./tokens");
								if(carpetaTokens.exists()){
									for(File token: carpetaTokens.listFiles()){
										token.delete();
									}
									GoogleDrive.downloadFile();
								}
							} catch(Exception e1) {
								JOptionPane.showMessageDialog(null, "Token expirado. Borre la carpeta tokens y vuelva a intentarlo");
								throw new RuntimeException(ex);
							}
						}
                    }
					Conexion.crearConexion();
				}
			}
			@Override
			public void windowIconified(WindowEvent e) {
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
			}
			@Override
			public void windowActivated(WindowEvent e) {
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		};
	}
}
