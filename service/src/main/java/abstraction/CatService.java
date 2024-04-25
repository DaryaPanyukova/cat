package abstraction;

import model.CatModel;

import java.util.List;
import java.util.Optional;

public interface CatService {
    CatModel createCat(CatModel catModel);

    CatModel updateCat(CatModel catModel);

    void deleteCat(CatModel catModel);

    List<CatModel> getAllCats();

    Optional<CatModel> findCatById(long catId);

    List<CatModel> getCatFriends(long catId);

    void makeFriends(long firstCatId, long secondCatId);

    void breakFriendship(long firstCatId, long secondCatId);

    List<CatModel> getCatsByOwnerId(Long ownerId);
}
