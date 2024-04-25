package dao;

import abstraction.OwnerRepository;
import entity.OwnerEntity;
import jakarta.persistence.EntityNotFoundException;
import transaction.TransactionManager;

import java.util.List;
import java.util.Optional;

public class OwnerDao implements OwnerRepository {

    private final TransactionManager transactionManager;

    public OwnerDao(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<OwnerEntity> findById(Long id) {
        return transactionManager.doInTransaction(entityManager -> {
            OwnerEntity ownerEntity = entityManager.find(OwnerEntity.class, id);
            return Optional.ofNullable(ownerEntity);
        });
    }

    @Override
    public List<OwnerEntity> getAll() {
        return transactionManager.doInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT o FROM OwnerEntity o", OwnerEntity.class).getResultList();
        });
    }

    @Override
    public void save(OwnerEntity ownerEntity) {
        transactionManager.doInTransaction(entityManager -> {
            entityManager.persist(ownerEntity);
        });
    }

    @Override
    public void update(OwnerEntity ownerEntity) {
        transactionManager.doInTransaction(entityManager -> {
            entityManager.merge(ownerEntity);
        });
    }

    @Override
    public void delete(OwnerEntity ownerEntity) {
        transactionManager.doInTransaction(entityManager -> {
            OwnerEntity managedOwnerEntity = entityManager.find(OwnerEntity.class, ownerEntity.getId());
            if (managedOwnerEntity == null) {
                throw new EntityNotFoundException("Owner with id " + ownerEntity.getId() + " not found");
            }
            entityManager.remove(managedOwnerEntity);
        });
    }
}
