package com.quantum.consultadisponibilidad.parseoMQ0701B;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mq0701bData {

    @SerializedName("tableId")
    @Expose
    private String tableId;
    @SerializedName("rowset")
    @Expose
    private List<RowsetB> rowset = null;
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

    public List<RowsetB> getRowset() {
        return rowset;
    }

    public void setRowset(List<RowsetB> rowset) {
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
