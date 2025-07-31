package project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Patient {
    private String name;
    private int age;
    private String gender;
    private String medicalHistory;
    private String doctor;
    private int no_visits;


    public Patient(String name, int age, String gender, String medicalHistory, String doctor, int no_visits) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
        this.doctor=doctor;
        this.no_visits=no_visits;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }
    public String getDoctor() {
           return doctor;
           }
    public int getNo_Visits() {
        return no_visits;
        }
       @Override
    public String toString() {
        return "Name: " + name + "\t Age: " + age + "\t Gender: " + gender + "\t Medical History: " + medicalHistory +"\t Doctor: "+ doctor +"\t Visited :" +no_visits;
    }
}

class HealthManagementSystem{
    private List<Patient> patients;
    private Connection connection;

    public HealthManagementSystem() {
        this.patients = new ArrayList<>();
        try {
            // Replace the following with your database connection details
            String url = "jdbc:mysql://localhost:3306/jdbc";
            String username = "root";
            String password = "Sathvik@k1";

            // Load the JDBC driver and establish a connection
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost/jdbc","root","Sathvik@k1");

            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void addPatient(Patient patient) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO patients (name, age, gender, medical_history, doctor, no_visits) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getName());
            statement.setInt(2, patient.getAge());
            statement.setString(3, patient.getGender());
            statement.setString(4, patient.getMedicalHistory());
            statement.setString(5, patient.getDoctor());
            statement.setInt(6, patient.getNo_Visits());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        System.out.println("Patient added successfully with ID: " + generatedId);
                    }
                }
            } else {
                System.out.println("Failed to add patient.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM patients")) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String medicalHistory = resultSet.getString("medical_history");
                String doctor  = resultSet.getString("doctor");
                int no_visits = resultSet.getInt("no_visits");

                Patient patient = new Patient(name, age, gender, medicalHistory, doctor, no_visits);
                patients.add(patient);

                System.out.println(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HealthManagementSystem healthManagementSystem = new HealthManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nHealth Management System Menu:");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter patient name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter patient age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();  // Consume the newline character
                    System.out.print("Enter patient gender: ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter patient medical history: ");
                    String medicalHistory = scanner.nextLine();
                    System.out.print("Enter consultant Doctor: ");
                    String doctor = scanner.nextLine();
                    System.out.print("Enter visit count: ");
                    int no_visits = scanner.nextInt();
                    Patient newPatient = new Patient(name, age, gender, medicalHistory, doctor, no_visits);
                    healthManagementSystem.addPatient(newPatient);
                    break;

                case 2:
                    healthManagementSystem.viewPatients();
                    break;

                case 3:
                    System.out.println("Exiting Health Management System. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
