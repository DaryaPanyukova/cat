package transaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;

public class TransactionManager {

    private final EntityManagerFactory entityManagerFactory;

    public TransactionManager(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> T doInTransaction(TransactionAction<T> action) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T result = action.execute(entityManager);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new EntityNotFoundException(e);
        } finally {
            entityManager.close();
        }
    }

    public void doInTransaction(TransactionActionNoResult action) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.execute(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new EntityNotFoundException(e);
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    public interface TransactionAction<T> {
        T execute(EntityManager entityManager);
    }

    @FunctionalInterface
    public interface TransactionActionNoResult {
        void execute(EntityManager entityManager);
    }
}
