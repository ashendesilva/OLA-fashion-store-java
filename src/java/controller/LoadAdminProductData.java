/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Brand;
import hibernate.Category;
import hibernate.Color;
import hibernate.HibernateUtil;
import hibernate.Product;
import hibernate.Size;
import hibernate.SubCategory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "LoadAdminProductData", urlPatterns = {"/LoadAdminProductData"})
public class LoadAdminProductData extends HttpServlet {

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //search-brands
        Criteria c1 = s.createCriteria(Brand.class);
        List<Brand> brandList = c1.list();

        //get-SubCategory
        Criteria c2 = s.createCriteria(SubCategory.class);
        List<SubCategory> subCategorylList = c2.list();
        //get-SubCategory-end

        //get-colors
        Criteria c3 = s.createCriteria(Color.class);
        List<Color> colorList = c3.list();
        //get-colors-end

        //get-Category
        Criteria c4 = s.createCriteria(Category.class);
        List<Category> categoryList = c4.list();
        //get-Category-end

        //get-quality
        Criteria c5 = s.createCriteria(Size.class);
        List<Size> sizeList = c5.list();
        //get-quality-end

        //get-quality
        Criteria c6 = s.createCriteria(Product.class);
        List<Product> productList = c6.list();
        //get-quality-end

        Gson gson = new Gson();

        responseObject.add("brandList", gson.toJsonTree(brandList));
        responseObject.add("sizeList", gson.toJsonTree(sizeList));
        responseObject.add("colorList", gson.toJsonTree(colorList));
        responseObject.add("subCategorylList", gson.toJsonTree(subCategorylList));
        responseObject.add("categoryList", gson.toJsonTree(categoryList));
        responseObject.add("productList", gson.toJsonTree(productList));

        responseObject.addProperty("status", true);

        String toJson = gson.toJson(responseObject);
        response.setContentType("application/json");
        response.getWriter().write(toJson);
        s.close();
    }

}
