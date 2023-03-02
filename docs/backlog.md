BACKLOG
=======
*Teletubbies & Co™ | Group 17 OOPP*

## Stakeholders

- **Users** - the people who use the application to manage boards.
- **Host** - the person that hosts and/or manages the server. This stakeholder has an administrative role.

## Terminology

- **Database** - store of all persistent data
- **Boards** - A set of lists
- **List** - set of the cards
- **Cards** - An item

## User Stories

### Epic 1: MVP (In order of priority)

As a **User** I want:

- to view the board (without registration), so I can see the contents of the board
  * Opening the application automatically loads the board from database
  * Loaded board is viewed in the UI
- to create/remove a list within a board, so I can create various categories for my cards (first mock)
  * Created lists appear on the board
  * Created lists are added to the database
  * Application asks for confirmation before deleting a list
  * Deleted lists do not appear on the board
  * Deleted lists removed from database
- to add/remove cards to lists, so I can add new TO DO’s when I have a new task I need to do and remove them in case
  they turn out to not be necessary (first mock)
  * Created cards appear on their list
  * Created cards are added to the database
  * Application asks for confirmation before deleting a card
  * Deleted cards do not appear on the board
  * Deleted cards removed from database
- a card to have only of a title, to keep list items simple to understand
  * When creating a card it has one mandatory text field
  * A card in the board overview shows its text content
- to move cards in a visual way (drag & drop), to help organize my lists easily
  * When card is clicked and held it can be moved around
  * When held card is dropped (release mouse button) it is put in the nearest list
  * Moved card is updated in the database
- to move cards up and down in a list, so I can prioritize my TO DO’s.
  * A list has an order
  * A card position in the list can be changed
  * Change in a card position is updated in the database
- to move cards between lists, so I can change the status of cards when I start working on them or am finished with them. (not sure we need this section since it is more of an optional way to use the board)
- lists on the board have a changeable order, so I can put the lists in a logical order and have the more
  commonly used lists more readily accessible.
  * Board has an order for its lists
  * Created list is added to some position in the board order
  * A list position can be changed
  * A change in the board order is updated in the database
- to see all changes in real time for every user, so you can collaborate more easily and don’t have to keep hitting a
  refresh button to see if another user has done something
  * Every change to the board automatically updates all connected clients view

### Epic 2: Multi-board

As a **User** I want:

- to view boards using a key (and password), so I can work on multiple boards which can be for different things
- to create new boards and get that board’s key,
so I can separate tasks for different projects for better organization (e.g. separate work and private chores).
This way I can also make personalized boards.
- to add a password to a board, so I can share what I’m doing without risking someone destroying my board
- to remove a password, so I can allow anyone to modify my board

### Epic 3: Board details

As a **User** I want:

- to add a description, so I can elaborate on what I mean with a task
- add a (potentially nested) task list,
so I can split the tasks into sub-tasks to make cards seem more doable and encourage progress.
- (un)check an item on the tasklist, so I can (un)mark it as done


### Epic 4: Tags

As a **User** I want:

- to create and delete tags, so I can have more flexibility in describing and organizing cards  
- Add and remove tags from/to cards, so I can specify the type of task it is and be able to retrieve it more easily later 
- Filter by tags (only cards with a certain tag will be visible), so I can more quickly find a specific card I’m looking for and/or to have a better overview of cards related to another card

### Epic 5: Customization

As a **User** I want:

- add background color to my board, to make them look nicer and/or to have a visual separation between boards
- Specify tag color, to be able to visually distinguish between different tags for better organization

### Epic 6: Keyboard shortcuts

As a **User** I want:

- to use keyboard shortcuts, to have a more efficient workflow

### Epic 7: Server management

As a **host** I want:

- to stop/restart the server, so I can fix potential glitches and/or perform updates
- change/reset passwords of boards, so I can help people who are locked out of their own board
- delete boards, so I can comply with local laws and/or remove offensive content

## Mocks

![](images/talio.png)
Attribution: this picture is from the slides

![](images/image2.png)
Attribution: this picture is from the slides
