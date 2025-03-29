package app.item.repository;

import app.item.model.ItemPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemPurchaseRepository extends JpaRepository<ItemPurchase, UUID> {

}
