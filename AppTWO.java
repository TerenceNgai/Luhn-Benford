//import java.io.*;
import java.util.*;


import java.io.*; 

import org.jfree.chart.ChartFactory; 
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import javafx.scene.chart.PieChart.Data;

import org.jfree.chart.ChartUtilities; 

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
// org.knowm.xchart.*;
import javax.swing.Renderer;




public class AppTWO {

    public static void main(String[] args) throws IOException {
        //AppTWO customerSystem = new AppTWO();
        //customerSystem.run();
        
        Scanner sc = new Scanner(System.in);

        Scanner scanner = new Scanner(System.in);
        //int option;

        while (true) {
            printMenu();
            int option = sc.nextInt();
            // Option 1: Load sales data from user input
            if (option == 1) {
                Object[] result = loadSales();
                if (result != null) {
                    BufferedReader reader = (BufferedReader) result[0];
                    benfordsLaw(reader);
                    reader.close();
                }
            // Option 2: Check for fraud in sales data and export frequency
            } else if (option == 2) {
                System.out.println("Check for fraud in sales data and export frequency.");
                try {
                    benfordsLaw(new BufferedReader((new FileReader("sales.csv"))));
                } catch (NullPointerException e) {
                    System.out.println("Sales file not loaded. Please load data first.");
                }
            }
            // Option 3:
            else if (option == 3){
                enterCustomerInformation(scanner, customers);
                //System.out.println("Option 3 has run");
            }
            // Option 4:
            else if (option == 4){
                generateCustomerDataFile(scanner);
                //System.out.println("Option 4 has run");
            }
            // Option 9: Exit the program
            else if (option == 9) {
                System.out.println("Exiting...");
                sc.close();
                System.exit(0);
            // Invalid option: Ask the user to enter a valid option
            } else {
                System.out.println("Invalid input. Please enter a valid option.");
            }
            // switch (option) {
                //     case 3:
                //         enterCustomerInformation(scanner);
                //         break;
                //     case 4:
                //         generateCustomerDataFile(scanner);
                //         break;
                //     // case 9:
                //     //     System.out.println("Exiting the program...");
                //     //     break;
                //     default:
                //         System.out.println("Invalid option. Please try again.");
                //         break;
                // }
        }
    }

    public static void printMenu() {
        // Print the menu options for the user
        System.out.println("\nSales System\n\n1. Load sales data\n2. Check for fraud in sales data and export frequency\n3. Enter customer information\n4. Generate customer data file\n9. Quit\nEnter menu option (1-9)");
    }

