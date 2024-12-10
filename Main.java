import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Customer> custData = new ArrayList<>();
        Admin admin = new Admin("admin123", "password123"); // Create a default admin
        Stat stats = new Stat(); // Create an instance of Stat

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int role;
            try {
                role = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (role) {
                case 1:
                    adminMenu(scanner, custData, admin, stats); // Pass stats to admin menu
                    break;
                case 2:
                    customerMenu(scanner, custData, stats); // Pass stats to customer menu
                    break;
                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    stats.printStats(); // Print statistics when the program exits
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Admin Menu
    private static void adminMenu(Scanner scanner, ArrayList<Customer> custData, Admin admin, Stat stats) {
        System.out.println("Enter Admin ID:");
        String id = scanner.nextLine();
        System.out.println("Enter Admin Password:");
        String password = scanner.nextLine();

        if (!admin.authenticate(id, password)) {
            System.out.println("Invalid credentials. Access denied.");
            return;
        }

        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Create Customer");
            System.out.println("2. Create Customer with Default Balance");
            System.out.println("3. View All Customers");
            System.out.println("4. Delete Customer");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    admin.createCustomer(scanner, custData);
                    stats.incrementCustomerCreated(); // Track customer creation
                    break;
                case 2:
                    admin.createCustomer(scanner, custData, 0.0); // Default balance
                    stats.incrementCustomerCreated(); // Track customer creation
                    break;
                case 3:
                    admin.viewAllCustomers(custData);
                    break;
                case 4:
                    admin.deleteCustomer(scanner, custData);
                    stats.incrementCustomerDeleted(); // Track customer deletion
                    break;
                case 5:
                    System.out.println("Exiting Admin menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Customer Menu
    private static void customerMenu(Scanner scanner, ArrayList<Customer> custData, Stat stats) {
        System.out.println("Enter your ID:");
        String id = scanner.nextLine();

        Customer currentCustomer = findCustomerById(custData, id);
        if (currentCustomer == null) {
            System.out.println("Customer not found. Please contact admin to create your account.");
            return;
        }

        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (currentCustomer.deposit(depositAmount)) {
                        stats.incrementDeposit(); // Track deposit
                    }
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (currentCustomer.withdraw(withdrawAmount)) {
                        stats.incrementWithdrawal(); // Track withdrawal
                    }
                    break;
                case 3:
                    System.out.println("Current Balance: " + currentCustomer.checkBalance());
                    break;
                case 4:
                    System.out.println("Exiting Customer menu. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Find a customer by ID
    private static Customer findCustomerById(ArrayList<Customer> custData, String id) {
        for (Customer customer : custData) {
            if (customer != null && customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }
}

class Stat {
    private int customersCreated = 0;
    private int customersDeleted = 0;
    private int deposits = 0;
    private int withdrawals = 0;

    // Methods to increment the statistics
    public void incrementCustomerCreated() {
        customersCreated++;
    }

    public void incrementCustomerDeleted() {
        customersDeleted++;
    }

    public void incrementDeposit() {
        deposits++;
    }

    public void incrementWithdrawal() {
        withdrawals++;
    }

    // Method to print the statistics
    public void printStats() {
        System.out.println("\nProgram Statistics:");
        System.out.println("Customers Created: " + customersCreated);
        System.out.println("Customers Deleted: " + customersDeleted);
        System.out.println("Deposits Made: " + deposits);
        System.out.println("Withdrawals Made: " + withdrawals);
    }
}

// Abstract Parent Class
abstract class User {
    private String username;
    private String id;
    private String password;

    public User(String username, String id, String password) {
        this.username = username;
        this.id = id;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public boolean authenticate(String id, String password) {
        return this.id.equals(id) && this.password.equals(password);
    }
}

// Admin Class
class Admin extends User {
    public Admin(String id, String password) {
        super("Admin", id, password); // Admin username is fixed
    }

    public void createCustomer(Scanner scanner, ArrayList<Customer> custData) {
        Customer newCustomer = Customer.createCustomer(scanner);
        if (newCustomer != null) {
            custData.add(newCustomer);
            System.out.println("Customer added successfully!");
        } else {
            System.out.println("Customer creation failed.");
        }
    }

    public void createCustomer(Scanner scanner, ArrayList<Customer> custData, double defaultBalance) {
        Customer newCustomer = Customer.createCustomer(scanner, defaultBalance);
        if (newCustomer != null) {
            custData.add(newCustomer);
            System.out.println("Customer added successfully with default balance!");
        } else {
            System.out.println("Customer creation failed.");
        }
    }

    public void viewAllCustomers(ArrayList<Customer> custData) {
        if (custData.isEmpty()) {
            System.out.println("No customers available.");
        } else {
            System.out.println("List of Customers:");
            for (Customer customer : custData) {
                if (customer != null) {
                    System.out.println("Username: " + customer.getUsername());
                    System.out.println("ID: " + customer.getId());
                    System.out.println("Balance: " + customer.checkBalance());
                    System.out.println("---------------------");
                }
            }
        }
    }

    public void deleteCustomer(Scanner scanner, ArrayList<Customer> custData) {
        System.out.println("Enter the ID of the customer to delete:");
        String id = scanner.nextLine();

        Customer customerToDelete = null;
        for (Customer customer : custData) {
            if (customer.getId().equals(id)) {
                customerToDelete = customer;
                break;
            }
        }

        if (customerToDelete != null) {
            custData.remove(customerToDelete);
            System.out.println("Customer deleted successfully!");
        } else {
            System.out.println("Customer not found.");
        }
    }
}

// Customer Class
class Customer extends User {
    private double balance;

    public Customer(String username, String id, String password, double balance) {
        super(username, id, password);
        this.balance = balance;
    }

    public static Customer createCustomer(Scanner scanner) {
        System.out.println("Creating a new Customer...");
        System.out.print("Enter your name: ");
        String username = scanner.nextLine();

        String id;
        while (true) {
            System.out.print("Enter your ID (10-digit number): ");
            id = scanner.nextLine();
            if (id.matches("\\d{10}")) break;
            System.out.println("Invalid ID. Must be a 10-digit number.");
        }
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (age < 18) {
            System.out.println("Sorry, you must be at least 18 years old to create an account.");
            return null; // Return null if age is less than 18
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your initial balance: ");
        double balance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        return new Customer(username, id, password, balance);
    }

    // Overloaded method with default balance
    public static Customer createCustomer(Scanner scanner, double defaultBalance) {
        System.out.println("Creating a new Customer with default balance...");
        System.out.print("Enter your name: ");
        String username = scanner.nextLine();

        String id;
        while (true) {
            System.out.print("Enter your ID (10-digit number): ");
            id = scanner.nextLine();
            if (id.matches("\\d{10}")) break;
            System.out.println("Invalid ID. Must be a 10-digit number.");
        }
        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (age < 18) {
            System.out.println("Sorry, you must be at least 18 years old to create an account.");
            return null; // Return null if age is less than 18
        }
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        return new Customer(username, id, password, defaultBalance);
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. New balance: " + balance);
            return true;
        } else {
            System.out.println("Invalid deposit amount.");
            return false;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
            return true;
        } else if (amount > balance) {
            System.out.println("Insufficient balance. Withdrawal failed.");
            return false;
        } else {
            System.out.println("Invalid withdrawal amount.");
            return false;
        }
    }

    public double checkBalance() {
        return balance;
    }
}