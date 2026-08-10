package com.example.DAO;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Configuration.HibernateConfig;
import com.example.Entity.User;

public class UserDAOImpl implements UserDAO{
	
	private static final Logger logger = LoggerFactory.getLogger( UserDAOImpl.class );

	@Override
	public void save(User user) {
		Transaction transaction = null;
		try(Session session = HibernateConfig.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.persist( user );
			transaction.commit();
			logger.info("Пользователь успешно сохранен: {}", user.getName());
		}
		catch (Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
			logger.error("Ошибка при сохранении пользователя", e);
		}
		
	}

	@Override
	public Optional<User> findById(Long id) {
		try(Session session = HibernateConfig.getSessionFactory().openSession()) {
			User user = session.get( User.class, id );
			return Optional.ofNullable( user );
		}
		catch (Exception e) {
			logger.error("Ошибка при поиске пользователя с ID: {}", id, e);
			return Optional.empty();
		}
	}

	@Override
	public List<User> findAll() {
		try(Session session = HibernateConfig.getSessionFactory().openSession()) {
			return session.createQuery( "From User", User.class ).list();
		}
		catch (Exception e) {
			logger.error("Ошибка при получении списка пользователей", e);
			return List.of();
		}
	}

	@Override
	public void update(User user) {
		Transaction transaction = null;
		try(Session session = HibernateConfig.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.merge( user );
			transaction.commit();
			logger.info("Пользователь с ID {} успешно обновлен", user.getId());
		}
		catch (Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
			logger.error("Ошибка при обновлении пользователя с ID: {}", user.getId(), e);
		}
	}

	@Override
	public void delete(Long id) {
		Transaction transaction = null;
		try(Session session = HibernateConfig.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			User user = session.get( User.class, id );
			if(user!=null) {
				session.remove( user );
				logger.info("Пользователь с ID {} успешно удален", id);
			}else {
				logger.warn("Попытка удалить несуществующего пользователя с ID {}", id);
			}
			transaction.commit();
		}
		catch (Exception e) {
			if(transaction!=null) {
				transaction.rollback();
			}
			logger.error("Ошибка при удалении пользователя с ID: {}", id, e);
		}
	}

}
