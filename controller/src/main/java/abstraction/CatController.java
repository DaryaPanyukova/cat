package abstraction;

import dto.CatDto;

import java.util.List;
import java.util.Optional;

public interface CatController {
    CatDto createCat(CatDto catDto);

    CatDto updateCat(CatDto catDto);

    void deleteCat(CatDto catDto);

    List<CatDto> getAllCats();

    Optional<CatDto> findCatById(Long catId);

    List<CatDto> getCatFriends(Long catId);

    List<CatDto> getCatsByOwnerId(Long id);

    void makeFriends(Long firstCatId, Long secondCatId);

    void breakFriendship(Long firstCatId, Long secondCatId);
}
