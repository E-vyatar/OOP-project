API
===

# MainController

Base Route: `/`

| Method Type | Route | Controller     | Parameters | Response | Description               |
|-------------|-------|----------------|------------|----------|---------------------------|
| GET         | /     | MainController | -          | `Board`  | Meta-data about the board |

# ListController

Base Route: `/lists`

| Method Type | Route           | Controller     | Parameters        | Response         | Description                    |
|-------------|-----------------|----------------|-------------------|------------------|--------------------------------|
| GET         | /lists/all      | ListController | BoardID           | `List<CardList>` | gets all lists from the server |
| PUT         | /lists/new      | ListController | BoardID, ListName | `ListID        ` | creates a new list             |
| GET         | /lists/{listID} | ListController | ListID            | `CardList      ` | gets a list from the server    |
| POST        | /lists/{listID} | ListController | ListID, ListName  | `bool`           | updates a list                 |
| DELETE      | /lists/{listID} | ListController | ListID            | `bool`           | deletes a list                 |

# CardController

Base Route: `/cards`

| Method Type | Route       | Controller     | Parameters | Description                    |
|-------------|-------------|----------------|------------|--------------------------------|
| GET         | /cards/all  | CardController | -          | gets all cards from the server |
| PUT         | /cards/new  | CardController | -          | creates a new card             |
| GET         | /cards/{id} | CardController | -          | gets a card from the server    |
| POST        | /cards/{id} | CardController | -          | updates a card                 |
| DELETE      | /cards/{id} | CardController | -          | deletes a card                 |

