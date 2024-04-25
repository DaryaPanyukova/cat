package dao;


import abstraction.CatRepository;
import entity.CatEntity;
import entity.OwnerEntity;
import jakarta.persistence.EntityNotFoundException;
import transaction.TransactionManager;

import java.util.List;
import java.util.Optional;

public class CatDao implements CatRepository {

    private final TransactionManager transactionManager;

    public CatDao(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<CatEntity> findById(Long id) {
        return transactionManager.doInTransaction(entityManager -> {
            CatEntity catEntity = entityManager.find(CatEntity.class, id);
            return Optional.ofNullable(catEntity);
        });
    }

    @Override
    public List<CatEntity> getAll() {
        return transactionManager.doInTransaction(entityManager -> {
            return entityManager.createQuery("SELECT c FROM CatEntity c", CatEntity.class).getResultList();
        });
    }

    @Override
    public void save(CatEntity catEntity) {
        transactionManager.doInTransaction(entityManager -> {
            entityManager.persist(catEntity);
        });
    }

    @Override
    public void update(CatEntity catEntity) {
        transactionManager.doInTransaction(entityManager -> {
            entityManager.merge(catEntity);
        });
    }

    @Override
    public void delete(CatEntity catEntity) {
        transactionManager.doInTransaction(entityManager -> {
            CatEntity managedCatEntity = entityManager.find(CatEntity.class, catEntity.getId());
            if (managedCatEntity == null) {
                throw new EntityNotFoundException("Cat with id " + catEntity.getId() + " not found");
            }
            entityManager.remove(managedCatEntity);
        });
    }

    @Override
    public void addFriend(Long catId, Long friendId) {
        transactionManager.doInTransaction(entityManager -> {
            CatEntity catEntity = entityManager.find(CatEntity.class, catId);
            if (catEntity == null) {
                throw new EntityNotFoundException("Cat with id " + catId + " not found");
            }
            CatEntity friendEntity = entityManager.find(CatEntity.class, friendId);
            if (friendEntity == null) {
                throw new EntityNotFoundException("Cat with id " + friendId + " not found");
            }
            catEntity.getFriends().add(friendEntity);
        });
    }

    @Override
    public void removeFriend(Long catId, Long friendId) {
        transactionManager.doInTransaction(entityManager -> {
            CatEntity catEntity = entityManager.find(CatEntity.class, catId);
            if (catEntity == null) {
                throw new EntityNotFoundException("Cat with id " + catId + " not found");
            }
            CatEntity friendEntity = entityManager.find(CatEntity.class, friendId);
            if (friendEntity == null) {
                throw new EntityNotFoundException("Cat with id " + friendId + " not found");
            }
            catEntity.getFriends().remove(friendEntity);
        });
    }

    @Override
    public List<CatEntity> getCatFriends(Long catId) {
        return transactionManager.doInTransaction(entityManager -> {
            CatEntity catEntity = entityManager.find(CatEntity.class, catId);
            if (catEntity == null) {
                throw new EntityNotFoundException("Cat with id " + catId + " not found");
            }
            return catEntity.getFriends();
        });
    }

    @Override
    public List<CatEntity> getCatsByOwnerId(Long ownerId) {
        return transactionManager.doInTransaction(entityManager -> {
            OwnerEntity ownerEntity = entityManager.find(OwnerEntity.class, ownerId);
            if (ownerEntity == null) {
                throw new EntityNotFoundException("Owner with id " + ownerId + " not found");
            }
            return ownerEntity.getCats();
        });
    }
}
