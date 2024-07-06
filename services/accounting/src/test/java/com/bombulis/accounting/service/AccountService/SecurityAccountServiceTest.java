package com.bombulis.accounting.service.AccountService;


import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.entity.SecurityPositionAccount;
import com.bombulis.accounting.entity.User;

import com.bombulis.accounting.dto.SecurityAccountDTO;
import com.bombulis.accounting.repository.CurrencyRepository;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import com.bombulis.accounting.service.UserService.UserException;
import com.bombulis.accounting.service.UserService.UserServiceImpl;
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
    private UserServiceImpl userService;

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateAccount() throws CurrencyNonFound, IOException, UserException, AccountOtherType {
        // Arrange
        Long userId = 1L;
        String ticker = "AAPL";
        SecurityAccountDTO securityAccountDTO = new SecurityAccountDTO();
        securityAccountDTO.setTicker(ticker);

        User user = new User();
        user.setUserId(userId);
        when(userService.findUserById(userId)).thenReturn(user);

        Currency currency = new Currency( "United States Dollar", "USD");
        when(currencyRepository.findCurrencyByIsoCode("USD")).thenReturn(Optional.of(currency));

        // Act
        SecurityPositionAccount result = accountService.createAccount(securityAccountDTO, userId);

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