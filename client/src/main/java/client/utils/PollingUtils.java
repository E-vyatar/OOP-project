package client.utils;

import jakarta.ws.rs.client.ClientBuilder;

import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import org.glassfish.jersey.client.ClientConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

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
    private ExecutorService executor = Executors.newCachedThreadPool();
    private List<Future<?>> futures = new ArrayList<>();
    /**
     * Start polling for changes to a card.
     * @param path where to poll
     * @param consumer what to call the result on. Will be run in the JavaFX Thread
     * @param type the class of the object that is expected to be returned
     * @param <T> the class of the object that is sent by the server and that the consumer takes
     */
    public <T> void pollForUpdates(String path, Consumer<T> consumer, Class<T> type) {
        Future<?> future = executor.submit(() -> {
            while (!Thread.interrupted()) {
                Response response = ClientBuilder.newClient(new ClientConfig())
                        .target(server)
                        .path(path)
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if (response.getStatus() == 200) {
                    T object = response.readEntity(type);
                    Platform.runLater(() -> {
                        consumer.accept(object);
                    });
                }
            }
        });
        futures.add(future);
    }

    /**
     * This method stops the polling.
     *
     * This method should be called whenever you disconnect from the board so that
     * 1. you will stop receiving unnecessary updates
     * 2. the client can shut down (it can't shut down when there are running background tasks)
     */
    public void disconnect() {
        for (Future future: futures) {
            future.cancel(true);
        }
        futures.clear();
    }


    /**
     * This method closes the ExecutorService so the app can close.
     */
    public void shutdown() {
        executor.shutdownNow();
    }
}
