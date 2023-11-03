package org.example;

public class CommandHandler {

    EventStoreImpl _eventStore = new EventStoreImpl();
    // Singleton implementiert
    private static CommandHandler INSTANCE;
    public static CommandHandler getInstance(){
        if(INSTANCE == null)
            INSTANCE = new CommandHandler();
        return INSTANCE;
    }

   public void handle(CommandCreateItem command){
        ItemAggregate itemAggregate = (ItemAggregate) _eventStore.loadAggregate(ItemAggregate.class, new EventScope());

        _eventStore.store(new EventMovingItemCreated(itemAggregate));

        //TODO: CommandCreateItem -> wtf muss da drin sein
       //TODO: EventMovingItemCreated -> wtf muss da drin sein
       //TODO: validierung in loadaggregate oder itemaggregate
    }



    public void handle(CommandMoveItem command){}
    public void handle(CommandDeleteItem command){}
    public void handle(CommandChangeValue command){}

    /*Map<String, MovingItemImpl> items;

    public CommandHandler() {
        this.items = new HashMap<>();
    }

    @Override
    public void createItem(String id) {
        if (!items.containsKey(id))
            items.put(id, new MovingItemImpl(id, 0));
    }

    @Override
    public void createItem(String id, int[] position, int value) {
        if (!items.containsKey(id))
            items.put(id, new MovingItemImpl(id, position, value));
    }

    @Override
    public void deleteItem(String id) {
        items.remove(id); //TODO: check if it is really deleted
    }

    @Override
    public void moveItem(String id, int[] vector) {
        MovingItemImpl currentItem = items.get(id);
        int[] location = currentItem.getLocation();
        for (int i = 0; i < location.length; i++) {
            location[i] += vector[i];
        }
        currentItem.setLocation(location);
        currentItem.setMoves();
    }

    @Override
    public void changeValue(String id, int newValue) {
        MovingItemImpl currentItem = items.get(id);
        currentItem.setValue(newValue);
    }*/
}
