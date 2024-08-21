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
import java.util.Date;

public class CSVParser {

    // Path to the CSV file
    private String filePath;
    private GUI gui;

    // Class fields to hold the parsed data
    private String account;
    private String documentNumber;
    private Date date;
    private String beneficiarysAccount;
    private String beneficiarysName;
    private String BICSWIFT;
    private String type;
    private String DC;
    private int amount;
    private String referenceNumber;
    private String archiveID;
    private String description;
    private int commissionFee;
    private String currency;
    private String IDRegistryCode;

    // Constructor to accept GUI instance
    public CSVParser(GUI gui) {
        this.gui = gui;
    }

    /**
     * Reads the CSV file and parses its content into class fields.
     */
    public void readCSV() {

        filePath = gui.getInputFilePathTextField().getText(); // Get the file path from the GUI

        //TODO: update logging functionality
        if (filePath == null) {
            System.err.println("File path is null.");
            return;
        }
        if (filePath.isEmpty()) {
            System.err.println("File path is empty.");
            return;
        }

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
                    // Access and parse values by header names
                    account = record.get("Kliendi konto");
                    documentNumber = record.get("Dokumendi number");
                    date = dateFormat.parse(record.get("Kuupäev")); // Parse date with the correct format
                    beneficiarysAccount = getOrDefault(record.get("Saaja/maksja konto"), "N/A");
                    beneficiarysName = getOrDefault(record.get("Saaja/maksja nimi"), "N/A");
                    BICSWIFT = record.get("Saaja panga kood");
                    type = record.get("Tüüp");
                    DC = record.get("Deebet/Kreedit (D/C)");
                    amount = parseAmount(record.get("Summa")); // Convert amount to cents
                    referenceNumber = getOrDefault(record.get("Viitenumber"), "N/A");
                    archiveID = record.get("Arhiveerimistunnus");
                    description = record.get("Selgitus");
                    commissionFee = parseAmount(record.get("Teenustasu")); // Convert commission fee to cents
                    currency = record.get("Valuuta");

                    // Print the object details for debugging
                    System.out.println(this);
                } catch (ParseException e) {
                    // Handle date parsing errors
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            // Handle file I/O errors
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        // Format the object details for output
        return String.format("InputTable {account='%s', documentNumber='%s', date=%s, beneficiarysAccount='%s', beneficiarysName='%s', BICSWIFT='%s', type='%s', DC='%s', amount=%d, referenceNumber='%s', archiveID='%s', description='%s', commissionFee=%d, currency='%s', IDRegistryCode='%s'}",
                account, documentNumber, date, beneficiarysAccount, beneficiarysName, BICSWIFT, type, DC, amount, referenceNumber, archiveID, description, commissionFee, currency, IDRegistryCode);
    }
}
