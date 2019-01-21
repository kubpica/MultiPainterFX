package jdbc;

import java.sql.*;
import java.util.Properties;

public class JDBC {
	/**
	 * Metoda �aduje sterownik jdbc
	 * @return true/false
	 */
	public static boolean checkDriver(String driver) {
		// LADOWANIE STEROWNIKA
		System.out.print("Sprawdzanie sterownika:");
		try {
			Class.forName(driver).newInstance();
			return true;
		} catch (Exception e) {
			System.out.println("Blad przy ladowaniu sterownika bazy!");
			return false;
		}
	}
	/**
	 * Metoda s�u�y do nawi�zania po��czenia z baz� danych
	 * 
	 * @param adress - adres bazy danych
	 * @param dataBaseName - nazwa bazy
	 * @param userName - login do bazy
	 * @param password - has�o do bazy
	 * @return - po��czenie z baz�
	 */
	public static Connection connectToDatabase(String kindOfDatabase, String adress,
			String dataBaseName, String userName, String password) {
		System.out.print("\nLaczenie z baz� danych:");
		String baza = kindOfDatabase + adress + "/" + dataBaseName;
		// objasnienie opisu bazy:
		// jdbc: - mechanizm laczenia z baza (moze byc inny, np. odbc)
		// mysql: - rodzaj bazy
		// adress - adres serwera z baza (moze byc tez z nazwy)
		// dataBaseName - nazwa bazy
		java.sql.Connection connection = null;
		try {
			connection = DriverManager.getConnection(baza, userName, password);
		} catch (SQLException e) {
			System.out.println("Blad przy po��czeniu z baz� danych!");
			System.exit(1);
		}
		return connection;
	}
	/**
	 * Metoda s�u�y do po��czenia z MySQL bez wybierania konkretnej bazy
	 * @return referencja do uchwytu bazy danych
	 */
	public static Connection getConnection(String kindOfDatabase, String adres, int port, String userName, String password) {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", userName);
		connectionProps.put("password", password);
		try {
			conn = DriverManager.getConnection(kindOfDatabase + adres + ":" + port + "/",
					connectionProps);
		} catch (SQLException e) {
			System.out.println("B��d po��czenia z baz� danych! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(2);
		}
		System.out.println("Po��czenie z baz� danych: ... OK");
		return conn;
	}
	
	/**
	 * tworzenie obiektu Statement przesy�aj�cego zapytania do bazy connection
	 * 
	 * @param connection - po��czenie z baz�
	 * @return obiekt Statement przesy�aj�cy zapytania do bazy
	 */
	private static Statement createStatement(Connection connection) {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			System.out.println("B��d createStatement! " + e.getMessage() + ": " + e.getErrorCode());
			System.exit(3);
		}
		return null;
	}

	/**
	 * Zamykanie po��czenia z baz� danych
	 * 
	 * @param connection - po��czenie z baz�
	 * @param s - obiekt przesy�aj�cy zapytanie do bazy
	 */
	private static void closeConnection(Connection connection, Statement s) {
		System.out.print("\nZamykanie polaczenia z baz�:");
		try {
			s.close();
			connection.close();
		} catch (SQLException e) {
			System.out
					.println("Bl�d przy zamykaniu pol�czenia z baz�! " + e.getMessage() + ": " + e.getErrorCode());;
			System.exit(4);
		}
		System.out.print(" zamkni�cie OK");
	}

	/**
	 * Wykonanie kwerendy i przes�anie wynik�w do obiektu ResultSet
	 * 
	 * @param s - Statement
	 * @param sql - zapytanie
	 * @return wynik
	 */
	private static ResultSet executeQuery(Statement s, String sql) {
		try {
			return s.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}
		return null;
	}
	private static int executeUpdate(Statement s, String sql) {
		try {
			return s.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
		}
		return -1;
	}
	
