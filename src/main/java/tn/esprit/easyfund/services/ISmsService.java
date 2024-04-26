package tn.esprit.easyfund.services;

public interface ISmsService {
    void sendSms(String phoneNumber, String message);
}
