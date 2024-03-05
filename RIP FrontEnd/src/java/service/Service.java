/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author jonty
 */
public class Service {
    private static final RipServiceInterface service;
    static{
        service=new RipServiceImplementation();
    }
    public static RipServiceInterface getService() {
        return service;
    }
}
