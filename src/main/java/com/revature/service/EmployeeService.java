package com.revature.service;

import com.revature.model.Employee;
import com.revature.model.Request;
import com.revature.util.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeService {

    /**
     * Takes in an email and password and compares it against the email and password in the database
     * @param email email of user
     * @param password password of user
     * @return the employee if credential match, null if they do not
     */
    public Employee login(String email, String password){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Employee e where email = :email");
        qry.setParameter("email", email);
        List l = qry.list();

        for (Object o : l) {
            Employee e = (Employee) o;

            if(e == null){
                return null;
            }else if(e.getPassword().equals(password)){
                System.out.println("Good");
                t.commit();
                session.close();
                return e;
            }else{
                System.out.println("Bad");
                t.commit();
                session.close();
                return null;
            }
        }
        return null;
    }

    /**
     * Adds an employee to the database
     * @param employee to add to db
     */
    public void addEmployee(Employee employee){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();

        Employee e = new Employee();

        e.setEmail("Brett@email");
        e.setManager(true);
        e.setName("Brett Davis");
        e.setPassword("1234");

        session.save(e);
        t.commit();
        session.close();
    }

    /**
     * Adds a reimbursement request into the database
     * @param id of user requesting the reimbursement
     * @param amount requested
     */
    public void reqReimburs(int id, double amount, String reason){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();

        Request r = new Request();

        r.setEmployeeId(id);
        r.setAmount(amount);
        r.setReason(reason);
        r.setStatus(Status.PENDING);

        session.save(r);
        t.commit();
        session.close();
    }

    /**
     * Builds a string of the past reimbursements of an employee
     * @param id of user
     * @return string of all past reimbursements
     */
    public String viewPast(int id){

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where employeeid = " + id);

        StringBuilder sb = new StringBuilder();
        addNav(sb);
        buildTable(sb, qry);

        t.commit();
        session.close();
        return sb.toString();
    }

    /**
     * Builds a string of all pending reimbursements of an employee
     * @param id of user
     * @return string of all pending reimbursements
     */
    public String viewPending(int id){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where employeeid = " + id + "and status = 'PENDING'");

        StringBuilder sb = new StringBuilder();
        addNav(sb);
        buildTable(sb, qry);

        t.commit();
        session.close();
        return sb.toString();
    }

    /**
     * Builds a string of all unapproved reimbursements in database
     * @return string of all unapproved reimbursements
     */
    public String viewUnapproved(){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where status = 'PENDING'");

        StringBuilder sb = new StringBuilder();
        addNav(sb);
        buildTable(sb, qry);

        t.commit();
        session.close();
        return sb.toString();
    }

    /**
     * Updates reimbursement status to approved
     * @param id of reimbursement
     */
    public void approve(int id){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where requestId = " + id);


        List l = qry.list();
        for (Object o : l) {
            Request request = (Request) o;
            request.setStatus(Status.ACCEPTED);
            session.update(request);

            t.commit();
            session.close();

            return;
        }
    }

    /**
     * Updates reimbursement status to denied
     * @param id of reimbursement
     */
    public void deny(int id){

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where requestId = " + id);


        List l = qry.list();
        for (Object o : l) {
            Request request = (Request) o;
            request.setStatus(Status.DENIED);
            session.update(request);

            t.commit();
            session.close();

            return;
        }
    }

    private void addNav(StringBuilder sb){
        sb.append("<nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n" +
                "    <a class=\"navbar-brand\" href=\"index.html\" onclick='deleteAllCookies()'>Logout</a>\n" +
                "    <button  class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
                "        <span class=\"navbar-toggler-icon\"></span>\n" +
                "    </button>\n" +
                "\n" +
                "    <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\n" +
                "        <ul class=\"navbar-nav mr-auto\">\n" +
                "            <li class=\"nav-item active \">\n" +
                "                <a class=\"nav-link text-right\" href=\"employee.html\">Menu<span class=\"sr-only\">(current)</span></a>\n" +
                "            </li>\n" +
                "        </ul>\n" +
                "    </div>\n" +
                "</nav>\n" +
                "<script>\n" +
                "    function deleteAllCookies() {\n" +
                "    var cookies = document.cookie.split(\";\");\n" +
                "\n" +
                "    for (var i = 0; i < cookies.length; i++) {\n" +
                "        var cookie = cookies[i];\n" +
                "        var eqPos = cookie.indexOf(\"=\");\n" +
                "        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;\n" +
                "        document.cookie = name + \"=;expires=Thu, 01 Jan 1970 00:00:00 GMT\";\n" +
                "    }\n" +
                "}\n" +
                "</script>");
    }

    private void buildTable(StringBuilder sb, Query qry) {
        sb.append("<div style=\"margin: 25px \">\n" +
                "<table class=\"table text-black font-weight-bold\">\n" +
                    "<thead>\n" +
                        "<tr>\n" +
                            "<th scope=\"col\">Id</th>\n" +
                            "<th scope=\"col\">Amount</th>\n" +
                            "<th scope=\"col\">Reason</th>\n" +
                            "<th scope=\"col\">Status</th>\n" +
                            "<th scope=\"col\">Employee</th>\n" +
                        "</tr>" +
                    "</thead>\n" +
                    "<tbody>");
        List l =qry.list();
        for (Object o : l) {
            Request request = (Request) o;
            sb.append("<tr><th scope=\"row\">")
                    .append(request.getRequestId())
                    .append("</th><td>")
                    .append(request.getAmount())
                    .append("</td><td>")
                    .append(request.getReason())
                    .append("</td><td>")
                    .append(request.getStatus())
                    .append("</td><td>")
                    .append(request.getEmployeeId())
                    .append("</td></tr>");
        }
        sb.append("</tbody>\n" +
                "</table>\n" +
                "<div>");
    }
}
