package app.user.model;


import app.item.model.Item;

import app.deliveryLocation.model.DeliveryLocation;
import app.review.model.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;


        @Column(unique = true, nullable = false)
        private String username;

        private String firstName;

        private String lastName;

        @Column(nullable = false)
        private String password;

        @Column(unique = true)
        private String email;

        private boolean isActive;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private UserRole role;

        @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
        private List<Item> items = new ArrayList<>();

        @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
        private List<DeliveryLocation> locations = new ArrayList<>();

        @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
        private List<Review> reviews = new ArrayList<>();


}
