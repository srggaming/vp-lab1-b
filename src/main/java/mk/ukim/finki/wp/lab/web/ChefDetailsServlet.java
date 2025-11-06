package mk.ukim.finki.wp.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mk.ukim.finki.wp.lab.model.Chef;
import mk.ukim.finki.wp.lab.service.ChefService;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "ChefDetailsServlet", urlPatterns = "/chefDetails")
public class ChefDetailsServlet extends HttpServlet {
    private final ChefService chefService;
    private final SpringTemplateEngine templateEngine;

    public ChefDetailsServlet(ChefService chefService, SpringTemplateEngine templateEngine) {
        this.chefService = chefService;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String chefIdParam = req.getParameter("chefId");
        Long chefId = Long.parseLong(chefIdParam);

        String dishId = req.getParameter("dishId");

        Chef chef = this.chefService.addDishToChef(chefId, dishId);

        resp.setContentType("text/html; charset=UTF-8");

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        context.setVariable("chef", chef);

        templateEngine.process("chefDetails", context, resp.getWriter());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.getWriter().println("<!DOCTYPE html>");
        resp.getWriter().println("<html>");
        resp.getWriter().println("<head>");
        resp.getWriter().println("    <meta charset=\"utf-8\">");
        resp.getWriter().println("    <title>Chef Details</title>");
        resp.getWriter().println("</head>");
        resp.getWriter().println("<body>");
        resp.getWriter().println("    <h1>Chef Details</h1>");
        resp.getWriter().println("    <p>Please select a chef and dish from the main page.</p>");
        resp.getWriter().println("    <a href=\"/listChefs\">Go to Chef List</a>");
        resp.getWriter().println("</body>");
        resp.getWriter().println("</html>");
    }
}
