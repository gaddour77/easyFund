    package tn.esprit.easyfund.services;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import tn.esprit.easyfund.entities.BankAccount;
    import tn.esprit.easyfund.entities.Transaction;
    import tn.esprit.easyfund.entities.TransactionType;
    import tn.esprit.easyfund.repositories.TransactionRep;
    import tn.esprit.easyfund.services.TransactionAnalysisService;

    import java.time.LocalDate;
    import java.time.YearMonth;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @Service
    public class TransactionAnalysisServiceImpl implements TransactionAnalysisService {

        @Autowired
        private TransactionRep transactionRepository;

        @Override
        public Map<String, Object> getTransactionTypeDistribution(Long accountId) {
            // Retrieve transactions associated with the provided accountId
            List<Transaction> transactions = transactionRepository.findByBankAccountId(accountId);

            // Create a map to store transaction type distribution
            Map<TransactionType, Long> distribution = new HashMap<>();

            // Calculate the count of each transaction type and total amount
            double totalAmount = 0.0;
            double minAmount = Double.MAX_VALUE;
            double maxAmount = Double.MIN_VALUE;
            for (Transaction transaction : transactions) {
                TransactionType type = transaction.getTransType();
                distribution.put(type, distribution.getOrDefault(type, 0L) + 1);

                // Update total amount
                totalAmount += transaction.getAmount();

                // Update min and max amounts
                if (transaction.getAmount() < minAmount) {
                    minAmount = transaction.getAmount();
                }
                if (transaction.getAmount() > maxAmount) {
                    maxAmount = transaction.getAmount();
                }
            }

            // Calculate average amount
            double averageAmount = totalAmount / transactions.size();

            // Calculate transaction velocity (rate of change in transaction volume)
            // For simplicity, let's calculate the change in transaction count over the past 30 days
            LocalDate currentDate = LocalDate.now();
            LocalDate thirtyDaysAgo = currentDate.minusDays(30);
            long transactionCountLastThirtyDays = transactions.stream()
                    .filter(transaction -> transaction.getTrans_date().isAfter(thirtyDaysAgo))
                    .count();
            double transactionVelocity = (double) transactionCountLastThirtyDays / 30.0;
            // Detect anomalies based on statistical measures
            List<Transaction> anomalies = new ArrayList<>();
            double threshold = 2.0 * averageAmount; // Example threshold for anomaly detection
            for (Transaction transaction : transactions) {
                double amount = transaction.getAmount();
                if (amount > threshold) {
                    anomalies.add(transaction);
                }
            }

            // Create a detailed return message with transaction type distribution, statistics, and velocity
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Transaction details for account with ID " + accountId);
            response.put("totalTransactions", transactions.size());
            response.put("totalAmount", totalAmount);
            response.put("transactionDateDistribution", getTransactionDateDistribution(accountId));
            response.put("transactionTypeDistribution", distribution);
            response.put("minAmount", minAmount);
            response.put("maxAmount", maxAmount);
            response.put("averageAmount", averageAmount);
            // Format the string with transactionVelocity
            String transactionVelocityMessage = String.format("The transactionVelocity value of %f means that, on average, there were approximately %f transactions per day over the past 30 days.", transactionVelocity, transactionVelocity);

// Put the formatted string into the map
            response.put("transactionVelocityMessage", transactionVelocityMessage);
            response.put("transactionAnomalies", anomalies);


            return response;
        }











        public Map<String, Long> getTransactionDateDistribution(Long accountId) {
            List<Transaction> transactions = transactionRepository.findByBankAccountId(accountId);
            Map<String, Long> distribution = new HashMap<>();

            for (Transaction transaction : transactions) {
                LocalDate date = transaction.getTrans_date();
                String dayKey = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String monthKey = YearMonth.from(date).toString();
                String yearKey = String.valueOf(date.getYear());

                // Update day distribution
                distribution.put(dayKey, distribution.getOrDefault(dayKey, 0L) + 1);
                // Update month distribution
                distribution.put(monthKey, distribution.getOrDefault(monthKey, 0L) + 1);
                // Update year distribution
                distribution.put(yearKey, distribution.getOrDefault(yearKey, 0L) + 1);
            }

            return distribution;
        }


        @Override
        public Map<String, Object> exploreTransactionRelationships() {
            // Get all transactions
            List<Transaction> transactions = transactionRepository.findAll();

            // Initialize a map to store relationships between accounts
            Map<String, Object> relationships = new HashMap<>();

            // Iterate through each transaction
            for (Transaction transaction : transactions) {
                // Get the sender and receiver accounts
                String senderAccountId = transaction.getAcc_from(); // Assuming acc_from is a string representing account ID
                String receiverAccountId = transaction.getAcc_to(); // Assuming acc_to is a string representing account ID
                String transactionType = transaction.getTransType().toString(); // Get transaction type

                // If both sender and receiver account IDs exist
                if (senderAccountId != null && receiverAccountId != null) {
                    // Generate a key for the relationship between the sender and receiver accounts
                    String relationshipKey = senderAccountId + "-" + receiverAccountId;

                    // Update the relationship map with the transaction details
                    if (relationships.containsKey(relationshipKey)) {
                        // If the relationship already exists, update the transaction count and add transaction type
                        Map<String, Object> relationshipDetails = (Map<String, Object>) relationships.get(relationshipKey);
                        int transactionCount = (int) relationshipDetails.get("transactionCount");
                        relationshipDetails.put("transactionCount", transactionCount + 1);
                        relationshipDetails.put("transactionType", transactionType); // Add transaction type
                    } else {
                        // If it's a new relationship, create a new entry in the map
                        Map<String, Object> relationshipDetails = new HashMap<>();
                        relationshipDetails.put("senderAccountId", senderAccountId);
                        relationshipDetails.put("receiverAccountId", receiverAccountId);
                        relationshipDetails.put("transactionCount", 1);
                        relationshipDetails.put("transactionType", transactionType); // Add transaction type
                        relationships.put(relationshipKey, relationshipDetails);
                    }
                }
            }

            // Generate a detailed output message
            String message = "Exploring relationships between accounts based on shared transactions or patterns";
            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("relationships", relationships);

            return response;
        }

    }
