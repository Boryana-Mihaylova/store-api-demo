package app.review.service;


import app.review.model.Review;
import app.review.repository.ReviewRepository;
import app.user.model.User;

import app.web.dto.CreateReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, PasswordEncoder passwordEncoder) {
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Review create(CreateReview createReview, User user) {

        Review review = Review.builder()

                .description(createReview.getDescription())
                .OwnerItem(createReview.getOwnerItem())
                .owner(user)
                .build();

        // Записване на артикула в базата данни
       review = reviewRepository.save(review);

        return review;
    }

    public List<Review> getAllByOwnerId(UUID ownerId) {
        return reviewRepository.findByOwnerId(ownerId);
    }
}
