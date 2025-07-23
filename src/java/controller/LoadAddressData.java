/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.Province;
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

/**
 *
 * @author Ashen
 */
@WebServlet(name = "LoadAddressData", urlPatterns = {"/LoadAddressData"})
public class LoadAddressData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();

        Criteria c = s.createCriteria(City.class);
        List<City> citydList = c.list();

        Criteria c2 = s.createCriteria(Province.class);
        List<Province> provinceList = c2.list();

        
        s.close();

        Gson gson = new Gson();
        JsonObject responseObject = new JsonObject();
        responseObject.addProperty("status", true);

        responseObject.add("citydList", gson.toJsonTree(citydList));
        responseObject.add("provinceList", gson.toJsonTree(provinceList));
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));

    }
    
}
