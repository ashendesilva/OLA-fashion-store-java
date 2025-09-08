package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "VerifyAccount", urlPatterns = {"/VerifyAccount"})
public class VerifyAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
        
        JsonObject resJsonObject = new JsonObject();
        resJsonObject.addProperty("status", false);
        Gson gson = new Gson();

        HttpSession ses = reqest.getSession();
        if (ses.getAttribute("email") == null) {
            resJsonObject.addProperty("message", "Email Not Found");
        } else {

            String email = ses.getAttribute("email").toString();

            JsonObject verification = gson.fromJson(reqest.getReader(), JsonObject.class);

            String verificationCode = verification.get("verificationCode").getAsString();

            SessionFactory sf = hibernate.HibernateUtil.getSessionFactory();
            Session s = sf.openSession();

            Criteria c1 = s.createCriteria(User.class);
            Criterion crt1 = Restrictions.eq("email", email);
            Criterion crt2 = Restrictions.eq("verification", verificationCode);

            c1.add(crt1);
            c1.add(crt2);

            if (c1.list().isEmpty()) {
                resJsonObject.addProperty("message", "Invalid verification code");
            } else {
                User user = (User) c1.list().get(0);
                user.setVerification("Verified");

                s.update(user);
                s.beginTransaction().commit();
                s.close();
                
                ses.setAttribute("user", user);

                resJsonObject.addProperty("status", true);
                resJsonObject.addProperty("message", "Verification succesful");
            }

        }

        String responseTesxt = gson.toJson(resJsonObject);
        response.setContentType("application/json");
        response.getWriter().write(responseTesxt);
    }

}
