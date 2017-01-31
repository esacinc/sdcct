package gov.hhs.onc.sdcct.xml.saxon.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.sf.saxon.Configuration;
import net.sf.saxon.event.FilterFactory;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.SaxonApiException;

public class AugmentedDestination implements Destination {
    private Destination delegate;
    private List<FilterFactory> filters = new ArrayList<>();

    public AugmentedDestination(Destination delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() throws SaxonApiException {
        this.delegate.close();
    }

    @Override
    public Receiver getReceiver(Configuration config) throws SaxonApiException {
        Receiver receiver = this.delegate.getReceiver(config);

        if (this.hasFilters()) {
            for (int a = (this.filters.size() - 1); a >= 0; a--) {
                receiver = this.filters.get(a).makeFilter(receiver);
            }
        }

        return receiver;
    }

    public Destination getDelegate() {
        return this.delegate;
    }

    public AugmentedDestination addFilters(FilterFactory ... filters) {
        Stream.of(filters).forEach(this.filters::add);

        return this;
    }

    public boolean hasFilters() {
        return !this.filters.isEmpty();
    }

    public List<FilterFactory> getFilters() {
        return this.filters;
    }

    public AugmentedDestination setFilters(List<FilterFactory> filters) {
        this.filters.clear();
        this.filters.addAll(filters);

        return this;
    }
}
