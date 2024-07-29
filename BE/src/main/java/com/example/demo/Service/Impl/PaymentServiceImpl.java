package com.example.demo.Service.Impl;

import com.example.demo.Service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {
    private String accessToken;
    private String deviceId;
    private String accountId = "00002189180";
    private String username = "";
    private String password = "";


    public String login() {
        deviceId = getDeviceId();
//      header
        HttpHeaders headers = new HttpHeaders();
        headers.set("APP_VERSION", "2024.02.24");
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "vi");
        headers.set("Authorization", "Bearer");
        headers.set("Content-Type", "application/json");
        headers.set("DEVICE_ID", deviceId);
        headers.set("DEVICE_NAME", "Chrome");
        headers.set("PLATFORM_NAME", "WEB");
        headers.set("PLATFORM_VERSION", "122");
        headers.set("SOURCE_APP", "HYDRO");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("sec-ch-ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"macOS\"");

        // Tạo data
        Map<String, Object> dataSend = new HashMap<>();
        dataSend.put("username", username);
        dataSend.put("password", password);
        dataSend.put("step_2FA", "VERIFY");
        dataSend.put("deviceId", deviceId);


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(dataSend, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://ebank.tpb.vn/gateway/api/auth/login",
                entity,
                String.class
        );

        JSONObject jsonObject = new JSONObject();
        jsonObject = (JSONObject) JSONValue.parse(response.getBody());
        accessToken = (String) jsonObject.getAsString("access_token");

        return accessToken;
    }

    public String getHistoriesTransactions() {
        accessToken =  login();
        String fromDate = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).minusDays(14).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String toDate = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Map<String, Object> data = new HashMap<>();
        data.put("pageNumber", 1);
        data.put("pageSize", 400);
        data.put("accountNo", accountId);
        data.put("currency", "VND");
        data.put("maxAcentrysrno", "");
        data.put("fromDate", fromDate);
        data.put("toDate", toDate);
        data.put("keyword", "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("APP_VERSION", "2024.02.24");
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "vi,en-US;q=0.9,en;q=0.8");
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/json");
        headers.set("DEVICE_ID", deviceId);
        headers.set("DEVICE_NAME", "Chrome");
        headers.set("Origin", "https://ebank.tpb.vn");
        headers.set("PLATFORM_NAME", "WEB");
        headers.set("PLATFORM_VERSION", "122");
        headers.set("SOURCE_APP", "HYDRO");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        headers.set("sec-ch-ua", "\"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"");
        headers.set("sec-ch-ua-mobile", "?0");
        headers.set("sec-ch-ua-platform", "\"macOS\"");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(
                    "https://ebank.tpb.vn/gateway/api/smart-search-presentation-service/v2/account-transactions/find",
                    entity,
                    String.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getBody();
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String makeDeviceId(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            builder.append(randomChar);
        }

        return builder.toString();
    }

    private String getDeviceId() {
        return this.makeDeviceId(45);
    }

    public String getQRCodeBanking(int amout, String orderInfo) {
        accessToken =  login();

        Map<String, Object> data = new HashMap<>();
        data.put("accountNo", accountId); //stk ngân hàng
        data.put("accountName", "LE NAM"); //username của acc
        data.put("acqId", "970423"); //mã bin của ngân hàng
        data.put("amount", amout); //số tiền
        data.put("addInfo", "THANH TOAN " + orderInfo); //nội dung ck
        data.put("format", "text");
        data.put("template", "compact2");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "vi,en-US;q=0.9,en;q=0.8");
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(
                    "https://api.vietqr.io/v2/generate",
                    entity,
                    String.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getBody();
    }


    public String getQRCodeBankingString(int amount, String orderInfo) {
        String info = getQRCodeBanking(amount, orderInfo);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(info, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
        String qrCode = (String) dataMap.get("qrDataURL");

        return qrCode;
    }

    @Override
    public String getQRCodeBankingForEmployee(int amout,String accountNo,String username,String bin_bank, String orderInfo) {
     //   accessToken =  login();

        Map<String, Object> data = new HashMap<>();
        data.put("accountNo", accountNo); //stk ngân hàng
        data.put("accountName", username); //username của acc
        data.put("acqId", bin_bank); //mã bin của ngân hàng
        data.put("amount", amout); //số tiền
        data.put("addInfo",  orderInfo); //nội dung ck
        data.put("format", "text");
        data.put("template", "compact2");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Language", "vi,en-US;q=0.9,en;q=0.8");
        headers.set("Connection", "keep-alive");
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(
                    "https://api.vietqr.io/v2/generate",
                    entity,
                    String.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response.getBody();
    }
}