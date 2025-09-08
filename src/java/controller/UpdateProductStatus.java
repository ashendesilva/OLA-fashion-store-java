/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Brand;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.ProductStatus;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "UpdateProductStatus", urlPatterns = {"/UpdateProductStatus"})
public class UpdateProductStatus extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("productId");
        String status = request.getParameter("status");
        
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        ProductStatus productStatus = (ProductStatus) s.get(ProductStatus.class, Integer.valueOf(status));
        Product product = (Product) s.get(Product.class, Integer.valueOf(productId));

        if (product == null) {
            responseObject.addProperty("message", "Invalid Product");
        } else {
            if (productStatus == null) {
                responseObject.addProperty("message", "Invalid Status");
            } else {
                product.setProductStatus(productStatus);
                s.saveOrUpdate(product);
                s.beginTransaction().commit();
                   
                s.close();
                responseObject.addProperty("status", true);
            } 
        }
      
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
    }
    

}
