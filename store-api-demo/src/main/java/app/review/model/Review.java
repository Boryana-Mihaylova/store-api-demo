package app.review.model;


import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String OwnerItem;

    @Column(nullable = false)
    private String description;


    @ManyToOne(optional = false)
    private User owner;

}
