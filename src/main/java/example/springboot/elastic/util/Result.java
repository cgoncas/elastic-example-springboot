package example.springboot.elastic.util;

import java.util.Optional;
import java.util.function.Function;

public class Result<T> {

    private Optional<T> value;
    private Optional<Throwable> error;

    private Result(T value, Throwable error) {
        this.value = Optional.ofNullable(value);
        this.error = Optional.ofNullable(error);
    }

    public static <U> Result<U> success(U value) {
        return new Result<>(value, null);
    }

    public static <U> Result<U> error(Throwable error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return error.isPresent();
    }

    public T getValue() {
        if (this.isError()) {
            throw new RuntimeException(error.get());
        }

        return value.get();
    }

    public Throwable getError() {
        return error.get();
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {

        if (this.isError()) {
            return Result.error(error.get());
        }

        return mapper.apply(value.get());
    }

}
