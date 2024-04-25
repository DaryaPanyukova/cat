package service;

import abstraction.CatRepository;
import abstraction.CatService;
import entity.CatEntity;
import exception.LonelyCatException;
import jakarta.persistence.EntityNotFoundException;
import mapper.CatModelEntityMapper;
import model.CatModel;

import java.util.List;
import java.util.Optional;

public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;

    public CatServiceImpl(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Override
    public CatModel createCat(CatModel catModel) {
        var catEntity = CatModelEntityMapper.modelToEntity(catModel);

        if (catEntity.getOwner() == null) {
            throw new LonelyCatException("Cat cannot be created without an owner");
        }

        catRepository.save(catEntity);
        return CatModelEntityMapper.entityToModel(catEntity);
    }

    @Override
    public CatModel updateCat(CatModel catModel) {
        var catEntity = CatModelEntityMapper.modelToEntity(catModel);

        if (catEntity.getOwner() == null) {
            throw new LonelyCatException("Cat cannot be updated without an owner");
        }

        catRepository.update(catEntity);
        return CatModelEntityMapper.entityToModel(catEntity);
    }

    @Override
    public void deleteCat(CatModel catModel) {
        var catEntity = CatModelEntityMapper.modelToEntity(catModel);
        catRepository.delete(catEntity);
    }

    @Override
    public List<CatModel> getAllCats() {
        List<CatEntity> catEntities = catRepository.getAll();
        return CatModelEntityMapper.entitiesToModels(catEntities);
    }

    @Override
    public Optional<CatModel> findCatById(long catId) {
        return catRepository.findById(catId).map(CatModelEntityMapper::entityToModel);
    }

    @Override
    public List<CatModel> getCatFriends(long catId) {
        List<CatEntity> catFriendEntities = catRepository.getCatFriends(catId);
        return CatModelEntityMapper.entitiesToModels(catFriendEntities);
    }

    @Override
    public void makeFriends(long firstCatId, long secondCatId) {
        try {
            catRepository.addFriend(firstCatId, secondCatId);
            catRepository.addFriend(secondCatId, firstCatId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("One of the cats not found with ids: " + firstCatId + ", " + secondCatId);
        }
    }

    @Override
    public void breakFriendship(long firstCatId, long secondCatId) {
        try {
            catRepository.removeFriend(firstCatId, secondCatId);
            catRepository.removeFriend(secondCatId, firstCatId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("One of the cats not found with ids: " + firstCatId + ", " + secondCatId);
        }
    }
    @Override
    public List<CatModel> getCatsByOwnerId(Long ownerId) {
        List<CatEntity> catEntities = catRepository.getCatsByOwnerId(ownerId);
        return CatModelEntityMapper.entitiesToModels(catEntities);
    }
}