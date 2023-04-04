package server.api;

import commons.Card;
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

        MoveCardMessage moveCardMessage = new MoveCardMessage(1,1,1);
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
    void moveCardSameListHigherIndex() {

    }

    @Test
    void moveCardSameListLowerIndex() {

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