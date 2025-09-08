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
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject resposeObject = new JsonObject();
        resposeObject.addProperty("status", false);

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c1 = s.createCriteria(Product.class);
        c1.add(Restrictions.eq("productStatus.id", 1));

        if (requestJsonObject.has("selectedBrand")) {
            String brandName = requestJsonObject.get("selectedBrand").getAsString();
            Criteria c2 = s.createCriteria(Brand.class);
            c2.add(Restrictions.eq("name", brandName));
            Brand brand = (Brand) c2.uniqueResult();
            if (brand != null) {
                c1.add(Restrictions.eq("brand.id", brand.getId()));
            }
        }

        if (requestJsonObject.has("selectedColor")) {
            String colorName = requestJsonObject.get("selectedColor").getAsString();
            Criteria c3 = s.createCriteria(Color.class);
            c3.add(Restrictions.eq("name", colorName));
            Color color = (Color) c3.uniqueResult();
            if (color != null) {
                c1.add(Restrictions.eq("color", color));
            }
        }

        if (requestJsonObject.has("selectedCategory")) {
            String categoryName = requestJsonObject.get("selectedCategory").getAsString();
            Criteria c5 = s.createCriteria(Category.class);
            c5.add(Restrictions.eq("name", categoryName));
            Category category = (Category) c5.uniqueResult();
            if (category != null) {
                String subCategoryName = requestJsonObject.get("selectedSubCategory").getAsString();
                Criteria c4 = s.createCriteria(SubCategory.class);
                c4.add(Restrictions.eq("category.id", category.getId()));
                c4.add(Restrictions.eq("name", subCategoryName));
                SubCategory subCategory = (SubCategory) c4.uniqueResult();
                if (subCategory != null) {
                    c1.add(Restrictions.eq("subCategory.id", subCategory.getId()));
                } else {
                    resposeObject.addProperty("message", "Invalid Category");
                }
            }
        }

        if (requestJsonObject.has("selectedSize")) {
            String sizeName = requestJsonObject.get("selectedSize").getAsString();
            Criteria c6 = s.createCriteria(Size.class);
            c6.add(Restrictions.eq("name", sizeName));
            Size size = (Size) c6.uniqueResult();
            if (size != null) {
                c1.add(Restrictions.eq("size", size));
            }
        }

        if (requestJsonObject.has("minPrice") && requestJsonObject.has("maxPrice")) {
            String minPriceStr = requestJsonObject.get("minPrice").getAsString();
            String maxPriceStr = requestJsonObject.get("maxPrice").getAsString();

            if (!minPriceStr.isEmpty() && !maxPriceStr.isEmpty()) {
                try {
                    double priceStart = Double.parseDouble(minPriceStr);
                    double priceEnd = Double.parseDouble(maxPriceStr);

                    if (priceStart < 0 || priceEnd < 0) {
                        resposeObject.addProperty("message", "Price must be a positive number");
                        response.getWriter().write(gson.toJson(resposeObject));
                        s.close();
                        return;
                    }

                    c1.add(Restrictions.ge("price", priceStart));
                    c1.add(Restrictions.le("price", priceEnd));

                } catch (NumberFormatException e) {
                    resposeObject.addProperty("message", "Invalid price format");
                    response.getWriter().write(gson.toJson(resposeObject));
                    s.close();
                    return;
                }
            }
        }

        resposeObject.addProperty("allProductCount", c1.list().size());

        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            c1.setFirstResult(firstResult);
        }

        // Get filtered product list
        List<Product> productList = c1.list();

        // Close session
        s.close();

        // Return response
        resposeObject.add("productList", gson.toJsonTree(productList));
        resposeObject.addProperty("status", true);
        String toJson = gson.toJson(resposeObject);
        response.getWriter().write(toJson);
    }

}
