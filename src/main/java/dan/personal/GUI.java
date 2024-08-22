package dan.personal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI extends Application {

    Stage window;

    // TextFields for displaying the file paths
    private TextField inputFilePathTextField;
    private TextField outputFilePathTextField;

    public TextField getInputFilePathTextField() {
        return inputFilePathTextField;
    }

    public TextField getOutputFilePathTextField() {
        return outputFilePathTextField;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("My domestic accounting");

        // Use tab pane with three tabs
        TabPane tabs = new TabPane();

        // Create and configure tabs
        Tab tabMain = new Tab();
        tabMain.setClosable(false);  // Disable closing for this tab
        tabMain.setText("Main");
        tabMain.setContent(mainPane());

        Tab inputTable = new Tab();
        inputTable.setClosable(false);  // Disable closing for this tab
        inputTable.setText("Input");
        inputTable.setContent(inputPane());

        Tab outputTable = new Tab();
        outputTable.setClosable(false);  // Disable closing for this tab
        outputTable.setText("Output");
        outputTable.setContent(outputPane());

        Tab tabLog = new Tab();
        tabLog.setClosable(false);  // Disable closing for this tab
        tabLog.setText("Log");
        tabLog.setContent(logPane());

        // Add tabs to the TabPane
        tabs.getTabs().addAll(tabMain, inputTable, outputTable, tabLog);

        // Set the scene and show the stage
        Scene scene = new Scene(tabs, 700, 432);
        window.setScene(scene);
        window.show();
    }


    // Define the methods as separate class methods
    private Pane mainPane() {
        // Create the main grid layout for arranging UI components
        GridPane grid = createGridPane();

        // Input Section
        // Create a VBox containing the main and secondary labels for the input file section
        VBox inputVBox = createLabelSection("Select input *.csv file");

        // Create an HBox with a Browse button and a text field for the input file selection
        inputFilePathTextField = new TextField(); // Create a text field for displaying the input file path
        HBox inputHBox = createFileSelectionSection(inputFilePathTextField, this::handleInputFileSelection);

        // Set up drag-and-drop functionality for inputVBox
        setupDragAndDrop(inputVBox, inputFilePathTextField, "csv");

        // Output Section
        // Create a VBox containing the main and secondary labels for the output file section
        VBox outputVBox = createLabelSection("Select output *.xlsx file");

        // Create an HBox with a Browse button and a text field for the output file selection
        outputFilePathTextField = new TextField();
        HBox outputHBox = createFileSelectionSection(outputFilePathTextField, this::handleOutputFileSelection);

        // Set up drag-and-drop functionality for outputVBox
        setupDragAndDrop(outputVBox, outputFilePathTextField, "xlsx");

        // Buttons Section
        HBox buttonBox = createButtonSection();

        // Add all components (VBoxes and HBoxes) to the grid layout with proper positioning
        grid.add(inputVBox, 0, 0);
        grid.add(inputHBox, 0, 1);
        grid.add(outputVBox, 0, 2);
        grid.add(outputHBox, 0, 3);
        grid.add(buttonBox, 0, 4);

        //TODO: Add possible exception to logging

        // Load and apply the CSS stylesheet
        grid.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Return the fully constructed grid layout as the main pane
        return grid;
    }

    // Helper method to create the main grid layout configuration
    private GridPane createGridPane() {
        // Create a GridPane layout with centered alignment and horizontal gaps between columns
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10); // Vertical gap between rows
        return grid;
    }

    // Helper method to create a VBox containing the main and secondary labels
    private VBox createLabelSection(String mainLabelText) {
        // Create a VBox for arranging the labels vertically
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10, 10, 0, 10)); // Add padding around the VBox
        vbox.setSpacing(5); // Vertical spacing between labels
        // Apply the custom class for reduced spacing
        vbox.getStyleClass().add("section-spacing");

        // Create the main label with the provided text
        Label mainLabel = new Label(mainLabelText);
        mainLabel.getStyleClass().add("label-main"); // Apply the style for bigger and bold text
        // Create the secondary label with the provided text
        Label secondaryLabel = new Label("Drag and drop file here");

        // Add both labels to the VBox
        vbox.getChildren().addAll(mainLabel, secondaryLabel);

        // Return the VBox containing the labels
        return vbox;
    }

    // Helper method to create an HBox containing a Browse button and a file path text field
    private HBox createFileSelectionSection(TextField filePathTextField, Runnable buttonAction) {
        // Create an HBox for arranging the button and text field horizontally
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5); // Horizontal spacing between button and text field
        hbox.getStyleClass().add("hbox-spacing"); // Custom class for further spacing control if needed

        // Create the Browse button with the provided label text
        Button browseButton = new Button("Browse");
        // Attach the provided event handler (file chooser logic) to the button
        browseButton.setOnAction(event -> buttonAction.run());

        // Create the text field for displaying the selected file path with a placeholder prompt
        filePathTextField.setPromptText("File path"); // Placeholder text to guide the user
        filePathTextField.setPrefWidth(400); // Set a preferred width to fit longer file paths
        HBox.setHgrow(filePathTextField, Priority.ALWAYS); // Allow the text field to grow with the available space

        // Add both the Browse button and the file path text field to the HBox
        hbox.getChildren().addAll(browseButton, filePathTextField);

        // Return the HBox containing the button and text field
        return hbox;
    }

    private void setupDragAndDrop(VBox targetVBox, TextField targetTextField, String validExtension) {
        targetVBox.setOnDragOver(event -> {
            if (event.getGestureSource() != targetVBox && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        targetVBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                List<File> files = db.getFiles();
                File file = files.get(0); // We take the first file dropped
                if (file.getName().toLowerCase().endsWith("." + validExtension)) {
                    targetTextField.setText(file.getAbsolutePath());
                    success = true;
                } else {
                    showAlert("Invalid File Type", "Please drop a *." + validExtension + " file.");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    // Event handler for handling file selection when the Browse button is clicked in the input section
    private void handleInputFileSelection() {
        // Create a FileChooser for selecting input files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Input CSV File"); // Set the dialog title
        fileChooser.setInitialDirectory(new File("C:/Users/Dan/Downloads")); // Set initial directory
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv")); // Set file type filter

        File selectedFile = fileChooser.showOpenDialog(window); // Show the file chooser dialog
        if (selectedFile != null) {
            inputFilePathTextField.setText(selectedFile.getAbsolutePath()); // Update the text field with the file path
        }
    }

    // Event handler for handling file selection when the Browse button is clicked in the output section
    private void handleOutputFileSelection() {
        // Create a FileChooser for selecting output files
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Output XLSX File"); // Set the dialog title
        fileChooser.setInitialDirectory(new File("C:/Users/Dan/OneDrive/Koduraamatupidamine")); // Set initial directory
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLSX Files", "*.xlsx")); // Set file type filter

        File selectedFile = fileChooser.showOpenDialog(window); // Show the file chooser dialog

        if (selectedFile != null) {
            outputFilePathTextField.setText(selectedFile.getAbsolutePath()); // Update the text field with the file path
        }
    }

    // Helper method to create the button section
    private HBox createButtonSection() {
        // Create an HBox to arrange the buttons horizontally
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER); // Center the buttons horizontally
        buttonBox.setSpacing(20); // Space between buttons
        buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Add top padding for spacing before buttons

        // Create buttons
        Button processButton = new Button("Process");
        processButton.getStyleClass().add("button");
        processButton.getStyleClass().add("button-process");

        Button saveButton = new Button("Save EXCEL");
        saveButton.getStyleClass().add("button");
        saveButton.getStyleClass().add("button-save");

        Button exitButton = new Button("Exit");
        exitButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("button-exit");

        // Attach actions to the buttons (replace with actual functionality)
        processButton.setOnAction(e -> handleProcessAction());
        saveButton.setOnAction(e -> handleSaveAction());
        exitButton.setOnAction(e -> window.close());

        // Ensure all buttons are the same size
        double buttonWidth = 100; // Set desired width
        processButton.setPrefWidth(buttonWidth);
        saveButton.setPrefWidth(buttonWidth);
        exitButton.setPrefWidth(buttonWidth);

        // Add buttons to the HBox
        buttonBox.getChildren().addAll(processButton, saveButton, exitButton);

        return buttonBox;
    }

    // Dummy event handlers for buttons (replace with actual logic)
    private void handleProcessAction() {
        CSVReader csvReader = new CSVReader(this);
        InputTable inputTable = new InputTable(csvReader.readCSV());
        inputTable.getTable();
    }

    private void handleSaveAction() {
        // Logic for saving the EXCEL file
        System.out.println("Save EXCEL button clicked.");
    }

    private Pane inputPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);  // Override default
        grid.setHgap(20);
        grid.setVgap(20);

        // Create an empty ArrayList if the CSV file is not processed yet
        InputTable inputTable = new InputTable(new ArrayList<>());
        grid.add(inputTable.getTable(), 0, 0);

        return grid; // Return a new GridPane instance
    }

    private Pane outputPane() {
        return new Pane(); // Return a new Pane instance
    }

    private Pane logPane() {
        return new Pane(); // Return a new Pane instance
    }

    //TODO: Make alerting separate class

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}