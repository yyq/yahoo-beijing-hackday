package com.tp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: naruto
 * Date: 13-5-19
 * Time: 上午3:10
 * To change this template use File | Settings | File Templates.
 */
public class GetMessageServlet extends HttpServlet {
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        double EARTH_RADIUS = 6378.137;
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String x = req.getParameter("x");
        String y = req.getParameter("y");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        Connection connection = null;
        PreparedStatement pr = null;
        ResultSet rs = null;
        try {
            connection = ConnUtil.getConnection();
            pr = connection.prepareStatement("select * from message order by time desc");
            rs = pr.executeQuery();
            List<Point> list = new ArrayList<Point>();
            while (rs.next()) {
                String x1 = rs.getString("x");
                String y1 = rs.getString("y");
                if (x1 == null || y1 == null) {
                    continue;
                }
                String message = rs.getString("message");
                String time = rs.getString("time");
                SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = dateformat1.format(new Date(Long.valueOf(time)));
                double distance = GetDistance(Double.valueOf(x1), Double.valueOf(y1), Double.valueOf(x), Double.valueOf(y));
                if (distance <= 1) {
                    list.add(new Point(x, y, time, message));
                }
            }
            StringBuilder json = new StringBuilder();
            json.append("{\"key\":[");
            int i = 0;
            if (list.size() > 1) {
                for (i = 0; i < list.size() - 1; i++) {
                    json.append("{\"x\":\"" + list.get(i).getX() + "\",\"y\":" + list.get(i).getY() + ",\"time\":\"" + list.get(i).getTime() + "\",\"message\":\"" + list.get(i).getMessage() + "\"}");
                    json.append(",");
                }
            }
            if (list.size() > 0) {
                json.append("{\"x\":\"" + list.get(i).getX() + "\",\"y\":" + list.get(i).getY() + ",\"time\":\"" + list.get(i).getTime() + "\",\"message\":\"" + list.get(i).getMessage() + "\"}");
                json.append("]}");
            }
            pw.write(json.toString());
            pw.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pr != null) {
                    pr.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    class Point {
        private String x;
        private String y;
        private String time;
        private String message;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        Point(String x, String y, String time, String message) {
            this.x = x;
            this.y = y;
            this.time = time;
            this.message = message;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }
}
