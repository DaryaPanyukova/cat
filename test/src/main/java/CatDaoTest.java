import cat.CatColor;
import dao.CatDao;
import entity.CatEntity;
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


public class CatDaoTest {
    private CatDao catDao;
    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<CatEntity> query;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        TransactionManager transactionManager = new TransactionManager(entityManagerFactory);
        catDao = new CatDao(transactionManager);
        Mockito.when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        Mockito.when(entityManager.createQuery("SELECT c FROM CatEntity c", CatEntity.class)).thenReturn(query);
        Mockito.when(query.getResultList())
                .thenReturn(Arrays.asList(new CatEntity(1L,
                                "Murzik",
                                LocalDate.of(2018, 1, 1),
                                "British Shorthair",
                                CatColor.BLACK,
                                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                                new ArrayList<>()),
                        new CatEntity(2L,
                                "Barsik",
                                LocalDate.of(2019, 1, 1),
                                "Scottish Fold",
                                CatColor.WHITE,
                                new OwnerEntity(2L, "John", LocalDate.of(1982, 10, 13), new ArrayList<>()),
                                new ArrayList<>())));
        Mockito.when(entityManager.getTransaction())
                .thenReturn(Mockito.mock(jakarta.persistence.EntityTransaction.class));
    }

    @Test
    public void testGetAll() {
        List<CatEntity> cats = catDao.getAll();
        Assertions.assertEquals(2, cats.size());

        Assertions.assertEquals("Murzik", cats.getFirst().getName());
        Assertions.assertEquals(LocalDate.of(2018, 1, 1), cats.getFirst().getBirthDate());
        Assertions.assertEquals("British Shorthair", cats.getFirst().getBreed());
        Assertions.assertEquals(CatColor.BLACK, cats.getFirst().getColor());
        Assertions.assertEquals("Dasha", cats.get(0).getOwner().getName());
        Assertions.assertEquals(0, cats.get(0).getFriends().size());

        Assertions.assertEquals("Barsik", cats.get(1).getName());
        Assertions.assertEquals(LocalDate.of(2019, 1, 1), cats.get(1).getBirthDate());
        Assertions.assertEquals("Scottish Fold", cats.get(1).getBreed());
        Assertions.assertEquals(CatColor.WHITE, cats.get(1).getColor());
        Assertions.assertEquals("John", cats.get(1).getOwner().getName());
        Assertions.assertEquals(0, cats.get(1).getFriends().size());
    }

    @Test
    public void testFindById() {
        Long catId = 1L;
        CatEntity expectedCat = new CatEntity(catId,
                "Murzik",
                LocalDate.of(2018, 1, 1),
                "British Shorthair",
                CatColor.BLACK,
                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                new ArrayList<>());
        Mockito.when(entityManager.find(CatEntity.class, catId)).thenReturn(expectedCat);

        CatEntity actualCat = catDao.findById(catId).orElse(null);

        Assertions.assertNotNull(actualCat);
        Assertions.assertEquals(expectedCat, actualCat);
        Mockito.verify(entityManager, Mockito.times(1)).find(CatEntity.class, catId);
    }

    @Test
    public void testFindById_NotExist() {
        Long catId = 100L;
        Mockito.when(entityManager.find(CatEntity.class, catId)).thenReturn(null);

        CatEntity actualCat = catDao.findById(catId).orElse(null);

        Assertions.assertNull(actualCat);
        Mockito.verify(entityManager, Mockito.times(1)).find(CatEntity.class, catId);
    }

    @Test
    public void testSave() {
        CatEntity catToSave = new CatEntity(null,
                "Vasya",
                LocalDate.of(2020, 1, 1),
                "Siberian",
                CatColor.GRAY,
                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                new ArrayList<>());

        catDao.save(catToSave);

        Mockito.verify(entityManager, Mockito.times(1)).persist(catToSave);
    }

    @Test
    public void testUpdate() {
        CatEntity catToUpdate = new CatEntity(1L,
                "Updated Murzik",
                LocalDate.of(2018, 1, 1),
                "British Shorthair",
                CatColor.BLACK,
                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                new ArrayList<>());

        catDao.update(catToUpdate);

        Mockito.verify(entityManager, Mockito.times(1)).merge(catToUpdate);
    }

    @Test
    public void testDelete() {
        Long catId = 1L;
        CatEntity catToDelete = new CatEntity(catId,
                "Murzik",
                LocalDate.of(2018, 1, 1),
                "British Shorthair",
                CatColor.BLACK,
                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                new ArrayList<>());
        Mockito.when(entityManager.find(CatEntity.class, catId)).thenReturn(catToDelete);

        catDao.delete(catToDelete);

        Mockito.verify(entityManager, Mockito.times(1)).remove(catToDelete);
    }

    @Test
    public void testAddFriend() {
        Long catId = 1L;
        Long friendId = 2L;
        CatEntity cat = new CatEntity(catId,
                "Murzik",
                LocalDate.of(2018, 1, 1),
                "British Shorthair",
                CatColor.BLACK,
                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                new ArrayList<>());
        CatEntity friend = new CatEntity(friendId,
                "Barsik",
                LocalDate.of(2019, 1, 1),
                "Scottish Fold",
                CatColor.WHITE,
                new OwnerEntity(2L, "John", LocalDate.of(1982, 10, 13), new ArrayList<>()),
                new ArrayList<>());
        Mockito.when(entityManager.find(CatEntity.class, catId)).thenReturn(cat);
        Mockito.when(entityManager.find(CatEntity.class, friendId)).thenReturn(friend);

        catDao.addFriend(catId, friendId);
        catDao.addFriend(friendId, catId);

        Assertions.assertTrue(cat.getFriends().contains(friend));
        Assertions.assertTrue(friend.getFriends().contains(cat));
    }

    @Test
    public void testRemoveFriend() {
        Long catId = 1L;
        Long friendId = 2L;
        CatEntity cat = new CatEntity(catId,
                "Murzik",
                LocalDate.of(2018, 1, 1),
                "British Shorthair",
                CatColor.BLACK,
                new OwnerEntity(1L, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>()),
                new ArrayList<>());
        CatEntity friend = new CatEntity(friendId,
                "Barsik",
                LocalDate.of(2019, 1, 1),
                "Scottish Fold",
                CatColor.WHITE,
                new OwnerEntity(2L, "John", LocalDate.of(1982, 10, 13), new ArrayList<>()),
                new ArrayList<>());


        Mockito.when(entityManager.find(CatEntity.class, catId)).thenReturn(cat);
        Mockito.when(entityManager.find(CatEntity.class, friendId)).thenReturn(friend);

        catDao.addFriend(catId, friendId);
        catDao.addFriend(friendId, catId);
        catDao.removeFriend(catId, friendId);
        catDao.removeFriend(friendId, catId);

        Assertions.assertFalse(cat.getFriends().contains(friend));
        Assertions.assertFalse(friend.getFriends().contains(cat));
    }

    @Test
    public void testGetCatsByOwnerId() {
        Long ownerId = 1L;
        OwnerEntity owner = new OwnerEntity(ownerId, "Dasha", LocalDate.of(2004, 11, 15), new ArrayList<>());

        var cat1 = new CatEntity(1L,
                "Murzik",
                LocalDate.of(2018, 1, 1),
                "British Shorthair",
                CatColor.BLACK,
                owner,
                new ArrayList<>());
        var cat2 = new CatEntity(2L,
                "Barsik",
                LocalDate.of(2019, 1, 1),
                "Scottish Fold",
                CatColor.WHITE,
                owner,
                new ArrayList<>());

        owner.getCats().add(cat1);
        owner.getCats().add(cat2);

        Mockito.when(entityManager.find(OwnerEntity.class, ownerId)).thenReturn(owner);

        List<CatEntity> cats = catDao.getCatsByOwnerId(ownerId);

        Assertions.assertEquals(2, cats.size());

        Assertions.assertEquals("Murzik", cats.getFirst().getName());
        Assertions.assertEquals(LocalDate.of(2018, 1, 1), cats.getFirst().getBirthDate());
        Assertions.assertEquals("British Shorthair", cats.getFirst().getBreed());
        Assertions.assertEquals(CatColor.BLACK, cats.getFirst().getColor());
        Assertions.assertEquals("Dasha", cats.get(0).getOwner().getName());
        Assertions.assertEquals(0, cats.get(0).getFriends().size());

        Assertions.assertEquals("Barsik", cats.get(1).getName());
        Assertions.assertEquals(LocalDate.of(2019, 1, 1), cats.get(1).getBirthDate());
        Assertions.assertEquals("Scottish Fold", cats.get(1).getBreed());
        Assertions.assertEquals(CatColor.WHITE, cats.get(1).getColor());
        Assertions.assertEquals("Dasha", cats.get(1).getOwner().getName());
        Assertions.assertEquals(0, cats.get(1).getFriends().size());
    }

    @Test
    public void testGetCatsByOwnerId_NotExist() {
        Long ownerId = 100L;
        Mockito.when(entityManager.find(OwnerEntity.class, ownerId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () -> catDao.getCatsByOwnerId(ownerId));

        Mockito.verify(entityManager, Mockito.times(1)).find(OwnerEntity.class, ownerId);
    }
}


