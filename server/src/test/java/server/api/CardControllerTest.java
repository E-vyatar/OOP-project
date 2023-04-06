package server.api;

import commons.Card;
import commons.CardList;
import commons.messages.MoveCardMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.database.CardRepositroy;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardRepositroy cardRepository;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void createCard() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardRepository.countByListId(any(Long.class))).thenReturn(0L);

        // cardJSON with all attributes of card class
        String cardJSON = "{\"id\": 0, \"title\": \"Card 1\", \"boardId\": 1, \"listId\": 1}";

        mockMvc.perform(put("/cards/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId()))
                .andExpect(jsonPath("$.title").value(card.getTitle()))
                .andExpect(jsonPath("$.boardId").value(card.getBoardId()))
                .andExpect(jsonPath("$.listId").value(card.getListId()))
                .andExpect(jsonPath("$.idx").value(card.getIdx()));

        verify(cardRepository, times(1)).save(any(Card.class));
        verify(cardRepository, times(1)).countByListId(any(Long.class));
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void getAllCards() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        when(cardRepository.findAll()).thenReturn(List.of(card));

        mockMvc.perform(get("/cards/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Card 1")))
                .andExpect(jsonPath("$[0].boardId", is(1)))
                .andExpect(jsonPath("$[0].listId", is(1)))
                .andExpect(jsonPath("$[0].idx", is(0)));

        verify(cardRepository, times(1)).findAll();
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void getCardById() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        mockMvc.perform(get("/cards/" + card.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Card 1")))
                .andExpect(jsonPath("$.boardId", is(1)))
                .andExpect(jsonPath("$.listId", is(1)))
                .andExpect(jsonPath("$.idx", is(0)));

        verify(cardRepository, times(1)).findById(card.getId());
        verifyNoMoreInteractions(cardRepository);

    }

    @Test
    void getCardsByBoardId() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        when(cardRepository.findAllByBoardId(card.getBoardId())).thenReturn(List.of(card));

        mockMvc.perform(get("/cards/board/" + card.getBoardId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Card 1")))
                .andExpect(jsonPath("$[0].boardId", is(1)))
                .andExpect(jsonPath("$[0].listId", is(1)))
                .andExpect(jsonPath("$[0].idx", is(0)));

        verify(cardRepository, times(1)).findAllByBoardId(card.getBoardId());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void getCardsByListId() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        when(cardRepository.findAllByListIdOrderByIdxAsc(card.getListId())).thenReturn(List.of(card));

        mockMvc.perform(get("/cards/list/" + card.getListId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Card 1")))
                .andExpect(jsonPath("$[0].boardId", is(1)))
                .andExpect(jsonPath("$[0].listId", is(1)))
                .andExpect(jsonPath("$[0].idx", is(0)));

        verify(cardRepository, times(1)).findAllByListIdOrderByIdxAsc(card.getListId());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void updateCard() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));

        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card card1 = invocation.getArgument(0);
            if(card1.getId() == card.getId()) {
                card.setTitle(card1.getTitle());
            }
            return card;
        });

        String cardJSON = "{\"id\": 1, \"title\": \"Card 1\", \"boardId\": 1, \"listId\": 1}";


        mockMvc.perform(post("/cards/" + card.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId()))
                .andExpect(jsonPath("$.title").value(card.getTitle()))
                .andExpect(jsonPath("$.boardId").value(card.getBoardId()))
                .andExpect(jsonPath("$.listId").value(card.getListId()))
                .andExpect(jsonPath("$.idx").value(card.getIdx()));

        verify(cardRepository, times(1)).findById(card.getId());
        verify(cardRepository, times(1)).save(any(Card.class));
        verifyNoMoreInteractions(cardRepository);

    }

    @Test
    void deleteCard() throws Exception {
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Card 1");
        card.setBoardId(1L);
        card.setListId(1L);
        card.setIdx(0);

        final boolean[] exists = {true};

        // if deletebyId is called, then exists should be turned to false
        doAnswer(invocation -> {
            exists[0] = false;
            return null;
        }).when(cardRepository).deleteById(card.getId());

        when(cardRepository.existsById(card.getId())).thenReturn(exists[0]);

        mockMvc.perform(delete("/cards/" + card.getId()))
                .andExpect(status().isOk());

        verify(cardRepository, times(1)).deleteById(card.getId());
        verify(cardRepository, times(1)).existsById(card.getId());
        verifyNoMoreInteractions(cardRepository);
    }

    @Test
    void moveCardSameIndex() throws Exception {
        Card card = new Card();
        card.setId(1);
        card.setIdx(0);
        card.setTitle("Card1");
        card.setListId(1);
        card.setBoardId(1);

        //MoveCardMessage moveCardMessage = new MoveCardMessage(1,1,1, 1);
        String json = "{\"id\": 1, \"title\": \"Card1\", \"boardId\": 1, \"listId\": 1}";
        mockMvc.perform(post("/cards/" + "move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId()))
                .andExpect(jsonPath("$.title").value(card.getTitle()))
                .andExpect(jsonPath("$.boardId").value(card.getBoardId()))
                .andExpect(jsonPath("$.listId").value(card.getListId()))
                .andExpect(jsonPath("$.idx").value(card.getIdx()));

        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        //when(cardRepository.updateIdxBetweenDown(card.getListId(), card.getIdx(), card.getIdx())).thenReturn();
    }

    @Test
    void moveCardSameListHigherIndex() throws Exception {
        // TEST: MOVING CARD 1 TO INDEX 2


        // create CardList, with 3 cards
        CardList list = new CardList();
        list.setId(1L);
        list.setTitle("List 1");
        list.setBoardId(1L);
        list.setIdx(0);

        Card card1 = new Card();
        card1.setId(1L);
        card1.setTitle("Card 1");
        card1.setBoardId(1L);
        card1.setListId(1L);
        card1.setIdx(0);

        Card card2 = new Card();
        card2.setId(2L);
        card2.setTitle("Card 2");
        card2.setBoardId(1L);
        card2.setListId(1L);
        card2.setIdx(1);

        Card card3 = new Card();
        card3.setId(3L);
        card3.setTitle("Card 3");
        card3.setBoardId(1L);
        card3.setListId(1L);
        card3.setIdx(2);

        List<Card> cards = List.of(card1, card2, card3);
        list.setCards(cards);

        // when findById.ispresent() is called, return card with that id
        when(cardRepository.findById(card1.getId())).thenReturn(Optional.of(card1));

        // when updatebetweendIdxDown is called, change cardList and indexes of cards
        doAnswer(invocation -> {
            Long listId = invocation.getArgument(0);
            Integer fromIdx = invocation.getArgument(1);
            Integer toIdx = invocation.getArgument(2);
            for(Card card : list.getCards()) {
                if(card.getIdx() > fromIdx && card.getIdx() <= toIdx) {
                    card.setIdx(card.getIdx() - 1);
                }
            }
            return null;
        }).when(cardRepository).updateIdxBetweenDown(list.getId(), card1.getIdx(), card2.getIdx());

        // when save is called change card idx according to what was given in save
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card card = invocation.getArgument(0);
            if(card.getId() == card1.getId()) {
                card1.setIdx(card.getIdx());
            }
            return card;
        });

        // check if the method returns true, using perform
        // address is /cards/move
        // content type is application/json
        // content is a message with cardId, newListId, and newIdx

        MoveCardMessage message = new MoveCardMessage(card1.getId(), card1.getListId(), list.getId(), 2);

        // convert message to json in format { "cardId": 1, "newListId": 1, "oldListId": 1, "newIdx": 2 }
        String messageJSON = "{\"cardId\": " + message.getCardId() + ", \"newListId\": " + message.getNewListId() + ", \"oldListId\": " + message.getOldListId() + ", \"newIndex\": " + message.getNewIndex() + "}";

        mockMvc.perform(post("/cards/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));


        // verify all methods and nothing else
        verify(cardRepository, times(2)).findById(card1.getId());
        verify(cardRepository, times(1)).updateIdxBetweenDown(card1.getListId(), 0, 2);
        verify(cardRepository, times(1)).save(any(Card.class));
        verifyNoMoreInteractions(cardRepository);

    }

    @Test
    void moveCardSameListLowerIndex() throws Exception {

        // TEST: MOVING CARD 3 TO INDEX 0


        // create CardList, with 3 cards
        CardList list = new CardList();
        list.setId(1L);
        list.setTitle("List 1");
        list.setBoardId(1L);
        list.setIdx(0);

        Card card1 = new Card();
        card1.setId(1L);
        card1.setTitle("Card 1");
        card1.setBoardId(1L);
        card1.setListId(1L);
        card1.setIdx(0);

        Card card2 = new Card();
        card2.setId(2L);
        card2.setTitle("Card 2");
        card2.setBoardId(1L);
        card2.setListId(1L);
        card2.setIdx(1);

        Card card3 = new Card();
        card3.setId(3L);
        card3.setTitle("Card 3");
        card3.setBoardId(1L);
        card3.setListId(1L);
        card3.setIdx(2);

        List<Card> cards = List.of(card1, card2, card3);
        list.setCards(cards);

        // when findById.ispresent() is called, return card with that id
        when(cardRepository.findById(card3.getId())).thenReturn(Optional.of(card3));

        // when updatebetweendIdxDown is called, change cardList and indexes of cards
        doAnswer(invocation -> {
            long listId = invocation.getArgument(0);
            long fromIdx = invocation.getArgument(1);
            long toIdx = invocation.getArgument(2);
            for(Card card : list.getCards()) {
                if(card.getIdx() >= fromIdx && card.getIdx() < toIdx) {
                    card.setIdx(card.getIdx() + 1);
                }
            }
            return null;
        }).when(cardRepository).updateIdxBetweenUp(list.getId(), card1.getIdx(), card3.getIdx());

        // when save is called change card idx according to what was given in save
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card card = invocation.getArgument(0);
            if(card.getId() == card3.getId()) {
                card3.setIdx(card.getIdx());
            }
            return card;
        });

        // check if the method returns true, using perform
        // address is /cards/move
        // content type is application/json
        // content is a message with cardId, newListId, and newIdx

        MoveCardMessage message = new MoveCardMessage(card3.getId(), card3.getListId(), list.getId(), 0);

        // convert message to json in format { "cardId": 1, "newListId": 1, "oldListId": 1, "newIdx": 2 }
        String messageJSON = "{\"cardId\": " + message.getCardId() + ", \"newListId\": " + message.getNewListId() + ", \"oldListId\": " + message.getOldListId() + ", \"newIndex\": " + message.getNewIndex() + "}";

        mockMvc.perform(post("/cards/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));


        // verify all methods and nothing else
        verify(cardRepository, times(2)).findById(card3.getId());
        verify(cardRepository, times(1)).updateIdxBetweenUp(card3.getListId(), 0, 2);
        verify(cardRepository, times(1)).save(any(Card.class));
        verifyNoMoreInteractions(cardRepository);

    }

    @Test
    void moveCardOtherList() {

    }

    @Test
    void moveNonExistentCard() {

    }

    @Test
    void longPollForUpdates() {

    }
}