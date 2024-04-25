package controller;

import abstraction.CatController;
import abstraction.CatService;
import dto.CatDto;
import mapper.CatModelDtoMapper;
import model.CatModel;

import java.util.List;
import java.util.Optional;

public class CatControllerImpl implements CatController {

    private final CatService catService;

    public CatControllerImpl(CatService catService) {
        this.catService = catService;
    }

    @Override
    public CatDto createCat(CatDto catDto) {
        CatModel catModel = CatModelDtoMapper.dtoToModel(catDto);
        CatModel createdCatModel = catService.createCat(catModel);
        return CatModelDtoMapper.modelToDto(createdCatModel);
    }

    @Override
    public CatDto updateCat(CatDto catDto) {
        CatModel catModel = CatModelDtoMapper.dtoToModel(catDto);
        CatModel updatedCatModel = catService.updateCat(catModel);
        return CatModelDtoMapper.modelToDto(updatedCatModel);
    }

    @Override
    public void deleteCat(CatDto catDto) {
        CatModel catModel = CatModelDtoMapper.dtoToModel(catDto);
        catService.deleteCat(catModel);
    }

    @Override
    public List<CatDto> getAllCats() {
        List<CatModel> catModels = catService.getAllCats();
        return CatModelDtoMapper.modelsToDtos(catModels);
    }

    @Override
    public Optional<CatDto> findCatById(Long catId) {
        Optional<CatModel> catModelOptional = catService.findCatById(catId);
        return catModelOptional.map(CatModelDtoMapper::modelToDto);
    }

    @Override
    public List<CatDto> getCatFriends(Long catId) {
        List<CatModel> catFriendModels = catService.getCatFriends(catId);
        return CatModelDtoMapper.modelsToDtos(catFriendModels);
    }

    @Override
    public void makeFriends(Long firstCatId, Long secondCatId) {
        catService.makeFriends(firstCatId, secondCatId);
    }

    @Override
    public void breakFriendship(Long firstCatId, Long secondCatId) {
        catService.breakFriendship(firstCatId, secondCatId);
    }
    @Override
    public List<CatDto> getCatsByOwnerId(Long id) {
        List<CatModel> catModels = catService.getCatsByOwnerId(id);
        return CatModelDtoMapper.modelsToDtos(catModels);
    }
}
