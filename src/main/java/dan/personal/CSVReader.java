package dan.personal;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVReader {

    private GUI gui;

    // Constructor to accept GUI instance
    public CSVReader(GUI gui) {
        this.gui = gui;
    }

    /**
     * Reads the CSV file and parses its content into class fields.
     */
    public ArrayList<BankTransaction> readCSV() {

        ArrayList<BankTransaction> bankTransactions = new ArrayList<>();

        // Path to the CSV file
        String filePath = gui.getInputFilePathTextField().getText(); // Get the file path from the GUI

        //TODO: update logging functionality
//        if (filePath == null) {
//            System.err.println("File path is null.");
//            return;
//        }
//        if (filePath.isEmpty()) {
//            System.err.println("File path is empty.");
//            return;
//        }

        //TODO: transfer the following code to a logger class
        System.out.println("Reading CSV file: " + filePath);

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            // Read first line to handle BOM if it exists
            String firstLine = bufferedReader.readLine();
            if (firstLine != null && firstLine.startsWith("\uFEFF")) {
                firstLine = firstLine.substring(1); // Remove BOM
            }

            // Prepare CSV content with BOM removed if present
            StringBuilder csvContent = new StringBuilder();
            if (firstLine != null) {
                csvContent.append(firstLine).append("\n");
            }

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                csvContent.append(line).append("\n");
            }

            // Create a reader from the string content with BOM removed
            Reader contentReader = new java.io.StringReader(csvContent.toString());

            // Parse CSV content
            org.apache.commons.csv.CSVParser csvParser = CSVFormat.DEFAULT
                    .builder()
                    .setDelimiter(';') // Set delimiter
                    .setHeader() // Use the first row as header
                    .setSkipHeaderRecord(true) // Skip the header row
                    .build()
                    .parse(contentReader);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); // Date format used in CSV

            for (CSVRecord record : csvParser) {
                try {
                    BankTransaction bankTransaction = new BankTransaction();
                    // Access and parse values by header names
                    bankTransaction.setAccount(record.get("Kliendi konto"));
                    bankTransaction.setDocumentNumber(record.get("Dokumendi number"));
                    bankTransaction.setDate(dateFormat.parse(record.get("Kuupäev"))); // Parse date with the correct format
                    bankTransaction.setBeneficiarysAccount(getOrDefault(record.get("Saaja/maksja konto"), "N/A"));
                    bankTransaction.setBeneficiarysName(getOrDefault(record.get("Saaja/maksja nimi"), "N/A"));
                    bankTransaction.setBICSWIFT(record.get("Saaja panga kood"));
                    bankTransaction.setType(record.get("Tüüp"));
                    bankTransaction.setDC(record.get("Deebet/Kreedit (D/C)"));
                    bankTransaction.setAmount(parseAmount(record.get("Summa"))); // Convert amount to cents
                    bankTransaction.setReferenceNumber(getOrDefault(record.get("Viitenumber"), "N/A"));
                    bankTransaction.setArchiveID(record.get("Arhiveerimistunnus"));
                    bankTransaction.setDescription(record.get("Selgitus"));
                    bankTransaction.setCommissionFee(parseAmount(record.get("Teenustasu"))); // Convert commission fee to cents
                    bankTransaction.setCurrency(record.get("Valuuta"));

                    bankTransactions.add(bankTransaction);
                } catch (ParseException e) {
                    // Handle date parsing errors
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            // Handle file I/O errors
            e.printStackTrace();
        }

        return bankTransactions;
    }

    /**
     * Converts the amount string from CSV (possibly using comma as decimal separator) to integer cents.
     * @param amountStr The amount string from CSV.
     * @return The amount in cents.
     */
    private int parseAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return 0; // Return 0 for empty or null amount
        }
        try {
            // Replace comma with dot for proper decimal conversion
            return (int) (Double.parseDouble(amountStr.replace(",", ".")) * 100); // Convert to cents
        } catch (NumberFormatException e) {
            // Handle number format errors
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns the provided value if it is not null or empty; otherwise returns the default value.
     * @param value The value to check.
     * @param defaultValue The default value to return if the value is null or empty.
     * @return The original value or the default value.
     */
    private String getOrDefault(String value, String defaultValue) {
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }

//    @Override
//    public String toString() {
//        // Format the object details for output
//        return String.format("InputTable {account='%s', documentNumber='%s', date=%s, beneficiarysAccount='%s', beneficiarysName='%s', BICSWIFT='%s', type='%s', DC='%s', amount=%d, referenceNumber='%s', archiveID='%s', description='%s', commissionFee=%d, currency='%s', IDRegistryCode='%s'}",
//                account, documentNumber, date, beneficiarysAccount, beneficiarysName, BICSWIFT, type, DC, amount, referenceNumber, archiveID, description, commissionFee, currency, IDRegistryCode);
//    }
}
