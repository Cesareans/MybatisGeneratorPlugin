package usr.cesare.builder;

public abstract class AbstractBuilder <T> {
    protected T t;

    public T build(){
        return t;
    }
}
