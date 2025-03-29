package app.item.service;


import app.item.repository.ItemPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ItemPurchaseService {

    private final ItemPurchaseRepository itemPurchaseRepository;

    private final ItemService itemService;

@Autowired
    public ItemPurchaseService(ItemPurchaseRepository itemPurchaseRepository, ItemService itemService) {
        this.itemPurchaseRepository = itemPurchaseRepository;
        this.itemService = itemService;
    }

//    public ItemPurchase getById(UUID id) {
//
//        return ItemPurchaseRepository.findById(id).orElseThrow(() -> new DomainException("Transaction with id [%s] does not exist.".formatted(id)));
//    }
}
