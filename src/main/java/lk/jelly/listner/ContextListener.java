package lk.jelly.listner;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lk.jelly.db.ConnectionPool;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionPool dbPool = new ConnectionPool(3);
        sce.getServletContext().setAttribute("pool", dbPool);
    }
}
