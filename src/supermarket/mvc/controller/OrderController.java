/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermarket.mvc.controller;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import supermarket.mvc.db.DBConnection;
import supermarket.mvc.model.OrderDetailModel;
import supermarket.mvc.model.OrderModel;

/**
 *
 * @author dell
 */
public class OrderController {
     public String placeOrder(OrderModel orderModel, ArrayList<OrderDetailModel> orderDetailModels) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            String orderQuery = "INSERT INTO Orders VALUES(?,?,?)";

            PreparedStatement statementForOrder = connection.prepareStatement(orderQuery);
            statementForOrder.setString(1, orderModel.getOrderId());
            statementForOrder.setString(2, orderModel.getOrderDate());
            statementForOrder.setString(3, orderModel.getCutomerId());

            if (statementForOrder.executeUpdate() > 0) {

                boolean isOrderDetailSaved = true;
                String orderDetailQuery = "INSERT INTO OrderDetail VALUES(?,?,?,?)";

                for (OrderDetailModel orderDetailModel : orderDetailModels) {
                    PreparedStatement statementForOrderDetail = connection.prepareStatement(orderDetailQuery);
                    statementForOrderDetail.setString(1, orderModel.getOrderId());
                    statementForOrderDetail.setString(2, orderDetailModel.getItemCode());
                    statementForOrderDetail.setInt(3, orderDetailModel.getQty());
                    statementForOrderDetail.setDouble(4, orderDetailModel.getDiscount());
                    
                    if(!(statementForOrderDetail.executeUpdate() > 0)){
                        isOrderDetailSaved = false;
                    }

                }
                
                if(isOrderDetailSaved){
                    boolean isItemUpdated = true;
                    String itemQuery = "UPDATE Item SET QtyOnHand = QtyOnHand - ? WHERE ItemCode = ?";
                    for (OrderDetailModel orderDetailModel : orderDetailModels) {
                        PreparedStatement statmentForItem = connection.prepareStatement(itemQuery);
                        statmentForItem.setInt(1, orderDetailModel.getQty());
                        statmentForItem.setString(2, orderDetailModel.getItemCode());
                        if(!(statmentForItem.executeUpdate()>=0)){
                            isItemUpdated = false;
                        }
                    }
                    
                    if(isItemUpdated){
                        connection.commit();
                        return "Success";
                    } else{
                        connection.rollback();
                        return "Item Update Error";
                    }
                            
                } else{
                    connection.rollback();
                    return "Order Detail Save Error";
                }

            } else {
                connection.rollback();
                return "Order Save Error";
            }

        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
            return e.getMessage();
        } finally {
            connection.setAutoCommit(true);
        }
    
     }
}
