package com.taller.pantallas;

import com.taller.jdialog.PanelInsertarCliente;
import com.taller.negocio.impl.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


public abstract class ComunPantalla extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    protected final ClienteBOImpl clienteBO = new ClienteBOImpl();
    protected final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();
    protected CitaBOImpl citaBO = new CitaBOImpl();
    protected final MaterialBOImpl materialBO = new MaterialBOImpl();
    protected final RecambioBOImpl recambioBO = new RecambioBOImpl();

    private static List<Object[]> lista;
    private static List<Object[]> listaNoModel;

    public static String filtroTabla = "";
    public static int columnaFiltrada = -1;
    public static JTable tabla;
    protected JButton btnNuevo, btnEditar, btnEliminar;
    protected JDialog jDialog;
    protected JScrollPane scrollPane;

    public ComunPantalla(JFrame padre, boolean modal) {
        jDialog = new JDialog(padre,modal);
        //super(padre,modal);
        jDialog.setResizable(false);
        jDialog.setTitle(getNombrePantalla());
        jDialog.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        jDialog.setShape(new RoundRectangle2D.Double(0, 0, 1444, 675, 30, 30));
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog.setBounds(100, 100, 1281, 591);
        jDialog.getContentPane().setLayout(null);

        tabla = new JTable() {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);

                try {
                    tip = getValueAt(rowIndex, colIndex)!=null?getValueAt(rowIndex, colIndex).toString():null;
                } catch (RuntimeException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage());
                }

                return tip;
            }
        };
        tabla.getSelectionModel().addListSelectionListener(arg0 -> {
            if (!arg0.getValueIsAdjusting()) {
                btnEditar.setEnabled(tabla.getSelectedRow() >= 0);
                btnEliminar.setEnabled(tabla.getSelectedRow() >= 0);
            }
        });
        tabla.setModel(new DefaultTableModel(new Object[0][], getNombreColumnas()){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column)!=null?getValueAt(0, column).getClass():Object.class;
            }
        });
        cargarTabla();

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setBounds(10, 82, 919, 330);
        setearAnchoColumnas();
        tabla.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int columnaFiltrando = tabla.columnAtPoint(e.getPoint());
                String nombreColumna = tabla.getColumnName(columnaFiltrando);

                String filtro = columnaFiltrando==columnaFiltrada?filtroTabla: "";
                String filtrando = JOptionPane.showInputDialog("Filtrar columna " + nombreColumna, filtro);
                if(filtrando != null){
                    cambiarNombreHeader(-1, true);
                    filtroTabla = filtrando;
                    columnaFiltrada = columnaFiltrando;
                    filtrarTabla(columnaFiltrando, filtroTabla);
                    if(!filtroTabla.isEmpty()){
                        cambiarNombreHeader(columnaFiltrando, false);
                    }
                }
            }
        });

        scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(null);
        scrollPane.setBounds(10, 62, 1247, 471);
        scrollPane.setViewportView(tabla);
        jDialog.getContentPane().add(scrollPane);

        configurarBotonNuevo(padre);
        btnNuevo.setEnabled(true);
        btnNuevo.setHorizontalAlignment(SwingConstants.CENTER);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFocusPainted(false);
        btnNuevo.setBackground(new Color(0, 119, 160));
        btnNuevo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jDialog.getContentPane().add(btnNuevo);

        configurarBotonEditar(padre);
        btnEditar.setHorizontalAlignment(SwingConstants.CENTER);
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setEnabled(false);
        btnEditar.setBackground(new Color(0, 119, 160));
        jDialog.getContentPane().add(btnEditar);

        configurarBotonEliminar();
        btnEliminar.setEnabled(false);
        btnEliminar.setHorizontalAlignment(SwingConstants.CENTER);
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBackground(new Color(0, 119, 160));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(e -> {
            eliminar();
        });
        jDialog.getContentPane().add(btnEliminar);

        configurarBounds();

        // Se visualiza la ventana.
        jDialog.setLocationRelativeTo(null);
        jDialog.setVisible(true);
    }

    private void eliminar() {
        int opcion = JOptionPane.showConfirmDialog(this, getMensajeEliminar(), "", JOptionPane.YES_NO_OPTION);
        if(opcion == 0){
            eliminarPorId((Integer) tabla.getModel().getValueAt(tabla.getSelectedRow(), 0));
            cargarTabla();
        }
    }

    public void cargarTabla() {
        limpiarTabla();
        lista = new ArrayList<>();
        listaNoModel = new ArrayList<>();
        List<Object> listaObjetos = obtenerListado();
        for (Object object : listaObjetos) {
            Object[] fila = aniadirFilaCargarTabla(object);
            ((DefaultTableModel) tabla.getModel()).addRow(fila);
            lista.add(fila);

            Object[] fila2 = aniadirFilaNoModelCargarTabla(object);
            listaNoModel.add(fila2);
        }
    }
    public static void limpiarTabla(){
        int numeroFilas = tabla.getModel().getRowCount();
        for(int i=0; i<numeroFilas; i++) {
            ((DefaultTableModel) tabla.getModel()).removeRow(0);
        }
    }

    private void filtrarTabla(int columnaFiltrando, String filtrando){
        limpiarTabla();
        for(int i=0; i<listaNoModel.size(); i++){
            Object[] fila = listaNoModel.get(i);
            if(fila[columnaFiltrando].toString().toUpperCase().contains(filtrando.toUpperCase())){
                ((DefaultTableModel) tabla.getModel()).addRow(lista.get(i));
            }
        }
        tabla.repaint();
        tabla.getTableHeader().repaint();
    }

    private void cambiarNombreHeader(int columna, boolean resetearTodas){
        if(resetearTodas){
            int columnas = tabla.getTableHeader().getColumnModel().getColumnCount();
            for(int i=0; i<columnas; i++){
                String valorHeader = tabla.getTableHeader().getColumnModel().getColumn(i).getHeaderValue().toString().replace(" *", "");
                tabla.getTableHeader().getColumnModel().getColumn(i).setHeaderValue(valorHeader);
            }
        }else{
            String valorHeader = tabla.getTableHeader().getColumnModel().getColumn(columna).getHeaderValue().toString();
            valorHeader = valorHeader + " *";
            tabla.getTableHeader().getColumnModel().getColumn(columna).setHeaderValue(valorHeader);
        }
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
                llamarCargarTabla();
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

    protected abstract String getNombrePantalla();
    protected abstract String[] getNombreColumnas();
    protected abstract void setearAnchoColumnas();
    protected abstract void configurarBotonNuevo(JFrame padre);
    protected abstract void configurarBotonEditar(JFrame padre);
    protected abstract void configurarBotonEliminar();
    protected abstract String getMensajeEliminar();
    protected abstract Object[] aniadirFilaCargarTabla(Object object);
    protected abstract Object[] aniadirFilaNoModelCargarTabla(Object object);
    protected abstract List<Object> obtenerListado();
    protected abstract void eliminarPorId(int id);
    protected void configurarBounds(){
    }
    protected abstract void llamarCargarTabla();
}