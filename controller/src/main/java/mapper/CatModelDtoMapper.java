package mapper;

import dto.CatDto;
import model.CatColor;
import model.CatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CatModelDtoMapper {

    public static CatDto modelToDto(CatModel catModel) {
        List<CatDto> friendDtos = new ArrayList<>();

        for (var friend : catModel.getFriends()) {
            friendDtos.add(new CatDto(friend.getId(),
                    friend.getName(),
                    friend.getBirthDate(),
                    friend.getBreed(),
                    friend.getColor().name(),
                    OwnerModelDtoMapper.modelToDto(friend.getOwner()),
                    Collections.emptyList()));
        }

        return new CatDto(catModel.getId(),
                catModel.getName(),
                catModel.getBirthDate(),
                catModel.getBreed(),
                catModel.getColor().name(),
                OwnerModelDtoMapper.modelToDto(catModel.getOwner()),
                friendDtos);
    }

    public static CatModel dtoToModel(CatDto catDto) {
        List<CatModel> friendModels = new ArrayList<>();

        for (var friend : catDto.friends()) {
            friendModels.add(new CatModel(friend.id(),
                    friend.name(),
                    friend.birthDate(),
                    friend.breed(),
                    CatColor.valueOf(friend.color()),
                    OwnerModelDtoMapper.dtoToModel(friend.owner()),
                    Collections.emptyList()));
        }

        return new CatModel(catDto.id(),
                catDto.name(),
                catDto.birthDate(),
                catDto.breed(),
                CatColor.valueOf(catDto.color()),
                OwnerModelDtoMapper.dtoToModel(catDto.owner()),
                friendModels);
    }

    public static List<CatDto> modelsToDtos(List<CatModel> catModels) {
        return catModels.stream().map(CatModelDtoMapper::modelToDto).collect(Collectors.toList());
    }

    public static List<CatModel> dtosToModels(List<CatDto> catDtos) {
        return catDtos.stream().map(CatModelDtoMapper::dtoToModel).collect(Collectors.toList());
    }
}
