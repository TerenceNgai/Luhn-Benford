import java.io.*;
import java.util.*;

public class CustomerSystem {
    private static final String POSTAL_CODES_FILE = "postal_codes.csv";

    private List<Customer> customers;
    private int customerIdCounter;

    public CustomerSystem() {
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

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("Customer and Sales System");
            System.out.println("1. Enter Customer Information");
            System.out.println("2. Generate Customer data file");
            //System.out.println("3. Report on total Sales");
            //System.out.println("4. Check for fraud in sales data");
            System.out.println("9. Quit");
            System.out.print("Enter menu option (1-9): ");

            option = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (option) {
                case 1:
                    enterCustomerInformation(scanner);
                    break;
                case 2:
                    generateCustomerDataFile(scanner);
                    break;
                //case 3:
                    // Implement the functionality to report total sales
                    //break;
                //case 4:
                    // Implement the functionality to check for fraud in sales data
                    //break;
                case 9:
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (option != 9);
    }

    private void enterCustomerInformation(Scanner scanner) {
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

    private boolean validatePostalCode(String postalCode) {
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

    private boolean validateCreditCardNumber(String creditCardNumber) {
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

    private void generateCustomerDataFile(Scanner scanner) {
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

    // private void generateCustomerDataFile(Scanner scanner) {
    //     System.out.print("Enter output file name and location: ");
    //     String fileName = scanner.nextLine();

    //     try (PrintWriter writer = new PrintWriter(fileName)) {
    //         writer.println("ID,First Name,Last Name,City,Postal Code,Credit Card Number");

    //         for (Customer customer : customers) {
    //             writer.println(customer.toCsvString());
    //         }

    //         System.out.println("Customer data file generated successfully.");
    //     } catch (FileNotFoundException e) {
    //         System.out.println("Output file not found or cannot be created.");
    //     }
    // }

    public static void main(String[] args) {
        CustomerSystem customerSystem = new CustomerSystem();
        customerSystem.run();
    }
}

class Customer {
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
