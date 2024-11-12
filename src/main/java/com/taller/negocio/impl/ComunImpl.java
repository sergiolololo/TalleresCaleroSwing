package com.taller.negocio.impl;

import com.taller.beans.ClienteBean;
import com.taller.beans.ErrorBean;
import com.taller.conexion.Conexion;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class ComunImpl {

    private Statement statement = null;

    public ErrorBean insertar(Object objetoInsert) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setMensaje(obtenerMensajeInsertOK());
        errorBean.setExisteError(false);

        List<Object> listaBuscar = buscar(objetoInsert);
        if(!listaBuscar.isEmpty()) {
            errorBean.setMensaje(obtenerMensajeInsertKO());
            errorBean.setExisteError(true);
        }else {
            try {
                int id = obtenerSecuencia();
                obtenerStatement().executeUpdate(obtenerQueryInsert(id, objetoInsert));
                errorBean.setId(id);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                errorBean.setMensaje(obtenerMensajeInsertKO());
                errorBean.setExisteError(true);
            }finally {
                cerrarConexion();
            }
        }
        return errorBean;
    }

    public List<Object> buscar(Object objetoBuscar) {
        List<Object> listadoSalida = new ArrayList<>();
        ResultSet resultSet;
        try {
            String consulta = getQueryBuscar(objetoBuscar);
            resultSet = obtenerStatement().executeQuery(consulta);
            while(resultSet.next()) {
                rellenarSalidaBuscar(resultSet, listadoSalida);
            }




            /*try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("clientesVehiculos.xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(is);
                XSSFSheet sheet = workbook.getSheetAt(0);

                int filaNum = 2;
                while(resultSet.next()) {
                    XSSFRow fila = sheet.createRow(filaNum);
                    *//*fila.createCell(1).setCellValue(resultSet.getInt("id_vehiculo"));
                    fila.createCell(2).setCellValue(resultSet.getString("marca"));
                    fila.createCell(3).setCellValue(resultSet.getString("modelo"));
                    fila.createCell(4).setCellValue(resultSet.getString("anio"));
                    fila.createCell(5).setCellValue(resultSet.getString("matricula"));
                    fila.createCell(6).setCellValue(resultSet.getString("vin"));
                    fila.createCell(7).setCellValue(resultSet.getString("cilindrada"));
                    fila.createCell(8).setCellValue(resultSet.getString("caballos"));
                    fila.createCell(9).setCellValue(resultSet.getString("tipo"));
                    fila.createCell(10).setCellValue(resultSet.getString("combustible"));
                    fila.createCell(11).setCellValue(resultSet.getInt("id_cliente"));*//*


                    fila.createCell(1).setCellValue(resultSet.getInt("id_cliente"));
                    fila.createCell(2).setCellValue(resultSet.getString("nombre"));
                    fila.createCell(3).setCellValue(resultSet.getString("apellido"));
                    fila.createCell(4).setCellValue(resultSet.getString("localidad"));
                    fila.createCell(5).setCellValue(resultSet.getLong("telefono"));



                    rellenarSalidaBuscar(resultSet, listadoSalida);






                    filaNum++;
                }


                File dirDestino = new File("C:\\Users\\sergy\\Documents\\GitHub\\Projects\\TalleresCaleroSwing\\src\\main\\resources\\clientesVehiculos.xlsx");
                FileOutputStream fileOut = new FileOutputStream(dirDestino.getAbsolutePath());
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();



                is.close();
            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }*/
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            cerrarConexion();
        }
        return listadoSalida;
    }

    public ErrorBean actualizar(Object objetoUpdate) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setMensaje(obtenerMensajeUpdateOK());
        errorBean.setExisteError(false);

        try {
            String update = getQueryUpdate(objetoUpdate);
            obtenerStatement().executeUpdate(update);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            errorBean.setMensaje(obtenerMensajeUpdateKO());
            errorBean.setExisteError(false);
        }finally {
            cerrarConexion();
        }
        return errorBean;
    }

    public int borrar(int id) {
        int registrosBorrados = 0;
        if(id != 0) {
            try {
                registrosBorrados = obtenerStatement().executeUpdate(getQueryDelete(id));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }finally {
                cerrarConexion();
            }
        }
        return registrosBorrados;
    }

    protected abstract String obtenerQueryInsert(int id, Object objetoInsert);
    protected String obtenerMensajeInsertOK() {
        return null;
    }
    protected String obtenerMensajeInsertKO(){
        return null;
    }
    protected String getQueryBuscar(Object objetoBuscar){
        return null;
    }
    protected void rellenarSalidaBuscar(ResultSet resultSet, List<Object> listadoSalida) throws SQLException   {
    };
    protected String getQueryUpdate(Object objetoUpdate){
        return null;
    }
    protected String obtenerMensajeUpdateOK(){
        return null;
    }
    protected String obtenerMensajeUpdateKO(){
        return null;
    }
    protected abstract String getQueryDelete(int id);

    public int obtenerSecuencia() {
        int secuencia = 1;
        try {
            statement = Conexion.obtenerConexion().createStatement();
            ResultSet resultSet = statement.executeQuery(obtenerQuerySecuencia());
            while(resultSet.next()) {
                secuencia = resultSet.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return secuencia;
    }

    protected abstract String obtenerQuerySecuencia();

    protected void cerrarConexion() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (Conexion.obtenerConexion() != null) {
                Conexion.obtenerConexion().close();
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected Statement obtenerStatement() throws SQLException {
        return Conexion.obtenerConexion().createStatement();
    }
}