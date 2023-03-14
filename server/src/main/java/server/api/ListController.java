package server.api;

import commons.CardList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ListRepository;

/**
 *
 */
@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListRepository listRepository;

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
}
