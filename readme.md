# Expense Reimbursement System (ERS)

The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time.
All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests.
Finance managers can log in and view all reimbursement requests and past history for all employees in the company.
Finance managers are authorized to approve and deny requests for expense reimbursement.

## Features
* Login for both Employees and Managers
* Create reimburement ticket
* Display active tickets for Employees
* Display all active ticketes for Managers
* Accept or Reject active tickets

## Getting Started
* Download this repo
* in hibernate.cfg.xml
  * Ensure the conection.url property is pointing to a useable database
  * Ensure the username and password match your mysql username and password
  * You can also turn show_sql to false iy you want.
* Set up and run your Tomcat in your prefered IDE
* Go to http://localhost:[your-port]/[context-path-from-tomcat]/login.html
* Attempt login to create the tables in the database
* Manually add an Employee and Manager to the database (using cmd or your favorite mysql workbench)
* Finally go to http://localhost:[your-port]/[context-path-from-tomcat]/index.html
* Everything should be set up and working.

## Usage
After Setup, you should be able to Login to either the Employee account or Manager Account.
After Login, you are pressented with four buttons:
  * View Past Reimburements - Shows all past reimburements for the employee logged in.
  * View Pending Reimbursements- Shows all pending reimburements for the employee logged in.
  * View Unapproved Reimburements- Shows all unapproved reimburements to the Manager logged in.
    * Here the Manager can approve or reject reimburements.
  * Request Reimburements- Shows a form to request a reimburement.

On Navbar:
  * Logo - redirects to home page. (Logs user out if logged in)
  * Login - redirects to login page.
  * Logout - logs out user and redirects to home page.
  * Menu - returns to the menu with the four buttons.

## Technologies Used
* Java
* Maven
* TomCat
* MySql
* HTML
* CSS
