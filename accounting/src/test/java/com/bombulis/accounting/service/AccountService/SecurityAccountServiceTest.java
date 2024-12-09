package com.bombulis.accounting.service.AccountService;


import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.entity.Acc_SecurityPositionAccount;
import com.bombulis.accounting.entity.Acc_User;

import com.bombulis.accounting.dto.Acc_SecurityAccountDTO;
import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.UserService.Acc_UserException;
import com.bombulis.accounting.service.UserService.Acc_UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class SecurityAccountServiceTest {

    @Mock
    private Acc_UserServiceImpl userService;

    @Mock
    private Acc_CurrencyRepository currencyRepository;

    @InjectMocks
    private Acc_AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAccount() throws Acc_CurrencyNonFound, IOException, Acc_UserException, Acc_AccountException {
        // Arrange
        Long userId = 1L;
        String ticker = "AAPL";
        Acc_SecurityAccountDTO securityAccountDTO = new Acc_SecurityAccountDTO();
        securityAccountDTO.setTicker(ticker);

        Acc_User user = new Acc_User();
        user.setUserId(userId);
        when(userService.findUserById(userId)).thenReturn(user);

        Acc_Currency currency = new Acc_Currency( "United States Dollar", "USD");
        when(currencyRepository.findCurrencyByIsoCode("USD")).thenReturn(Optional.of(currency));

        // Act
        Acc_SecurityPositionAccount result = (Acc_SecurityPositionAccount) accountService.createAccount(securityAccountDTO, userId);

        // Assert
        //assertNotNull(result);
        assertEquals(ticker, result.getTicker());
        assertEquals(user, result.getUser());
        assertEquals(currency, result.getCurrency());

    }

    @Test
    public void findAccount() {
    }

    @Test
    public void createAccount() {


    }

    @Test
    public void deleteAccount() {
    }

    @Test
    public void findUserAccounts() {
    }
}