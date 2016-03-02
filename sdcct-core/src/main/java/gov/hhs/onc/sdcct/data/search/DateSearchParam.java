package gov.hhs.onc.sdcct.data.search;

import java.util.Date;

public interface DateSearchParam extends SearchParam {
    public Date getValue();

    public void setValue(Date value);
}
