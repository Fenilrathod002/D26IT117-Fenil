# MiniBank

MiniBank is a Java 17 console banking application. Its source files are organized
into `model`, `util`, and `service` packages under `src/main/java`.

## Build and run

From this folder, build and run the standalone JAR:

```bash
./build.sh
java -jar build/minibank.jar
```

JUnit 5 tests are configured in `pom.xml`. Install Maven if it is not available,
then run the tests and package with:

```bash
mvn test
mvn package
java -jar target/minibank.jar
```

The menu lets you open accounts, deposit, withdraw, transfer, list accounts,
view a statement, run concurrency and transaction-processing demos, generate an
end-of-day report, and exit. New accounts ask for a valid email and mobile number.
Withdrawal rules are demonstrated with both an anonymous class and a lambda.
Runtime annotations (`@Id`, `@Positive`, and `@MaxLength`) are checked by
`util.AnnotationValidator` using reflection. Checked bank exceptions report invalid
amounts, missing accounts, and insufficient funds; transfers roll back on failure.
The console scanner is managed by a try-with-resources `BankSession`. Account deposits, withdrawals, and balance reads are synchronized. Choose menu option 7 to compare unsynchronized and synchronized deposits. Menu option 8 runs a four-thread transaction pool, a wait/notify producer-consumer queue, and opposite-order versus ordered-lock transfers.

Account data is loaded from `data/accounts.dat` at startup and saved when you
exit. Deposits, withdrawals, and transfers are appended to
`data/logs/transactions.log`. Choose menu option 9 to generate and display the
end-of-day report at `data/reports/end-of-day-report.txt`. These runtime data
files are excluded from Git.

Accounts are held in an `AccountStore` backed by a `HashMap` keyed by account
number. The account list and startup summary are sorted by descending balance;
account number is the tie-breaker, and `Account` also implements
`Comparable<Account>` by account number. Transaction history is retained in an
`ArrayList` and can be viewed with menu option 10. The transaction processor
uses a `ConcurrentHashMap` for account references shared with worker tasks.
The generic `Repository<T>` reads entity identifiers from fields marked with
`@Id`, and supports saving, lookup, listing, deletion, and batch saving. Choose
menu option 11 to demonstrate account and customer repositories; option 12 exits
and saves account data. The `BankService` layer connects the repository,
validation and bank exceptions to the menu's account operations. The JUnit tests
cover deposits, rejected withdrawals, and concurrent deposits.

## Package layout

- `model`: accounts, customers, transactions, and account interfaces
- `util`: input and annotation validation, command parsing, and statement formatting
- `model.annotation`: runtime field annotations used for identifiers and validation
- `exception`: checked bank exception hierarchy for transaction failures
- `service`: the menu, transaction worker pool, and producer-consumer buffer
