package server.api;

import commons.Card;
import commons.CardList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.database.ListRepository;

/**
 *
 */
@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListRepository listRepository;
    private Logger logger = LoggerFactory.getLogger(ListController.class);

    /**
     * Constructs an instance of ListController
     * @param listRepository a list repository
     */
    public ListController(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    /**
     * Get all lists
     *
     * @return all lists
     */
    @GetMapping("all")
    public Iterable<CardList> getAllLists() {
        return listRepository.findAll();
    }

    /**
     * Creates new lists
     *
     * @param cardList list to be created
     * @return the created list
     */
    @MessageMapping("/lists")
    @SendTo("/topic/lists")
    public CardList addListMessage(CardList cardList){
        listRepository.save(cardList);
        return cardList;
    }
    @PutMapping(value = "new", consumes = "application/json", produces = "application/json")
    public CardList createList(@RequestBody CardList cardList) {
        logger.info("createList() called with: cardList = [" + cardList + "]");
        return listRepository.save(cardList);
    }

    /**
     * Gets a list by id
     *
     * @param id the id of the list
     * @return the list
     */
    @GetMapping("{id}")
    public CardList getListById(@PathVariable("id") long id) {
        return listRepository.findById(id).orElse(null);
    }

    /**
     * Gets all lists by board id
     *
     * @param id the id of the board
     * @return all lists in the board
     */
    @GetMapping("board/{id}")
    public Iterable<CardList> getListsByBoardId(@PathVariable("id") long id) {
        return listRepository.findAllByBoardId(id);
    }

    /**
     * Updates an existing list
     *
     * @param id the id of the list
     * @param cardList the list to update
     * @return the updated list
     */
    @MessageMapping("/lists")
    @SendTo("/topic/lists")
    public CardList editMessage(CardList cardList, long id){
        if(listRepository.findById(id).isPresent()){
            cardList.setId(id);
            listRepository.save(cardList);
            return cardList;
        }
        return null;
    }
    @PostMapping(value = "{id}", consumes = "application/json", produces = "application/json")
    public CardList updateList(@PathVariable("id") long id, @RequestBody CardList cardList) {
        logger.info("updateList() called with: id = [" + id + "], cardList = [" + cardList + "]");
        if (listRepository.findById(id).isPresent()) {
            cardList.setId(id);
            return listRepository.save(cardList);
        }
        return null;
    }

    /**
     * Deletes a list
     *
     * @param id the id of the list
     */
    @DeleteMapping("{id}")
    public void deleteList(@PathVariable("id") long id) {
        listRepository.deleteById(id);
    }

    // TODO: POST --> mapping = "move", reqBody = listId, boardId, newIndex
}
