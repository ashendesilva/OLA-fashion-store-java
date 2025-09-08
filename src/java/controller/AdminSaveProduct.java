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
import hibernate.ProductStatus;
import hibernate.Size;
import hibernate.SubCategory;
import hibernate.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ashen
 */
@MultipartConfig
@WebServlet(name = "AdminSaveProduct", urlPatterns = {"/AdminSaveProduct"})
public class AdminSaveProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String qty = request.getParameter("qty");
        String category = request.getParameter("category");
        String subCategory = request.getParameter("subCategory");
        String color = request.getParameter("color");
        String size = request.getParameter("size");
        String brand = request.getParameter("brand");

        Part part1 = request.getPart("image1");
        Part part2 = request.getPart("image2");
        Part part3 = request.getPart("image3");

        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", false);

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        //validation
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            responseObject.addProperty("message", "Please sign in!");
        } else if (user.getUserType().getId() != 2) {
            responseObject.addProperty("message", "You do not have permission to add products.");
        } else if (title.isEmpty()) {
            responseObject.addProperty("message", "Product title can not be empty");
        } else if (description.isEmpty()) {
            responseObject.addProperty("message", "Product description can not be empty");
        } else if (!Util.isDouble(price) || Double.parseDouble(price) <= 0) {
            responseObject.addProperty("message", "Invalid price.");
        } else if (!Util.isInteger(qty) || Integer.parseInt(qty) <= 0) {
            responseObject.addProperty("message", "Invalid quantity.");
        } else if (!Util.isInteger(category)) {
            responseObject.addProperty("message", "Invalid brand!");
        } else if (Integer.parseInt(category) == 0) {
            responseObject.addProperty("message", "Please select a Category!");
        } else if (!Util.isInteger(subCategory)) {
            responseObject.addProperty("message", "Invalid model!");
        } else if (Integer.parseInt(subCategory) == 0) {
            responseObject.addProperty("message", "Please select a Sub Category!");
        } else if (!Util.isInteger(subCategory)) {
            responseObject.addProperty("message", "Invalid model!");
        } else if (Integer.parseInt(subCategory) == 0) {
            responseObject.addProperty("message", "Please select a Sub Category!");
        } else if (!Util.isInteger(color)) {
            responseObject.addProperty("message", "Invalid color!");
        } else if (Integer.parseInt(color) == 0) {
            responseObject.addProperty("message", "Please select a color!");
        } else if (!Util.isInteger(size)) {
            responseObject.addProperty("message", "Invalid size!");
        } else if (Integer.parseInt(size) == 0) {
            responseObject.addProperty("message", "Please select a size!");
        } else if (!Util.isInteger(brand)) {
            responseObject.addProperty("message", "Invalid brand!");
        } else if (Integer.parseInt(brand) == 0) {
            responseObject.addProperty("message", "Please select a brand!");
        } else if (part1.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image one is required");
        } else if (part2.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image two is required");
        } else if (part3.getSubmittedFileName() == null) {
            responseObject.addProperty("message", "Product image three is required");
        } else {
            Category selectedCategory = (Category) s.get(Category.class, Integer.parseInt(category));
            if (selectedCategory == null) {
                responseObject.addProperty("message", "Please select a valid Category!");
            } else {
                SubCategory selectedSubCategory = (SubCategory) s.get(SubCategory.class, Integer.parseInt(subCategory));
                if (selectedSubCategory == null) {
                    responseObject.addProperty("message", "Please select a valid Sub Category!");
                } else {
                    if (selectedSubCategory.getCategory().getId() != selectedCategory.getId()) {
                        responseObject.addProperty("message", "Please select a suitable Sub Category!");
                    } else {
                        Color selectedColor = (Color) s.get(Color.class, Integer.parseInt(color));
                        if (selectedColor == null) {
                            responseObject.addProperty("message", "Please select a valid Color!");
                        } else {
                            Size selectedSize = (Size) s.get(Size.class, Integer.parseInt(size));
                            if (selectedSize == null) {
                                responseObject.addProperty("message", "Please select a valid Size!");
                            } else {
                                Brand selectedBrand = (Brand) s.get(Brand.class, Integer.parseInt(brand));
                                if (selectedBrand == null) {
                                    responseObject.addProperty("message", "Please select a valid Brand!");
                                } else {
                                    ProductStatus status = (ProductStatus) s.get(ProductStatus.class, 1);

                                    Product p = new Product();
                                    p.setTitle(title);
                                    p.setDescription(description);
                                    p.setPrice(Double.parseDouble(price));
                                    p.setQty(Integer.parseInt(qty));
                                    p.setCategory(selectedCategory);
                                    p.setSubCategory(selectedSubCategory);
                                    p.setBrand(selectedBrand);
                                    p.setProductStatus(status);
                                    p.setCreated_at(new Date());
                                    p.setColor(selectedColor);
                                    p.setSize(selectedSize);

                                    int id = (int) s.save(p);
                                    s.beginTransaction().commit();
                                    s.close();

                                    //image uploading
                                    String appPath = getServletContext().getRealPath(""); //Full path of the Web Pages folder

                                    String newPath = appPath.replace("build" + File.separator + "web", "web" + File.separator + "product-images");

                                    File productFolder = new File(newPath, String.valueOf(id));
                                    productFolder.mkdir();

                                    File file1 = new File(productFolder, "image1.png");
                                    Files.copy(part1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file2 = new File(productFolder, "image2.png");
                                    Files.copy(part2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file3 = new File(productFolder, "image3.png");
                                    Files.copy(part3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                    //image uploading

                                    responseObject.addProperty("status", true);

                                }
                            }
                        }
                    }
                }
            }

        }
        //send response
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        //send response
    }

}
