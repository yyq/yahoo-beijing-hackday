package com.tp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: naruto
 * Date: 13-5-19
 * Time: 上午1:27
 * To change this template use File | Settings | File Templates.
 */
public class MessageServelet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String message = new String(request.getParameter("message").getBytes("ISO-8859-1"), "UTF-8");
        Connection conn = null;
        PreparedStatement pr = null;
        try {
            conn = ConnUtil.getConnection();
            pr = conn.prepareStatement("insert into message (x,y,message,time) values (?,?,?,?)");
            pr.setString(1, x);
            pr.setString(2, y);
            pr.setString(3, message);
            pr.setString(4,String.valueOf(new Date().getTime()));
            pr.execute();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
