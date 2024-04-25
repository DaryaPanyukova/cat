package controller;

import abstraction.OwnerController;
import abstraction.OwnerService;
import dto.OwnerDto;
import mapper.OwnerModelDtoMapper;
import model.OwnerModel;

import java.util.List;
import java.util.Optional;

public class OwnerControllerImpl implements OwnerController {

    private final OwnerService ownerService;

    public OwnerControllerImpl(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    public OwnerDto createOwner(OwnerDto ownerDto) {
        OwnerModel ownerModel = OwnerModelDtoMapper.dtoToModel(ownerDto);
        OwnerModel createdOwnerModel = ownerService.createOwner(ownerModel);
        return OwnerModelDtoMapper.modelToDto(createdOwnerModel);
    }

    @Override
    public OwnerDto updateOwner(OwnerDto ownerDto) {
        OwnerModel ownerModel = OwnerModelDtoMapper.dtoToModel(ownerDto);
        OwnerModel updatedOwnerModel = ownerService.updateOwner(ownerModel);
        return OwnerModelDtoMapper.modelToDto(updatedOwnerModel);
    }

    @Override
    public void deleteOwner(OwnerDto ownerDto) {
        OwnerModel ownerModel = OwnerModelDtoMapper.dtoToModel(ownerDto);
        ownerService.deleteOwner(ownerModel);
    }

    @Override
    public List<OwnerDto> getAllOwners() {
        List<OwnerModel> ownerModels = ownerService.getAllOwners();
        return OwnerModelDtoMapper.modelsToDtos(ownerModels);
    }

    @Override
    public Optional<OwnerDto> findOwnerById(Long id) {
        Optional<OwnerModel> ownerModelOptional = ownerService.findOwnerById(id);
        return ownerModelOptional.map(OwnerModelDtoMapper::modelToDto);
    }
}
