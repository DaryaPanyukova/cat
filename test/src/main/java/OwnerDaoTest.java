import dao.OwnerDao;
import entity.OwnerEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import transaction.TransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OwnerDaoTest {
    private OwnerDao ownerDao;
    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<OwnerEntity> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        TransactionManager transactionManager = new TransactionManager(entityManagerFactory);
        ownerDao = new OwnerDao(transactionManager);

        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.createQuery("SELECT o FROM OwnerEntity o", OwnerEntity.class)).thenReturn(query);
        Mockito.when(query.getResultList())
                .thenReturn(Arrays.asList(new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                        new OwnerEntity(2L, "John", LocalDate.of(1982, 10, 13), new ArrayList<>())));
        Mockito.when(entityManager.getTransaction())
                .thenReturn(Mockito.mock(jakarta.persistence.EntityTransaction.class));
    }

    @Test
    public void testGetAll() {
        List<OwnerEntity> owners = ownerDao.getAll();
        Assertions.assertEquals(2, owners.size());

        Assertions.assertEquals("Dasha", owners.getFirst().getName());
        Assertions.assertEquals(LocalDate.of(2004, 11, 15), owners.getFirst().getBirthDate());
        Assertions.assertEquals(0, owners.get(0).getCats().size());

        Assertions.assertEquals("John", owners.get(1).getName());
        Assertions.assertEquals(LocalDate.of(1982, 10, 13), owners.get(1).getBirthDate());
        Assertions.assertEquals(0, owners.get(1).getCats().size());
    }

    @Test
    public void testFindById() {
        Long ownerId = 1L;
        OwnerEntity expectedOwner = new OwnerEntity(ownerId, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>());
        Mockito.when(entityManager.find(OwnerEntity.class, ownerId)).thenReturn(expectedOwner);

        OwnerEntity actualOwner = ownerDao.findById(ownerId).orElse(null);

        Assertions.assertNotNull(actualOwner);
        Assertions.assertEquals(expectedOwner, actualOwner);
        Mockito.verify(entityManager, Mockito.times(1)).find(OwnerEntity.class, ownerId);
    }

    @Test
    public void testFindById_NotExist() {
        Long ownerId = 100L;
        Mockito.when(entityManager.find(OwnerEntity.class, ownerId)).thenReturn(null);

        OwnerEntity actualOwner = ownerDao.findById(ownerId).orElse(null);

        Assertions.assertNull(actualOwner);
        Mockito.verify(entityManager, Mockito.times(1)).find(OwnerEntity.class, ownerId);
    }

    @Test
    public void testSave() {
        OwnerEntity ownerToSave = new OwnerEntity(null, "Vasya", LocalDate.of(1990, 1, 1), new ArrayList<>());

        ownerDao.save(ownerToSave);

        Mockito.verify(entityManager, Mockito.times(1)).persist(ownerToSave);
    }

    @Test
    public void testUpdate() {
        OwnerEntity ownerToUpdate = new OwnerEntity(1L, "Updated Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>());

        ownerDao.update(ownerToUpdate);

        Mockito.verify(entityManager, Mockito.times(1)).merge(ownerToUpdate);
    }

    @Test
    public void testDelete() {
        Long ownerId = 1L;
        OwnerEntity ownerToDelete = new OwnerEntity(ownerId, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>());
        Mockito.when(entityManager.find(OwnerEntity.class, ownerId)).thenReturn(ownerToDelete);

        ownerDao.delete(ownerToDelete);

        Mockito.verify(entityManager, Mockito.times(1)).remove(ownerToDelete);
    }

    @Test
    public void testDelete_NotExist() {
        Long ownerId = 100L;
        OwnerEntity ownerToDelete = new OwnerEntity(ownerId, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>());
        Mockito.when(entityManager.find(OwnerEntity.class, ownerId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> ownerDao.delete(ownerToDelete));

        Mockito.verify(entityManager, Mockito.times(1)).find(OwnerEntity.class, ownerId);
        Mockito.verify(entityManager, Mockito.never()).remove(ownerToDelete);
    }
}
