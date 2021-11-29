package com.revature.servlet;

import com.revature.model.Employee;
import com.revature.service.EmployeeService;
import com.revature.util.ServiceFactory;

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
        setStyle(pw);

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
                        pw.append(es.viewPast(Integer.parseInt(cookie.getValue())));
                        break;
                    }
                }
                break;
            case "viewPending":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("id")) {
                        pw.append(es.viewPending(Integer.parseInt(cookie.getValue())));
                        break;
                    }
                }
                break;
            case "viewUnapproved":
                for (Cookie cookie : ck) {
                    if (cookie.getName().equals("user") && cookie.getValue().equals("true")) {
                        pw.append(es.viewUnapproved());
                        pw.append("<div class=\"text-light font-weight-bold mx-auto text-center container\" style=\"background-color: #374b57\"\">\n" +
                                "  <form action=\"employee\" method=\"post\" style=\"margin-top: 10px\">\n" +
                                "    <div class=\"form-group\">\n" +
                                "      <input id=\"id\" type=\"text\" name=\"id\" placeholder=\"Id\" required><br>\n" +
                                "    </div>\n" +
                                "    <div class=\"form-group\">\n" +
                                "      <label for=\"approve\">Approve:</label>\n" +
                                "      <input id=\"approve\" type=\"radio\" name=\"_call\" value=\"approve\"><br>\n" +
                                "      <label for=\"deny\">Deny:</label>\n" +
                                "      <input id=\"deny\" type=\"radio\" name=\"_call\" value=\"deny\">\n" +
                                "    </div>\n" +
                                "    <button type=\"submit\" class=\"btn btn-secondary btn-block\">Submit</button>\n" +
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
                        int id = Integer.parseInt(cookie.getValue());
                        double amount = Double.parseDouble(req.getParameter("amount"));
                        String reason = req.getParameter("reason");
                        es.reqReimburs(id, amount, reason);
                        resp.sendRedirect("employee.html");
                        break;
                    }
                }
                break;
        }
    }

    private void setStyle(PrintWriter pw) {
        pw.append("<head>" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">\n" +
                "<script src=\"https://code.jquery.com/jquery-1.10.2.js\"></script>");
        pw.append("<style>\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "  border-spacing: 0;\n" +
                "  width: 100%;\n" +
                "  border: 1px solid #ddd;\n" +
                "}\n" +
                "\n" +
                "th, td {\n" +
                "  text-align: left;\n" +
                "  padding: 16px;\n" +
                "}\n" +
                "\n" +
                "tbody>tr:nth-child(even) {\n" +
                "  background-color: #b7c7d1;\n" +
                "}\n" +
                "tbody>tr:nth-child(odd) {\n" +
                "  background-color: #a1b6c3;\n" +
                "}\n" +
                "thead {\n" +
                "  background-color: #7695a7;\n" +
                "}\n" +
                ".container{\n" +
                "   width: 20%;\n" +
                "   border-style: solid;\n" +
                "   border-width: 3px;\n" +
                "   border-color: white;\n" +
                "   margin-top: 5%;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body style=\"background-color: #537182\">");
    }
}
