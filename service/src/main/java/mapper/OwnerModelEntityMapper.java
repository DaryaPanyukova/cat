package mapper;

import entity.CatEntity;
import entity.OwnerEntity;
import model.CatModel;
import model.OwnerModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OwnerModelEntityMapper {

    public static OwnerModel entityToModel(OwnerEntity entity) {
        var ownerModel = new OwnerModel(entity.getId(), entity.getName(), entity.getBirthDate(), new ArrayList<>());

        for (var catEntity : entity.getCats()) {
            ownerModel.getCats()
                    .add(new CatModel(catEntity.getId(),
                            catEntity.getName(),
                            catEntity.getBirthDate(),
                            catEntity.getBreed(),
                            catEntity.getColor(),
                            ownerModel,
                            Collections.emptyList()));
        }
        return ownerModel;
    }

    public static OwnerEntity modelToEntity(OwnerModel model) {
        var ownerEntity = new OwnerEntity();
        ownerEntity.setId(model.getId());
        ownerEntity.setName(model.getName());
        ownerEntity.setBirthDate(model.getBirthDate());
        ownerEntity.setCats(new ArrayList<>());

        for (var catModel : model.getCats()) {
            var catEntity = new CatEntity();
            catEntity.setId(catModel.getId());
            catEntity.setName(catModel.getName());
            catEntity.setBirthDate(catModel.getBirthDate());
            catEntity.setBreed(catModel.getBreed());
            catEntity.setColor(catModel.getColor());
            catEntity.setOwner(OwnerModelEntityMapper.modelToEntity(catModel.getOwner()));
            catEntity.setFriends(new ArrayList<>());

            ownerEntity.getCats().add(catEntity);
        }
        return ownerEntity;
    }

    public static List<OwnerModel> entitiesToModels(List<OwnerEntity> entities) {
        List<OwnerModel> models = new ArrayList<>();
        for (OwnerEntity entity : entities) {
            models.add(entityToModel(entity));
        }
        return models;
    }

    public static List<OwnerEntity> modelsToEntities(List<OwnerModel> models) {
        List<OwnerEntity> entities = new ArrayList<>();
        for (OwnerModel model : models) {
            entities.add(modelToEntity(model));
        }
        return entities;
    }
}