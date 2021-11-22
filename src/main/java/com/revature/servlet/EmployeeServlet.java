package com.revature.servlet;

import com.revature.model.Employee;
import com.revature.model.Request;
import com.revature.service.EmployeeService;
import com.revature.util.ServiceFactory;
import com.revature.util.Status;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class EmployeeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmployeeService es = ServiceFactory.getService();
        Cookie[] ck =req.getCookies();
        PrintWriter pw = resp.getWriter();
        resp.setContentType("text/html");

        switch (req.getParameter("_call")) {
            case "login":
                Employee employee = es.login(req.getParameter("email"), req.getParameter("password"));
                if (employee != null) {
                    resp.addCookie(new Cookie("user", Boolean.toString(employee.isManager())));
                    resp.addCookie(new Cookie("id", Integer.toString(employee.getEmployeeId())));
                    resp.sendRedirect("employee.html");
                }else{
                    resp.addCookie(new Cookie("user", "Null"));
                    resp.addCookie(new Cookie("id", "Null"));
                    resp.sendRedirect("login.html");
                }
                break;
            case "viewPast":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("id")) {
                        pw.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                                "    <script src=\"https://code.jquery.com/jquery-1.10.2.js\"></script>");
                        pw.append("<body class=\"bg-secondary\">");
                        pw.append(es.viewPast(Integer.parseInt(cookie.getValue())));
                        break;
                    }
                }
                break;
            case "viewPending":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("id")) {
                        pw.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                                "    <script src=\"https://code.jquery.com/jquery-1.10.2.js\"></script>");
                        pw.append("<body class=\"bg-secondary\">");
                        pw.append(es.viewPending(Integer.parseInt(cookie.getValue())));
                        break;
                    }
                }
                break;
            case "viewUnapproved":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("user") && cookie.getValue().equals("true")) {
                        pw.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                                "    <script src=\"https://code.jquery.com/jquery-1.10.2.js\"></script>");
                        pw.append("<body class=\"bg-secondary\">");
                        pw.append(es.viewUnapproved());
                        pw.append("<div class=\"d-flex justify-content-center \">\n" +
                                "  <form action=\"employee\" method=\"post\" >\n" +
                                "    <div class=\"form-group\">\n" +
                                "      <label for=\"id\">Id:</label><br>\n" +
                                "      <input id=\"id\" type=\"text\" name=\"id\"><br>\n" +
                                "    </div>\n" +
                                "    <div class=\"form-group\">\n" +
                                "      <label for=\"approve\">Approve:</label>\n" +
                                "      <input id=\"approve\" type=\"radio\" name=\"_call\" value=\"approve\">\n" +
                                "      <label for=\"deny\">Deny:</label>\n" +
                                "      <input id=\"deny\" type=\"radio\" name=\"_call\" value=\"deny\">\n" +
                                "    </div>\n" +
                                "    <button type=\"submit\" class=\"btn btn-primary\">Submit</button>\n" +
                                "  </form>\n" +
                                "</div>");
                        break;
                    }
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmployeeService es = ServiceFactory.getService();
        Cookie[] ck =req.getCookies();

        switch (req.getParameter("_call")) {
            case "approve":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("user") && cookie.getValue().equals("true")) {
                        es.approve(Integer.parseInt(req.getParameter("id")));
                        resp.sendRedirect("employee.html");
                        break;
                    }
                }
                break;
            case "deny":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("user") && cookie.getValue().equals("true")) {
                        es.deny(Integer.parseInt(req.getParameter("id")));
                        resp.sendRedirect("employee.html");
                        break;
                    }
                }
                break;
            case "addEmployee":
                Employee employee = new Employee();
                employee.setName(req.getParameter("name"));
                employee.setEmail(req.getParameter("email"));
                employee.setPassword(req.getParameter("password"));
                employee.setManager(false);
                es.addEmployee(employee);
                break;
            case "reqReimburs":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("id")) {
                        es.reqReimburs(Integer.parseInt(cookie.getValue()), (Double.parseDouble(req.getParameter("amount"))));
                        resp.sendRedirect("employee.html");
                        break;
                    }
                }
                break;
        }
    }
}
