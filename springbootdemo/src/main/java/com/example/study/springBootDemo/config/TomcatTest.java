//package com.example.study.springBootDemo.config;
//
//import org.apache.catalina.LifecycleEvent;
//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.core.AprLifecycleListener;
//import org.apache.catalina.core.StandardContext;
//import org.apache.catalina.startup.Tomcat;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//
//public class TomcatTest {
//
//
//    public static void main(String[] args) throws ServletException, LifecycleException {
//        start();
//    }
//
//    public static void start() throws ServletException, LifecycleException {
//        //创建Tomcat容器和设置端口
//        Tomcat tomcatServer = new Tomcat();
//        tomcatServer.setPort(9001);
//        tomcatServer.setBaseDir("/");
//
//        HttpServlet httpServlet = new HttpServlet() {
//            @Override
//            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//                resp.getWriter().print("success request!!!");
//            }
//        };
//
//        System.out.println(new File("src/main").getAbsolutePath());
//
//        StandardContext ctx = (StandardContext) tomcatServer.addWebapp("/demo", new File("src/main").getAbsolutePath());
//        ctx.setReloadable(false);
//
//        ctx.addLifecycleListener(new AprLifecycleListener(){
//            @Override
//            public void lifecycleEvent(LifecycleEvent event) {
//                super.lifecycleEvent(event);
//            }
//        });
//
//        tomcatServer.addServlet("/demo","testServlet",httpServlet);
//        ctx.addServletMappingDecoded("/test.do", "testServlet");
//        //启动容器
//        tomcatServer.start();
//        tomcatServer.getServer().await();
//
//    }
//}