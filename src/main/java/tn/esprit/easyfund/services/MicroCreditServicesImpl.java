package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.CreditStatus;
import tn.esprit.easyfund.entities.CreditType;
import tn.esprit.easyfund.entities.MicroCredit;
import tn.esprit.easyfund.repositories.IMicroCreditRepositories;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class MicroCreditServicesImpl implements IMicroCreditService {

    private static final DecimalFormat df = new DecimalFormat("0.00000", DecimalFormatSymbols.getInstance(Locale.US));
    @Autowired
    private IMicroCreditRepositories microCreditRepositories;

    @Override
    public MicroCredit createMicroCredit(MicroCredit microCredit) {
        return microCreditRepositories.save(microCredit);

    }

    @Override
    public MicroCredit findCreditById(Long id) {
        MicroCredit credit = microCreditRepositories.findById(id).orElse(null);
        if (microCreditRepositories.findById(id).isPresent()) {
            System.out.println("Credit found");
        } else {
            System.out.println("Credit not found");
        }
        return credit;
    }

    @Override
    public List<MicroCredit> findAllCredits() {
        List<MicroCredit> credits = microCreditRepositories.findAll();
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;

    }

    @Override
    public void deleteCredit(Long id) {
        MicroCredit credit = microCreditRepositories.findById(id).orElse(null);
        if (microCreditRepositories.findById(id).isPresent()) {
            assert credit != null;
            microCreditRepositories.delete(credit);
            System.out.println("Credit deleted");
        } else {
            System.out.println("Credit not found");
        }
    }

    @Override
    public MicroCredit updateCredit(MicroCredit microCredit) {
        Long creditId = microCredit.getMicroCreditId();
        if (creditId != null && microCreditRepositories.existsById(creditId)) {
            System.out.println("Credit updated");
            return microCreditRepositories.save(microCredit);
        } else {
            System.out.println("Credit not found");
            return null;
        }
    }


    @Override
    public MicroCredit updateStatus(Long id, CreditStatus status) {
        MicroCredit credit = microCreditRepositories.findById(id).orElse(null);
        if (microCreditRepositories.findById(id).isPresent()) {
            assert credit != null;
            credit.setCreditStatus(status);
            System.out.println("Credit Status Changed...");
            return microCreditRepositories.save(credit);
        } else {
            System.out.println("Credit not found");
        }
        return null;
    }

    @Override
    public List<MicroCredit> getCreditsByStatus(CreditStatus status) {
        List<MicroCredit> credits = microCreditRepositories.retrieveCreditsByStatus(status);
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;
    }

    @Override
    public List<MicroCredit> getCreditsByType(CreditType type) {
        List<MicroCredit> credits = microCreditRepositories.retrieveCreditsByType(type);
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;
    }

    @Override
    public List<MicroCredit> getCreditByAccountId(Long id) {
        List<MicroCredit> credits = microCreditRepositories.retrieveCreditsByAccountID(id);
        if (credits.isEmpty()) {
            System.out.println("No credits found");
            return null;
        }
        return credits;
    }

    ///// ADVANCED METHODS IMPLEMENTATION
    @Override
    public List<Object> Simulation(double amount, int period, String typePeriod) {
        List<Object> simulation = new ArrayList<>();
        double monthlyPayment;
        double credit = amount;
        double interestRate = 6.25 + calculateInterest(scoreCredit(amount, period, typePeriod)); // Personal score calculation
//        simulation.add(interestRate); // Add initial interest rate to output
        interestRate = interestRate / 100;

        // Calculate interest rate based on payment type
        switch (typePeriod.toUpperCase()) {
            case "MONTHLY":
                interestRate = Math.pow(1 + interestRate, (1d / 12)) - 1;
                monthlyPayment = (amount * interestRate) / (1 - Math.pow(1 + interestRate, -period));
                break;
            case "HALF-YEARLY":
                interestRate = Math.pow(1 + interestRate, 1d / 2) - 1;
                monthlyPayment = (amount * interestRate) / (1 - Math.pow(1 + interestRate, -period));
                break;
            case "QUARTERLY":
                interestRate = Math.pow(1 + interestRate, 1d / 4) - 1;
                monthlyPayment = (amount * interestRate) / (1 - Math.pow(1 + interestRate, -period));
                break;
            case "YEARLY":
                monthlyPayment = (amount * interestRate) / (1 - Math.pow(1 + interestRate, -period));
                break;
            default:
                System.out.println("Invalid Type");
                return simulation; // Return empty list for invalid type
        }

        // Calculate and add payment details for each period
        double totalProfit = 0;
        double totalInterest = 0;
        double totalBilling = 0;
        for (int i = 1; i <= period; i++) {
            Hashtable<String, Object> payment = new Hashtable<>();
            double interestAmount = credit * interestRate;
            payment.put("Interest Amount", Double.parseDouble(df.format(interestAmount)));

            // Adjust principal to ensure credit remaining is 0 in the last iteration
            double billForOriginalAmount = Math.min(monthlyPayment - interestAmount, credit);
            payment.put("Bill payment (Tax excluded)", Double.parseDouble(df.format(billForOriginalAmount)));
            totalInterest += interestAmount;
            credit -= billForOriginalAmount;
            totalBilling += monthlyPayment;

            payment.put("Bill Payment (Tax Included)", Double.parseDouble(df.format(monthlyPayment)));
            payment.put("Credit Amount", Double.parseDouble(df.format(amount))); // Credit remaining will be 0 in the last iteration
            payment.put("Amount Remaining", Double.parseDouble(df.format(credit))); // Credit remaining will be 0 in the last iteration
            simulation.add(payment);
        }
        totalProfit = amount + totalInterest - amount;
        totalBilling = totalBilling/period;

        // Add summary object with totals
        Hashtable<String, Object> summary = new Hashtable<>();
        summary.put("Interest Rate", Double.parseDouble(df.format(interestRate * 100))); // Convert back to percentage
        summary.put("Credit Amount", Double.parseDouble(df.format(amount)));
        summary.put("Profit from Credit", Double.parseDouble(df.format(totalProfit)));
        summary.put("Average Bill Payment", Double.parseDouble(df.format(totalBilling)));
        summary.put("Credit Returned", Double.parseDouble(df.format(amount + totalInterest)));

        simulation.add(summary);
        return simulation;
    }


    @Override
    public double scoreCredit(double amount, int period, String typePeriod) {
        double score = 0;

        // Calculate score based on the loan amount
        if (amount <= 10000)
            score = 400 - (amount / 10000) * 100;
        else if (amount <= 20000)
            score = 300 - ((amount - 10000) / 10000) * 100;
        else if (amount <= 30000)
            score = 200 - ((amount - 20000) / 10000) * 100;
        else if (amount <= 40000)
            score = 100 - ((amount - 30000) / 10000) * 100;

        // Calculate period score based on the type of period
        switch (typePeriod.toUpperCase()) {
            case "MONTHLY": {
            }
            break;
            case "QUARTERLY": {
                period = period * 4;
            }
            break;
            case "HALF-YEARLY": {
                period = period * 6;
            }
            break;
            case "YEARLY": {
                period = period * 12;
            }
            break;
            default: {
                score = 0;
            }
        }
        if (period <= 21)
            score += 400 - ((double) period / 21) * 100;
        else if (period <= 42)
            score += 300 - (((double) period - 21) / 21) * 100;
        else if (period <= 63)
            score += 200 - (((double) period - 42) / 21) * 100;
        else if (period <= 84)
            score += 100 - (((double) period - 63) / 21) * 100;

        return score;
    }


    @Override
    public double calculateInterest(double score) {
        double interest = 0;

        if (score <= 500) interest = 4;
        else if ((score > 500) && (score <= 1000)) interest = 3.5;
        else if ((score > 1000) && (score <= 1300)) interest = 3;
        else if ((score > 1300) && (score <= 1500)) interest = 2.5;
        else if ((score > 1500) && (score <= 1800)) interest = 2;
        else if ((score > 1800) && (score <= 2000)) interest = 1.5;
        else {
            interest = 1;
        }
        return interest;
    }

    @Override
    public List<Object> FailureToPay(long idCredit, int period, double interestAmount) {

        MicroCredit credit = findCreditById(idCredit);
        int newPeriod =(int) (credit.getCreditRemaining()/credit.getPayedAmount()) + 3;
        double newAmount = credit.getCreditRemaining()+interestAmount;

        return Simulation(newAmount,newPeriod, String.valueOf(credit.getTypePeriod()));
    }
}