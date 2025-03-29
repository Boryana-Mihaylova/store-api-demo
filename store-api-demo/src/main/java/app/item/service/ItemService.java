package app.item.service;


import app.item.model.Item;
import app.item.repository.ItemRepository;
import app.user.model.User;

import app.web.dto.CreateNewItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;




    @Autowired
    public ItemService(ItemRepository itemRepository, PasswordEncoder passwordEncoder) {

        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Item create(CreateNewItem createNewItem, User user) {
        // Създаване на новия артикул с данни от формата
        Item item = Item.builder()
                .imageUrl(createNewItem.getImageUrl())
                .name(createNewItem.getName())
                .description(createNewItem.getDescription())
                .category(createNewItem.getCategory())
                .gender(createNewItem.getGender())
                .size(createNewItem.getSize())
                .price(BigDecimal.valueOf(1.00)) // Може да използваш динамично цената, ако е предоставена
                .period(createNewItem.getPeriod())
                .owner(user) // Свързване на артикула със съществуващ потребител
                .build();

        // Записване на артикула в базата данни
        item = itemRepository.save(item);  // Записваме артикула и получаваме обекта със записаното ID

        return item;  // Връщаме създадения артикул
    }

    public List<Item> getAll() {
        return itemRepository.findAll();
    }


//    public Item getById(UUID id) {
//
//        return itemRepository.findById(id)
//                .orElseThrow(() -> new DomainException("Item with id [%s] does not exist.".formatted(id)));
//    }


    public List<Item> getAllByOwnerId(UUID ownerId) {

        return itemRepository.findByOwnerId(ownerId);
    }



    public List<Item> getItemsFromOthers(UUID userId) {
        // Взимаме всички артикули
        List<Item> allItems = itemRepository.findAll();

        // Филтрираме артикулите, като изключваме тези, които принадлежат на текущия потребител
        return allItems.stream()
                .filter(item -> !item.getOwner().getId().equals(userId)) // филтрираме по ownerId
                .collect(Collectors.toList());
    }


}
