package mapper;

import entity.CatEntity;
import model.CatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CatModelEntityMapper {

    public static CatModel entityToModel(CatEntity entity) {
        List<CatModel> friendModels = new ArrayList<>();

        for (var friend : entity.getFriends()) {
            friendModels.add(new CatModel(friend.getId(),
                    friend.getName(),
                    friend.getBirthDate(),
                    friend.getBreed(),
                    friend.getColor(),
                    OwnerModelEntityMapper.entityToModel(friend.getOwner()),
                    Collections.emptyList()));
        }

        return new CatModel(entity.getId(),
                entity.getName(),
                entity.getBirthDate(),
                entity.getBreed(),
                entity.getColor(),
                OwnerModelEntityMapper.entityToModel(entity.getOwner()),
                friendModels);
    }

    public static CatEntity modelToEntity(CatModel model) {
        ArrayList<CatEntity> friendEntities = new ArrayList<>();

        for (var friend : model.getFriends()) {
            var friendEntity = new CatEntity();
            friendEntity.setId(friend.getId());
            friendEntity.setName(friend.getName());
            friendEntity.setBirthDate(friend.getBirthDate());
            friendEntity.setBreed(friend.getBreed());
            friendEntity.setColor(friend.getColor());
            friendEntity.setOwner(OwnerModelEntityMapper.modelToEntity(friend.getOwner()));
            friendEntity.setFriends(new ArrayList<>());
            friendEntities.add(friendEntity);
        }

        CatEntity entity = new CatEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setBirthDate(model.getBirthDate());
        entity.setBreed(model.getBreed());
        entity.setColor(model.getColor());
        entity.setOwner(OwnerModelEntityMapper.modelToEntity(model.getOwner()));
        entity.setFriends(friendEntities);
        return entity;
    }

    public static List<CatModel> entitiesToModels(List<CatEntity> entities) {
        List<CatModel> models = new ArrayList<>();
        for (CatEntity entity : entities) {
            models.add(entityToModel(entity));
        }
        return models;
    }

    public static List<CatEntity> modelsToEntities(List<CatModel> models) {
        List<CatEntity> entities = new ArrayList<>();
        for (CatModel model : models) {
            entities.add(modelToEntity(model));
        }
        return entities;
    }
}
