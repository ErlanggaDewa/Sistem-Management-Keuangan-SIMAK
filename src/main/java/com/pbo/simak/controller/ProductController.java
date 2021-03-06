package com.pbo.simak.controller;

import com.pbo.simak.middleware.Validation;
import com.pbo.simak.model.ProductModel;
import com.pbo.simak.utils.SceneUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    private final HashMap<String, String> storeData = new HashMap<>();
    private final HashMap<String, String> selectedProduct = new HashMap<>();

    @FXML
    private TableView<ProductModel> productTable;
    @FXML
    private TableColumn<ProductModel, Integer> colProductId;
    @FXML
    private TableColumn<ProductModel, String> colProductName;
    @FXML
    private TableColumn<ProductModel, String> colPrice;
    @FXML
    private TableColumn<ProductModel, String> colCategory;
    @FXML
    private Label productMsg;
    @FXML
    private TextField productCategory;
    @FXML
    private TextField productName;
    @FXML
    private TextField productId;
    @FXML
    private TextField productPrice;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadProduct();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProduct() throws SQLException {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("productCategory"));

        ObservableList<ProductModel> productList = ProductModel.getAllProduct();

        productTable.setItems(productList);

    }

    @FXML
    private void deleteProductAcction(ActionEvent actionEvent) throws SQLException {

        int status = ProductModel.destroy(selectedProduct.get("productId"));
        if (status == 0) {
            productMsg.setText("Gagal Menghapus Data");
        } else if (status == 1) {
            productMsg.setText("Berhasil menghapus Data");
        }

        loadProduct();

    }

    @FXML
    private void getSelectedProduct(MouseEvent event) {
        int index;
        index = productTable.getSelectionModel().getSelectedIndex();

        if (index <= -1) {
            return;
        }


        selectedProduct.put("productId", colProductId.getCellData(index).toString());
        selectedProduct.put("productName", colProductName.getCellData(index));
        selectedProduct.put("productPrice", colPrice.getCellData(index));
        selectedProduct.put("productCategory", colCategory.getCellData(index));

        productId.setText(selectedProduct.get("productId"));
        productCategory.setText(selectedProduct.get("productCategory"));
        productName.setText(selectedProduct.get("productName"));
        productPrice.setText(selectedProduct.get("productPrice"));

    }


    @FXML
    private void submitProductAction(ActionEvent actionEvent) throws SQLException {
        boolean isNumber = Validation.validateNumber(productPrice.getText());

        if (!isNumber) {
            productMsg.setText("Harga Harus Berupa Angka!");
            return;
        }

        storeData.put("productName", productName.getText());
        storeData.put("productPrice", productPrice.getText());
        storeData.put("productCategory", productCategory.getText());
        if (!productName.getText().isBlank() && !productPrice.getText().isBlank() && !productCategory.getText().isBlank()) {
            int rowAffected = ProductModel.store(storeData);

            if (rowAffected == 1) {
                productMsg.setText("Berhasil Menambah Data");
                productCategory.clear();
                productPrice.clear();
                productName.clear();
                loadProduct();
            } else {
                productMsg.setText("Gagal Menambah Data");
            }
        } else {
            productMsg.setText("Data Tidak Boleh Kosong");
        }

    }

    @FXML
    private void updateProductAcction(ActionEvent actionEvent) throws SQLException {
        boolean isNumber = Validation.validateNumber(productPrice.getText());

        if (!isNumber) {
            productMsg.setText("Harga Harus Berupa Angka!");
            return;
        }

        storeData.put("productId", productId.getText());
        storeData.put("productName", productName.getText());
        storeData.put("productPrice", productPrice.getText());
        storeData.put("productCategory", productCategory.getText());

        int status = ProductModel.update(storeData);

        if (status == 0) {
            productMsg.setText("Gagal Mengedit Data");
        } else if (status == 1) {
            productMsg.setText("Berhasil Mengedit Data");
        }
        productCategory.clear();
        productPrice.clear();
        productName.clear();
        loadProduct();
    }

    @FXML
    private void viewDashboard(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("dashboard.fxml", actionEvent);
    }

    @FXML
    private void viewProduct(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("product.fxml", actionEvent);
    }

    @FXML
    private void viewTransaction(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("transaction.fxml", actionEvent);
    }

    @FXML
    private void viewExpenditure(ActionEvent actionEvent) throws IOException {
        SceneUtils.switchTo("expenditure.fxml", actionEvent);

    }

}
