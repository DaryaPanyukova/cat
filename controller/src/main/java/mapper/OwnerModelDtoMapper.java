package mapper;

import dto.CatDto;
import dto.OwnerDto;
import model.CatColor;
import model.CatModel;
import model.OwnerModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OwnerModelDtoMapper {

    public static OwnerDto modelToDto(OwnerModel ownerModel) {
        var ownerDto = new OwnerDto(ownerModel.getId(),
                ownerModel.getName(),
                ownerModel.getBirthDate(),
                new ArrayList<>());

        for (var catModel : ownerModel.getCats()) {
            ownerDto.cats()
                    .add(new CatDto(catModel.getId(),
                            catModel.getName(),
                            catModel.getBirthDate(),
                            catModel.getBreed(),
                            catModel.getColor().name(),
                            ownerDto,
                            Collections.emptyList()));
        }
        return ownerDto;
    }

    public static OwnerModel dtoToModel(OwnerDto ownerDto) {
        var ownerModel = new OwnerModel(ownerDto.id(), ownerDto.name(), ownerDto.birthDate(), new ArrayList<>());

        for (var catDto : ownerDto.cats()) {
            ownerModel.getCats()
                    .add(new CatModel(catDto.id(),
                            catDto.name(),
                            catDto.birthDate(),
                            catDto.breed(),
                            CatColor.valueOf(catDto.color()),
                            ownerModel,
                            Collections.emptyList()));
        }
        return ownerModel;
    }

    public static List<OwnerDto> modelsToDtos(List<OwnerModel> ownerModels) {
        return ownerModels.stream().map(OwnerModelDtoMapper::modelToDto).collect(Collectors.toList());
    }

    public static List<OwnerModel> dtosToModels(List<OwnerDto> ownerDtos) {
        return ownerDtos.stream().map(OwnerModelDtoMapper::dtoToModel).collect(Collectors.toList());
    }
}