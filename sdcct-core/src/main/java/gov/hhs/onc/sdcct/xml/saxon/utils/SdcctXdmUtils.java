package gov.hhs.onc.sdcct.xml.saxon.utils;

import gov.hhs.onc.sdcct.utils.SdcctIteratorUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.s9api.SaxonApiUncheckedException;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmSequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AtomicValue;
import org.apache.commons.lang3.text.StrBuilder;

public final class SdcctXdmUtils {
    private SdcctXdmUtils() {
    }

    public static Stream<XdmNode> streamNodes(XdmSequenceIterator iterator) {
        return SdcctStreamUtils.asInstances(streamItems(iterator), XdmNode.class);
    }

    public static Stream<String> streamStrings(XdmSequenceIterator iterator) {
        return streamAtomicValues(iterator).map(XdmItem::getStringValue);
    }

    public static Stream<XdmAtomicValue> streamAtomicValues(XdmSequenceIterator iterator) {
        return SdcctStreamUtils.asInstances(streamItems(iterator), XdmAtomicValue.class);
    }

    public static Stream<XdmItem> streamItems(XdmSequenceIterator iterator) {
        return SdcctIteratorUtils.stream(iterator);
    }

    @Nullable
    public static String getStringValue(@Nullable Sequence seq, @Nullable Sequence defaultSeq) {
        return Optional.ofNullable(getStringValue(seq)).orElseGet(() -> getStringValue(defaultSeq));
    }

    @Nullable
    public static String getStringValue(@Nullable Sequence seq) {
        if (seq == null) {
            return null;
        }

        if (seq instanceof AtomicValue) {
            return ((AtomicValue) seq).getStringValue();
        } else if (seq instanceof Item) {
            return ((Item) seq).getStringValue();
        } else {
            try {
                SequenceIterator seqIterator = seq.iterate();
                StrBuilder builder = new StrBuilder();
                Item seqItem;

                while ((seqItem = seqIterator.next()) != null) {
                    Optional.ofNullable(getStringValue(seqItem)).ifPresent(builder::append);
                }

                return builder.build();
            } catch (XPathException e) {
                throw new SaxonApiUncheckedException(e);
            }
        }
    }
}
