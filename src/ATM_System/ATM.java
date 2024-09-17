package ATM_System;

import java.sql.*;
import java.util.Scanner;


class Details {

    private final Connection connection;
    private final Scanner scanner;

    public Details(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }



    public void MainDisplay() {
        while (true) {
            System.out.println();
            System.out.println("1. Withdraw Balance.");
            System.out.println("2. Deposit Balance.");
            System.out.println("3. Check Balance.");
            System.out.println("0. Exit");
            System.out.println();
            System.out.print("Enter your choice : ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    withdraw(connection, scanner);
                    break;
                case 2:
                    deposit(connection, scanner);
                    break;
                case 3:
                    checkBalance(connection);
                    break;
                case 0:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice...");
            }
        }
    }


    // Fetch the total balance from the database
    public static double Balance(Connection connection) {
        double balance = 0;
        String query = "select balance from user_one";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                balance = resultSet.getDouble("balance");
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return balance;
    }

    // Money withdraw method....................................
    public static void withdraw(Connection connection, Scanner scanner) {
        System.out.print("Enter the withdrawn amount : ");
        double amountWithdraw = scanner.nextDouble();

        String updateQuery = "update user_one set balance = balance - ?";

        try {
            if (Balance(connection) >= amountWithdraw) {
                PreparedStatement preparedStatement1 = connection.prepareStatement(updateQuery);
                preparedStatement1.setDouble(1, amountWithdraw);
                int affective = preparedStatement1.executeUpdate();
                if (affective > 0) {
                    System.out.println("Amount withdraw successfully...");
                }else {
                    System.out.println("Failed to withdraw... ");
                }
            }
            else {
                System.out.println("Insufficient balance...");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Money withdraw method .............................
    public static void deposit(Connection connection, Scanner scanner) {

        String updateQuery = "update user_one set balance = balance + ?";
        System.out.print("Enter the amounts for the Deposit : ");
        double amountDeposit = scanner.nextDouble();

        try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(updateQuery);
                preparedStatement1.setDouble(1, amountDeposit);
                int affective = preparedStatement1.executeUpdate();
                if (affective > 0) {
                    System.out.println("Balance Deposit Successfully...");
                }
                else {
                    System.out.println("Failed to deposit...");
                }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void checkBalance(Connection connection) {
        String query = "select balance from user_one";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                double totalAmount = resultSet.getDouble("balance");
                System.out.println("Total Balance is : " + totalAmount);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}




public class ATM {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/atm_system";   //set your port number and db_name
        String username = "root"; // Set your username of db
        String password = "";  //Enter your password here

        Scanner scanner = new Scanner(System.in);
        String query = "select atm_pin from user_one";

        Connection connection = null;

        int pin = 0;
        try {
            connection = DriverManager.getConnection(url, username, password);

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pin = resultSet.getInt("atm_pin");

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        while (true){
            System.out.print("Enter pin : ");
            int enteredPin = scanner.nextInt();

            if (pin == enteredPin){

                System.out.println();
                System.out.println("Welcome to ATM Service.");

                Details d = new Details(connection, scanner);
                d.MainDisplay();
                break;
            }
            else {
                System.out.println("Wrong pin number.");
            }
        }
    }
}