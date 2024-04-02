package tn.esprit.easyfund.services;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}