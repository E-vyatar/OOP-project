package server.api;

import commons.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepositroy;

/**
 *
 */
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardRepositroy cardRepository;
    private Logger logger = LoggerFactory.getLogger(CardController.class);

    /**
     * Initialize a card controller.
     * @param cardRepositroy the card repository to use with the card controller
     */
    public CardController(CardRepositroy cardRepositroy) {
        this.cardRepository = cardRepositroy;
    }

    /**
     * Create a new card
     *
     * @param card the card to create
     * @return the created card
     */
    @MessageMapping("/cards/new")
    @SendTo("/topic/cards/new")
    public Card addMessage(Card card){
        cardRepository.save(card);
        return card;
    }
    @PutMapping(value = "new", consumes = "application/json", produces = "application/json")
    public Card createCard(@RequestBody Card card) {
        logger.info("createCard() called with: card = [" + card + "]");
        return cardRepository.save(card);
    }

    /**
     * Get all cards
     *
     * @return all cards
     */
    @GetMapping("all")
    public Iterable<Card> getAllCards() {
        return cardRepository.findAll();
    }

    /**
     * Get a card by id
     *
     * @param id the id of the card
     * @return the card
     */
    @GetMapping("{id}")
    public Card getCardById(@PathVariable("id") long id) {
        return cardRepository.findById(id).orElse(null);
    }

    /**
     * Get all cards by board id
     *
     * @param id the id of the board
     * @return all cards of the board
     */
    @GetMapping("board/{id}")
    public Iterable<Card> getCardsByBoardId(@PathVariable("id") long id) {
        return cardRepository.findAllByBoardId(id);
    }

    /**
     * Get all cards by list id
     *
     * @param id the id of the list
     * @return all cards of the list
     */
    @GetMapping("list/{id}")
    public Iterable<Card> getCardsByListId(@PathVariable("id") long id) {
        return cardRepository.findAllByListIdOrderByIdxAsc(id);
    }

    /**
     * Update a card
     *
     * @param id   the id of the card
     * @param card the card to update
     * @return the updated card
     */
    @MessageMapping("/cards")
    @SendTo("/topic/cards")
    public Card updateMessage(Card card, long id){
        if(cardRepository.findById(id).isPresent()){
            card.setId(id);
            cardRepository.save(card);
            return card;
        }
        return null;
    }
    @PostMapping(value = "{id}", consumes = "application/json", produces = "application/json")
    public Card updateCard(@PathVariable("id") long id, @RequestBody Card card) {
        logger.info("updateCard() called with: id = [" + id + "], card = [" + card + "]");
        if (cardRepository.findById(id).isPresent()) {
            card.setId(id);
            return cardRepository.save(card);
        }
        return null;
    }

    /**
     * Delete a card
     *
     * @param id the id of the card
     */
//    @MessageMapping("/cards")
//    @SendTo("/topic/cards")
//    public void deleteMessage(long id){
//
//    }
    @DeleteMapping("{id}")
    public void deleteCard(@PathVariable("id") long id) {
        cardRepository.deleteById(id);
    }

    // TODO: POST --> mapping = "move", reqBody = cardId, listId, boardId newIndex
}
