package com.pbo.simak.controller;

import com.pbo.simak.model.ProductModel;
import com.pbo.simak.model.TransactionModel;
import com.pbo.simak.utils.SceneUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    private final HashMap<String, String> storeData = new HashMap<>();

    private String selectedId;
    private ObservableList<TransactionModel> transactionList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<TransactionModel, String> colDescription;
    @FXML
    private TableColumn<TransactionModel, String> colProductName;
    @FXML
    private TableColumn<TransactionModel, String> colTotalPrice;
    @FXML
    private TableColumn<TransactionModel, Integer> colTransactionId;
    @FXML
    private TableColumn<TransactionModel, String> colTime;
    @FXML
    private TableColumn<TransactionModel, String> colUnit;
    @FXML
    private DatePicker transactionTime;
    @FXML
    private TextArea transactionDescription;
    @FXML
    private ComboBox<String> productName;
    @FXML
    private TextField transactionCount;
    @FXML
    private Label transactionMsg;
    @FXML
    private TableView<TransactionModel> transactionTable;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadTransaction();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadTransaction() throws SQLException {
        colTransactionId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("productCount"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("transactionTime"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        transactionList = TransactionModel.getAllTransactions();

        transactionTable.setItems(transactionList);

        loadProductName();

    }

    public void loadProductName() throws SQLException {
        ObservableList<String> productNames = ProductModel.getAllProductNames();
        productName.setItems(productNames);
    }

    public void logoutAction(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("login.fxml", actionEvent);
    }

    public void getSelectedProduct(MouseEvent event) {
        int index;
        index = transactionTable.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }

        selectedId = colTransactionId.getCellData(index).toString();
    }

    public void submitTransactionAction(ActionEvent actionEvent) throws SQLException {
        storeData.put("productName", productName.getSelectionModel().getSelectedItem());
        storeData.put("productCount", transactionCount.getText());
        storeData.put("description", transactionDescription.getText());
        storeData.put("transactionTime", transactionTime.getValue().toString());

        if (!transactionCount.getText().isBlank() && !transactionDescription.getText().isBlank() && !productName.getSelectionModel().isEmpty()) {
            int rowAffected = TransactionModel.store(storeData);
            if (rowAffected == 1) {
                transactionMsg.setText("Berhasil Menambah Data");
                transactionCount.clear();
                transactionDescription.clear();
                loadTransaction();
            } else {
                transactionMsg.setText("Gagal Menambah Data");
            }
        } else {
            transactionMsg.setText("Data Tidak Boleh Kosong");
        }
    }

    public void viewDashboard(ActionEvent actionEvent) {
    }


    public void viewProduct(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("product.fxml", actionEvent);
    }

    public void viewTransaction(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("transaction.fxml", actionEvent);
    }


    public void deleteTransactionAcction(ActionEvent actionEvent) throws SQLException {
        int status = TransactionModel.destroy(selectedId);
        if (status == 0) {
            transactionMsg.setText("Gagal Menghapus Data");
        } else if (status == 1) {
            transactionMsg.setText("Berhasil menghapus Data");
        }

        loadTransaction();
    }
}

