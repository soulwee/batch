/*
 * Decompiled with CFR 0.149.
 */
package com.demo.services;

import com.demo.domain.DeletedUserVo;
import com.demo.utils.JdbcUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OperationService {

    public void deleteData() {
        System.out.println("---------------------------delete start---------------------------");
        Long currTime = System.currentTimeMillis();
        JdbcUtils.initConnectionParams();
        Connection conn = JdbcUtils.getConnection();
        System.out.println("---------------------   loading userList  -----------------------");
        List<DeletedUserVo> dataList = this.getDataList(conn);
        Long currTime2 = System.currentTimeMillis();
        System.out.println("---------------delete userList size:" + dataList.size() + " ---------------");
        System.out.println("---------------spending time:" + (currTime2 - currTime) + " ---------------");
        this.deleteUser(dataList, conn);
        Long currTime3 = System.currentTimeMillis();
        System.out.println("----------------delete user complete-------------------------------------");
        System.out.println("---------------spending time:" + (currTime3 - currTime2) + " ---------------");
    }

    private void deleteUser(List<DeletedUserVo> dataList, Connection con) {
        try {
            DeleteUser.deleteUserTask(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getFileList() throws IOException {
        ArrayList<String> list = new ArrayList<>();
        FileInputStream is = new FileInputStream("www/data.txt");
        InputStreamReader isr = new InputStreamReader((InputStream) is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        return list;
    }
    public List<DeletedUserVo> getDataList(Connection con) {
        try {
            ArrayList<DeletedUserVo> resultList = new ArrayList<>();
            OperationService targetService = new OperationService();
            List<String> list = targetService.getFileList();
            if (list != null && list.size() > 0) {
                PreparedStatement stmt = null;
                ResultSet rs = null;
                for (int i = 1; i < list.size(); i++) {
                    if (i % 299 == 0) {
                        JdbcUtils.cleanUp(stmt, rs);
                        con = JdbcUtils.releaseConnection(con, false);
                    }
                    String name = list.get(i).trim();
                    String SQL = "select usr_id,usr_ent_id from reguser where usr_ste_usr_id = ? and usr_status='DELETED'";
                    stmt = con.prepareStatement(SQL);
                    stmt.setString(1, name);
                    rs = stmt.executeQuery();
                    DeletedUserVo info = null;
                    if (rs.next()) {
                        info = new DeletedUserVo();
                        info.setUsrSteUsrId(name);
                        info.setUsrEntId(rs.getLong("usr_ent_id"));
                        info.setUsrId(rs.getString("usr_id"));
                        info.setDeleteSyncInd(false);
                        resultList.add(info);
                    }
                }
                return resultList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

