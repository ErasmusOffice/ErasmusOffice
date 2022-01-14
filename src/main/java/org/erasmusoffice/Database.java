package org.erasmusoffice;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.*;
import javafx.scene.control.ButtonType;

import org.apache.ibatis.jdbc.ScriptRunner;

public class Database {
    public final static int MAX_DAYS_TO_RETURN = 20;

    private static final String dbUrl = "jdbc:postgresql://localhost:5432/erasmus_db";
    public static String dbAdmin = "manager";
    public static String dbPassword = "0000";


    public static void main(String[] args) throws SQLException {
        File file = new File("src/main/resources/database_files/erasmus_create_tables.sql");
        importSqlQuery(file);
        file = new File("src/main/resources/database_files/erasmus_fill_tables.sql");
        importSqlQuery(file);
        file = new File("src/main/resources/database_files/erasmus_functions.sql");
        importSqlQuery(file);
        file = new File("src/main/resources/database_files/trigger.sql");
        importSqlQuery(file);
        file = new File("src/main/resources/database_files/privileges.sql");
        importSqlQuery(file);
        testDb();
        System.out.println("-Database.java main terminated successfully-");
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
    public static void importSqlQuery(File file) throws SQLException {
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword)) {
            ScriptRunner runner = new ScriptRunner(conn);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            runner.setSendFullScript(true);
            runner.runScript(reader);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getUniversities() {
        String query = "SELECT DISTINCT name FROM universities";
        ArrayList<String> names = new ArrayList<String>();
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); Statement stmt = conn.createStatement()) {
            ResultSet r = stmt.executeQuery(query);
            while (r.next()) {
                names.add(r.getString("name"));
            }


        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
        }
        return names;
    }


    public static void application() {
        String query = "Insert into applications Values(?, ?, ?, ?,?,?,?)";
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); PreparedStatement pstmt =
                conn.prepareStatement(query)) {
            //    pstmt.setString(1,);
            //   pstmt.setString(2,student);
            //  pstmt.setString(3,);
            // pstmt.setString(3,);

        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
        }
    }

    public static int getPriority(int id) {
        String query = "Select count(*) from applications where std_id  = ?";
        int priority = 0;
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); PreparedStatement pstmt =
                conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet r = pstmt.executeQuery();
            if (r.next()) {
                priority = r.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
        }
        return priority;
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


    /**
     * Searches student with the given login info in the students table.
     *
     * @return object of found student if exists,
     * null object otherwise
     */
    public static Student getStudent(int stdId, String password) throws SQLException {
        String sql = "SELECT std_id, exam_result, gpa, department, consultant_id, fname, lname FROM students" +
                " WHERE std_id = ? and password = ?";
        Student student = new Student();

        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stdId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
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
     * null string otherwise
     */
    public static String checkLoginInfo(String username, String password) throws SQLException {
        String sql = "SELECT role FROM login_infos" +
                " WHERE username = ? and password = ?";
        String role;

        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
                return role;
            } else {
                return null;
            }
        }
    }

    /**
     * Returns all of given student's applications.
     *
     * @return ArrayList containing ApplicationModels of the found applications
     */
    public static ArrayList<ApplicationModel> getApplicationsOfStudent(int student_id) {
        String sql = "select * from manager_get_applications(?)";
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); PreparedStatement pstmt =
                conn.prepareStatement(sql)) {
            ArrayList<ApplicationModel> applications = new ArrayList<>();

            pstmt.setInt(1, student_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ApplicationModel application = new ApplicationModel();
                application.setAppID(rs.getInt(1));
                application.setStudentID(rs.getInt(2));
                application.setUniversityName(rs.getString(3));
                application.setTerm(rs.getString(4));
                application.setResult(rs.getBoolean(5));
                applications.add(application);
                System.out.println("appId " + application.getAppID());
            }
            return applications;
        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
            return null;
        }
    }



    public static ArrayList<UniversityModel> getUniversitiesInfo() {
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); Statement stmt = conn.createStatement()) {
            String sql = "SELECT country, name, fall_applicant_count, spring_applicant_count FROM universities";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<UniversityModel> universities = new ArrayList<>();
            while (rs.next()) {
                UniversityModel university = new UniversityModel();
                university.setCountry(rs.getString("country"));
                university.setFallQuota(rs.getInt("fall_applicant_count"));
                university.setSpringQuota(rs.getInt("spring_applicant_count"));
                university.setName(rs.getString("name"));
                universities.add(university);
            }
            return universities;

        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
            return null;
        }

    }

    public static ArrayList<ApplicationModel> getApplicationsInfo() {
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); Statement stmt = conn.createStatement()) {
            String sql = "SELECT application_id, std_id, name, term, result FROM applications a INNER JOIN " +
                    "universities  u ON u.uni_id = a.target_uni_id";
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList<ApplicationModel> applications = new ArrayList<>();
            while (rs.next()) {
                ApplicationModel application = new ApplicationModel();
                application.setAppID(rs.getInt("application_id"));
                application.setStudentID(rs.getInt("std_id"));
                application.setUniversityName(rs.getString("name"));
                application.setTerm(rs.getString("term"));
                application.setResult(rs.getBoolean("result"));
                applications.add(application);
            }
            return applications;

        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
            return null;
        }

    }

    public static void deleteApplication(int applicationID) {
        String sql = "DELETE FROM applications WHERE application_id = ?";
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); PreparedStatement pstmt =
                conn.prepareStatement(sql)) {
            pstmt.setInt(1, applicationID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error: Could not run the query.");
            e.printStackTrace();
        }
    }

    public static ArrayList<Integer> placeStudents() {
        String sql = "SELECT * FROM place_students()";
        ArrayList<Integer> app_ids = new ArrayList<>();
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            //            System.out.println(rs.getArray(rs.getRow()));
            //            System.out.println(rs.getArray(rs.getRow()));
            while (rs.next()) {
                app_ids.add(rs.getInt("app_id"));
            }
            return app_ids;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ApprovedApplication> showApprovedApplications(ArrayList<Integer> app_ids) {
        String sql = "SELECT applications.std_id, fname, country, name, term FROM applications INNER JOIN students ON" +
                " " +
                "applications.std_id = students.std_id INNER JOIN universities u on applications.target_uni_id = u" +
                ".uni_id WHERE application_id = ?";
        ArrayList<ApprovedApplication> approvedApplications = new ArrayList<>();
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); PreparedStatement pstmt =
                conn.prepareStatement(sql)) {
            for (Integer app_id : app_ids) {
                pstmt.setInt(1, app_id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    ApprovedApplication newApp = new ApprovedApplication();
                    newApp.setStd_id(rs.getInt("std_id"));
                    newApp.setCountry(rs.getString("country"));
                    newApp.setTerm(rs.getString("term"));
                    newApp.setStd_name(rs.getString("fname"));
                    newApp.setUni_name(rs.getString("name"));
                    approvedApplications.add(newApp);
                }
            }

            return approvedApplications;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static Integer getUniversityId(String uni_name) {
        String sql = "SELECT uni_id FROM universities WHERE name = ? ";
        int uni_id;
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uni_name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                uni_id = rs.getInt("uni_id");
                return uni_id;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer getNextPriority(int std_id) {
        String sql = "SELECT max(priority) as priority FROM applications WHERE std_id = ?";
        Integer next_priority;
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, std_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                next_priority = rs.getInt("priority");
                return next_priority + 1;
            }
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertApplication(ApplicationModel application) {
        String sql = "INSERT INTO applications(std_id, target_uni_id, priority, fund, term) VALUES (?, ?, ?, " +
                "?, ?)";
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, application.getStudentID());
            pstmt.setInt(2, application.getUniversityId());
            pstmt.setInt(3, application.getPriority());
            pstmt.setFloat(4, application.getFund());
            pstmt.setString(5, application.getTerm());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert;
            if (e.getSQLState().equals("23505")) {
                alert = new Alert(Alert.AlertType.ERROR, "You've already registered to this university for this term",
                        ButtonType.OK);
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "You reached the maximum number of applications.\nPlease " +
                        "delete to be able to add new applications.",
                        ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public static Student getStudentInfo(int std_id) {
        String sql = "SELECT * FROM students WHERE std_id = ?";
        Student student = new Student();
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, std_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                student.setStdID(rs.getInt("std_id"));
                student.setFname(rs.getString("fname"));
                student.setLname(rs.getString("lname"));
                student.setExamResult(rs.getInt("exam_result"));
                student.setGPA(rs.getDouble("GPA"));
                return student;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not found student.",
                    ButtonType.OK);
            return null;
        }
    }

    public static void updateApplication(int applicationID, String term, String universityName) {
            String sql = "UPDATE applications SET term = ?, target_uni_id = ? WHERE application_id = ?";
        try (Connection conn = connectToDatabase(dbAdmin, dbPassword); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, term);
            pstmt.setInt(2, Database.getUniversityId(universityName));
            pstmt.setInt(3, applicationID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "The application already exist",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }
}
