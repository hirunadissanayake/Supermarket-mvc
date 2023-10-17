/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermarket.mvc.controller;

import supermarket.mvc.model.CustomerModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import supermarket.mvc.db.DBConnection;

/**
 *
 * @author dell
 */
public class CustomerController {
    
    public String saveCustomer(CustomerModel customer) throws SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        
        String query = "INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)";
        
        PreparedStatement PreparedStatement = connection.prepareStatement(query);
        PreparedStatement.setString(1,customer.getCustId() );
        PreparedStatement.setString(2,customer.getTitle() );
        PreparedStatement.setString(3,customer.getName());
        PreparedStatement.setString(4,customer.getDob());
        PreparedStatement.setDouble(5,customer.getSalary());
        PreparedStatement.setString(6,customer.getAddress());
        PreparedStatement.setString(7,customer.getCity());
        PreparedStatement.setString(8,customer.getProvince());
        PreparedStatement.setString(9,customer.getZip());
        
        if(PreparedStatement.executeUpdate() > 0){
            return "Success";
            
        }else{
            return "Fail";
        }
        
    }
    public ArrayList<CustomerModel>getAllCustomers() throws SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        
        String query = "SELECT * FROM customer";
        
        PreparedStatement statement = connection.prepareStatement(query);
        
        ResultSet rst = statement.executeQuery();
        
        ArrayList<CustomerModel> customerModels = new ArrayList<>();
        
         while (rst.next()) {            
            CustomerModel cm = new CustomerModel(rst.getString(1),
                    rst.getString(2), 
                    rst.getString(3), 
                    rst.getString(4), 
                    rst.getDouble(5),
                    rst.getString(6), 
                    rst.getString(7), 
                    rst.getString(8), 
                    rst.getString(9));
            
            customerModels.add(cm);
        }
        return customerModels;
    }

    public CustomerModel searchCustomer(String custId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM customer WHERE CustID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, custId);
        
        ResultSet rst = statement.executeQuery();
        while (rst.next()) {            
            return new CustomerModel(rst.getString(1),
                    rst.getString(2), 
                    rst.getString(3), 
                    rst.getString(4), 
                    rst.getDouble(5),
                    rst.getString(6), 
                    rst.getString(7), 
                    rst.getString(8), 
                    rst.getString(9));
        }
        
        return null;
        
    }

    public String updateCustomer(CustomerModel customerModel) throws SQLException {
         Connection connection = DBConnection.getInstance().getConnection();
        
        String query = "UPDATE customer SET CustTitle=?, CustName =?, DOB=?, salary=?, CustAddress=?,City=?, Province=?,PostalCode=? WHERE CustID=?";
        
        PreparedStatement statement = connection.prepareStatement(query);
        
        statement.setString(1, customerModel.getTitle());
        statement.setString(2, customerModel.getName());
        statement.setString(3, customerModel.getDob());
        statement.setDouble(4, customerModel.getSalary());
        statement.setString(5, customerModel.getAddress());
        statement.setString(6, customerModel.getCity());
        statement.setString(7, customerModel.getProvince());
        statement.setString(8, customerModel.getZip());
        statement.setString(9, customerModel.getCustId());
        
        if(statement.executeUpdate() > 0){
            return "Success";
        } else{
            return "Fail";
        }
    }

    public String deleteCustomer(String custId) throws SQLException {
         Connection connection = DBConnection.getInstance().getConnection();
        String query = "DELETE FROM customer WHERE CustID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, custId);
        
        if(statement.executeUpdate() > 0){
            return "Success";
        } else{
            return "Fail";
        }
        
    }
        
}

    
