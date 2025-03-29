package app.web.mapper;


import app.item.model.Item;
import app.item.service.ItemPurchaseService;
import app.item.service.ItemService;
import app.security.AuthenticationMetadata;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.CreateNewItem;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final UserService userService;
    private final ItemService itemService;

    private final ItemPurchaseService itemPurchaseService;

    @Autowired
    public ItemController(UserService userService, ItemService itemService, ItemPurchaseService itemPurchaseService) {
        this.userService = userService;
        this.itemService = itemService;
        this.itemPurchaseService = itemPurchaseService;
    }


    @GetMapping("/new")
    public ModelAndView getNewItemPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {


        User user = userService.getById(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-item");

        modelAndView.addObject("user", user);
        //добавяме и потребителя, защото това определя какво ще виждаме в хедъра като бутони, когато сме в стамп страницата

        modelAndView.addObject("createNewItem", CreateNewItem.builder().build());

        return modelAndView;
    }


    @PostMapping()
    public String createNewItem(@Valid CreateNewItem createNewItem, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            return "add-item";
        }


        User user = userService.getById(authenticationMetadata.getUserId());


        Item item = itemService.create(createNewItem, user);
        UUID itemId = item.getId();


        return "redirect:/items/my-closet";
    }


    @GetMapping("/my-closet")
    public ModelAndView getMyCloset(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {


        User user = userService.getById(authenticationMetadata.getUserId());

        List<Item> items = itemService.getAllByOwnerId(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("my-closet");
        modelAndView.addObject("user", user);
        modelAndView.addObject("items", items);

        return modelAndView;
    }


    @GetMapping("/offers")
    public ModelAndView getItemsForOthers(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        // Извикваме метода от ItemService, за да получим всички артикули без тези на текущия потребител
        List<Item> itemsFromOthers = itemService.getItemsFromOthers(authenticationMetadata.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("offers"); // Можеш да направиш нова страница, която да показва тези артикули
        modelAndView.addObject("user", user);
        modelAndView.addObject("itemsFromOthers", itemsFromOthers);

        return modelAndView;
    }

//    @GetMapping("/{id}")
//    public ModelAndView details(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
//
//        User user = userService.getById(authenticationMetadata.getUserId());
//
//       ItemPurchase itemPurchase = itemPurchaseService.getById(id);
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("my-bag");
//        modelAndView.addObject("itemPurchase", itemPurchase);
//        modelAndView.addObject("user", user);
//
//        return modelAndView;
//
//    }


}
