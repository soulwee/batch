/*
 * Decompiled with CFR 0.149.
 */
package com.demo.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class DeletedUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    Long usrEntId;
    String usrId;   //等于“s1u” +usrEntId
    String usrSteUsrId;
    Date deleteDate;
    boolean deleteSyncInd;

    public Long getUsrEntId() {
        return usrEntId;
    }

    public void setUsrEntId(Long usrEntId) {
        this.usrEntId = usrEntId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getUsrSteUsrId() {
        return usrSteUsrId;
    }

    public void setUsrSteUsrId(String usrSteUsrId) {
        this.usrSteUsrId = usrSteUsrId;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public boolean isDeleteSyncInd() {
        return deleteSyncInd;
    }

    public void setDeleteSyncInd(boolean deleteSyncInd) {
        this.deleteSyncInd = deleteSyncInd;
    }
}

