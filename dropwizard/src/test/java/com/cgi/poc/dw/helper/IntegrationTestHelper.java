package com.cgi.poc.dw.helper;

import com.cgi.poc.dw.CgiPocConfiguration;
import com.cgi.poc.dw.dao.HibernateUtil;
import com.cgi.poc.dw.dao.model.EventNotification;
import com.cgi.poc.dw.rest.model.LoginUserDto;
import com.cgi.poc.dw.rest.resource.UserRegistrationResourceIntegrationTest;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

public class IntegrationTestHelper {
  public static void deleteAllEventNotfications()
      throws SQLException {
    Connection sqlConnection = null;
    
    
    try {
      SessionFactory sessionFactory = HibernateUtil.getInstance().getSessionFactory();
      sqlConnection = ((SessionImpl) sessionFactory.openSession()).connection();
      Statement st = sqlConnection.createStatement();
      int res = st.executeUpdate("delete from event_notification_zipcode");
      res = st.executeUpdate("delete from event_notification");
      sqlConnection.commit();
    } catch (HibernateException | SQLException ex) {
      sqlConnection.rollback();
      ex.printStackTrace();
    }
  }   
   public static void addEventNotfication(EventNotification event)
      throws SQLException {
    Connection sqlConnection = null;
    
    
    try {
      SessionFactory sessionFactory = HibernateUtil.getInstance().getSessionFactory();
      sqlConnection = ((SessionImpl) sessionFactory.openSession()).connection();
      Statement st = sqlConnection.createStatement();
      int res = st.executeUpdate("INSERT INTO event_notification (id, description," +
           "user_id,type,geometry,url1,url12,citizensAffected) VALUES (" 
            + event.getId() + " ," 
            + " '" + event.getDescription() + "',"
            + event.getUserId().getId() + " ," 
            + " '" + event.getType() + "',"
            + " '" + event.getGeometry() + "',"
            + " '" + event.getUrl1() + "',"
            + " '" + event.getUrl12() + "',"
            + event.getCitizensAffected()+ " )"); 


      sqlConnection.commit();
    } catch (HibernateException | SQLException ex) {
      sqlConnection.rollback();
      ex.printStackTrace();
    }
  }
  public static void signupAdminUser()
      throws SQLException {
    Connection sqlConnection = null;
    try {
      SessionFactory sessionFactory = HibernateUtil.getInstance().getSessionFactory();
      sqlConnection = ((SessionImpl) sessionFactory.openSession()).connection();
      Statement st = sqlConnection.createStatement();
      int res = st.executeUpdate(
          "INSERT INTO user (id, first_name, last_name, email, password, phone, zip_code, role, latitude, longitude)\n"
              + "VALUES ( 100,\n"
              + "'john',\n"
              + "'smith',\n"
              + "'admin100@cgi.com',\n"
              + "'518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af',\n"
              + "'1234567890',\n"
              + "'95814',\n"
              + "'ADMIN',\n"
              + "38.5824933,\n"
              + "-121.4941738\n"
              + ")");

      sqlConnection.commit();
    } catch (Exception ex) {
      sqlConnection.rollback();
      ex.printStackTrace();
    }
  }

  public static String getAuthToken(String email, String password,
      DropwizardAppRule<CgiPocConfiguration> RULE) {
    Client client = new JerseyClientBuilder().build();
    LoginUserDto loginUserDto = new LoginUserDto();
    loginUserDto.setEmail(email);
    loginUserDto.setPassword(password);
    Response loginResponse = client
        .target(String.format("http://localhost:%d/login", RULE.getLocalPort())).request()
        .post(Entity.json(loginUserDto));
    JSONObject loginResponseJo = null;
    try {
      loginResponseJo = new JSONObject(loginResponse.readEntity(String.class));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    String authToken = loginResponseJo.optString("authToken");
    Assert.assertTrue(!StringUtils.isBlank(authToken));
    return authToken;
  }

  public static void cleanDbState() {
    try {
      SessionFactory sessionFactory = HibernateUtil.getInstance().getSessionFactory();
      Connection sqlConnection = ((SessionImpl) sessionFactory.openSession()).connection();
      Statement st = sqlConnection.createStatement();
      int res = st.executeUpdate("delete from event_notification_zipcode");
      res = st.executeUpdate("delete from event_notification");
      res = st.executeUpdate("delete from user_notification");
      res = st.executeUpdate("delete from user");

      sqlConnection.commit();
    } catch (Exception ex) {
      Logger.getLogger(UserRegistrationResourceIntegrationTest.class.getName())
          .log(Level.SEVERE, null, ex);
    }

  }

}
