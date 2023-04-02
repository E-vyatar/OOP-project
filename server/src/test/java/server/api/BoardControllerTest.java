package server.api;

import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import server.database.BoardRepository;
import server.database.ListRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@WebMvcTest(BoardController.class)
@ContextConfiguration(classes = BoardController.class)
public class BoardControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private ListRepository listRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBoards() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/boards/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
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
            board.setTitle(board1.getTitle());
            return board;
        }
        ).thenReturn(board);

        // board shown in JSON format, with all attributes, including cardList
        String boardJSON = "{ \"id\": 1, \"title\": \"Updated Test Board\", \"cardLists\": [] }";

        mockMvc.perform(MockMvcRequestBuilders.post("/boards/" + boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardJSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(boardId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Test Board"));
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
    }

    @Test
    public void testDeleteBoard() throws Exception {
        long boardId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/boards/" + boardId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
