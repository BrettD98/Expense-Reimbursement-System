package com.revature.util;

import com.revature.service.EmployeeService;

public class ServiceFactory {
    private static EmployeeService service;

    public static EmployeeService getService(){
        if(service == null){
            service = new EmployeeService();
        }
        return service;
    }
}
