package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankServiceTest {
    private BankService service;

    @BeforeEach
    void setUp() {
        service = new BankService();
    }

    @Test
    void addingAccountIncreasesAccountCount() {
        service.addAccount("AC0001", "Riya", 4000);
        assertEquals(1, service.accountCount());
    }

    @Test
    void invalidMenuActionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> service.validateAction(9));
    }

    @Test
    void addingDuplicateAccountNumberThrowsException() {
        service.addAccount("AC0001", "Riya", 4000);
        assertThrows(IllegalArgumentException.class,
                () -> service.addAccount("AC0001", "Aarav", 5000));
    }
}