	/**
	 * Wy�wietla dane uzyskane zapytaniem select
	 * @param r - wynik zapytania
	 */
	private static void printDataFromQuery(ResultSet r) {
		ResultSetMetaData rsmd;
		try {
			rsmd = r.getMetaData();
			int numcols = rsmd.getColumnCount(); // pobieranie liczby kolumn
			// wyswietlanie nazw kolumn:
			for (int i = 1; i <= numcols; i++) {
				System.out.print("\t" + rsmd.getColumnLabel(i) + "\t|");
			}
			System.out
					.print("\n____________________________________________________________________________\n");
			/**
			 * r.next() - przej�cie do kolejnego rekordu (wiersza) otrzymanych wynik�w
			 */
			// wyswietlanie kolejnych rekordow:
			while (r.next()) {
				for (int i = 1; i <= numcols; i++) {
					Object obj = r.getObject(i);
					if (obj != null)
						System.out.print("\t" + obj.toString() + "\t|");
					else
						System.out.print("\t");
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Bl�d odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
		}
	}
	/**
	 * Metoda pobiera dane na podstawie nazwy kolumny
	 */
	public static void sqlGetDataByName(ResultSet r) {
		System.out.println("Pobieranie danych z wykorzystaniem nazw kolumn");
		try {
			ResultSetMetaData rsmd = r.getMetaData();
			int numcols = rsmd.getColumnCount();
			// Tytul tabeli z etykietami kolumn zestawow wynikow
			for (int i = 1; i <= numcols; i++) {
				System.out.print(rsmd.getColumnLabel(i) + "\t|\t");
			}
			System.out
			.print("\n____________________________________________________________________________\n");
			while (r.next()) {
				int size = r.getMetaData().getColumnCount();
				for(int i = 1; i <= size; i++){
					switch(r.getMetaData().getColumnTypeName(i)){
					case "INT":
						System.out.print(r.getInt(r.getMetaData().getColumnName(i)) + "\t|\t");
						break;
					case "DATE":
						System.out.print(r.getDate(r.getMetaData().getColumnName(i)) + "\t|\t");
						break;
					case "VARCHAR":
						System.out.print(r.getString(r.getMetaData().getColumnName(i)) + "\t|\t");
						break;
					default:
						System.out.print(r.getMetaData().getColumnTypeName(i));
					}
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Bl�d odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
		}
	}

	/*public static void main(String[] args) {
		if (checkDriver("com.mysql.jdbc.Driver"))
			System.out.println(" ... OK");
		else
			System.exit(1);
		// 2 spos�b po��czenia
		Connection con = getConnection("jdbc:mysql://", "localhost", 3306, "root", "root");
		Statement st = createStatement(con);
		// pr�ba wybrania bazy
		if (executeUpdate(st, "USE nowaBaza;") == 0)
			System.out.println("Baza wybrana");
		else {
			System.out.println("Baza nie istnieje! Tworzymy baz�: ");
			if (executeUpdate(st, "create Database nowaBaza;") == 1)
				System.out.println("Baza utworzona");
			else
				System.out.println("Baza nieutworzona!");
			if (executeUpdate(st, "USE nowaBaza;") == 0)
				System.out.println("Baza wybrana");
			else
				System.out.println("Baza niewybrana!");
		}
		if (executeUpdate(st,
				"CREATE TABLE ksiazki_ ( id INT NOT NULL, tytul VARCHAR(50) NOT NULL, autor INT NOT NULL, PRIMARY KEY (id) );") == 0)
			System.out.println("Tabela utworzona");
		else
			System.out.println("Tabela nie utworzona!");
		String sql = "INSERT INTO ksiazki_ VALUES(2, 'Pan Tadeusz', 1);";
		executeUpdate(st, sql);
		sql = "Select * from ksiazki_;";
		printDataFromQuery(executeQuery(st, sql));
		closeConnection(con, st);
		java.sql.Connection connection = connectToDatabase("jdbc:mysql://", "127.0.0.1", "hotel", "root", "root");
		if (connection != null)
			System.out.print(" polaczenie OK\n");
		// WYKONYWANIE OPERACJI NA BAZIE DANYCH
		System.out.println("Pobieranie danych z bazy:");
		sql = "Select * from rezerwacje;";
		Statement s = createStatement(connection);
		ResultSet r = executeQuery(s, sql);
		// printDataFromQuery(r);
		sqlGetDataByName(r);
		closeConnection(connection, s);
	}*/
}