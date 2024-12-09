package main.models.strategy;

public class DeleteContext {
    private DeleteStrategy strategy;

    public DeleteContext(DeleteStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeStrategy() {
        strategy.execute();
    }
}

