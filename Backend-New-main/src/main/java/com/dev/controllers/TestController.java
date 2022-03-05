package com.dev.controllers;

import com.dev.Persist;
import com.dev.objects.*;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;


@RestController
public class TestController {


    @Autowired
    private Persist persist;

    @PostConstruct
    private void init() {

    }

    @RequestMapping(value = "first-login", method = RequestMethod.GET)
    public boolean firstLogin(String token) {
        return persist.firstLogin(token);
    }

    @RequestMapping(value = "sign-in")
    public String logIn (String username , String password){
        return persist.logIn(username,password);
    }
    @RequestMapping(value = "create-account", method = RequestMethod.POST))
    public boolean createAccount(String username, String password) {
        boolean success = false;
        boolean alreadyExists = persist.getTokenByUsernameAndPassword(username, password) != "This account not exist!";
        if (!alreadyExists) {
            UserObject userObject = new UserObject();
            userObject.setUsername(username);
            userObject.setPassword(password);
            String hash = Utils.createHash(username, password);
            userObject.setToken(hash);
            userObject.setFirstLogin(true);
            success = persist.createAccount(userObject);
        }
        System.out.println(success);
        return success;
    }

    @RequestMapping(value = "get-username-by-token" , method = RequestMethod.GET)
    public String getUsernameByToken(String token) {
        return persist.getUsernameByToken(token);
    }

    @RequestMapping(value = "user-exist" , method = RequestMethod.GET)
    public boolean doseUserExist(String username) {
        return persist.doseUserExist(username);
    }

    @RequestMapping("get-organizations" , method = RequestMethod.GET)
    public List<OrganizationObject> getOrganizations() {
        return persist.getOrganizations();
    }

    @RequestMapping("get-organizations-of-user", method = RequestMethod.GET)
    public List<Organization> getOrganizationOfUser(String token) {
        return persist.getUserOrganizations(token);
    }

    @RequestMapping(value = "get-user-sales", method = RequestMethod.GET)
    public List<SaleObject> getuserSales(String token) {
        return this.persist.getuserSales(token);
    }

    @RequestMapping(value = "get-all-stores", method = RequestMethod.GET)
    public List<storeObject> getStores() {
        return this.persist.getStores();
    }

    @RequestMapping(value = "get-all-searches", method = RequestMethod.GET)
    public List<SaleObject> getAllSearches(String text) {return this.persist.getAllSearches(text);}

    @RequestMapping(value = "get-store-name", method = RequestMethod.GET)
    public String getStoreName(Integer id) {
        return this.persist.getStoreName(id);
    }

    @RequestMapping(value = "get-store-sales", method = RequestMethod.GET)
    public List<SaleObject> getStoreSales(Integer id) {
        return this.persist.getStoreSales(id);
    }

    @RequestMapping(value = "add-organization-to-user", method = RequestMethod.POST)
    public void addOrganizationToUser(String token, int organId) {
        persist.addOrganizationToUser(token, organId);}

    @RequestMapping(value = "delete-organization-from-user", method = RequestMethod.POST)
    public void deleteOrganizationFromUser(String token, int organId) {
        persist.deleteOrganizationFromUser(token, organId);}
}