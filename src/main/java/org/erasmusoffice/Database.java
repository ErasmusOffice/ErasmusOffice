package org.erasmusoffice;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class Database {
    public final static int MAX_DAYS_TO_RETURN = 20;

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/erasmus_db";
    private static final String dbAdmin = "manager";
    private static final String dbPassword = "0000";


    public static void main(String[] args) {
        //        importSqlQuery("src/main/resources/database_files/erasmus_create_tables.sql");
        //        importSqlQuery("src/main/resources/database_files/erasmus_fill_tables.sql");
        testDb();

        System.out.println("-Database.java main terminated succesfully-");
    }

    /**
     * Reads sql query lines from a file and builds a sql string containing them.
     *
     * @param queryPath absolute or relative path of the sql text file
     * @return a ready to execute sql string
     */
    public static String getSqlQuery(String queryPath) {
        File file = new File(queryPath);
        StringBuilder builder = new StringBuilder();

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNextLine()) {
                builder.append(scan.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return builder.toString();
    }

    /**
     * Establishes connection with the database.
     */
    public static Connection connectToDatabase(String user, String password) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbUrl, user, password);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return conn;
    }

    /**
     * Runs DDL queries from a file.
     * Creates all of the needed database tables for the application.
     * No operation if the tables already exist.
     */
    public static void importSqlQuery(String queryPath) {
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); Statement stmt = conn.createStatement()) {
            String sql = getSqlQuery(queryPath);

            for (String s : sql.split(";")) {
                if (!s.equals("\n")) {
                    stmt.execute(s + ";");
                }
            }
        } catch (SQLException e) {
            System.out.println("error: Could not create the tables.");
            e.printStackTrace();
        }
    }

    /**
     * Establishes connection with the database and tries to get metadata about tables.
     * This method can be used to test database connection.
     */
    public static void testDb() {
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); Statement stmt = conn.createStatement()) {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            ResultSet rs = dbMetaData.getTables(null, null, null, new String[] {"TABLE"});

            System.out.println("Available tables:");
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String remarks = rs.getString("REMARKS");

                System.out.println("TABLE_NAME: " + tableName + " REMARKS: " + remarks);
            }
            System.out.println("-Database connection established-");

        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
        }
    }


    public static Student getStudent(int stdId, String password) throws SQLException{
        String sql = "SELECT std_id, exam_result, gpa, department, consultant_id, fname, lname FROM students" +
                " WHERE std_id = ? and password = ?";
        Student student = new Student();

        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stdId );
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                student.setStdID(stdId);
                student.setExamResult(rs.getInt("exam_result"));
                student.setGPA(rs.getDouble("gpa"));
                student.setDepartment(rs.getString("department"));
                student.setConsultantID(rs.getInt("consultant_id"));
                student.setFname(rs.getString("fname"));
                student.setLname(rs.getString("lname"));
                return student;
            } else {
                return null;
            }
        }
    }

    /**
     * Checks for the record with the given credentials in the login_infos tables.
     *
     * @return role of the person if found,
     *         otherwise null
     */
    public static String checkLoginInfo(String username, String password) throws SQLException{
        String sql = "SELECT role FROM login_infos" +
                " WHERE username = ? and password = ?";
        String role;

        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username );
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                role = rs.getString("role");
                return role;
            } else {
                return null;
            }
        }
    }

}
