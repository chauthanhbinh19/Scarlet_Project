package com.example.scarlet.Data;

public class Payment {
    private String type; // Loại thẻ
    private String cardNumber; // Số thẻ
    private String cardholderName; // Tên chủ thẻ
    private String expirationDate; // Ngày hết hạn
    private String cvv; // Mã CVV
    private String transactionId; // Mã giao dịch
    private String otp; // Mã xác minh (OTP)
    private String bankName; // Tên ngân hàng thanh toán
    private String token;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Payment() {
    }

    public Payment(String type, String cardNumber, String cardholderName, String expirationDate, String cvv, String transactionId, String otp, String bankName, String token) {
        this.type = type;
        this.cardNumber = cardNumber;
        this.cardholderName = cardholderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.transactionId = transactionId;
        this.otp = otp;
        this.bankName = bankName;
        this.token = token;
    }
}
