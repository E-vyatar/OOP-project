package server.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepositroy;
import server.models.Card;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardRepositroy cardRepository;
    Logger logger = LoggerFactory.getLogger(CardController.class);

    public CardController(CardRepositroy cardRepositroy) {
        this.cardRepository = cardRepositroy;
    }

    @PutMapping(value = "new", consumes = "application/json", produces = "application/json")
    public Card createCard(@RequestBody Card card) {
        logger.info("createCard() called with: card = [" + card + "]");
        return cardRepository.save(card);
    }

    @GetMapping("all")
    public Iterable<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @GetMapping("{id}")
    public Card getCardById(@PathVariable("id") String id) {
        return cardRepository.findById(id).orElse(null);
    }

    @PostMapping(value = "{id}", consumes = "application/json", produces = "application/json")
    public Card updateCard(@PathVariable("id") String id, @RequestBody Card card) {
        logger.info("updateCard() called with: id = [" + id + "], card = [" + card + "]");
        if (cardRepository.findById(id).isPresent()) {
            card.setId(id);
            return cardRepository.save(card);
        }
        return null;
    }

    @DeleteMapping("{id}")
    public void deleteCard(@PathVariable("id") String id) {
        cardRepository.deleteById(id);
    }

}
