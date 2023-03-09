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

    @PostMapping(value = "new", consumes = "application/json", produces = "application/json")
    public Card createCard(@RequestBody Card card) {
        logger.info("createCard() called with: card = [" + card + "]");
        return cardRepository.save(card);
    }

    @GetMapping("all")
    public Iterable<Card> getAllCards() {
        return cardRepository.findAll();
    }


}
