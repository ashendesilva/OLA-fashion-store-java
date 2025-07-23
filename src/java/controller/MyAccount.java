package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Address;
import hibernate.City;
import hibernate.HibernateUtil;
import hibernate.User;
import hibernate.UserStatus;
import hibernate.UserType;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Mail;
import model.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dilhara
 */
@WebServlet(name = "MyAccount", urlPatterns = {"/MyAccount"})
public class MyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession(false);
        if (ses != null && ses.getAttribute("user") != null) {
            User user = (User) ses.getAttribute("user");
            JsonObject responseObject = new JsonObject();
            responseObject.addProperty("firstName", user.getF_name());
            responseObject.addProperty("lastName", user.getL_name());
            responseObject.addProperty("password", user.getPassword());
            responseObject.addProperty("email", user.getEmail());
            responseObject.addProperty("mobile", user.getPhone());

            String since = new SimpleDateFormat("MMM yyyy").format(user.getCreated_at());
            responseObject.addProperty("since", since);

            Gson gson = new Gson();
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            Criteria c = s.createCriteria(Address.class);
            c.add(Restrictions.eq("user", user));
            if (!c.list().isEmpty()) {
                List<Address> addressList = c.list();
                responseObject.add("addressList", gson.toJsonTree(addressList));
            }

            String toJson = gson.toJson(responseObject);
            response.setContentType("application/json");
            response.getWriter().write(toJson);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject resJsonObject = new JsonObject();
        response.setContentType("application/json");

        JsonObject address = gson.fromJson(request.getReader(), JsonObject.class);

        String lineOne = address.get("lineOne").getAsString();
        String lineTwo = address.get("lineTwo").getAsString();
        String provinceId = address.get("provinceId").getAsString();
        String cityId = address.get("cityId").getAsString();
        String postalCode = address.get("postalCode").getAsString();

        // Validation
        if (lineOne.isEmpty()) {
            resJsonObject.addProperty("message", "Address Line One is required");
        } else if (lineTwo.isEmpty()) {
            resJsonObject.addProperty("message", "Address Line Two is required");
        } else if (Integer.parseInt(provinceId) == 0) {
            resJsonObject.addProperty("message", "Please Select Province");
        } else if (Integer.parseInt(cityId) == 0) {
            resJsonObject.addProperty("message", "Please Select City");
        } else if (postalCode.isEmpty()) {
            resJsonObject.addProperty("message", "Postal code is required");
        } else if (!Util.isCodeValid(postalCode)) {
            resJsonObject.addProperty("message", "Please enter a valid postal code");
        } else {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            User user = (User) request.getSession().getAttribute("user");
            Criteria c1 = s.createCriteria(User.class);
            c1.add(Restrictions.eq("email", user.getEmail()));
            User u1 = (User) c1.uniqueResult();

            City city = (City) s.get(City.class, Integer.parseInt(cityId));
            Address a = new Address();
            a.setAddress_line_1(lineOne);
            a.setAddress_line_2(lineTwo);
            a.setPostal_code(postalCode);
            a.setCity(city);
            a.setUser(u1);

            s.save(a);
            s.beginTransaction().commit();
            resJsonObject.addProperty("status", true);
            resJsonObject.addProperty("message", "Address Register Successfully");
            s.close();
        }

        String responseText = gson.toJson(resJsonObject);
        response.setContentType("application/json");
        response.getWriter().write(responseText);

    }

}
