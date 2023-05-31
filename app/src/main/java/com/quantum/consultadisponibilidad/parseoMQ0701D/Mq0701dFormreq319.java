package com.quantum.consultadisponibilidad.parseoMQ0701D;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quantum.consultadisponibilidad.parseoMQ0701A.RowsetA;

public class Mq0701dFormreq319 {

    @SerializedName("formId")
    @Expose
    private String formId;
    @SerializedName("gridId")
    @Expose
    private String gridId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("rowset")
    @Expose
    private List<RowsetD> rowset = null;
    @SerializedName("records")
    @Expose
    private Integer records;
    @SerializedName("moreRecords")
    @Expose
    private Boolean moreRecords;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RowsetD> getRowset() {
        return rowset;
    }

    public void setRowset(List<RowsetD> rowset) {
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
