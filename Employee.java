import java.io.*;
import java.util.*;

class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String department;
    private double baseSalary;
    private double allowances;

    public Employee(int id, String name, String department, double baseSalary, double allowances) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.allowances = allowances;
    }

    public int getId() { return id; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public void setAllowances(double allowances) { this.allowances = allowances; }

    public double calculateTotalSalary() {
        return baseSalary + allowances;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Dept: " + department + 
               " | Base: $" + baseSalary + " | Allowances: $" + allowances + 
               " | Total Salary: $" + calculateTotalSalary();
    }
}

public class EmployeeManagementSystem {
    private static final String FILE_NAME = "employees.dat";
    private static List<Employee> employeeList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] resignation) {
        loadFromFile();
        while (true) {
            System.out.println("\n--- Employee Management System ---");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Save & Exit");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> viewEmployees();
                    case 3 -> searchEmployee();
                    case 4 -> updateEmployee();
                    case 5 -> deleteEmployee();
                    case 6 -> {
                        saveToFile();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid numerical choice.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void addEmployee() {
        try {
            System.out.print("Enter ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (findEmployeeById(id) != null) {
                System.out.println("Error: Employee with this ID already exists.");
                return;
            }
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Department: ");
            String dept = scanner.nextLine();
            System.out.print("Enter Base Salary: ");
            double base = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Allowances: ");
            double allowances = Double.parseDouble(scanner.nextLine());

            employeeList.add(new Employee(id, name, dept, base, allowances));
            System.out.println("Employee added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format input. Operation cancelled.");
        }
    }

    private static void viewEmployees() {
        if (employeeList.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        employeeList.forEach(System.out.println);
    }

    private static void searchEmployee() {
        try {
            System.out.print("Enter Employee ID to search: ");
            int id = Integer.parseInt(scanner.nextLine());
            Employee emp = findEmployeeById(id);
            if (emp != null) {
                System.out.println(emp);
            } else {
                System.out.println("Employee not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static void updateEmployee() {
        try {
            System.out.print("Enter Employee ID to update: ");
            int id = Integer.parseInt(scanner.nextLine());
            Employee emp = findEmployeeById(id);
            if (emp == null) {
                System.out.println("Employee not found.");
                return;
            }
            System.out.print("Enter New Name: ");
            emp.setName(scanner.nextLine());
            System.out.print("Enter New Department: ");
            emp.setDepartment(scanner.nextLine());
            System.out.print("Enter New Base Salary: ");
            emp.setBaseSalary(Double.parseDouble(scanner.nextLine()));
            System.out.print("Enter New Allowances: ");
            emp.setAllowances(Double.parseDouble(scanner.nextLine()));
            System.out.println("Employee updated successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid format entered. Update failed.");
        }
    }

    private static void deleteEmployee() {
        try {
            System.out.print("Enter Employee ID to delete: ");
            int id = Integer.parseInt(scanner.nextLine());
            Employee emp = findEmployeeById(id);
            if (emp != null) {
                employeeList.remove(emp);
                System.out.println("Employee removed successfully.");
            } else {
                System.out.println("Employee not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private static Employee findEmployeeById(int id) {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            employeeList = (List<Employee>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading stored data. Starting with an empty database.");
        }
    }

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employeeList);
            System.out.println("Data saved cleanly to storage.");
        } catch (IOException e) {
            System.out.println("System failure: Could not preserve data to disk. " + e.getMessage());
        }
    }
}