    public static Object[] loadSales() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Your csv file must be located in this folder and must be named sales.csv. If you want to continue, type in y, otherwise type in anything else to exit:");
        String loadSalesConfirmation = sc.next();
        if (loadSalesConfirmation.equals("y")) {
            BufferedReader reader;
            try {
                 // Attempt to open the "sales.csv" file for reading
                reader = new BufferedReader(new FileReader("sales.csv"));
            } catch (IOException e) {
                System.out.println("Your csv file doesn't exist or it is not named correctly.");
                return null;
            }
            // Data loaded successfully
            System.out.println("Data loaded successfully.");
            return new Object[] { reader };
        } else {
            return null;
        }
    }

    public static void benfordsLaw(BufferedReader reader) throws IOException {
        // Step 1: Record all transactions from the provided reader
        List<String> allTransactions = recordTransactions(reader);
        // Step 2: Calculate the percentage of first digits in the transactions
        List<Double> firstDigitPercentages = calculatePercentages(allTransactions);
        // Step 3: Create a new thread to perform fraud detection
        Thread t = new Thread() {
            public void run() {
        // Check if the percentage of the first digit falls within the expected range
                if (firstDigitPercentages.get(0) >= 29 && firstDigitPercentages.get(0) <= 32) {
                    System.out.println("Fraud likely did not occur.");
                } else {
                    System.out.println("It is likely fraud occurred.");
                }
            }
        };
        t.start();
                generateGraph(firstDigitPercentages);
         // Step 5: Export the calculated data
        exportData(firstDigitPercentages);
    }

    public static List<String> recordTransactions(BufferedReader reader) throws IOException {
        List<String> transactions = new ArrayList<String>();
        String line;
        // Skip the header line
        reader.readLine();
        // Read each line of the reader until the end
        while ((line = reader.readLine()) != null) {
            // Split the line into fields using comma as the delimiter
            String[] fields = line.split(",");
            // Add the second field (index 1) to the list of transactions
            transactions.add(fields[1]);
        }
        return transactions;
    }

    public static List<Double> calculatePercentages(List<String> transactions) {
        List<Double> firstDigitPercentages = new ArrayList<>();
        // Iterate through digits 1 to 9
        for (int i = 1; i <= 9; i++) {
            String digit = String.valueOf(i);
            int firstDigitNum = 0;
             // Count the number of transactions with the current first digit
            for (String transaction : transactions) {
                if (transaction.startsWith(digit)) {
                    firstDigitNum++;
                }
            }
            // Calculate the percentage of transactions with the current first digit
            double average = (firstDigitNum / (double)transactions.size()) * 100.0;
             // Round the average to one decimal place
            average = Math.round(average * 10.0) / 10.0;
            // Add the calculated average to the list of percentages
            firstDigitPercentages.add(average);
        }
        // Return the list of first digit percentages
        return firstDigitPercentages;
    }
    
    
    public static void generateGraph(List<Double> firstDigitPercentages) throws IOException {
        // Create Bar Chart using the percentages
        CategoryDataset dataset = createDataset(firstDigitPercentages);

        // Creating the Bar Graph
        JFreeChart chart = ChartFactory.createBarChart(
                "First Digit Frequency",
                "Digits",
                "Frequency %",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Changing chart setup
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(
                new ItemLabelPosition(
                        ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT
                )
        );

        // Generate the File
        int width = 800;
        int height = 800;
        File outputFile = new File("bar_chart.jpg");

        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
            System.out.println("Bar chart created.");
    } catch (IOException e) {
        System.err.println("Error saving the bar chart: " + e.getMessage());
        }
    }

    private static CategoryDataset createDataset(List<Double> firstDigitPercentages) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 1; i <= 9; i++) {
            dataset.addValue(firstDigitPercentages.get(i - 1), "Percentage", String.valueOf(i));
        }
        return dataset;
    }

    public static boolean exportData(List<Double> data) {
        File file = new File("results.csv");
        try {
            FileWriter writer = new FileWriter(file);
            // Write the header line
            writer.write("First Digit,Frequency\n");
            // Write the data for each digit
            for (int i = 0; i < data.size(); i++) {
                // Format the digit and percentage as a CSV row
                writer.write((i + 1) + "," + data.get(i) + "%\n");
            }
             // Close the writer
            writer.close();
            // Pause for 1 second
            Thread.sleep(1000);
            // Print success message
            System.out.println("Data successfully exported. Results.csv generated.");
            // Pause for 1 second
            Thread.sleep(1000);
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    






















    private static final String POSTAL_CODES_FILE = "postal_codes.csv";

    private static List<Customer> customers = new ArrayList<>();

    private static int customerIdCounter;

    public AppTWO() {
        customers = new ArrayList<>();
        customerIdCounter = getLastCustomerIdFromCSV();
    }

    private int getLastCustomerIdFromCSV() {
        int lastCustomerId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("customer_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                int customerId = Integer.parseInt(fields[0]);
                if (customerId > lastCustomerId) {
                    lastCustomerId = customerId;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Customer data file not found. Starting with customer ID 1.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the customer data file: " + e.getMessage());
        }

        return lastCustomerId;
    }

    // Rest of the code remains the same

    // public void run() {
    //     Scanner scanner = new Scanner(System.in);
    //     int option;

    //     do {
    //         System.out.println("Customer and Sales System");
    //         System.out.println("3. Enter Customer Information");
    //         System.out.println("4. Generate Customer data file");
    //         //System.out.println("3. Report on total Sales");
    //         //System.out.println("4. Check for fraud in sales data");
    //         System.out.println("9. Quit");
    //         System.out.print("Enter menu option (1-9): ");

    //         option = scanner.nextInt();
    //         scanner.nextLine(); // consume the newline character

    //         switch (option) {
    //             case 3:
    //                 enterCustomerInformation(scanner);
    //                 break;
    //             case 4:
    //                 generateCustomerDataFile(scanner);
    //                 break;
    //             // case 9:
    //             //     System.out.println("Exiting the program...");
    //             //     break;
    //             default:
    //                 System.out.println("Invalid option. Please try again.");
    //                 break;
    //         }
    //     } while (option != 9);
    // }

    private static void enterCustomerInformation(Scanner scanner, List<Customer> customers) {
        System.out.println("Enter Customer Information:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("City: ");
        String city = scanner.nextLine();
        System.out.print("Postal Code: ");
        String postalCode = scanner.nextLine();
        System.out.print("Credit Card Number: ");
        String creditCardNumber = scanner.nextLine();

        if (validatePostalCode(postalCode) && validateCreditCardNumber(creditCardNumber)) {
            Customer customer = new Customer(customerIdCounter, firstName, lastName, city, postalCode, creditCardNumber);
            customers.add(customer);
            customerIdCounter++;
            System.out.println("Customer information entered successfully.");
        } else {
            System.out.println("Invalid postal code or credit card number. Customer information not saved.");
        }
    }

    private static boolean validatePostalCode(String postalCode) {
        try (Scanner fileScanner = new Scanner(new File(POSTAL_CODES_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] fields = line.split("\\|");
                String code = fields[0];

                if (code.length() >= 3 && postalCode.startsWith(code.substring(0, 3))) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Postal codes file not found.");
            return false;
        }

        return false;
    }

    private static boolean validateCreditCardNumber(String creditCardNumber) {
        creditCardNumber = creditCardNumber.replaceAll("\\s+", ""); // remove whitespace characters

        if (creditCardNumber.length() < 9) {
            return false;
        }

        int sum = 0;
        boolean doubleDigit = false;

        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            int digit = creditCardNumber.charAt(i) - '0';

            if (doubleDigit) {
                digit *= 2;
                digit = (digit % 10) + (digit / 10);
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        return sum % 10 == 0;
    }

    private static void generateCustomerDataFile(Scanner scanner) {
        System.out.print("Enter output file name and location: ");
        String fileName = scanner.nextLine();
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            if (writer.checkError()) {
                throw new IOException("Error occurred while writing to the file.");
            }
    
            for (Customer customer : customers) {
                writer.println(customer.toCsvString());
            }
    
            System.out.println("Customer data appended to the file successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }


    // (String[] args) {
    //     AppTWO customerSystem = new AppTWO();
    //     customerSystem.run();
    // }
    

    static class Customer {
        private int id;
        private String firstName;
        private String lastName;
        private String city;
        private String postalCode;
        private String creditCardNumber;

        public Customer(int id, String firstName, String lastName, String city, String postalCode, String creditCardNumber) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.city = city;
            this.postalCode = postalCode;
            this.creditCardNumber = creditCardNumber;
        }

        public String toCsvString() {
            return id + "," + firstName + "," + lastName + "," + city + "," + postalCode + "," + creditCardNumber;
        }

    }
}

























