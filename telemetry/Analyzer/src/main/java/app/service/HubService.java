package app.service;

public interface HubService<T> {

    void process(T event);
}
