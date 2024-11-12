package com.taller.pantallas;

import com.taller.beans.ClienteBean;
import com.taller.beans.VehiculoBean;
import com.taller.negocio.impl.CitaBOImpl;
import com.taller.negocio.impl.VehiculoBOImpl;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.swing.border.BevelBorder;

public class GraficoFinanzas extends JDialog {
    private final CitaBOImpl citaBO = new CitaBOImpl();
    private final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();

    private final ChartPanel chart;
    private final JLabel lblAnio;
    private final JComboBox<String> cmbAnio;
    private final JTable tabla;
    private Map<Integer, List<Object[]>> mapaAnioMeses = new LinkedHashMap<>();
    private final JComboBox<String> cmbCliente;
    private JComboBox<String> cmbVehiculo;

    private List<Object> listadoVehiculos;
    private VehiculoBean vehiculoBeanFiltrar = new VehiculoBean();

    public GraficoFinanzas( JFrame padre ) throws IOException, SQLException {
        super(padre,true);
        setResizable(false);
        setTitle("Flujo de gastos y benefecios");
        setFont(new Font("Lucida Sans", Font.PLAIN, 14));
        setShape(new RoundRectangle2D.Double(0, 0, 1588, 1053, 30, 30));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1344, 984);
        getContentPane().setLayout(null);

        chart = new ChartPanel(ChartFactory.createBarChart(
                "Gráfico de gastos y beneficios",
                "",
                "Euros",
                new DefaultCategoryDataset(),
                PlotOrientation.VERTICAL,
                true, true, false));
        chart.setLocation(38, 45);
        chart.setSize( 1262, 489 );
        getContentPane().add(chart);

        JComboBox<String> cmbMesAnio = crearComboMesAnio();
        getContentPane().add(cmbMesAnio);
        
