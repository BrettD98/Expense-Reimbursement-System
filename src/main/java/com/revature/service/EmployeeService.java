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

    public boolean addEmployee(Employee employee){
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
        return true;
    }

    public boolean reqReimburs(int id, double amount){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();

        Request r = new Request();

        r.setEmployeeId(id);
        r.setAmount(amount);
        r.setStatus(Status.PENDING);

        session.save(r);
        t.commit();
        session.close();
        return true;
    }

    public String viewPast(int id){

        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where employeeid = " + id);

        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table\"><thead><tr><th scope=\"col\">Id</th><th scope=\"col\">Amount</th><th scope=\"col\">Status</th></thead><tbody>");
        List l = qry.list();
        for (Object o : l) {
            Request request = (Request) o;
            sb.append("<tr><th scope=\"row\">")
                    .append(request.getRequestId())
                    .append("</th><td>")
                    .append(request.getAmount())
                    .append("</td><td>")
                    .append(request.getStatus())
                    .append("</td>");
        }
        sb.append("</tbody></table>");

        t.commit();
        session.close();
        return sb.toString();
    }

    public String viewPending(int id){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where employeeid = " + id + "and status = 'PENDING'");

        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table\"><thead><tr><th scope=\"col\">Id</th><th scope=\"col\">Amount</th><th scope=\"col\">Status</th></thead><tbody>");
        List l =qry.list();
        for (Object o : l) {
            Request request = (Request) o;
            sb.append("<tr><th scope=\"row\">")
                    .append(request.getRequestId())
                    .append("</th><td>")
                    .append(request.getAmount())
                    .append("</td><td>")
                    .append(request.getStatus())
                    .append("</td>");
        }
        sb.append("</tbody></table>");

        t.commit();
        session.close();
        return sb.toString();
    }

    public String viewUnapproved(){
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");

        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        Transaction t = session.beginTransaction();
        Query qry = session.createQuery("from Request r where status = 'PENDING'");

        StringBuilder sb = new StringBuilder();
        sb.append("<table class=\"table\"><thead><tr><th scope=\"col\">Id</th><th scope=\"col\">Amount</th><th scope=\"col\">Status</th></thead><tbody>");
        List l =qry.list();
        for (Object o : l) {
            Request request = (Request) o;
            sb.append("<tr><th scope=\"row\">")
                    .append(request.getRequestId())
                    .append("</th><td>")
                    .append(request.getAmount())
                    .append("</td><td>")
                    .append(request.getStatus())
                    .append("</td>");
        }
        sb.append("</tbody></table>");

        t.commit();
        session.close();
        return sb.toString();
    }

    public boolean approve(int id){
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

            return true;
        }
        return false;
    }

    public boolean deny(int id){

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

            return true;
        }
        return false;
    }
}
