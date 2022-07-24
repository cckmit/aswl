package com.aswl.as.common.core.wrapper;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author df
 * @date 2021/06/08 14:21
 */
public class CellRangeAddressWrapper implements Comparable<CellRangeAddressWrapper> {

    public CellRangeAddress range;

    public CellRangeAddressWrapper(CellRangeAddress theRange) {
        this.range = theRange;
    }

    public int compareTo(CellRangeAddressWrapper craw) {
        if (range.getFirstColumn() < craw.range.getFirstColumn()
                && range.getFirstRow() < craw.range.getFirstRow()) {
            return -1;
        } else if (range.getFirstColumn() == craw.range.getFirstColumn()
                && range.getFirstRow() == craw.range.getFirstRow()) {
            return 0;
        } else {
            return 1;
        }
    }

} 