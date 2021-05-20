package at.fhtw.client;

public class TourPlannerClientFactory {
    public static TourPlannerClient getClient() {
        // TODO Make Mockable
        return new TourPlannerClient();
    }
}
