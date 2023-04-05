package server.api;

import commons.CardList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.ListRepository;

/**
 *
 */
@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListRepository listRepository;
    private final SimpMessagingTemplate msgs;
    private final Logger logger = LoggerFactory.getLogger(ListController.class);

    /**
     * Constructor
     *
     * @param listRepository the repository (used for querying the DB)
     * @param msgs not used TODO
     */
    public ListController(ListRepository listRepository, SimpMessagingTemplate msgs) {
        this.listRepository = listRepository;
        this.msgs = msgs;
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
    @MessageMapping("/lists/new") //app/lists/new
    @SendTo("/topic/lists/new")
    public CardList addListMessage(CardList cardList){
        logger.info("addlistmessage called ");
        cardList.setIdx(listRepository.countByBoardId(cardList.getBoardId()));
        CardList temp = listRepository.save(cardList);
        logger.info("addMessage called back with cardList = [" + temp + "]");
        return temp;
    }

    @PutMapping(value = "new", consumes = "application/json", produces = "application/json")
    public CardList createList(@RequestBody CardList cardList) {
        logger.info("createList() called with: cardList = [" + cardList + "]");
        msgs.convertAndSend("/topic/lists/new", cardList);
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
     * @param cardList the list to update
     * @return the updated list
     */
    @MessageMapping("/lists/edit") // app/lists/edit
    @SendTo("/topic/lists/edit")
    public CardList editListMessage(CardList cardList){
        long id = cardList.getId();
        if(listRepository.findById(id).isPresent()){
            listRepository.save(cardList);
            return cardList;
        }
        return null;
    }

    /**
     * Updates an existing list
     *
     * @param id the id of the list
     * @param cardList the list to update
     * @return the updated list
     */
    // TODO Clean up (delete this)
    @PostMapping(value = "{id}", consumes = "application/json", produces = "application/json")
    public CardList updateList(@PathVariable("id") long id, @RequestBody CardList cardList) {
        logger.info("updateList() called with: id = [" + id + "], cardList = [" + cardList + "]");
        var cardList1Opt = listRepository.findById(id);
        if (cardList1Opt.isPresent()) {
            var list = cardList1Opt.get();
            list.setTitle(cardList.getTitle());
            return listRepository.save(list);
        }

        return null;
    }

    /**
     * Deletes a list
     *
     * @param id the id of the list
     * @return Long for the id of the list that was deleted
     */
    @MessageMapping("/lists/delete") // app/lists/delete
    @SendTo("/topic/lists/delete")
    public Long deleteListMessage(long id){
        listRepository.deleteById(id);
        logger.info("cardlist has been deleted from db");
        return id;
    }

    /**
     * Deletes a list
     *
     * @param id the id of the list
     */
    // TODO clean up
    @DeleteMapping("{id}")
    public void deleteList(@PathVariable("id") long id) {
        listRepository.deleteById(id);
    }

    // TODO: POST --> mapping = "move", reqBody = listId, boardId, newIndex
}
