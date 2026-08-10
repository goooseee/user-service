package com.example.Configuration;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.example.Entity.User;

public class HibernateConfig {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	
	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.addAnnotatedClass( User.class );
			return configuration.buildSessionFactory();
		}catch (Exception ex) {
			System.err.println("Ошибка инициализации SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}
