package com.example.demo.Service;

public interface PaymentService {
    String login();
    String getHistoriesTransactions();
    String getQRCodeBanking(int amout, String orderInfo);
}
