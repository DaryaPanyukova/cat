package abstraction;


import model.OwnerModel;

import java.util.List;
import java.util.Optional;

public interface OwnerService {
    OwnerModel createOwner(OwnerModel ownerModel);

    OwnerModel updateOwner(OwnerModel ownerModel);

    void deleteOwner(OwnerModel ownerModel);

    List<OwnerModel> getAllOwners();

    Optional<OwnerModel> findOwnerById(Long ownerId);
}
