package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import cat.CatColor;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@Table(name = "cats")
@NoArgsConstructor
@AllArgsConstructor
public class CatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "breed")
    private String breed;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private CatColor color;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private OwnerEntity owner;

    @ManyToMany
    @JoinTable(name = "cat_friends", joinColumns = @JoinColumn(name = "cat_id"), inverseJoinColumns =
    @JoinColumn(name = "friend_id"))
    private ArrayList<CatEntity> friends;
}