        JLabel lblNewLabel = new JLabel("Rango");
        lblNewLabel.setBounds(38, 10, 56, 25);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        getContentPane().add(lblNewLabel);

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
        tabla.setModel(new DefaultTableModel(new Object[0][], new String []{"Fecha", "Gastos", "Ingresos", "Ganancia neta", "Pendiente pago"}){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setBounds(10, 82, 919, 330);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportBorder(null);
        scrollPane.setBounds(38, 566, 989, 360);
        scrollPane.setViewportView(tabla);
        getContentPane().add(scrollPane);
        
        lblAnio = new JLabel("Año");
        lblAnio.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblAnio.setBounds(219, 11, 45, 25);
        getContentPane().add(lblAnio);

        cmbAnio = new JComboBox<>();
        cmbAnio.setBounds(259, 13, 78, 21);
        getContentPane().add(cmbAnio);

        JPanel panel = new JPanel();
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel.setBounds(1037, 566, 263, 360);
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblFiltro = new JLabel("Filtrar por:");
        lblFiltro.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblFiltro.setBounds(21, 20, 92, 25);
        panel.add(lblFiltro);

        JLabel lblCliente = new JLabel("Cliente");
        lblCliente.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblCliente.setBounds(21, 66, 92, 25);
        panel.add(lblCliente);
        
        cmbCliente = new JComboBox<>();
        cmbCliente.setBounds(21, 96, 221, 21);
        cmbCliente.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                String cliente = e.getItem().toString();
                List<String> vehiculos = listadoVehiculos.stream()
                        .filter(obj -> (((VehiculoBean) obj).getClienteBean().getNombre() + " " + ((VehiculoBean) obj).getClienteBean().getApellido()).equals(cliente))
                        .map(obj -> ((VehiculoBean) obj).getMarca() + " " + ((VehiculoBean) obj).getModelo())
                        .distinct()
                        .sorted()
                        .toList();

                aniadirValoresCombo(vehiculos, cmbVehiculo);

                vehiculoBeanFiltrar = new VehiculoBean();
                if(!e.getItem().equals("--- TODOS ---")){
                    vehiculoBeanFiltrar.setClienteBean(new ClienteBean());
                    vehiculoBeanFiltrar.getClienteBean().setNombre(cliente);
                }
                refrescarDatos();
            }
        });
        panel.add(cmbCliente);

        JLabel lblVehiculo = new JLabel("Vehículo");
        lblVehiculo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblVehiculo.setBounds(21, 126, 92, 25);
        panel.add(lblVehiculo);
        
        cmbVehiculo = new JComboBox<>();
        cmbVehiculo.setEnabled(false);
        cmbVehiculo.setBounds(21, 156, 221, 21);
        cmbVehiculo.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                if(!e.getItem().equals("--- TODOS ---")){
                    vehiculoBeanFiltrar.setMarca(e.getItem().toString());
                }else{
                    vehiculoBeanFiltrar.setMarca(null);
                }
                refrescarDatos();
            }
        });
        panel.add(cmbVehiculo);

        rellenarCombos();
        refrescarDatos();

        // Se visualiza la ventana.
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refrescarDatos() {
        List<Object[]> listadoSalida;
        try {
            listadoSalida = citaBO.obtenerGananciasPorAnio(true, vehiculoBeanFiltrar);
            crearMapaAnioMeses(true, listadoSalida);
            DefaultComboBoxModel<String> comboBoxModelAnios = new DefaultComboBoxModel<>();
            mapaAnioMeses.forEach((k, v) -> comboBoxModelAnios.addElement(String.valueOf(k)));

            cmbAnio.setModel(comboBoxModelAnios);
            cmbAnio.addItemListener(ee -> {
                if(ee.getStateChange() == ItemEvent.SELECTED) {
                    chart.setChart(createChart(true, ee.getItem().toString()));
                }
            });
            cmbAnio.setSelectedIndex(comboBoxModelAnios.getSize()-1);
            chart.setChart(createChart(true, cmbAnio.getSelectedIndex()>=0?cmbAnio.getSelectedItem().toString(): ""));

            cargarTabla();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void aniadirValoresCombo(List<String> valores, JComboBox<String> cmb) {
        cmb.setEnabled(true);
        cmb.removeAllItems();
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("--- TODOS ---");
        for (String valor : valores) {
            comboBoxModel.addElement(valor);
        }
        cmb.setModel(comboBoxModel);
    }

    private void rellenarCombos() {
        listadoVehiculos = vehiculoBO.buscar(new VehiculoBean());
        List<String> clientes = listadoVehiculos.stream()
                .map(obj -> ((VehiculoBean) obj).getClienteBean())
                .map(cliente -> cliente.getNombre() + " " + cliente.getApellido())
                .distinct()
                .sorted()
                .toList();

        DefaultComboBoxModel<String> comboBoxModelClientes = new DefaultComboBoxModel<>();
        comboBoxModelClientes.addElement("--- TODOS ---");
        for (String cliente : clientes) {
            comboBoxModelClientes.addElement(cliente);
        }
        cmbCliente.setModel(comboBoxModelClientes);

        DefaultComboBoxModel<String> comboBoxModelVehiculos = new DefaultComboBoxModel<>();
        comboBoxModelVehiculos.addElement("--- TODOS ---");
        cmbVehiculo.setModel(comboBoxModelVehiculos);
    }

    private JComboBox<String> crearComboMesAnio() {
        ComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(new String[] {"Mensual", "Anual"});
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setModel(comboBoxModel);
        comboBox.setBounds(97, 13, 89, 21);
        comboBox.addItemListener(e -> {
            if(e.getStateChange() == ItemEvent.SELECTED) {
                List<Object[]> listadoSalida;
                try {
                    listadoSalida = citaBO.obtenerGananciasPorAnio(e.getItem().equals("Mensual"), vehiculoBeanFiltrar);
                    crearMapaAnioMeses(e.getItem().equals("Mensual"), listadoSalida);
                    if(!e.getItem().equals("Mensual")){
                        lblAnio.setVisible(false);
                        cmbAnio.setVisible(false);
                        chart.setChart(createChart(e.getItem().equals("Mensual"), ""));
                    }else{
                        lblAnio.setVisible(true);
                        cmbAnio.setVisible(true);
                        chart.setChart(createChart(e.getItem().equals("Mensual"), cmbAnio.getSelectedIndex()>=0?cmbAnio.getSelectedItem().toString(): ""));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return comboBox;
    }

    private void crearMapaAnioMeses(boolean esMensual, List<Object[]> listado) {
        mapaAnioMeses = new LinkedHashMap<>();
        String anioChart = null;
        if(esMensual){
            for(Object[] filaArray: listado){
                int anio = Integer.parseInt(String.valueOf(filaArray[0]).substring(0, 4));
                if(anioChart == null){
                    anioChart = String.valueOf(anio);
                }
                List<Object[]> lista = mapaAnioMeses.get(anio)!=null?mapaAnioMeses.get(anio):new ArrayList<>();
                lista.add(filaArray);
                mapaAnioMeses.put(anio, lista);
            }
        }else{
            for(Object[] filaArray: listado){
                int anio = Integer.parseInt(String.valueOf(filaArray[0]));
                if(anioChart == null){
                    anioChart = String.valueOf(anio);
                }
                List<Object[]> lista = mapaAnioMeses.get(anio)!=null?mapaAnioMeses.get(anio):new ArrayList<>();
                lista.add(filaArray);
                mapaAnioMeses.put(anio, lista);
            }
        }
    }

    private void cargarTabla() {
        ((DefaultTableModel) tabla.getModel()).setRowCount(0);
        for(Map.Entry<Integer, List<Object[]>> mapa: mapaAnioMeses.entrySet()){
            for(Object[] fila: mapa.getValue()){
                String anio = String.valueOf(fila[0]).substring(0, 4);
                String mes = getMonth((Integer) fila[0]);
                ((DefaultTableModel) tabla.getModel()).addRow(new Object[]{(
                        anio + " - " + mes),
                        String.format("%.2f", (double)fila[1]).replace(",", "."),
                        String.format("%.2f", (double)fila[5]).replace(",", "."),
                        String.format("%.2f", (double)fila[3]).replace(",", "."),
                        String.format("%.2f", (double)fila[6]).replace(",", ".")});
            }
        }
    }

    private CategoryDataset createDataset(boolean esMensual, Map<Integer, List<Object[]>> mapaAnioMeses) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
        if(!esMensual){
            for(Map.Entry<Integer, List<Object[]>> mapa: mapaAnioMeses.entrySet()){
                dataset.addValue( (Double) mapa.getValue().get(0)[1] , "Gastos" , mapa.getKey() );
                dataset.addValue( (Double) mapa.getValue().get(0)[5] , "Ingresos" , mapa.getKey() );
                dataset.addValue( (Double) mapa.getValue().get(0)[3] , "Neto" , mapa.getKey() );
                dataset.addValue( (Double) mapa.getValue().get(0)[6] , "Pendiente pago" , mapa.getKey() );
            }
        }else{
            if(cmbAnio.getSelectedIndex()>=0){
                int anio = Integer.parseInt(cmbAnio.getSelectedItem().toString());
                for(Object[] fila: mapaAnioMeses.get(anio)){
                    dataset.addValue( (Double) fila[1] , "Gastos" , getMonth((Integer) fila[0]) );
                    dataset.addValue( (Double) fila[5] , "Ingresos" , getMonth((Integer) fila[0]) );
                    dataset.addValue( (Double) fila[3] , "Neto" , getMonth((Integer) fila[0]) );
                    dataset.addValue( (Double) fila[6] , "Pendiente pago" , getMonth((Integer) fila[0]) );
                }
            }
        }
        return dataset;
    }

    private String getMonth(int i){

        int mes = Integer.parseInt(String.valueOf(i).substring(4));
        return switch (mes) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            default -> "Diciembre";
        };
    }

    private JFreeChart createChart(boolean esMensual, String leyenda) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Gráfico de gastos y beneficios",
                leyenda,
                "Euros",
                createDataset(esMensual, mapaAnioMeses),
                PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        DecimalFormat decimalformat1 = new DecimalFormat("#0.00");
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        renderer.setDefaultItemLabelsVisible(true);
        renderer.setDefaultSeriesVisible(true);

        return chart;
    }
}