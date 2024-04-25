package dto;

import java.time.LocalDate;
import java.util.List;

public record CatDto(Long id, String name, LocalDate birthDate, String breed, String color, OwnerDto owner,
                     List<CatDto> friends) {
}
