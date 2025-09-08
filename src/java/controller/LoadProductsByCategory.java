
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hibernate.Brand;
import hibernate.Category;
import hibernate.HibernateUtil;
import hibernate.Product;
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


@WebServlet(name = "LoadProductsByCategory", urlPatterns = {"/LoadProductsByCategory"})
public class LoadProductsByCategory extends HttpServlet {

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

        String categoryName = requestJsonObject.get("selectedCategory").getAsString();

        // get Category details 
        Criteria c2 = s.createCriteria(Category.class);
        c2.add(Restrictions.eq("name", categoryName));
        Category category = (Category) c2.uniqueResult();

        c1.add(Restrictions.eq("category.id", category.getId()));

        List<Product> productList = c1.list();
        s.close();
        resposeObject.add("productList", gson.toJsonTree(productList));
        resposeObject.addProperty("status", true);
        String toJson = gson.toJson(resposeObject);
        response.getWriter().write(toJson);

    }
}
