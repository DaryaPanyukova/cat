package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OwnerModel {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private List<CatModel> cats;
    public OwnerModel(Long id) {
        this.id = id;
    }
}
