package com.taller;

import com.taller.beans.ClienteBean;
import com.taller.beans.VehiculoBean;
import com.taller.conexion.Conexion;
import com.taller.negocio.impl.ClienteBOImpl;
import com.taller.negocio.impl.VehiculoBOImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImportarVehiculos {
    private static final VehiculoBOImpl vehiculoBO = new VehiculoBOImpl();
    public static void main(String[] args) throws IOException {

        Conexion.crearConexion();

        FileInputStream fis = new FileInputStream(new File("C:\\Users\\sergy\\Downloads\\DATOS PROGRAMA.xlsx"));
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
        readExcelXls(mySheet);
        myWorkBook.close();
        Conexion.cerrarConexion();
    }

    private static void readExcelXls(XSSFSheet mySheet) {
        for(int i = 44; i < 91; i++){
            try{
                Row row = mySheet.getRow(i);

                Cell marca = row.getCell(1);
                Cell modelo = row.getCell(2);
                Cell anio = row.getCell(3);
                Cell matricula = row.getCell(4);
                Cell vin = row.getCell(5);
                Cell cilindrada = row.getCell(6);
                Cell caballos = row.getCell(7);
                Cell tipo = row.getCell(8);
                Cell combustible = row.getCell(9);
                Cell cliente = row.getCell(10);

                VehiculoBean vehiculo = new VehiculoBean();
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

                vehiculoBO.insertar(vehiculo);
                System.out.println("Vehiculo insertado: " + i);

                if(marca.getStringCellValue().equals("OPEL") && modelo.getStringCellValue().equals("CORSA")){
                    break;
                }
            }catch (Exception e){
                System.out.println("Error en la fila " + i + " " + e.getMessage());
            }
        }
    }

    private static String getValorCelda(Row row, int column) {
        String valorCelda = null;
        if(row.getCell(column)!=null&&row.getCell(column).getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
            double valorNumerico = row.getCell(column).getNumericCellValue();
            valorCelda = String.valueOf((int) valorNumerico);
        }else {
            valorCelda = row.getCell(column)!=null?row.getCell(column).getStringCellValue():"";
        }
        return valorCelda;
    }
}
