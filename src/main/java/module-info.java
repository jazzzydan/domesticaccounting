module dan.personal {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.csv;

    opens dan.personal to javafx.fxml;
    exports dan.personal;
}