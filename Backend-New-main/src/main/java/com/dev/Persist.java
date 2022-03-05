package com.dev;
import com.dev.objects.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;



import static com.dev.utils.Utils.createHash;

@Component
public class Persist {
    private Connection connection;
    private final SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    @PostConstruct
    public void createConnectionToDatabase() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ashcollage?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=IST", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createAccount(UserObject userObject) {
        boolean success = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(userObject);
        transaction.commit();
        session.close();
        if (userObject.getId() > 0) {
            success = true;
        }
        return success;
    }

    public String logIn(String username, String password) {
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session.createQuery("FROM UserObject u WHERE u.username =:username AND u.password =:password")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        session.close();
        if (userObject != null)
            return userObject.getToken();
        else {
            return null;
        }
    }



    public UserObject getUserIdByToken(String token) {
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session
                .createQuery("FROM UserObject u WHERE u.token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        return userObject;
    }

public String getUsernameById(int id) {
        String name = "";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT username FROM users WHERE id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                name = resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;}

public String getUsernameByToken(String token) {
        String username = null;
        Session session = sessionFactory.openSession();
        UserObject userObject = (UserObject) session
                .createQuery("FROM UserObject WHERE token = :token")
                .setParameter("token", token)
                .uniqueResult();
        session.close();
        if (userObject != null) {
            username = userObject.getUsername();
        }
        return username;

    }


public boolean doseUserExist(String username) {
        boolean userExist = false;

        try {
        PreparedStatement preparedStatement = this.connection.prepareStatement(
        "SELECT * FROM users WHERE username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
        userExist = true;
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userExist;
    }

public boolean firstLogin(String token){
        boolean flag=false;
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();

        Integer id=getUserIdByToken(token);
        if(id!=null){
            UserObject userObject=session
                .load(UserObject.class,getUserIdByToken(token));
        if(userObject!=null){
            check=userObject.isFirstLogin();
            userObject.setFirstLogin(false);
            session.update(userObject);
            transaction.commit();
        }
        }
        session.close();
        return check;
    }

public Integer getUserIdByUsername(String username) {
        Integer id = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT id FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

public int wrongLoginTry(String username) {
        int wrongTry = 0;

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT loginTries  FROM users WHERE username =?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                wrongTry = resultSet.getInt("loginTries");
            }
        }catch (SQLException e) {
            e.printStackTrace();}
        return wrongTry;
    }

public void addWrongLogin(String username) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE users\n" +
                    "SET loginTries = loginTries + 1\n WHERE username = ?");
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public List<OrganizationObject> getOrganizations() {
        Session session = sessionFactory.openSession();
        List<OrganizationObject> organizations = session
                .createQuery("FROM OrganizationObject")
                .list();
        return organizations;
        }
public List<StoreObject> getStores() {
        Session session = sessionFactory.openSession();
        List<StoreObject> stores = session
                .createQuery("SELECT s from StoreObject s", StoreObject.class)
                .getResultList();
        session.close();
        return stores;
        }
public List<OrganizationObject> getUserOrganizationsgetUserOrganizations(String token) {
        Session session = sessionFactory.openSession();
        List<UserToOrganization> userToOrganizations = session
                .createQuery("FROM UserToOrganization us WHERE us.UserObject.token = :token")
                .setParameter("token", token)
        .list();
        List<OrganizationObject> organizations = new ArrayList<Organization>();
        for (UserToOrganization userToOrganization : userToOrganizations)
            organizations.add(userToOrganization.getOrganization());
        return organizations;
        }

public List<SaleObject> getuserSales(String token) {
        Session session = sessionFactory.openSession();
        List<SaleObject> sales = session
                .createQuery("SELECT s FROM SaleObject s", SaleObject.class)
                .getResultList();
        List<SaleObject> openSales = new ArrayList<>();
        for (SaleObject sale : sales) {
            if (isSaleOpen(token, sale.getId())) {
                openSales.add(sale);}
        }
        session.close();
        return openSales;
        }


public String getStoreName(Integer id) {
        Session session = sessionFactory.openSession();
        StoreObject store = (StoreObject) session
                .createQuery("from StoreObject where id =: id")
                .setParameter("id", id)
                .uniqueResult();
        session.close();
        return store.getName();

    }
public List<SaleObject> getStoreSales(Integer id) {
        Session session = sessionFactory.openSession();
        List<SaleObject> sales =  session
                .createQuery("from SaleObject where store.id =: id")
                .setParameter("id", id)
                .getResultList();
        session.close();
        return sales;
        }

public void  deleteOrganizationFromUser(String token, int organId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject user = (UserObject) session
                .createQuery("FROM UserObject WHERE token = :token" )
                .setParameter("token", token).getSingleResult();
        OrganizationObject organization = session
                .load(OrganizationObject.class, organId);
        if (user != null && organization != null) {
            user.getOrganizations().delete(organization);
            organization.getUsers().delete(user);
        }
        transaction.commit();
        session.close();
        }

public void addOrganizationToUser(String token, int organId){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserObject user = (UserObject) session
                .createQuery("FROM UserObject WHERE token = :token" )
                .setParameter("token", token).getSingleResult();
        OrganizationObject organization = session
                .load(OrganizationObject.class, id);
        if (user != null && organization != null) {
            user.getOrganizations().add(organization);
            organization.getUsers().add(user);
        }
        transaction.commit();
        session.close();
    }

}