package com.quantum.consultadisponibilidad.parseoMQ0701A;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mq0701aData {

    @SerializedName("tableId")
    @Expose
    private String tableId;
    @SerializedName("rowset")
    @Expose
    private List<RowsetA> rowset = null;
    @SerializedName("records")
    @Expose
    private Integer records;
    @SerializedName("moreRecords")
    @Expose
    private Boolean moreRecords;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<RowsetA> getRowset() {
        return rowset;
    }

    public void setRowset(List<RowsetA> rowset) {
        this.rowset = rowset;
    }

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Boolean getMoreRecords() {
        return moreRecords;
    }

    public void setMoreRecords(Boolean moreRecords) {
        this.moreRecords = moreRecords;
    }

}
