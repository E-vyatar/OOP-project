package client.utils;

import client.scenes.BoardOverviewCtrl;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class SocketsUtils {
    private BoardOverviewCtrl boardOverviewCtrl;
    private StompSession session;
    private List<StompSession.Subscription> subscriptionList = new ArrayList<>();
    private String server;

    /**
     * Set the hostname of the server and then connect to it
     * @param hostname the hostname
     */
    public void setHostnameAndConnect(String hostname) {
        System.out.println("Connecting to server: " + hostname);
        this.server = "http://" + hostname + ":8080";
        session = connect("ws://" + hostname + ":8080/websocket");
    }
    /**
     * @return returns the session, used it in disconnect method in board overview
     */
    public StompSession getSession() {
        return session;
    }


    /**
     * @param url address
     */
    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connect(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param destination destination for the upcoming messages
     * @param type        class type
     * @param consumer    the subscriber
     * @param <T>         generic class
     */
    public <T> void registerMessages(String destination, Class<T> type, Consumer<T> consumer) {
        subscriptionList.add(session.subscribe(destination, new StompFrameHandler() {
            /**
             * @param headers the headers of a message
             * @return the type that is requested in the register message method above
             */
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            /**
             * @param headers the headers of the frame
             * @param payload the payload, or {@code null} if there was no payload
             */
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Platform.runLater(() -> {
                    consumer.accept((T) payload);
                });
            }
        })) ;
    }

    /**
     * @param destination server address
     * @param o object that will be handled by server
     */
    public void send(String destination, Object o){
        System.out.println("object being sent " + o.toString());
        session.send(destination, o);
    }

    /**
     * this initializes the utils class so it can use the controller
     * @param boardOverviewCtrl board overview controller
     */
    public void initialize(BoardOverviewCtrl boardOverviewCtrl){
        this.boardOverviewCtrl = boardOverviewCtrl;
    }


    /**
     * This method makes it so that the sockets listen only for changes to
     * this board.
     * Yet to be implemented.
     * @param boardId the id of the board for which to listen
     */
    public void listenForBoard(long boardId) {
        // TODO: implement
    }

    /**
     * Stop listening for changes
     */
    public void unsubscribeAll() {
        //TODO: implement
    }

    /**
     * To changes to this board
     */
    public void stopListening() {
        // TODO: implement
    }
}
