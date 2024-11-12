package com.taller.conexion;

import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.Server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conexion {

	private static Server server;
	private static Connection connection;
	
	public static void crearConexion() {
		try {
			if(server == null || server.isNotRunning()) {
				File file = new File("DatoJava/HSQLDB/miBaseDeDatos");
				System.out.println("Ruta base de datos: " + file.getAbsolutePath());
				
				HsqlProperties hsqlProperties = new HsqlProperties();
				hsqlProperties.setProperty("server.database.0","file:DatoJava/HSQLDB/miBaseDeDatos");
				hsqlProperties.setProperty("server.dbname.0", "mdb");
				hsqlProperties.setProperty("server.port", "9137");
				 
				server = new Server();
				server.setProperties(hsqlProperties);
				server.setTrace(false);
				server.start();
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void cerrarConexion() {
		try {
			obtenerConexion();
			Statement statement = connection.createStatement();
			statement.executeQuery("SHUTDOWN COMPACT");
            statement.close();
            if (connection != null) {
				connection.close();
		    }
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Connection obtenerConexion() {
		try {
			crearConexion();
			if(connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost:9137/mdb", "SA", "");
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return connection;
	}
}
