package tn.esprit.easyfund.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
public class SmsController {

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {
        String twilioAccountSid = System.getenv("TWILIO_ACCOUNT_SID");
        String twilioAuthToken = System.getenv("TWILIO_AUTH_TOKEN");

        if (twilioAccountSid == null || twilioAuthToken == null) {
            return new ResponseEntity<String>("Twilio credentials are not set", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Twilio.init(twilioAccountSid, twilioAuthToken);
        Message.creator(new PhoneNumber("+21620955588"),
                    new PhoneNumber("+19283251100"), "Hello from Twilio").create();

        return new ResponseEntity<String>("Message sent successfully", HttpStatus.OK);
    }
}