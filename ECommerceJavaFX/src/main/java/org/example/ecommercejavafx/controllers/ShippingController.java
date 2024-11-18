package org.example.ecommercejavafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.ecommercejavafx.models.Shipping;
import org.example.ecommercejavafx.services.ShippingService;

import java.time.LocalDate;

public class ShippingController {

    @FXML
    private TextField shippingProviderField;
    @FXML
    private TextField trackingNumberField;
    @FXML
    private DatePicker estimatedDeliveryField; // Use DatePicker for estimated delivery date
    @FXML
    private ComboBox<String> shippingStatusComboBox;
    @FXML
    private Button addShippingButton;
    @FXML
    private Button updateShippingButton;
    @FXML
    private TableView<Shipping> shippingTableView;
    @FXML
    private TableColumn<Shipping, Integer> shippingIdColumn;
    @FXML
    private TableColumn<Shipping, String> shippingProviderColumn;
    @FXML
    private TableColumn<Shipping, String> trackingNumberColumn;
    @FXML
    private TableColumn<Shipping, String> estimatedDeliveryColumn;
    @FXML
    private TableColumn<Shipping, String> shippingStatusColumn;

    private final ShippingService shippingService = new ShippingService();

    @FXML
    public void initialize() {
        // Set Table Columns
        shippingIdColumn.setCellValueFactory(new PropertyValueFactory<>("shippingId"));
        shippingProviderColumn.setCellValueFactory(new PropertyValueFactory<>("shippingProvider"));
        trackingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));
        estimatedDeliveryColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedDelivery"));
        shippingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("shippingStatus"));

        // Set Actions for Buttons
        addShippingButton.setOnAction(event -> addShipping());
        updateShippingButton.setOnAction(event -> updateShipping());

        // Load Shipping Data into Table
        loadShippingData();
    }

    private void addShipping() {
        String provider = shippingProviderField.getText();
        String trackingNumber = trackingNumberField.getText();
        LocalDate estimatedDate = estimatedDeliveryField.getValue();
        String status = shippingStatusComboBox.getValue();

        if (provider.isEmpty() || trackingNumber.isEmpty() || estimatedDate == null || status == null || status.isEmpty()) {
            System.out.println("Error: All fields must be filled to add shipping info.");
            return;
        }

        Shipping shipping = new Shipping(0, 0, provider, trackingNumber, estimatedDate.atStartOfDay(), status);
        shippingService.addShipping(shipping);
        System.out.println("Shipping info added successfully.");
        loadShippingData();
        clearShippingFields();
    }

    private void updateShipping() {
        try {
            int shippingId = Integer.parseInt(shippingTableView.getSelectionModel().getSelectedItem().getShippingId() + "");
            String provider = shippingProviderField.getText();
            String trackingNumber = trackingNumberField.getText();
            LocalDate estimatedDate = estimatedDeliveryField.getValue();
            String status = shippingStatusComboBox.getValue();

            if (provider.isEmpty() || trackingNumber.isEmpty() || estimatedDate == null || status == null || status.isEmpty()) {
                System.out.println("Error: All fields must be provided to update shipping info.");
                return;
            }

            Shipping shipping = new Shipping(shippingId, 0, provider, trackingNumber, estimatedDate.atStartOfDay(), status);
            shippingService.updateShipping(shipping);
            System.out.println("Shipping info updated successfully.");
            loadShippingData();
            clearShippingFields();
        } catch (NumberFormatException e) {
            System.out.println("Error: Shipping ID must be a valid number.");
        }
    }

    private void loadShippingData() {
        shippingTableView.getItems().clear();
        shippingTableView.getItems().addAll(shippingService.getAllShipping());
        System.out.println("Loaded shipping data successfully.");
    }

    private void clearShippingFields() {
        shippingProviderField.clear();
        trackingNumberField.clear();
        estimatedDeliveryField.setValue(null);
        shippingStatusComboBox.setValue(null);
    }
}
