package gov.hhs.onc.sdcct.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public final class SdcctStreamUtils {
    public static class TraversingSpliterator<T> implements Spliterator<T> {
        protected int flags = Spliterator.ORDERED;
        protected Function<T, TraversingSpliterator<T>> spliteratorBuilder;
        protected Function<T, Iterator<T>> iteratorBuilder;
        protected boolean includeParent;
        protected T obj;
        protected Iterator<T> iterator;
        protected TraversingSpliterator<T> spliterator;

        public TraversingSpliterator(Function<T, Iterator<T>> iteratorBuilder, boolean includeParent, T obj) {
            this(subObj -> new TraversingSpliterator<>(iteratorBuilder, true, subObj), iteratorBuilder, includeParent, obj);
        }

        public TraversingSpliterator(Function<T, TraversingSpliterator<T>> spliteratorBuilder, Function<T, Iterator<T>> iteratorBuilder, boolean includeParent,
            T obj) {
            this.spliteratorBuilder = spliteratorBuilder;
            this.iteratorBuilder = iteratorBuilder;
            this.includeParent = includeParent;
            this.obj = obj;
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (this.iterator == null) {
                this.iterator = this.iteratorBuilder.apply(this.obj);

                if (this.includeParent) {
                    action.accept(this.obj);

                    return true;
                }
            }

            return (((this.spliterator != null) && this.spliterator.tryAdvance(action)) || (this.iterator.hasNext() && (this.spliterator =
                this.spliteratorBuilder.apply(this.iterator.next())).tryAdvance(action)));
        }

        @Nullable
        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Nonnegative
        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Nonnegative
        @Override
        public int characteristics() {
            return this.flags;
        }
    }

    private SdcctStreamUtils() {
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>> Collector<V, ?, Map<T, U>> toMap() {
        return toMap(Entry::getKey, Entry::getValue, HashMap::new);
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T, U, V extends Entry<? extends T, ? extends U>, W extends Map<T, U>> Collector<V, ?, W> toMap(Supplier<W> mapSupplier) {
        return toMap(Entry::getKey, Entry::getValue, mapSupplier);
    }

    public static <T, U, V, W extends Map<U, V>> Collector<T, ?, W> toMap(Function<? super T, ? extends U> keyMapper,
        Function<? super T, ? extends V> valueMapper, Supplier<W> mapSupplier) {
        return Collectors.toMap(keyMapper, valueMapper, (value1, value2) -> value2, mapSupplier);
    }

    @Nullable
    public static <T, U> U findInstance(Stream<T> stream, Class<U> clazz) {
        return stream.filter(SdcctStreamUtils.<T, U>instances(clazz)).findFirst().map(clazz::cast).orElse(null);
    }

    public static <T, U> Stream<U> asInstances(Stream<T> stream, Class<U> clazz) {
        return stream.filter(SdcctStreamUtils.<T, U>instances(clazz)).map(clazz::cast);
    }

    public static <T, U> Predicate<? super T> instances(Class<U> clazz) {
        return clazz::isInstance;
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES })
    public static <T extends Iterable<T>> Stream<T> traverse(boolean includeParent, @Nullable T obj) {
        return traverse(Iterable::iterator, includeParent, obj);
    }

    public static <T> Stream<T> traverse(Function<T, Iterator<T>> iteratorBuilder, boolean includeParent, @Nullable T obj) {
        return ((obj != null) ? StreamSupport.stream(new TraversingSpliterator<>(iteratorBuilder, includeParent, obj), false) : Stream.empty());
    }
}
