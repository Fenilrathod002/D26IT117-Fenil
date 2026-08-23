package Practical_5;

abstract class Employee {
    String name;
    int id;
    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee {
    private double fixedSalary;
    FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    double monthlySalary() {
        return fixedSalary;
    }
}

class PartTime extends Employee {
    private double hours;
    private double rate;
    PartTime(String name, int id, double hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee {
    private double stipend;
    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    double monthlySalary() {
        return stipend;
    }
}

public class Payroll {
    public static void main(String[] args) {

        Employee[] employees = {
                new FullTime("Yash", 101, 50000),
                new PartTime("Rahul", 102, 80, 300),
                new Intern("Amit", 103, 15000),
                new FullTime("Jay", 104, 45000),
                new Intern("Karan", 105, 12000)
        };

        double total = 0;
        System.out.println("Employee Payroll:");

        for (Employee employee : employees) {
            double salary = employee.monthlySalary();
            System.out.println("Name: " + employee.name + ", ID: " + employee.id + 
                                ", Salary: " + String.format("%.2f", salary));
            if (employee instanceof Intern) {
                System.out.println("Note => " + employee.name + " is an Intern.\n");
            }
            total += salary;
        }

        System.out.println("Total Payroll = " + String.format("%.2f", total));
    }
}