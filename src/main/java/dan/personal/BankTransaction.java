package dan.personal;

import java.util.Date;

public class BankTransaction {

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBeneficiarysAccount() {
        return beneficiarysAccount;
    }

    public void setBeneficiarysAccount(String beneficiarysAccount) {
        this.beneficiarysAccount = beneficiarysAccount;
    }

    public String getBeneficiarysName() {
        return beneficiarysName;
    }

    public void setBeneficiarysName(String beneficiarysName) {
        this.beneficiarysName = beneficiarysName;
    }

    public String getBICSWIFT() {
        return BICSWIFT;
    }

    public void setBICSWIFT(String BICSWIFT) {
        this.BICSWIFT = BICSWIFT;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDC() {
        return DC;
    }

    public void setDC(String DC) {
        this.DC = DC;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getArchiveID() {
        return archiveID;
    }

    public void setArchiveID(String archiveID) {
        this.archiveID = archiveID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(int commissionFee) {
        this.commissionFee = commissionFee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIDRegistryCode() {
        return IDRegistryCode;
    }

    public void setIDRegistryCode(String IDRegistryCode) {
        this.IDRegistryCode = IDRegistryCode;
    }
}
