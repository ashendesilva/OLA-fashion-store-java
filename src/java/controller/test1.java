/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import hibernate.Animal;
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
@WebServlet(name = "test1", urlPatterns = {"/test1"})
public class test1 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        SessionFactory sf = hibernate.HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Animal animal = new Animal();
        animal.setName("dog");
        s.save(animal);
        s.beginTransaction().commit();
        s.close();
        
    }

   

}
