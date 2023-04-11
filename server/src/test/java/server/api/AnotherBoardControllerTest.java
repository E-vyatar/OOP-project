package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.database.BoardRepository;
import server.database.ListRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AnotherBoardControllerTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private SimpMessagingTemplate msgs;
    @InjectMocks
    private BoardController boardController;

    @Mock
    private ListRepository listRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
    }

    @Test
    public void testUpdateBoard () throws Exception {
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Test Board");

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when boardRepository.save() is called, the board object is updated with the new title and returned
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> {
                    Board board1 = invocation.getArgument(0);
                    if (board1.getId() == board.getId()) {
                        board.setTitle(board1.getTitle());
                    }
                    return board;
                }
        );

        // board shown in JSON format, with all attributes, including cardList
        String boardJSON = "{ \"id\": 1, \"title\": \"Updated Test Board\", \"cardLists\": [] }";

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/update/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardJSON))
                // Expect json with null
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());


        verify(boardRepository, times(1)).findById(boardId);
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void testUpdateBoard2() throws Exception {
        long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Test Board");

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when boardRepository.save() is called, the board object is updated with the new title and returned
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> {
                    Board board1 = invocation.getArgument(0);
                    if (board1.getId() == board.getId()) {
                        board.setTitle(board1.getTitle());
                    }
                    return board;
                }
        );

        // board shown in JSON format, with all attributes, including cardList
        String boardJSON = "{ \"id\": 1, \"title\": \"Updated Test Board\", \"cardLists\": [] }";

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Test Board"));

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
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Board"));

        verify(boardRepository, times(1)).save(any(Board.class));
        verifyNoMoreInteractions(boardRepository);
    }



    @Test
    public void testDeleteBoard() throws Exception {
        long boardId = 1L;
        Board board = new Board(boardId, "Board");

        final boolean[] exists = {true};

        doAnswer(invocation ->{
            exists[0] = false;
            return null;

        }).when(boardRepository).deleteById(boardId);

        mockMvc.perform(delete("/boards/" + board.getId()))
                .andExpect(status().isOk());

        verify(boardRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void findBoardsById() throws Exception {
        Board board1 = new Board(1L, "Board 1");
        Board board2 = new Board(2L, "Board 2");
        Board board3 = new Board(3L, "Board 3");

        when(boardRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(List.of(board1, board2, board3));

        List<Board> boards = List.of(board1, board2, board3);

        String boardIDsJSON = "[1, 2, 3]";

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardIDsJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Board 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Board 2"))
                .andExpect(jsonPath("$[2].id").value(3L))
                .andExpect(jsonPath("$[2].title").value("Board 3"));

        verify(boardRepository, times(1)).findAllById(List.of(1L, 2L, 3L));
        verifyNoMoreInteractions(boardRepository);
    }

    @Test
    public void findBoardsByIdsNull() throws Exception{
        when(boardRepository.findAllById(any())).thenReturn(null);

        String boardIDsJSON = "[1, 2, 3]";

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/find")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardIDsJSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());

        verify(boardRepository, times(1)).findAllById(any());
        verifyNoMoreInteractions(boardRepository);
    }
}
