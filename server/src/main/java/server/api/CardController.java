package server.api;

import commons.Card;
import commons.messages.MoveCardMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.CardRepositroy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 *
 */
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardRepositroy cardRepository;
    private final SimpMessagingTemplate msgs;
    private Logger logger = LoggerFactory.getLogger(CardController.class);

    /**
     * Constructor
     *
     * @param cardRepository the repository (used for querying the DB)
     * @param msgs not used TODO
     */
    public CardController(CardRepositroy cardRepository, SimpMessagingTemplate msgs) {
        this.cardRepository = cardRepository;
        this.msgs = msgs;
    }

    /**
     * Create a new card
     *
     * @param card the card to create
     * @return the created card
     */
    @MessageMapping("/cards/new") // app/cards/new
    @SendTo("/topic/cards/new")
    public Card addMessage(Card card){
        long listSize = cardRepository.countByListId(card.getListId());
        System.out.println("addmessage called");
        card.setIdx(listSize);
        return cardRepository.save(card);
    }

    @PutMapping(value = "new", consumes = "application/json", produces = "application/json")
    public Card createCard(@RequestBody Card card) {
        logger.info("createCard() called with: card = [" + card + "]");
        long listSize = cardRepository.countByListId(card.getListId());
        card.setIdx(listSize);
        Card newCard = cardRepository.save(card);
        //msgs.convertAndSend("/topic/cards/new", newCard);
        return newCard;

    }
    @MessageMapping("/cards")
    @SendTo("/topic/cards")
    public Card updateMessage(Card card){
        long id = card.getId();
        if(cardRepository.findById(id).isPresent()){
            System.out.println("poop");
            card.setIdx(cardRepository.countByListId(card.getListId()));
            cardRepository.save(card);
            return card;
        }
        return null;
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
        var optCard = cardRepository.findById(id);
        if (optCard.isPresent()) {
            Card storedCard = optCard.get();
            storedCard.setTitle(card.getTitle());
            Card result = cardRepository.save(storedCard);
            for (Consumer<Card> listener : this.listeners.values()) {
                listener.accept(result);
            }
            return result;
        }
        return null;
    }

    /**
     * Delete a card
     *
     * @param id the id of the card
     * @return true if card doesn't exist in the database after deletion, false otherwise
     */
    @MessageMapping("/cards/delete") //app/cards/{id}
    @SendTo("/topic/cards/delete")
    public Long deleteMessage(long id){
        cardRepository.deleteById(id);
        logger.info("card has been deleted from db");
        return id;
    }
    @DeleteMapping("{id}")
    public boolean deleteCard(@PathVariable("id") long id) {
        cardRepository.deleteById(id);
        return !cardRepository.existsById(id);
    }

    /**
     * Move a card
     * <p>
     * Transactional annotation is used to ensure that the database is updated in a consistent way
     *
     * @param message the message containing the card id, the list id, the board id and the index
     * @return true if the card was moved successfully, false otherwise
     */
    @PostMapping(value = "move", consumes = "application/json", produces = "application/json")
    @Transactional
    public boolean moveCard(@RequestBody MoveCardMessage message) {

        long cardId = message.getCardId(), newListId = message.getNewListId(), newIndex =
            message.getNewIndex();

        // log the call
        logger.info("moveCard() called with: cardId = [" + cardId + "], listId = [" + newListId +
            "], newIndex = [" + newIndex + "]");

        // check if cardId is valid
        if (cardRepository.findById(cardId).isPresent()) {
            // get the card
            Card card = cardRepository.findById(cardId).get();

            //check if the card is being moved in the same list
            if (newListId == card.getListId()) {
                // check if the card is being moved to the same index
                if (newIndex == card.getIdx()) {
                    return true;
                }

                // check if the card is being moved to a higher index
                if (newIndex > card.getIdx()) {
                    // update all cards with index between old and new index
                    cardRepository.updateIdxBetweenDown(card.getListId(), card.getIdx(), newIndex);


                } else {
                    // update all cards with index between new and old index
                    cardRepository.updateIdxBetweenUp(card.getListId(), newIndex, card.getIdx());


                }
            } else {
                // move all cards in the old list down
                cardRepository.moveAllCardsHigherThanIndexDown(card.getListId(), card.getIdx());
                //move all cards in the new list up, to make room for the new card
                cardRepository.moveAllCardsHigherEqualThanIndexUp(newListId, newIndex);

                // update the list id of the card
                card.setListId(newListId);
            }
            // update the index of the card
            card.setIdx(newIndex);
            cardRepository.save(card);
            return true;
        }
        return false;
    }
    // We use a Concurrent HashMap to prevent race conditions.
    private Map<Object, Consumer<Card>> listeners = new ConcurrentHashMap<>();

    /**
     * Long poll for updates to a card.
     *
     * @param boardId the board for which to poll for updates
     *
     * @return the deferred result. This will wait for 5s
     *      to return a card and otherwise not return anything.
     */
    @GetMapping(value = "updates/{id}", produces = "application/json")
    public DeferredResult<Card> longPollForUpdates(@PathVariable("id") long boardId) {

        logger.info("Received polling request");

        var timeout = ResponseEntity.status(HttpStatus.NO_CONTENT).build();


        DeferredResult<Card> defResult = new DeferredResult<>(5000L, timeout);

        Object key = new Object();
        listeners.put(key, c -> {
            if (c.getBoardId() == boardId) {
                defResult.setResult(c);
            }
        });

        // Make sure we always clean up.
        defResult.onCompletion(() -> {
            listeners.remove(key);
        });

        return defResult;
    }
}
