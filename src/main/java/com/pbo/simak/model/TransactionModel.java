package com.pbo.simak.model;

import com.pbo.simak.database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionModel extends DatabaseConnection {
    int transactionId;
    String productName;
    String productCount;
    String totalPrice;
    String description;

    public TransactionModel() {
    }

    public TransactionModel(int transactionId, String productName, String productCount, String totalPrice, String description) {
        this.transactionId = transactionId;
        this.productName = productName;
        this.productCount = productCount;
        this.totalPrice = totalPrice;
        this.description = description;
    }

    public static ObservableList<TransactionModel> getAllTransactions() throws SQLException {
        ObservableList<TransactionModel> selectedList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM transaction_product";

        pst = connectDB.prepareStatement(sql);

        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            int transactionId = rs.getInt("transaction_id");
            String productName = rs.getString("product_name");
            String productCount = rs.getString("product_count");
            String totalPrice = rs.getString("total_price");
            String description = rs.getString("description");

            selectedList.add(new TransactionModel(transactionId, productName, productCount, totalPrice, description));
        }
        return selectedList;
    }

    public static int store(HashMap<String, String> storeData) throws SQLException {

        ObservableList<ProductModel> selectedList = FXCollections.observableArrayList(ProductModel.getAllProduct());

        AtomicInteger totalPrice = new AtomicInteger();
        selectedList.forEach(item -> {
            if (item.getProductName().equals(storeData.get("productName"))) {
                int hargaBarang = Integer.parseInt(item.getProductPrice());
                int unitBarang = Integer.parseInt(storeData.get("productCount"));
                totalPrice.set(hargaBarang * unitBarang);
            }
        });


        String sql = "INSERT INTO transaction_product VALUES (0, '%s', '%s', '%s', '%s')";

        sql = String.format(sql, storeData.get("productName"), storeData.get("productCount"), totalPrice,
                storeData.get("desctiption"));
        System.out.println(sql);

        pst = connectDB.prepareStatement(sql);
        return pst.executeUpdate(sql);
    }

    public static int destroy(String id) throws SQLException {
        String sql = "DELETE FROM transaction_product WHERE transaction_id = '%s'";

        sql = String.format(sql, id);
        pst = connectDB.prepareStatement(sql);
        return pst.executeUpdate(sql);
    }


    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

