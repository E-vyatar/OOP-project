API
===

Subject to change.

# MainController

Base Route: `/`

| Method Type | Route | Controller     | Request Body | Response | Description               |
|-------------|-------|----------------|--------------|----------|---------------------------|
| GET         | /     | MainController | -            | `Board`  | Meta-data about the board |

# CardController

Base Route: `/cards`

| Method Type | Route                  | Controller     | Request Body             | Description                    |
|-------------|------------------------|----------------|--------------------------|--------------------------------|
| GET         | /cards/all             | CardController | -                        | gets all cards from the server |
| PUT         | /cards/new             | CardController | Card                     | creates a new card             |
| GET         | /cards/{id}            | CardController | -                        | gets a card from the server    |
| GET         | /cards/board/{boardId} | CardController | -                        | gets all cards from a board    |
| GET         | /cards/list/{listId}   | CardController | -                        | gets all cards from a list     |
| POST        | /cards/{id}            | CardController | Card                     | updates the specified card     |
| DELETE      | /cards/{id}            | CardController | -                        | deletes a card                 |
| POST        | /cards/move            | CardController | cardId, listId, newIndex | moves a card                   |

# ListController

Base Route: `/lists`

| Method Type | Route                | Controller     | Request Body     | Description                    |
|-------------|----------------------|----------------|------------------|--------------------------------|
| GET         | /lists/all           | ListController | -                | gets all lists from the server |
| PUT         | /lists/new           | ListController | List             | creates a new list             |
| GET         | /lists/{id}          | ListController | -                | gets a list from the server    |
| GET         | /lists/board/{board} | ListController | -                | gets all lists from a board    |
| POST        | /lists/{id}          | ListController | List             | updates the specified list     |
| DELETE      | /lists/{id}          | ListController | -                | deletes a list                 |
| POST        | /lists/move          | ListController | listId, newIndex | moves a list                   |

# BoardController

Base Route: `/boards`

> ⚠ Routes marked with `*` will be implemented in the future

| Method Type | Route         | Controller      | Request Body | Description                              |
|-------------|---------------|-----------------|--------------|------------------------------------------|
| GET         | /boards/all   | BoardController | -            | gets all (&ge; 1) boards from the server |
| PUT         | /boards/new*  | BoardController | Board        | creates a new board                      |
| GET         | /boards/{id}  | BoardController | -            | gets a board from the server             |
| POST        | /boards/{id}  | BoardController | Board        | updates the specified board              |
| DELETE      | /boards/{id}* | BoardController | -            | deletes a board                          |

