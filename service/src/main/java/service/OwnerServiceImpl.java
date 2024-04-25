package service;

import abstraction.CatRepository;
import abstraction.OwnerRepository;
import abstraction.OwnerService;
import entity.CatEntity;
import entity.OwnerEntity;
import jakarta.persistence.EntityNotFoundException;
import mapper.OwnerModelEntityMapper;
import model.OwnerModel;
import java.util.List;
import java.util.Optional;

public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository, CatRepository catRepository) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
    }

    @Override
    public OwnerModel createOwner(OwnerModel ownerModel) {
        OwnerEntity ownerEntity = OwnerModelEntityMapper.modelToEntity(ownerModel);

        List<CatEntity> cats = ownerEntity.getCats();
        for (CatEntity cat : cats) {
            if (catRepository.findById(cat.getId()).isEmpty()) {
                throw new EntityNotFoundException("Cat not found with id: " + cat.getId());
            }
        }

        ownerRepository.save(ownerEntity);
        return OwnerModelEntityMapper.entityToModel(ownerEntity);
    }

    @Override
    public OwnerModel updateOwner(OwnerModel ownerModel) {
        OwnerEntity oldEntity = ownerRepository.findById(ownerModel.getId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerModel.getId()));

        OwnerEntity newEntity = OwnerModelEntityMapper.modelToEntity(ownerModel);

        List<CatEntity> oldCats = oldEntity.getCats();
        List<CatEntity> newCats = newEntity.getCats();

        for (CatEntity cat : oldCats) {
            if (!newCats.contains(cat)) {
                catRepository.delete(cat);
            }
        }
        ownerRepository.update(newEntity);
        return OwnerModelEntityMapper.entityToModel(newEntity);
    }

    @Override
    public void deleteOwner(OwnerModel ownerModel) {
        OwnerEntity ownerEntity = OwnerModelEntityMapper.modelToEntity(ownerModel);
        List<CatEntity> cats = ownerEntity.getCats();
        for (CatEntity cat : cats) {
            cat.setOwner(null);
            catRepository.update(cat);
        }
        ownerRepository.delete(ownerEntity);
    }

    @Override
    public List<OwnerModel> getAllOwners() {
        List<OwnerEntity> ownerEntities = ownerRepository.getAll();
        return OwnerModelEntityMapper.entitiesToModels(ownerEntities);
    }

    @Override
    public Optional<OwnerModel> findOwnerById(Long ownerId) {
        Optional<OwnerEntity> ownerEntityOptional = ownerRepository.findById(ownerId);
        return ownerEntityOptional.map(OwnerModelEntityMapper::entityToModel);
    }
}
