package app.web.mapper;

import app.item.model.Item;
import app.review.model.Review;
import app.review.service.ReviewService;

import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;

import app.web.dto.CreateNewItem;
import app.web.dto.CreateReview;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/new")
    public ModelAndView getNewReviewPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {


        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-review");

        modelAndView.addObject("user", user);

        modelAndView.addObject("createReview", CreateReview.builder().build());

        return modelAndView;
    }

    @PostMapping()
    public String createReview(@Valid CreateReview createReview, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            return "add-review";
        }


        User user = userService.getById(authenticationMetadata.getUserId());


        Review review = reviewService.create(createReview, user);
        UUID itemId = review.getId();


        return "redirect:/reviews/my-review";
    }

    @GetMapping("/my-review")
    public ModelAndView getMyReviews(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {


        User user = userService.getById(authenticationMetadata.getUserId());

        List<Review> reviews = reviewService.getAllByOwnerId(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("my-review");
        modelAndView.addObject("user", user);
        modelAndView.addObject("reviews", reviews);

        return modelAndView;
    }
}
