package org.example.api;

import org.example.dto.TransferRequestDto;
import org.example.dto.TransferResponseDto;
import org.example.entity.AccountEntity;
import org.example.entity.AccountSettingsEntity;
import org.example.entity.CurrencyRateEntity;
import org.example.model.AccountCurrency;
import org.example.model.AccountStatus;
import org.example.repository.AccountRepository;
import org.example.repository.CurrencyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferApiTest {

    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.task.scheduling.enabled", () -> "false");
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @LocalServerPort
    private int port;
    private RestTestClient restTestClient;
    private String ibanFrom;
    private String ibanTo;

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .defaultHeaders(headers -> headers.setBasicAuth("user", "user123"))
                .build();

        accountRepository.deleteAll();
        currencyRateRepository.deleteAll();

        LocalDate today = LocalDate.now();
        saveCurrencyRate(AccountCurrency.USD, BigDecimal.valueOf(2.8230), 1, today);
        saveCurrencyRate(AccountCurrency.EUR, BigDecimal.valueOf(3.3018), 1, today);
        saveCurrencyRate(AccountCurrency.RUB, BigDecimal.valueOf(3.7683), 100, today);

        AccountEntity from = new AccountEntity();
        from.setIban("IBAN0000000000000000000000000001");
        from.setBalance(new BigDecimal("1000.00"));
        from.setCurrency(AccountCurrency.USD);
        from.setStatus(AccountStatus.ACTIVE);
        AccountSettingsEntity settingsEntityFrom = new AccountSettingsEntity();
        settingsEntityFrom.setMonthlyLimit(null);
        settingsEntityFrom.setSmsNotificationsEnabled(false);
        from.setSettings(settingsEntityFrom);
        settingsEntityFrom.setAccount(from);
        from = accountRepository.save(from);
        ibanFrom = from.getIban();

        AccountEntity to = new AccountEntity();
        to.setIban("IBAN0000000000000000000000000002");
        to.setBalance(new BigDecimal("500.00"));
        to.setCurrency(AccountCurrency.BYN);
        to.setStatus(AccountStatus.ACTIVE);
        AccountSettingsEntity settingsEntityTo = new AccountSettingsEntity();
        settingsEntityTo.setMonthlyLimit(null);
        settingsEntityTo.setSmsNotificationsEnabled(false);
        to.setSettings(settingsEntityTo);
        settingsEntityTo.setAccount(to);
        to = accountRepository.save(to);
        ibanTo = to.getIban();
    }

    private void saveCurrencyRate(AccountCurrency currency, BigDecimal rate, int scale, LocalDate date) {
        CurrencyRateEntity entity = new CurrencyRateEntity();
        entity.setCurrency(currency);
        entity.setRate(rate);
        entity.setScale(scale);
        entity.setDate(date);
        currencyRateRepository.save(entity);
    }

    @Test
    void performTransfer_success_withCurrencyConversion() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo(ibanTo);
        request.setSentAmount(new BigDecimal("100.00")); // 100 USD

        TransferResponseDto response = restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransferResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getIbanFrom()).isEqualTo(ibanFrom);
        assertThat(response.getIbanTo()).isEqualTo(ibanTo);
        assertThat(response.getAmount()).isEqualByComparingTo("100.00");
        assertThat(response.getSenderCurrency()).isEqualTo(AccountCurrency.USD);
        assertThat(response.getReceiverCurrency()).isEqualTo(AccountCurrency.BYN);

        AccountEntity senderAfter = accountRepository.findByIban(ibanFrom).orElseThrow();
        AccountEntity receiverAfter = accountRepository.findByIban(ibanTo).orElseThrow();

        // 100 USD -> BYN по курсу 2.8230 = 282.30 BYN
        assertThat(senderAfter.getBalance()).isEqualByComparingTo("900.00");   // 1000 - 100
        assertThat(receiverAfter.getBalance()).isEqualByComparingTo("782.30"); // 500 + 282.30

        assertThat(response.getSenderBalance()).isEqualByComparingTo("900.00");
        assertThat(response.getReceiverBalance()).isEqualByComparingTo("782.30");
    }

    @Test
    void performTransfer_success_sameCurrency() {
        AccountEntity toUsd = new AccountEntity();
        toUsd.setIban("IBAN0000000000000000000000000003");
        toUsd.setBalance(new BigDecimal("200.00"));
        toUsd.setCurrency(AccountCurrency.USD);
        toUsd.setStatus(AccountStatus.ACTIVE);
        AccountSettingsEntity settingsEntity = new AccountSettingsEntity();
        settingsEntity.setMonthlyLimit(null);
        settingsEntity.setSmsNotificationsEnabled(false);
        toUsd.setSettings(settingsEntity);
        settingsEntity.setAccount(toUsd);
        toUsd = accountRepository.save(toUsd);
        String ibanToUsd = toUsd.getIban();

        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo(ibanToUsd);
        request.setSentAmount(new BigDecimal("50.00")); // 50 USD

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isOk();

        AccountEntity sender = accountRepository.findByIban(ibanFrom).orElseThrow();
        AccountEntity receiver = accountRepository.findByIban(ibanToUsd).orElseThrow();
        assertThat(sender.getBalance()).isEqualByComparingTo("950.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("250.00");
    }

    @Test
    void performTransfer_insufficientFunds_returns400() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo(ibanTo);
        request.setSentAmount(new BigDecimal("2000.00")); // больше баланса (1000)

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class);

        AccountEntity sender = accountRepository.findByIban(ibanFrom).orElseThrow();
        AccountEntity receiver = accountRepository.findByIban(ibanTo).orElseThrow();
        assertThat(sender.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(receiver.getBalance()).isEqualByComparingTo("500.00");
    }

    @Test
    void performTransfer_senderNotFound_returns404() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom("IBAN0000000000000000000000000000");
        request.setIbanTo(ibanTo);
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class);
    }

    @Test
    void performTransfer_receiverNotFound_returns404() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo("IBAN0000000000000000000000000000");
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class);
    }

    @Test
    void performTransfer_sameIban_returns400() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo(ibanFrom); // тот же IBAN
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class);
    }

    @Test
    void performTransfer_invalidSenderIban_returns404() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom("INVALID_IBAN");
        request.setIbanTo(ibanTo);
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void performTransfer_invalidReceiverIban_returns404() {
        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo("INVALID_IBAN");
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void performTransfer_senderNotActive_returns400() {
        AccountEntity sender = accountRepository.findByIban(ibanFrom).orElseThrow();
        sender.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(sender);

        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo(ibanTo);
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class);
    }

    @Test
    void performTransfer_receiverNotActive_returns400() {
        AccountEntity receiver = accountRepository.findByIban(ibanTo).orElseThrow();
        receiver.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(receiver);

        TransferRequestDto request = new TransferRequestDto();
        request.setIbanFrom(ibanFrom);
        request.setIbanTo(ibanTo);
        request.setSentAmount(new BigDecimal("10.00"));

        restTestClient
                .post()
                .uri("/transfers")
                .body(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class);
    }
}