package client.utils;

import client.scenes.BoardOverviewCtrl;
import commons.Card;
import jakarta.ws.rs.client.ClientBuilder;

import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import org.glassfish.jersey.client.ClientConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Currently, it only polls for updates to a card.
 */
public class PollingUtils {

    private String server;

    /**
     * Sets the hostname. This is where the server polls from
     * @param hostname the hostname
     */
    public void setHostname(String hostname) {
        this.server = "http://" + hostname + ":8080";
    }

    // We're using an ExecutorService over a thread since it can run multiple tasks at once.
    // This is ideal for tasks like long polling, where most of the time, nothing is done.
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Start polling for changes to a card.
     * @param boardOverviewCtrl the boardOverviewCtrl. This is used to update the card in the UI
     */
    public void pollForCardUpdates(BoardOverviewCtrl boardOverviewCtrl) {
        // Whenever it is shutdown, we can't re-use it so we need to create a new instance
        if (executor.isShutdown()) {
            executor = Executors.newSingleThreadExecutor();
        }
        executor.submit(() -> {
            while (!Thread.interrupted()) {
                long boardId = boardOverviewCtrl.getBoard().getId();

                Response response = ClientBuilder.newClient(new ClientConfig())
                        .target("http://localhost:8080")
                        .path("cards/updates/" + boardId)
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if (response.getStatus() == 200) {
                    Card card = response.readEntity(Card.class);
                    Platform.runLater(() -> {
                        boardOverviewCtrl.updateCard(card);
                    });
                }
            }
        });
    }

    /**
     * This method stops the polling. It waits for the last poll
     * to finish and then stops making new ones.
     *
     * This method should be called whenever you disconnect from the board so that
     * 1. you will stop receiving unnecessary updates
     * 2. the client can shutdown (it can't shutdown when there are running background tasks)
     */
    public void disconnect() {
        if (!executor.isShutdown()){
            executor.shutdownNow();
        }
    }

}
