package gov.hhs.onc.sdcct.config.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import javax.annotation.Nullable;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.apache.commons.lang3.tuple.ImmutablePair;

public final class SdcctPropertiesUtils {
    public static class LinkedProperties extends Properties {
        public class LinkedPropertiesKeyIterator extends AbstractIteratorDecorator<Object> {
            private Object key;

            public LinkedPropertiesKeyIterator() {
                super(LinkedProperties.this.orderedKeys.iterator());
            }

            @Override
            public void remove() {
                LinkedProperties.super.remove(this.key);

                this.getIterator().remove();
            }

            @Override
            public Object next() {
                return (this.key = this.getIterator().next());
            }

            @Nullable
            public Object getKey() {
                return this.key;
            }
        }

        public class LinkedPropertiesKeySet extends AbstractSet<Object> {
            @Override
            public int size() {
                return LinkedProperties.this.size();
            }

            @Override
            public LinkedPropertiesKeyIterator iterator() {
                return new LinkedPropertiesKeyIterator();
            }
        }

        public class LinkedPropertiesValueIterator extends AbstractIteratorDecorator<Object> {
            public LinkedPropertiesValueIterator() {
                super(new LinkedPropertiesKeyIterator());
            }

            @Override
            public void remove() {
                LinkedPropertiesKeyIterator delegate = this.getIterator();
                Object key = delegate.getKey();

                LinkedProperties.this.remove(key, LinkedProperties.this.get(key));

                delegate.remove();
            }

            @Override
            public Object next() {
                return LinkedProperties.this.get(this.getIterator().next());
            }

            @Override
            protected LinkedPropertiesKeyIterator getIterator() {
                return ((LinkedPropertiesKeyIterator) super.getIterator());
            }
        }

        public class LinkedPropertiesValueCollection extends AbstractCollection<Object> {
            @Override
            public int size() {
                return LinkedProperties.this.size();
            }

            @Override
            public LinkedPropertiesValueIterator iterator() {
                return new LinkedPropertiesValueIterator();
            }
        }

        public class LinkedPropertiesEntryIterator implements Iterator<Entry<Object, Object>> {
            private Iterator<Object> keyIterator;
            private Object key;

            public LinkedPropertiesEntryIterator() {
                this.keyIterator = LinkedProperties.this.orderedKeys.iterator();
            }

            @Override
            public void remove() {
                LinkedProperties.super.remove(this.key);

                this.keyIterator.remove();
            }

            @Override
            public Entry<Object, Object> next() {
                return new ImmutablePair<>((this.key = this.keyIterator.next()), LinkedProperties.this.get(this.key));
            }

            @Override
            public boolean hasNext() {
                return this.keyIterator.hasNext();
            }
        }

        public class LinkedPropertiesEntrySet extends AbstractSet<Entry<Object, Object>> {
            @Override
            public int size() {
                return LinkedProperties.this.size();
            }

            @Override
            public LinkedPropertiesEntryIterator iterator() {
                return new LinkedPropertiesEntryIterator();
            }
        }

        private final static long serialVersionUID = 0L;

        private ListOrderedSet<Object> orderedKeys = new ListOrderedSet<>();

        public LinkedProperties() {
            super();
        }

        @Override
        public synchronized void clear() {
            super.clear();
        }

        @Override
        public synchronized boolean remove(Object key, Object value) {
            if (super.remove(key, value)) {
                this.orderedKeys.remove(key);

                return true;
            } else {
                return false;
            }
        }

        @Nullable
        @Override
        public synchronized Object remove(Object key) {
            Object value = super.remove(key);

            if (value != null) {
                this.orderedKeys.remove(key);
            }

            return value;
        }

        @Nullable
        @Override
        public synchronized Object put(Object key, Object value) {
            Object prevValue = super.put(key, value);

            this.orderedKeys.add(key);

            return prevValue;
        }

        @Override
        public synchronized void forEach(BiConsumer<? super Object, ? super Object> consumer) {
            this.entrySet().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
        }

        @Override
        public LinkedPropertiesEntrySet entrySet() {
            return new LinkedPropertiesEntrySet();
        }

        @Override
        public synchronized Enumeration<Object> elements() {
            return IteratorUtils.asEnumeration(this.values().iterator());
        }

        @Override
        public LinkedPropertiesValueCollection values() {
            return new LinkedPropertiesValueCollection();
        }

        @Override
        public synchronized Enumeration<Object> keys() {
            return IteratorUtils.asEnumeration(this.keySet().iterator());
        }

        @Override
        public LinkedPropertiesKeySet keySet() {
            return new LinkedPropertiesKeySet();
        }

        public ListOrderedSet<Object> getOrderedKeys() {
            return this.orderedKeys;
        }
    }

    private SdcctPropertiesUtils() {
    }

    public static Properties singleton(String propName, Object propValue) {
        Properties props = new Properties();
        props.put(propName, propValue);

        return props;
    }

    public static Properties clone(@Nullable Properties props) {
        return (MapUtils.isEmpty(props) ? new Properties() : props.entrySet().stream().collect(toProperties()));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T extends Entry<?, ?>> Collector<T, ?, Properties> toProperties() {
        return SdcctStreamUtils.toMap(Properties::new);
    }
}
