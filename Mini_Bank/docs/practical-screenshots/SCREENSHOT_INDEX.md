# MiniBank Practical Screenshot Index

All images are clearly labelled rendered evidence: code images come from the current source; output images come from actual commands run on 11 October 2026. They are not claimed to be native IDE or terminal screenshots.

| Practical | Screenshot Filename | Type | Code Section / Feature | Corresponding Evidence | Status |
|---|---|---|---|---|---|
| P01 | [P01_CODE_01_MenuSwitch.png](P01_Menu_System/CODE/P01_CODE_01_MenuSwitch.png) | CODE | `MiniBank.interactive`, menu and switch | P01 output | Verified |
| P01 | [P01_OUTPUT_01_MenuAndExit.png](P01_Menu_System/OUTPUT/P01_OUTPUT_01_MenuAndExit.png) | OUTPUT | Invalid choice and exit | P01 code | Verified |
| P02 | [P02_CODE_01_AccountOperations.png](P02_Customer_Account/CODE/P02_CODE_01_AccountOperations.png) | CODE | Encapsulated account operations and transfer | P02 output | Verified |
| P02 | [P02_OUTPUT_01_AccountOperations.png](P02_Customer_Account/OUTPUT/P02_OUTPUT_01_AccountOperations.png) | OUTPUT | Deposit and withdrawal balance | P02 code | Verified |
| P03 | [P03_CODE_01_CustomerClone.png](P03_Object_Methods_Cloning/CODE/P03_CODE_01_CustomerClone.png) | CODE | Customer clone and static nested Address | P03 output | Verified |
| P03 | [P03_OUTPUT_01_ObjectMethods.png](P03_Object_Methods_Cloning/OUTPUT/P03_OUTPUT_01_ObjectMethods.png) | OUTPUT | Clone, equals and toString checks | P03 code | Verified |
| P04 | [P04_CODE_01_ValidatorParser.png](P04_Validation_Commands/CODE/P04_CODE_01_ValidatorParser.png) | CODE | Regex patterns and parser | P04 output | Verified |
| P04 | [P04_OUTPUT_01_ValidationCommands.png](P04_Validation_Commands/OUTPUT/P04_OUTPUT_01_ValidationCommands.png) | OUTPUT | Valid/invalid mobile and parsed command | P04 code | Verified |
| P05 | [P05_CODE_01_PolymorphicAccounts.png](P05_Abstract_Classes/CODE/P05_CODE_01_PolymorphicAccounts.png) | CODE | Savings account inheritance and withdrawal rule | P05 output | Verified |
| P05 | [P05_OUTPUT_01_Polymorphism.png](P05_Abstract_Classes/OUTPUT/P05_OUTPUT_01_Polymorphism.png) | OUTPUT | Concrete types, rates and pattern matching | P05 code | Verified |
| P06 | [P06_CODE_01_Interfaces.png](P06_Interfaces_Packages/CODE/P06_CODE_01_Interfaces.png) | CODE | Default interface method | P06 output | Verified |
| P06 | [P06_OUTPUT_01_InterfaceDemo.png](P06_Interfaces_Packages/OUTPUT/P06_OUTPUT_01_InterfaceDemo.png) | OUTPUT | Yearly interest via interface | P06 code | Verified |
| P07 | [P07_CODE_01_AnnotationValidator.png](P07_Annotations_Reflection/CODE/P07_CODE_01_AnnotationValidator.png) | CODE | Reflection-based annotation checks | P07 output | Verified |
| P07 | [P07_OUTPUT_01_AnnotationValidation.png](P07_Annotations_Reflection/OUTPUT/P07_OUTPUT_01_AnnotationValidation.png) | OUTPUT | Negative/maximum-length fixture errors | P07 code | Verified |
| P08 | [P08_CODE_01_BankExceptions.png](P08_Exception_Handling/CODE/P08_CODE_01_BankExceptions.png) | CODE | Checked exceptions, withdrawal and transfer | P08 output | Verified |
| P08 | [P08_OUTPUT_01_CheckedExceptions.png](P08_Exception_Handling/OUTPUT/P08_OUTPUT_01_CheckedExceptions.png) | OUTPUT | Handled insufficient-funds exception | P08 code | Verified |
| P09 | [P09_CODE_01_AccountWorker.png](P09_Multithreading/CODE/P09_CODE_01_AccountWorker.png) | CODE | Runnable worker | P09 output | Verified |
| P09 | [P09_OUTPUT_01_ThreadSafety.png](P09_Multithreading/OUTPUT/P09_OUTPUT_01_ThreadSafety.png) | OUTPUT | Ten workers and expected 1000 balance | P09 code | Verified |
| P10 | [P10_CODE_01_ProcessorBuffer.png](P10_Concurrency/CODE/P10_CODE_01_ProcessorBuffer.png) | CODE | wait/notify bounded buffer | P10 output | Verified |
| P10 | [P10_OUTPUT_01_ProcessorConsumer.png](P10_Concurrency/OUTPUT/P10_OUTPUT_01_ProcessorConsumer.png) | OUTPUT | Consumer completion | P10 code | Verified |
| P11 | [P11_CODE_01_PersistenceReporting.png](P11_File_Persistence/CODE/P11_CODE_01_PersistenceReporting.png) | CODE | Serializable state persistence | P11 output | Verified |
| P11 | [P11_OUTPUT_01_PersistenceReport.png](P11_File_Persistence/OUTPUT/P11_OUTPUT_01_PersistenceReport.png) | OUTPUT | Account reload and report path | P11 code | Verified |

The completed final application does not include an intentionally unsafe live race-condition or deadlock run, because normal banking code must remain safe. The evidence documents the synchronised worker and bounded producer/consumer implementation instead.
