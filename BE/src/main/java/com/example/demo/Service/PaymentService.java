package com.example.demo.Service;

public interface PaymentService {
    String login();
    String getHistoriesTransactions();
    String getQRCodeBanking(int amout, String orderInfo);
    String getQRCodeBankingString(int amount, String orderInfo);

    String getQRCodeBankingForEmployee(int amout,String accountNo,String username,String bin_bank, String orderInfo);
}
