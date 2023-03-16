package server.database;

import commons.CardList;
import org.springframework.data.repository.CrudRepository;

public interface ListRepository extends CrudRepository<CardList, Long> {
}
