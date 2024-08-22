package dan.personal;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class InputTable {

    List<BankTransaction> bankTransactions;

    public InputTable(List<BankTransaction> bankTransactions) {
        // If the list is null, create a new empty list
        this.bankTransactions = bankTransactions != null ? bankTransactions : new ArrayList<>();
    }

    public VBox getTable() {

        Label label = new Label("Account Statement");
        label.setPadding(new Insets(10, 10, 0, 10));
        label.getStyleClass().add("label-main"); // Apply the style for bigger and bold text
        TableView table = new TableView();
        table.setEditable(true);

        //TODO: table generation from CSV not working, move to separate method
        //TODO: scrollpane not working, move to separate method

        TableColumn accountCol = new TableColumn("Account");
        accountCol.setCellValueFactory(new PropertyValueFactory<>("account"));

        TableColumn documentNumberCol = new TableColumn("Document Number");
        documentNumberCol.setCellValueFactory(new PropertyValueFactory<>("documentNumber"));

        TableColumn dateCol = new TableColumn("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn beneficiarysAccountCol = new TableColumn("Beneficiary's Account");
        beneficiarysAccountCol.setCellValueFactory(new PropertyValueFactory<>("beneficiarysAccount"));

        TableColumn beneficiarysNameCol = new TableColumn("Beneficiary's Name");
        beneficiarysNameCol.setCellValueFactory(new PropertyValueFactory<>("beneficiarysName"));

        TableColumn BICSWIFTCol = new TableColumn("BIC/SWIFT");
        BICSWIFTCol.setCellValueFactory(new PropertyValueFactory<>("BICSWIFT"));

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn DCCol = new TableColumn("D/C");
        DCCol.setCellValueFactory(new PropertyValueFactory<>("DC"));

        TableColumn amountCol = new TableColumn("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn referenceNumberCol = new TableColumn("Reference Number");
        referenceNumberCol.setCellValueFactory(new PropertyValueFactory<>("referenceNumber"));

        TableColumn archiveIDCol = new TableColumn("Archive ID");
        archiveIDCol.setCellValueFactory(new PropertyValueFactory<>("archiveID"));

        TableColumn descriptionCol = new TableColumn("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn commissionFeeCol = new TableColumn("Commission Fee");
        commissionFeeCol.setCellValueFactory(new PropertyValueFactory<>("commissionFee"));

        TableColumn currencyCol = new TableColumn("Currency");
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));

        table.getColumns().addAll(accountCol,
                documentNumberCol,
                dateCol,
                beneficiarysAccountCol,
                beneficiarysNameCol,
                BICSWIFTCol,
                typeCol,
                DCCol,
                amountCol,
                referenceNumberCol,
                archiveIDCol,
                descriptionCol,
                commissionFeeCol,
                currencyCol);

        // Wrap the TableView in a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(table);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Create a VBox to hold the label and the scroll pane
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, scrollPane);

        // Load and apply the CSS stylesheet
        vbox.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return vbox;
    }
}
