package server.api;

import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.database.ListRepository;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ListControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ListRepository listRepository;
    @Mock
    private SimpMessagingTemplate msg;
    @InjectMocks
    private ListController listController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(listController).build();
    }

    @Test
    public void testGetAllLists() throws Exception {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("List 1");

        when(listRepository.findAll()).thenReturn(Collections.singletonList(cardList));

        mockMvc.perform(get("/lists/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("List 1")));

        verify(listRepository, times(1)).findAll();
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    public void testCreateList() throws Exception {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("List 1");

        when(listRepository.save(any(CardList.class))).thenReturn(cardList);

        mockMvc.perform(put("/lists/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"List 1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("List 1")));

        verify(listRepository, times(1)).save(any(CardList.class));
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    public void testGetListById() throws Exception {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("List 1");

        when(listRepository.findById(1L)).thenReturn(Optional.of(cardList));

        mockMvc.perform(get("/lists/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("List 1")));

        verify(listRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    public void testGetListsByBoardId() throws Exception {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("List 1");

        when(listRepository.findAllByBoardId(1L)).thenReturn(Collections.singletonList(cardList));

        mockMvc.perform(get("/lists/board/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("List 1")));

        verify(listRepository, times(1)).findAllByBoardId(1L);
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    public void testUpdateList() throws Exception {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("List 1");
        cardList.setBoardId(1L);
        cardList.setCards(Collections.emptyList());

        CardList cardList2 = new CardList();
        cardList2.setId(100L);
        cardList2.setTitle("List 100");
        cardList2.setBoardId(1L);
        cardList2.setCards(Collections.emptyList());


        when(listRepository.findById(cardList.getId())).thenReturn(Optional.of(cardList));

        // when findById is called with 100L, then return nothing
        when(listRepository.findById(100L)).thenReturn(Optional.empty());

        when(listRepository.save(any(CardList.class))).thenAnswer(invocation -> {
                CardList c1 = invocation.getArgument(0);
                if(c1.getId()==cardList.getId()){
                    cardList.setTitle(c1.getTitle());
                }

                return cardList;

                });

        String cardListJson = "{\"id\": 1, \"title\": \"Updated List 1\", \"boardId\": 1, \"cards\": []}";

        mockMvc.perform(post("/lists/" + cardList.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardListJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cardList.getId()))
                .andExpect(jsonPath("$.title").value("Updated List 1"));

        cardListJson = "{\"id\": 100, \"title\": \"Updated List 100\", \"boardId\": 1, \"cards\": []}";

        // verify that updating list 2 will return a json with null
        mockMvc.perform(post("/lists/" + cardList2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());


        verify(listRepository, times(1)).save(any(CardList.class));
        verify(listRepository, times(1)).findById(cardList.getId());
        verify(listRepository, times(1)).findById(cardList2.getId());
        verifyNoMoreInteractions(listRepository);
    }

    @Test
    public void testDeleteList() throws Exception {
        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setTitle("List 1");

        mockMvc.perform(delete("/lists/1"))
                .andExpect(status().isOk());

        verify(listRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(listRepository);
    }

}