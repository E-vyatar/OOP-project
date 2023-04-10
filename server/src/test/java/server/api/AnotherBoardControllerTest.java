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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AnotherBoardControllerTest {
    @Mock
    private BoardRepository boardRepository;
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

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/update/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardJSON))
                .andExpect(status().isNotFound());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(boardId))
                //.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Test Board"));

        //verify(boardRepository, times(1)).findById(boardId);
        //verify(boardRepository, times(1)).save(any(Board.class));
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
}
