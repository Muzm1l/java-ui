package page.backend;

import page.model.Engagement;

/**
 * The backend for this sample. create() is the method that receives the form.
 * It does not know about buttons or text fields.
 * In the other project, replace the body of create() with the save method that already exists.
 */
public class EngagementService {

    private Engagement lastCreated;

    public void create(Engagement engagement) {
        lastCreated = engagement;
    }

    public Engagement getLastCreated() {
        return lastCreated;
    }
}
