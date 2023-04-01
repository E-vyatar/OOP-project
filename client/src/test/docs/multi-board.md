# Tests for the multi-board feature

<!-- TOC -->
* [Tests for the multi-board feature](#tests-for-the-multi-board-feature)
  * [Create boards](#create-boards)
  * [Renaming test](#renaming-test)
<!-- TOC -->

## Create boards
Go to the list of boards by connecting to the server:
![list of boards](images/ListOfBoards.png)
click the button create new board.
![interface to add a board](images/AddBoard.png)
Press cancel,
you should be back to the initial list of boards:
![list of boards](images/ListOfBoards.png)
Now click create new board again.
![interface to add a board](images/AddBoard.png)
Add a title and then press create board.
You should now see the list of boards but with new board added.
![list of boards with an additional entry](images/ListOfBoardsBoardAdded.png)

## Renaming test
Open two clients and open the same board in both
![overview of a board](images/BoardOverviewOldName.png)
Press the edit button (button in top bar with E ) of the board overview in one client
![popup with board details. You can change the board name and see the id](images/EditNamePopupOldName.png)
Test that you cannot change the id.  
Change the name of the board and then press cancel.  
The name of the board should not have changed:
![overview of a board. Nothing has changed since last time.](images/BoardOverviewOldName.png)
Nothing should have changed.
Let's edit the board details again.
![popup with board details. The title has reset to the old value](images/EditNamePopupOldName.png)
It has the old board name again.
Let's modify it:
![popup with board details. The title has been changed](images/EditNamePopupChangedName.png)
This time, we'll press save.
![overview of a board. The name has changed.](images/BoardOverviewNameChanged.png)
Verify that it has also changed in your second client.

In one of the client's go back to the list of boards.
![list of boards with name change reflected in list](images/ListOfBoardsNameChanged.png)
The board name should have changed here too.

Let's open a different different from before oard and edit its name.
![popup with board details of a different board.](images/EditNamePopupDifferentBoard.png)
Let's change the name to "Very different name" and press save.
Assert that only the board name of the client with this board open has changed, and not the client that still has the other board open. 
just opened board:
![overview of a board we just opened. The name of has changed like before.](images/BoardOverviewNewlyOpenedBoardNameChanged.png)
Other client with the old board:
![overview of the old board we opened before The name of has not changed.](images/BoardOverviewNameChanged.png)

You've successfully tested renaming boards!