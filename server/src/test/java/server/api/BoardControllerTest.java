package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.database.BoardRepository;
import server.database.ListRepository;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class BoardControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    private BoardController boardController;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ListRepository listRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();

    }

    @Test
    public void testGetAllBoards() throws Exception {
        Board board = new Board(1,"Test board");

        when(boardRepository.findAll()).thenReturn(Collections.singletonList(board));

        mockMvc.perform(MockMvcRequestBuilders.get("/boards/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", is("Test board")));

        verify(boardRepository, times(1)).findAll();
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void testGetBoardById() throws Exception {
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Test Board");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        mockMvc.perform(MockMvcRequestBuilders.get("/boards/" + boardId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(boardId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Board"));

        verify(boardRepository, times(1)).findById(boardId);
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void testUpdateBoard() throws Exception {
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Test Board");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when boardRepository.save() is called, the board object is updated with the new title and returned
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> {
            Board board1 = invocation.getArgument(0);
            if(board1.getId()==board.getId()) {
                board.setTitle(board1.getTitle());
            }
            return board;
        }
        );

        // board shown in JSON format, with all attributes, including cardList
        String boardJSON = "{ \"id\": 1, \"title\": \"Updated Test Board\", \"cardLists\": [] }";

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardJSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(boardId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Test Board"));

        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, times(1)).save(any(Board.class));
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void testCreateBoard() throws Exception {
        Board board = new Board();
        board.setId(1L);
        board.setTitle("Test Board");

        when(boardRepository.save(any())).thenReturn(board);

        mockMvc.perform(MockMvcRequestBuilders.put("/boards/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Test Board\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Board"));

        verify(boardRepository, times(1)).save(any(Board.class));
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void testDeleteBoard() throws Exception {
        long boardId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/boards/" + boardId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(boardRepository, times(1)).deleteById(boardId);
    }
}
