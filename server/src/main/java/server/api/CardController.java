package server.api;

import commons.Card;
import commons.messages.MoveCardMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
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
     * Constructor
     *
     * @param cardRepository the repository (used for querying the DB)
     */
    public CardController(CardRepositroy cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Create a new card
     *
     * @param card the card to create
     * @return the created card
     */
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
    @DeleteMapping("{id}")
    public void deleteCard(@PathVariable("id") long id) {
        cardRepository.deleteById(id);
    }

    /**
     * Move a card
     *
     * Transactional annotation is used to ensure that the database is updated in a consistent way
     *
     * @param message the message containing the card id, the new list id, the board id and the new index
     * @return true if the card was moved successfully, false otherwise
     */
    @PostMapping(value = "move", consumes = "application/json", produces = "application/json")
    @Transactional
    public boolean moveCard(@RequestBody MoveCardMessage message) {

        long cardId = message.getCardId();
        long newListId = message.getNewListId();
        long boardId = message.getBoardId();
        long newIndex = message.getNewIndex();

        //print the values
        System.out.println("cardId: " + cardId);
        System.out.println("newListId: " + newListId);
        System.out.println("boardId: " + boardId);
        System.out.println("newIndex: " + newIndex);

        // log the call
        logger.info("moveCard() called with: cardId = [" + cardId + "], listId = [" + newListId + "], boardId = [" + boardId + "], newIndex = [" + newIndex + "]");

        // check if cardId is valid
        if (cardRepository.findById(cardId).isPresent()) {
            // get the card
            Card card = cardRepository.findById(cardId).get();

            // check if boardId is valid
            if(boardId != card.getBoardId()) {
                return false;
            }

            //check if the card is being moved in the same list
            if(newListId == card.getListId()) {
                // check if the card is being moved to the same index
                if(newIndex == card.getIdx()) {
                    return true;
                }

                // check if the card is being moved to a higher index
                if(newIndex > card.getIdx()) {
                    // update all cards with index between old and new index
                    cardRepository.updateIdxBetweenDown(card.getListId(), card.getIdx(), newIndex);

                    // update the index of the card
                    card.setIdx(newIndex);

                } else {
                    System.out.println("new index is lower");
                    // update all cards with index between new and old index
                    cardRepository.updateIdxBetweenUp(card.getListId(), newIndex, card.getIdx());

                    // update the index of the card
                    card.setIdx(newIndex);
                }
            } else {
                // move all cards in the old list down
                cardRepository.moveAllCardsHigherThanIndexDown(card.getListId(), card.getIdx());

                //move all cards in the new list up, to make room for the new card
                cardRepository.moveAllCardsHigherEqualThanIndexUp(newListId, newIndex);

                // update the list id of the card
                card.setListId(newListId);

                // update the index of the card
                card.setIdx(newIndex);
            }


            cardRepository.save(card);
            return true;
        }
        return false;
    }
}
