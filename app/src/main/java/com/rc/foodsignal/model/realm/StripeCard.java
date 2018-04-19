package com.rc.foodsignal.model.realm;

import com.google.gson.Gson;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StripeCard extends RealmObject {
    // If you are using GSON, field names should not be obfuscated.
    // Add either the proguard rule in proguard-rules.pro or the @SerializedName annotation.

    @PrimaryKey
    private String cardNumber;
    private String cardLastFourNumber;
    private String cardName;
    private int cardExpireMonth;
    private int cardExpireYear;
    private String cardCvc;
    private String cardZip;

    public StripeCard() {
    }

    public StripeCard(String cardNumber, String cardLastFourNumber, String cardName, int cardExpireMonth, int cardExpireYear, String cardCvc, String cardZip) {
        this.cardNumber = cardNumber;
        this.cardLastFourNumber = cardLastFourNumber;
        this.cardName = cardName;
        this.cardExpireMonth = cardExpireMonth;
        this.cardExpireYear = cardExpireYear;
        this.cardCvc = cardCvc;
        this.cardZip = cardZip;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardLastFourNumber() {
        return cardLastFourNumber;
    }

    public void setCardLastFourNumber(String cardLastFourNumber) {
        this.cardLastFourNumber = cardLastFourNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardExpireMonth() {
        return cardExpireMonth;
    }

    public void setCardExpireMonth(int cardExpireMonth) {
        this.cardExpireMonth = cardExpireMonth;
    }

    public int getCardExpireYear() {
        return cardExpireYear;
    }

    public void setCardExpireYear(int cardExpireYear) {
        this.cardExpireYear = cardExpireYear;
    }

    public String getCardCvc() {
        return cardCvc;
    }

    public void setCardCvc(String cardCvc) {
        this.cardCvc = cardCvc;
    }

    public String getCardZip() {
        return cardZip;
    }

    public void setCardZip(String cardZip) {
        this.cardZip = cardZip;
    }

    @Override
    public String toString() {
        return "{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardLastFourNumber='" + cardLastFourNumber + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardExpireMonth=" + cardExpireMonth +
                ", cardExpireYear=" + cardExpireYear +
                ", cardCvc='" + cardCvc + '\'' +
                ", cardZip='" + cardZip + '\'' +
                '}';
    }

    public static <T> T getResponseObject(String jsonString, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, clazz);
    }

    public static <T> String getResponseString(T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}