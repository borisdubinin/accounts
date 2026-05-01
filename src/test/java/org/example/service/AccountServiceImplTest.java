package org.example.service;

import org.example.converter.AccountConverter;
import org.example.entity.AccountEntity;
import org.example.entity.AccountSettingsEntity;
import org.example.exception.EntityNotFoundException;
import org.example.model.Account;
import org.example.model.AccountCurrency;
import org.example.model.AccountSettings;
import org.example.model.AccountStatus;
import org.example.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountConverter accountConverter;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private Account testAccount;
    private AccountEntity testAccountEntity;

    @BeforeEach
    public void setUp() {
        testAccountEntity = new AccountEntity();
        testAccountEntity.setId(1L);
        testAccountEntity.setIban("IBANECQDA3T6IWGSQD1W3FBMU51GRPI7");
        testAccountEntity.setBalance(BigDecimal.valueOf(1000));
        testAccountEntity.setCurrency(AccountCurrency.BYN);
        testAccountEntity.setStatus(AccountStatus.ACTIVE);
        AccountSettingsEntity settingsEntity = new AccountSettingsEntity();
        settingsEntity.setSmsNotificationsEnabled(false);
        testAccountEntity.setSettings(settingsEntity);

        testAccount = new Account();
        testAccount.setIban(null);
        testAccount.setStatus(null);
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount.setCurrency(AccountCurrency.BYN);
    }

    @Nested
    class CreateTests {

        @Test
        void create_ShouldSetDefaultStatusAndIban_WhenAccountIsValid() {
            when(accountRepository.save(any(AccountEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountServiceImpl.create(testAccount);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(AccountStatus.ACTIVE);
            assertThat(result.getIban()).isNotBlank();
        }

        @Test
        void create_ShouldSetDefaultSettings_WhenSettingsIsNull() {
            when(accountRepository.save(any(AccountEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountServiceImpl.create(testAccount);

            assertThat(result.getSettings()).isNotNull();
            assertThat(result.getSettings().getMonthlyLimit()).isNull();
            assertThat(result.getSettings().getSmsNotificationsEnabled()).isFalse();
        }

        @Test
        void create_ShouldSetDefaultSmsNotification_WhenSettingsExistsButSmsNull() {
            AccountSettings settings = new AccountSettings();
            settings.setSmsNotificationsEnabled(null);
            testAccount.setSettings(settings);

            when(accountRepository.save(any(AccountEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountServiceImpl.create(testAccount);

            assertThat(result.getSettings()).isNotNull();
            assertThat(result.getSettings().getSmsNotificationsEnabled()).isFalse();
        }

        @Test
        void create_ShouldPreserveExistingSettings_WhenSmsNotificationIsSet() {
            AccountSettings settings = new AccountSettings();
            settings.setMonthlyLimit(BigDecimal.valueOf(5000));
            settings.setSmsNotificationsEnabled(true);
            testAccount.setSettings(settings);

            when(accountRepository.save(any(AccountEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Account result = accountServiceImpl.create(testAccount);

            assertThat(result.getSettings()).isNotNull();
            assertThat(result.getSettings().getMonthlyLimit()).isEqualTo(settings.getMonthlyLimit());
            assertThat(result.getSettings().getSmsNotificationsEnabled()).isEqualTo(settings.getSmsNotificationsEnabled());
        }

        @Test
        void create_ShouldGenerateUniqueIban_ForEachAccount() {
            when(accountRepository.save(any(AccountEntity.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Account result1 = accountServiceImpl.create(testAccount);
            String iban1 = result1.getIban();

            Account result2 = accountServiceImpl.create(testAccount);
            String iban2 = result2.getIban();

            assertThat(iban1).isNotEqualTo(iban2);
        }
    }

    @Nested
    class GetAllTests {

        @Test
        void getAll_ShouldReturnListOfAccounts_WhenAccountsExist() {
            Account expectedAccount = new Account();
            expectedAccount.setId(testAccountEntity.getId());
            expectedAccount.setBalance(testAccountEntity.getBalance());
            expectedAccount.setCurrency(testAccountEntity.getCurrency());
            expectedAccount.setStatus(testAccountEntity.getStatus());
            expectedAccount.setIban(testAccountEntity.getIban());
            expectedAccount.setSettings(new AccountSettings(testAccountEntity.getSettings()));
            List<Account> expectedAccounts = List.of(expectedAccount);
            List<AccountEntity> entities = List.of(testAccountEntity);

            when(accountRepository.findAllWithSettings()).thenReturn(entities);

            List<Account> result = accountServiceImpl.getAll();

            assertThat(result.size()).isEqualTo(expectedAccounts.size());
            assertThat(result).usingRecursiveComparison().isEqualTo(expectedAccounts);
            verify(accountRepository).findAllWithSettings();
            verify(accountConverter).toModels(entities);
        }

        @Test
        void getAll_ShouldReturnEmptyList_WhenNoAccountsExist() {
            List<AccountEntity> entities = List.of();
            when(accountRepository.findAllWithSettings()).thenReturn(entities);

            List<Account> result = accountServiceImpl.getAll();

            assertThat(result).isEmpty();
            verify(accountRepository).findAllWithSettings();
        }

    }

    @Nested
    class GetByIbanTests {

        @Test
        void getByIban_ShouldReturnAccount_WhenIbanExists() {
            Account expectedAccount = new Account();
            expectedAccount.setId(testAccountEntity.getId());
            expectedAccount.setBalance(testAccountEntity.getBalance());
            expectedAccount.setCurrency(testAccountEntity.getCurrency());
            expectedAccount.setStatus(testAccountEntity.getStatus());
            expectedAccount.setIban(testAccountEntity.getIban());
            expectedAccount.setSettings(new AccountSettings(testAccountEntity.getSettings()));
            when(accountRepository.findByIban(testAccountEntity.getIban()))
                    .thenReturn(Optional.of(testAccountEntity));

            Account result = accountServiceImpl.getByIban(testAccountEntity.getIban());

            assertThat(result).usingRecursiveComparison().isEqualTo(expectedAccount);
            verify(accountRepository).findByIban(testAccountEntity.getIban());
        }

        @Test
        void getByIban_ShouldThrowEntityNotFoundException_WhenIbanDoesNotExist() {
            String iban = "IBAN9999999999999999999999999999";
            when(accountRepository.findByIban(iban)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountServiceImpl.getByIban(iban))
                    .isInstanceOf(EntityNotFoundException.class);

            verify(accountRepository).findByIban(iban);
        }
    }
}
