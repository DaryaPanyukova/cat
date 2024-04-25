package abstraction;

import dto.OwnerDto;

import java.util.List;
import java.util.Optional;

public interface OwnerController {
    OwnerDto createOwner(OwnerDto ownerDto);

    OwnerDto updateOwner(OwnerDto ownerDto);

    void deleteOwner(OwnerDto ownerDto);

    List<OwnerDto> getAllOwners();

    Optional<OwnerDto> findOwnerById(Long id);
}
