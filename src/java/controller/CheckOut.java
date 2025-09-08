/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.Cart;
import hibernate.HibernateUtil;
import hibernate.OrderItems;
import hibernate.Orders;
import hibernate.Product;
import hibernate.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.PayHere;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "CheckOut", urlPatterns = {"/CheckOut"})
public class CheckOut extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        String selectedAddressID = requJsonObject.get("selectedAddressID").getAsString();
        String line_one = requJsonObject.get("line_one").getAsString();
        String line_two = requJsonObject.get("line_two").getAsString();
        String postalCode = requJsonObject.get("postalCode").getAsString();
        String city = requJsonObject.get("city").getAsString();
        String province = requJsonObject.get("province").getAsString();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tr = s.beginTransaction();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            responseObject.addProperty("message", "Session expired! Please log in again");
        } else {
            String firrstName = user.getF_name();
            String lastName = user.getL_name();
            String Mobile = user.getPhone();

            if (line_one.isEmpty()) {
                responseObject.addProperty("message", "Address line one is required");
            } else if (line_two.isEmpty()) {
                responseObject.addProperty("message", "Address line two is required");
            } else if (postalCode.isEmpty()) {
                responseObject.addProperty("message", "Your postal code is required");
            } else if (city.isEmpty()) {
                responseObject.addProperty("message", "City is required");
            } else if (province.isEmpty()) {
                responseObject.addProperty("message", "Province is required");
            } else {
                Criteria c1 = s.createCriteria(Address.class);
                c1.add(Restrictions.eq("id", Integer.valueOf(selectedAddressID)));
                Address address = (Address) c1.list().get(0);
                
                processCheckout(s, tr, user, address, responseObject);
            }
        }

//        JsonObject responseObject = new JsonObject();
//        responseObject.addProperty("status", false);
        response.setContentType("application/json");
        String toJson = gson.toJson(responseObject);
        response.getWriter().write(toJson);
    }

    private void processCheckout(Session s,
            Transaction tr,
            User user,
            Address address,
            JsonObject responseObject) {

        try {
            Orders orders = new Orders();
            orders.setAddress(address);
            orders.setCreatedAt(new Date());
            orders.setUser(user);

            int orderId = (int) s.save(orders);

            Criteria c1 = s.createCriteria(Cart.class);
            c1.add(Restrictions.eq("user", user));
            List<Cart> cartList = c1.list();


            double amount = 0;
            String items = "";

            for (Cart cart : cartList) {
                amount += cart.getQty() * cart.getProduct().getPrice();

                OrderItems orderItems = new OrderItems();

                items += cart.getProduct().getTitle() + " x " + cart.getQty() + ", ";

                Product product = cart.getProduct();
                orderItems.setOrders(orders);
                orderItems.setProduct(product);
                orderItems.setQty(cart.getQty());

                s.save(orderItems);

                //update product qty
                product.setQty(product.getQty() - cart.getQty());
                s.update(product);

                // delete cart item
                s.delete(cart);
            }

            tr.commit();
//
//            PayHere process
            String merahantID = "1225015";
            String merchantSecret = "MTkzMTQ5NDA2ODI4MzYzMDk2NjEyNjA2OTEwNjIzMjA2MDM4OTg1Mg==";
            String orderID = "#000" + orderId;
            String currency = "LKR";
            String formattedAmount = new DecimalFormat("0.00").format(amount);
            String merchantSecretMD5 = PayHere.generateMD5(merchantSecret);

            String hash = PayHere.generateMD5(merahantID + orderID + formattedAmount + currency + merchantSecretMD5);
//
            JsonObject payHereJson = new JsonObject();
            payHereJson.addProperty("sandbox", true);
            payHereJson.addProperty("merchant_id", merahantID);

            payHereJson.addProperty("return_url", "");
            payHereJson.addProperty("cancel_url", "");
            payHereJson.addProperty("notify_url", "https://99833d6dbafb.ngrok-free.app/OLA_Fasion_Store/VerifyPayments");
//
            payHereJson.addProperty("order_id", orderID);
            payHereJson.addProperty("items", items);
            payHereJson.addProperty("amount", formattedAmount);
            payHereJson.addProperty("currency", currency);
            payHereJson.addProperty("hash", hash);
//
            payHereJson.addProperty("first_name", user.getF_name());
            payHereJson.addProperty("last_name", user.getL_name());
            payHereJson.addProperty("email", user.getEmail());
//
            payHereJson.addProperty("phone", user.getPhone());
            payHereJson.addProperty("address", address.getAddress_line_1()+ ", " + address.getAddress_line_2());
            payHereJson.addProperty("city", address.getCity().getName());
            payHereJson.addProperty("country", "Sri Lanka");
//
            responseObject.addProperty("status", true);
            responseObject.addProperty("message", "Cechkout completed");
            responseObject.add("payhereJson", new Gson().toJsonTree(payHereJson));

        } catch (Exception e) {
            tr.rollback();
        }
    }

}
