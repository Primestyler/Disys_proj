package com.example.javafx;

import com.example.javafx.model.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField customerIdField;

    @FXML
    private Button generateInvoiceButton;

    @FXML
    private TableView<Invoice> invoiceTable;

    private final String BASE_URL = "http://localhost:8080/invoices/";

    @FXML
    protected void onClickGenerateInvoice() {
        String customerId = customerIdField.getText();
        if (!customerId.isEmpty()) {
            try {
                URL url = new URL(BASE_URL + customerId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_ACCEPTED || responseCode == HttpURLConnection.HTTP_OK) {
                    // Wait 10 seconds for the invoice to be generated
                    Thread.sleep(10000);
                    HttpURLConnection getConnection = getResponseGETRequest(customerId);
                    if (getConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        System.out.println("Invoice generated for customer ID: " + customerId);
                        invoiceTable.getItems().add(new Invoice(customerId, createViewInvoiceButton(customerId)));
                        if (viewInvoice(customerId)) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Invoice generated!");
                            alert.showAndWait();
                        }
                    } else {
                        System.out.println("Invoice not ready for customer ID: " + customerId);
                        showAlert(Alert.AlertType.ERROR, "Error", "Invoice not ready", "The invoice for customer ID " + customerId + " is not ready yet.");
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Exception", e.getMessage());
            }
        }
    }

    private HttpURLConnection getResponseGETRequest(String customerId) throws IOException {
        URL url = new URL(BASE_URL + customerId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }

    private Button createViewInvoiceButton(String customerId) {
        Button button = new Button("View");
        button.setOnAction(event -> {
            try {
                viewInvoice(customerId);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Unable to open invoice", e.getMessage());
            }
        });
        return button;
    }

    private boolean viewInvoice(String customerId) throws IOException {
        String filePath = "files/Customer_" + customerId + "_" + LocalDate.now() + ".pdf";
        File file = new File(filePath);

        if (file.exists()) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Unsupported Operation", "Desktop is not supported on this system.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "File Not Found", "The invoice file for customer ID " + customerId + " was not found.");
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
