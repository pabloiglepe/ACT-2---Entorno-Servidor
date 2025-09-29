package Laboral;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CalculaNominas {

	public static void main(String[] args) throws SQLException {
		/*
		 * // PARTE 1 Empleado james = new Empleado('M', "32000032G", "James Cosling",
		 * 7, 4); Empleado ada = new Empleado('F', "32000031R", "Ada Lovelace");
		 * 
		 * escribe(james, ada);
		 * 
		 * ada.incrAnyo(); james.setCategoria(9);
		 * 
		 * escribe(james, ada);
		 */

		Scanner sc = new Scanner(System.in);
		int opcion;

		// PARTE 2
		// LECTURA .TXT
		String archivo = "empleados.txt";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
			bw.write("James Cosling, M, 32000032G, 7, 4");
			bw.newLine();
			bw.write("Ada Lovelace, F, 32000031R, 0, 1");

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Archivo generado correctamente");

		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				System.out.println(linea);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// CONEXION BBDD
		String url = "jdbc:mysql://localhost:3306/empleadosnominas";
		String usuario = "root";
		String passwd = "123456";

		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(url, usuario, passwd);
			st = conn.createStatement();

			String query = "SELECT * FROM empleados";

			rs = st.executeQuery(query);

			System.out.println("Resultados: ");
			while (rs.next()) {
				String nombre = rs.getString("nombre");
				String sexo = rs.getString("sexo");
				String dni = rs.getString("dni");
				int anyosTrabajados = rs.getInt("anyosTrabajados");
				int categoria = rs.getInt("categoria");
				System.out.println("Nombre: " + nombre + ", sexo: " + sexo + ", dni: " + dni + ", anyosTrabajados: "
						+ anyosTrabajados + ", categoria: " + categoria);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		altaEmpleado(conn);

		do {

			System.out.println("MENÚ");
			System.out.println("0 - SALIR");
			System.out.println("1 - Mostrar información existente");
			System.out.println("2 - Mostrar salario existente");
			System.out.println("3 - Modifica os datos de los empleados de la BBDD");
			System.out.println("4 - Recalcula el sueldo de un empleado");
			System.out.println("5 - Recalcula el sueldo de todos los empleado");
			System.out.println("6 - Realiza una copia de seguridad de los ficheros");

			System.out.print("Elige una opción: ");
			opcion = sc.nextInt();

			// Estructura switch
			switch (opcion) {
			case 1:
				getAllEmpleados(conn);

				break;
			case 2:
				System.out.print("Dame un DNI: ");
				String dni = sc.nextLine().trim();
				mostrarSueldoDni(conn, dni);

				break;

			case 3:

				break;

			case 4:

				break;

			case 5:

				break;

			case 6:
				break;

			default:
				break;
			}

		} while (opcion != 0);
	}

	private static void escribe(Empleado e1, Empleado e2) {
		e1.Imprime();
		System.out.println("El sueldo de James es: " + Nomina.sueldo(e1));

		e2.Imprime();
		System.out.println("El sueldo de Ada es: " + Nomina.sueldo(e2));
	}

	private static void altaEmpleado(Connection conn) throws SQLException {

		String lectura = "empleadosNuevos.txt";
		String delimiter = ",";
		String query = "INSERT INTO empleados (nombre, sexo, dni, anyosTrabajados, categoria) VALUES (?,?,?,?,?)";

		try (BufferedReader br = new BufferedReader(new FileReader(lectura));
				PreparedStatement pst = conn.prepareStatement(query)) {

			String linea;

			while ((linea = br.readLine()) != null) {
				String partes[] = linea.split(delimiter);

				if (partes.length == 5) {
					String nombre = partes[0].trim();
					String sexo = partes[1].trim();
					String dni = partes[2].trim();
					int anyosTrabajados = Integer.parseInt(partes[3].trim());
					int categoria = Integer.parseInt(partes[4].trim());

					pst.setString(1, nombre);
					pst.setString(2, sexo);
					pst.setString(3, dni);
					pst.setInt(4, anyosTrabajados);
					pst.setInt(5, categoria);
				}
			}

			pst.executeQuery();
			System.out.println("Registros insertados!");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// MOSTRAR LOS EMPLEADOS
	private static List<Empleado> getAllEmpleados(Connection conn) throws SQLException {
		List<Empleado> lista = new ArrayList<>();
		String sql = "SELECT nombre, dni, sexo, categoria, anyos FROM Empleados";

		try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
			while (rs.next()) {
				String nombre = rs.getString("nombre");
				String dni = rs.getString("dni");
				char sexo = rs.getString("sexo").charAt(0);
				int categoria = rs.getInt("categoria");
				int anyosTrabajados = rs.getInt("anyosTrabajados");
				lista.add(new Empleado(sexo, dni, nombre, anyosTrabajados, categoria));
			}
		}
		return lista;
	}

	// MOSTRAR EL SUELDO POR DNI
	private static int mostrarSueldoDni(Connection conn, String dni) throws SQLException {
		String sql = "SELECT sueldo FROM Nominas WHERE dni = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, dni);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("sueldo");
				} else {
					return -1;
				}
			}

		}
	}

}
