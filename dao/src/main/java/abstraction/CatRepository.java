package abstraction;

import entity.CatEntity;

import java.util.List;

public interface CatRepository extends Repository<CatEntity> {
    void addFriend(Long catId, Long friendId);

    void removeFriend(Long catId, Long friendId);

    List<CatEntity> getCatFriends(Long catId);

    List<CatEntity> getCatsByOwnerId(Long ownerId);
}