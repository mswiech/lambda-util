package msw.util.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Java8 extended Collectors
 */
public class Collectors {
    /**
     *
     * @param keyMapper key mapper
     * @param itemMapper item mapper
     * @param collectionSupplier supplier for create collection
     * @param mapSupplier supplier for create map
     * @param <T> input type
     * @param <K> type of key in map
     * @param <U> type of item in collection
     * @param <C> type of collection in map
     * @param <M> type of map
     * @return Collector (to map with collection)
     */
    public static <T, K, U, C extends Collection<U>, M extends Map<K, C>> Collector<T, ?, M> toCollectionGroupingBy(final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends U> itemMapper,
                                                                                                                    final Supplier<C> collectionSupplier, final Supplier<M> mapSupplier) {
        final Function<? super T, C> listValueMapper = t -> {
            final C result = collectionSupplier.get();
            result.add(itemMapper.apply(t));
            return result;
        };

        final BinaryOperator<C> mergeFunction = (l1, l2) -> {
            l1.addAll(l2);
            return l1;
        };

        return java.util.stream.Collectors.toMap(keyMapper, listValueMapper, mergeFunction, mapSupplier);
    }

    /**
     * @param keyMapper key mapper
     * @param itemMapper item mapper
     * @param collectionSupplier supplier for create collection
     * @param <T> input type
     * @param <K> type of key in map
     * @param <U> type of item in collection
     * @param <C> type of collection in map
     * @param <M> type of map
     * @return Collector (to map with collection)
     */
    @SuppressWarnings("unchecked")
    public static <T, K, U, C extends Collection<U>, M extends Map<K, C>> Collector<T, ?, M> toCollectionGroupingBy(final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends U> itemMapper,
                                                                                                                    final Supplier<C> collectionSupplier) {
        final Supplier<M> mapSupplier = () -> (M) new HashMap<K, C>();
        return toCollectionGroupingBy(keyMapper, itemMapper, collectionSupplier, mapSupplier);
    }

    /**
     * @param keyMapper key mapper
     * @param itemMapper item mapper
     * @param <T> input type
     * @param <K> type of key in map
     * @param <U> type of item in list
     * @param <C> type of list in map
     * @param <M> type of map
     * @return Collector (to map with list)
     */
    @SuppressWarnings("unchecked")
    public static <T, K, U, C extends List<U>, M extends Map<K, C>> Collector<T, ?, M> toListGroupingBy(final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends U> itemMapper) {
        final Supplier<C> collectionSupplier = () -> (C) new ArrayList<U>();
        return toCollectionGroupingBy(keyMapper, itemMapper, collectionSupplier);
    }

    /**
     * @param keyMapper key mapper
     * @param itemMapper item mapper
     * @param <T> input type
     * @param <K> type of key in map
     * @param <U> type of item in set
     * @param <C> type of set in map
     * @param <M> type of map
     * @return Collector (to map with set)
     */
    @SuppressWarnings("unchecked")
    public static <T, K, U, C extends Set<U>, M extends Map<K, C>> Collector<T, ?, M> toSetGroupingBy(final Function<? super T, ? extends K> keyMapper, final Function<? super T, ? extends U> itemMapper) {
        final Supplier<C> collectionSupplier = () -> (C) new HashSet<U>();
        return toCollectionGroupingBy(keyMapper, itemMapper, collectionSupplier);
    }
}
