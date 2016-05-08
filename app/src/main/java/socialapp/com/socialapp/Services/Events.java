package socialapp.com.socialapp.Services;

/**
 * Created by SAMAR on 5/8/2016.
 */
public class Events {

    public Events() {

    }

    public static final int OPERATION_CREATED = 0;
    public static final int OPERATION_DELETED = 1;

    public static final int ENTITY_CONTACT_REQUEST = 1;
    public static final int ENTITY_CONTACT = 2;
    public static final int ENTITY_MESSAGE = 3;

    public static class OnNotificationReceivedEvent {
        public int OperationType;
        public int EntityType;
        public int EntityId;
        public int EntityOwnerId;
        public String EntitiyOwnername;


        public OnNotificationReceivedEvent(int operationType, int entityType, int entityId, int entityOwnerId, String entitiyOwnername) {
            OperationType = operationType;
            EntityType = entityType;
            EntityId = entityId;
            EntityOwnerId = entityOwnerId;
            EntitiyOwnername = entitiyOwnername;
        }
    }
}
