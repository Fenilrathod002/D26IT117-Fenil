# MiniBank

Console-based Java 17+ OOP practical project covering Practical 1 through 11.

## Build and run

```powershell
javac -d out (Get-ChildItem -Recurse src/main/java -Filter *.java | ForEach-Object FullName)
java -cp out MiniBank --demo
java -cp out MiniBank
jar --create --file minibank.jar --main-class MiniBank -C out .
java -jar minibank.jar --demo
```

The interactive menu provides opening an account, deposits, withdrawals, transfers and working hours. `--demo` runs concise demonstrations of validation, polymorphism, annotations, exceptions, threads, producer/consumer coordination, serialization, append-only logs and reporting.

## Layout and lab notes

`model` contains domain types and annotations, `util` contains validation/parsing/persistence/reporting, `service` contains threading utilities and `exception` contains checked banking exceptions. Balance can only change through operations; no balance setter exists. Account numbers are identity for equality and hash code.

The log format is `timestamp|TYPE|accountNumber|amount`, where TYPE is `DEPOSIT` or `WITHDRAW`. Reports ignore malformed entries and non-`.log` files. Serialized files must only be files created by this application.

Regex checks are format checks only: mobile `^[6-9][0-9]{9}$`, email `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$`, PAN `^[A-Z]{5}[0-9]{4}[A-Z]$`, IFSC `^[A-Z]{4}0[A-Z0-9]{6}$`.

`String` is immutable, `StringBuilder` is mutable and unsynchronised, and `StringBuffer` is synchronised. Interfaces specify capabilities; abstract classes share state/implementation. Annotations describe metadata at runtime; reflection reads it. Checked exceptions force callers to handle recoverable banking failures. `synchronized` protects state; lock ordering prevents transfer deadlock.

