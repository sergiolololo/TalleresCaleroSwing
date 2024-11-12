package com.taller;

import com.taller.beans.ClienteBean;
import com.taller.conexion.Conexion;
import com.taller.negocio.impl.ClienteBOImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImportarClientes {
    private static final ClienteBOImpl clienteBO = new ClienteBOImpl();
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
        for(int i = 1; i < mySheet.getPhysicalNumberOfRows(); i++){
            try{
                Row row = mySheet.getRow(i);
                Cell celdaNombre = row.getCell(1);
                Cell celdaApellido = row.getCell(2);
                Cell celdaLocalidad = row.getCell(3);
                Cell celdaTelefono = row.getCell(4);

                ClienteBean cliente = new ClienteBean();
                cliente.setNombre(celdaNombre.getStringCellValue());
                cliente.setApellido(celdaApellido.getStringCellValue());
                cliente.setLocalidad(celdaLocalidad.getStringCellValue());
                if(celdaTelefono != null && !celdaTelefono.getStringCellValue().trim().isEmpty()){
                    cliente.setTelefono(Long.parseLong(celdaTelefono.getStringCellValue().replace(" ", "")));
                }else{
                    cliente.setTelefono(0L);
                }
                clienteBO.insertar(cliente);

                if(celdaNombre.getStringCellValue().equals("ALEX") && celdaApellido.getStringCellValue().equals("CHAPISTA")){
                    break;
                }
            }catch (Exception e){
                System.out.println("Error en la fila " + i + " " + e.getMessage());
            }
        }
    }
}
