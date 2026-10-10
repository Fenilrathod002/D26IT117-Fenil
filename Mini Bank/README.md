# MiniBank

MiniBank is a beginner-friendly Java console application for an Object-Oriented Programming practical. It provides a menu shell for future banking operations; it does not store accounts or process transactions.

## Prerequisites

- JDK 17 or newer

## Project structure

```text
Mini Bank/
├── src/
│   ├── BankInfo.java
│   ├── MenuOption.java
│   └── MiniBank.java
└── README.md
```

## Compile and run

From the project root, run:

```powershell
javac -d out src/*.java
java -cp out MiniBank
```

On Windows `cmd.exe`, use this equivalent compile command if required:

```cmd
javac -d out src\*.java
```

## Available menu options

1. Open Account (placeholder)
2. Deposit (placeholder)
3. Withdraw (placeholder)
4. Transfer (placeholder)
5. Exit
6. Bank Working Hours

Invalid, empty, and non-numeric input is handled without ending the application.

## Java concepts used

- **Record:** `BankInfo` is a record containing read-only bank name and branch data.
- **Enum:** `MenuOption` defines the fixed set of actions available in the menu.
- **Scanner:** reads each menu choice from the console.
- **Switch expression:** converts menu numbers into `MenuOption` values with `case ->` labels.

## GitHub preparation

Create a **private** GitHub repository named `oop-minibank-<rollno>` (replace `<rollno>` with your actual roll number). In the repository's **Settings → Collaborators**, invite your faculty member using the GitHub account details they provide.

If Git has not been initialized locally, run:

```powershell
git init
git add src README.md
git commit -m "feat: implement MiniBank menu shell"
git tag v1.0-lab1
```

## Viva questions

### Q1. What is the role of the JVM, and what file does the `javac` compiler produce?

The Java Virtual Machine (JVM) runs Java bytecode. The `javac` compiler translates `.java` source code into bytecode stored in `.class` files. A JVM on different operating systems can run the same bytecode, which provides platform independence.

### Q2. How does a switch expression differ from a traditional switch statement?

A switch expression produces a value, so it can be assigned to a variable. It can use arrow labels (`->`), which do not fall through to later cases. A traditional switch statement primarily performs actions and can require `break` statements to prevent fall-through.

### Q3. Why use an enum for fixed menu options and a record for read-only data?

An enum provides a type-safe, fixed set of constants, so it is suitable for menu choices. A record concisely represents read-only data and automatically supplies accessors and useful members such as `equals`, `hashCode`, and `toString`.
