package ScheduledExecutorServicePart2;

public enum TaskType {

    ONE_SHOT,
    FIXED_RATE,
    FIXED_DELAY;

    public boolean isPeriodic() {
        return this != ONE_SHOT;
    }
}
