package com.demo.services;

import com.demo.domain.DeletedUserVo;
import com.demo.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class DeleteUser {

    public static void deleteUserTask(List<DeletedUserVo> dataList) {
        int success = 0,fail = 0;
        for (DeletedUserVo user : dataList) {
            try {
                if (user != null && user.getUsrEntId() != null && user.getUsrEntId() > 0) {
                    Connection con = JdbcUtils.getConnection();
                    try {
                        deleteUserAsync(con, user.getUsrEntId(), user.getUsrSteUsrId(),
                                user.getUsrId(), user.isDeleteSyncInd());
                        System.out.println("delete success:" + user.getUsrSteUsrId());
                        success += 1;
                    } catch (SQLException e) {
                        System.out.println("delete failed:" + user.getUsrSteUsrId());
                        fail += 1;
                        e.printStackTrace();
                    } finally {
                        JdbcUtils.releaseConnection(con, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("--------------success:"+success+",fail:"+fail+"------------------");
    }


    public static void deleteUserAsync(Connection con,
                                       Long usr_ent_id,
                                       String usr_ste_usr_id,
                                       String usr_id,
                                       boolean ignoreException) throws SQLException {

       // 个人设定

        upd(con, ignoreException, " delete from psnBiography where pbg_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from psnPreference where pfr_ent_id = ?", usr_ent_id);

        // 目录
        upd(con, ignoreException, " delete from aeCatalogAccess where cac_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeTreeNodeSubscribe where tsb_ent_id = ?", usr_ent_id);

        // 报表
        upd(con, ignoreException, " delete from ReportSpec where rsp_ent_id = ?", usr_ent_id);

        // 邮件和站内信

        upd(con, ignoreException, " delete from webMsgReadHistory where wmrh_wmsg_id in (select wmsg_id from webMessage where wmsg_send_ent_id = ? or wmsg_create_ent_id = ? or wmsg_rec_ent_id = ?)", usr_ent_id, usr_ent_id, usr_ent_id);
        upd(con, ignoreException, " delete from webMessage where wmsg_send_ent_id = ?  ", usr_ent_id);
        upd(con, ignoreException, " delete from webMessage where wmsg_create_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from webMessage where wmsg_rec_ent_id = ? ", usr_ent_id);

        upd(con, ignoreException, " delete from emailMsgRecHistory where emrh_emsg_id in (select emsg_id from emailMessage where emsg_rec_ent_ids = '" + usr_ent_id + "' or emsg_send_ent_id = ?)", usr_ent_id);
        upd(con, ignoreException, " delete from emailMessage where emsg_rec_ent_ids = '" + usr_ent_id + "' or emsg_send_ent_id = ?", usr_ent_id);

        // 培训中心
        upd(con, ignoreException, " delete from tcTrainingCenterOfficer where tco_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from tcTrainingCenterTargetEntity where tce_ent_id = ?", usr_ent_id);

        // 问答

        upd(con, ignoreException, " delete from sns_doing where s_doi_target_id in (select ans_id from knowAnswer where ans_create_ent_id = ?) and s_doi_module = 'answer' ", usr_ent_id);
        upd(con, ignoreException, " delete from knowVoteDetail where kvd_ans_id in (select ans_id from knowAnswer where ans_create_ent_id = ?) ", usr_ent_id);
        upd(con, ignoreException, " delete from knowAnswer where ans_create_ent_id = ? ", usr_ent_id);
        if (!con.getAutoCommit()) {
            con.commit();
        }
        //删除用户所提过的问题

/*

		List<Long> que_id_list = getQueIdListByUsrEntId(con, usr_ent_id);
		for(long que_id : que_id_list){
			delKownQuestion(con, que_id);
		}*/


        // 积分
        upd(con, ignoreException, " delete from userCreditsDetailLog where ucl_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from userCreditsDetail where ucd_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from userCredits where uct_ent_id = ?", usr_ent_id);

        // 目标学员
        upd(con, ignoreException, " delete from RegUserSkillSet where uss_ent_id = ?", usr_ent_id);

        //知识中心

        upd(con, ignoreException, " delete from kb_item_view where kiv_usr_ent_id = ?", usr_ent_id);

        upd(con, ignoreException, " delete from usrPwdResetHis where prh_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from UsrRoleTargetEntity where rte_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeNotifyEntity where nen_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from acEntityRole where erl_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from enrolment where enr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from resourcePermission where rpm_ent_id = ?", usr_ent_id);


        // 删除报名学习记录
        upd(con, ignoreException, " delete from aeLearningSoln where lsn_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from IndustryCode where idc_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from cmAssessmentUnit where asu_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from cmAssessment where asm_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from ObjectiveAccess where oac_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from cfCertification where cfn_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from AiccPath where acp_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from aeItemcomments where ict_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from accomplishment where apm_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from measurementevaluation where mtv_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from moduleevaluationhistory where mvh_ent_id =?", usr_ent_id);
        upd(con, ignoreException, " delete from progressattemptsaveanswer where psa_tkh_id in (select app_tkh_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from progressattemptsave where pas_tkh_id in (select app_tkh_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from progressattachment where pat_tkh_id in (select app_tkh_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from progressattachment where pat_prg_usr_id in (select pgr_usr_id from progress where pgr_usr_id =?)", usr_id);
        upd(con, ignoreException, " delete from ProgressAttempt where atm_tkh_id in (select app_tkh_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from ProgressAttempt where atm_pgr_usr_id in (select pgr_usr_id from progress where pgr_usr_id =?)", usr_id);
        upd(con, ignoreException, " delete from progress where pgr_usr_id =?", usr_id);
        if (!con.getAutoCommit()) {
            con.commit();
        }
        upd(con, ignoreException, " delete from progress where pgr_tkh_id in (select app_tkh_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from moduleevaluation where mov_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from courseevaluation where cov_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeappnOpenItem where aoi_app_id in (select app_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from aeAppnTargetEntity where ate_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from TrackingHistory where tkh_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeAppnCommHistory where ach_app_id in (select app_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from aeAppnApprovalList where aal_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeAppnApprovalList where aal_app_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeAppnApprovalList where aal_action_taker_usr_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from aeAppnapprovallist where aal_app_id in (select app_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from aeappnactnhistory where aah_app_id in (select app_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from aeAttendance where att_app_id in (select app_id from aeApplication where app_ent_id =?)", usr_ent_id);
        upd(con, ignoreException, " delete from aeApplication where app_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from InstructorComment where itc_ent_id = ? or itc_iti_ent_id = ? ", usr_ent_id, usr_ent_id);
        upd(con, ignoreException, " delete from InstructorCos where ics_iti_ent_id = ? ", usr_ent_id);


        // 用户组、直属上司
        upd(con, ignoreException, " delete from EntityLoginHistory where elh_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from EntityRelationHistory where erh_child_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from EntityRelation where ern_child_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from SuperviseTargetEntity where spt_source_usr_ent_id = ? or spt_target_ent_id = ? ", usr_ent_id, usr_ent_id);
        upd(con, ignoreException, " delete from aeAccount where acn_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from aeItemrating where irt_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from aeItemLessonInstructor where ili_usr_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from aeItemaccess where iac_ent_id = ? ", usr_ent_id);

        //	社区

        upd(con, ignoreException, " delete from sns_collect where s_clt_uid = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_attention where s_att_source_uid = ? or s_att_target_uid = ? ", usr_ent_id, usr_ent_id);
        upd(con, ignoreException, " delete from sns_share where s_sha_uid = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_setting where s_set_uid = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_valuation_log where s_vtl_uid = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_count where s_cnt_target_id in (select s_cmt_id from sns_comment where s_cmt_uid = ?) and s_cnt_is_comment = 1 ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_count where s_cnt_target_id in (select s_doi_id from sns_doing where s_doi_uid = ?) and Lower(s_cnt_module) = 'doing' and s_cnt_is_comment = 0 ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_comment where s_cmt_uid = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_doing where s_doi_uid = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from sns_group_member where s_gpm_grp_id in (select s_grp_id from sns_group where s_grp_uid = ?) or s_gpm_usr_id = ? ", usr_ent_id, usr_ent_id);
        upd(con, ignoreException, " delete from sns_group where s_grp_uid = ? ", usr_ent_id);

        // 供应商
        upd(con, ignoreException, " delete from supplierComment where scm_ent_id = ? or scm_spl_id in (select spl_id from supplier where spl_create_usr_id = ?) ", usr_ent_id, usr_id);
        upd(con, ignoreException, " delete from supplierCooperationExperience where sce_spl_id in (select spl_id from supplier where spl_create_usr_id = ?) ", usr_id);
        upd(con, ignoreException, " delete from supplierMainCourse where smc_spl_id in (select spl_id from supplier where spl_create_usr_id = ?) ", usr_id);
        upd(con, ignoreException, " delete from supplier where spl_create_usr_id = ? ", usr_id);
        // 用户信息

        upd(con, ignoreException, " delete from UserPositionRelation where upr_usr_ent_id = ? ", usr_ent_id);
        upd(con, ignoreException, " delete from UserClassification where ucf_ent_id = ?", usr_ent_id);
        upd(con, ignoreException, " delete from RegUserExtension where urx_usr_ent_id = ?", usr_ent_id);


        upd(con, ignoreException, " delete from RegUser where usr_ent_id = ?", usr_ent_id);
        //用户的token
        upd(con, ignoreException, " delete from APIToken where atk_usr_ent_id = ? ", usr_ent_id);
//		if(!con.getAutoCommit()) {con.commit();}
        upd(con, ignoreException, " delete from Entity where ent_id = ?", usr_ent_id);
//		提交删除
        if (!con.getAutoCommit()) {
            con.commit();
        }
        // updUsrInfo(con, usr_id, usr_ent_id);
    }

    public static List<Long> getQueIdListByUsrEntId(Connection con, long usr_ent_id) throws SQLException {
        String sql = " select que_id from knowQuestion where que_create_ent_id = ? ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Long> que_id_list = new ArrayList<Long>();
        try {
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, usr_ent_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                que_id_list.add(rs.getLong("que_id"));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return que_id_list;
    }

    // 更新创建人修改人信息
    private static void updUsrInfo(Connection con, String usr_id, long usr_ent_id) throws SQLException {
        String admin_usr_id = "s1u3";
        String admin_usr_ent_id = "3";
        upd(con, " update acEntity set ac_ent_create_usr_id = '" + admin_usr_id + "' where ac_ent_create_usr_id = ?", usr_id);
        upd(con, " update acHomePage set ac_hom_create_usr_id = '" + admin_usr_id + "' where ac_hom_create_usr_id = ?", usr_id);
        upd(con, " update acReportTemplate set ac_rte_create_usr_id = '" + admin_usr_id + "' where ac_rte_create_usr_id = ?", usr_id);
        upd(con, " update acResources set ac_res_create_usr_id = '" + admin_usr_id + "' where ac_res_create_usr_id = ?", usr_id);
        upd(con, " update acSignonLink set slk_usr_id = '" + admin_usr_id + "' where slk_usr_id = ?", usr_id);
        upd(con, " update aeAccount set acn_create_usr_id = '" + admin_usr_id + "' where acn_create_usr_id = ?", usr_id);
        upd(con, " update aeAccount set acn_upd_usr_id = '" + admin_usr_id + "' where acn_upd_usr_id = ?", usr_id);
        upd(con, " update aeAccountTransaction set axn_create_usr_id = '" + admin_usr_id + "' where axn_create_usr_id = ?", usr_id);
        upd(con, " update aeApplication set app_create_usr_id = '" + admin_usr_id + "' where app_create_usr_id = ?", usr_id);
        upd(con, " update aeApplication set app_upd_usr_id = '" + admin_usr_id + "' where app_upd_usr_id = ?", usr_id);
        upd(con, " update aeAppnActnHistory set aah_create_usr_id = '" + admin_usr_id + "' where aah_create_usr_id = ?", usr_id);
        upd(con, " update aeAppnActnHistory set aah_upd_usr_id = '" + admin_usr_id + "' where aah_upd_usr_id = ?", usr_id);
        upd(con, " update aeAppnCommHistory set ach_create_usr_id = '" + admin_usr_id + "' where ach_create_usr_id = ?", usr_id);
        upd(con, " update aeAppnCommHistory set ach_upd_usr_id = '" + admin_usr_id + "' where ach_upd_usr_id = ?", usr_id);
        upd(con, " update aeAppnTargetEntity set ate_create_usr_id = '" + admin_usr_id + "' where ate_create_usr_id = ?", usr_id);
        upd(con, " update aeAttendance set att_create_usr_id = '" + admin_usr_id + "' where att_create_usr_id = ?", usr_id);
        upd(con, " update aeAttendance set att_update_usr_id = '" + admin_usr_id + "' where att_update_usr_id = ?", usr_id);
        upd(con, " update aeCatalog set cat_create_usr_id = '" + admin_usr_id + "' where cat_create_usr_id = ?", usr_id);
        upd(con, " update aeCatalog set cat_upd_usr_id = '" + admin_usr_id + "' where cat_upd_usr_id = ?", usr_id);
        upd(con, " update aeCatalogAccess set cac_create_usr_id = '" + admin_usr_id + "' where cac_create_usr_id = ?", usr_id);
        upd(con, " update aeCatalogItemType set cit_create_usr_id = '" + admin_usr_id + "' where cit_create_usr_id = ?", usr_id);
        upd(con, " update aeFigure set fig_update_usr_id = '" + admin_usr_id + "' where fig_update_usr_id = ?", usr_id);
        upd(con, " update aeFigure set fig_create_usr_id = '" + admin_usr_id + "' where fig_create_usr_id = ?", usr_id);
        upd(con, " update aeItem set itm_create_usr_id = '" + admin_usr_id + "' where itm_create_usr_id = ?", usr_id);
        upd(con, " update aeItem set itm_upd_usr_id = '" + admin_usr_id + "' where itm_upd_usr_id = ?", usr_id);
        upd(con, " update aeItem set itm_approve_usr_id = '" + admin_usr_id + "' where itm_approve_usr_id = ?", usr_id);
        upd(con, " update aeItem set itm_submit_usr_id = '" + admin_usr_id + "' where itm_submit_usr_id = ?", usr_id);
        upd(con, " update aeItemCompetency set itc_create_usr_id = '" + admin_usr_id + "' where itc_create_usr_id = ?", usr_id);
        upd(con, " update aeItemCost set ito_create_usr_id = '" + admin_usr_id + "' where ito_create_usr_id = ?", usr_id);
        upd(con, " update aeItemCost set ito_update_usr_id = '" + admin_usr_id + "' where ito_update_usr_id = ?", usr_id);
        upd(con, " update aeItemLesson set ils_create_usr_id = '" + admin_usr_id + "' where ils_create_usr_id = ?", usr_id);
        upd(con, " update aeItemLesson set ils_update_usr_id = '" + admin_usr_id + "' where ils_update_usr_id = ?", usr_id);
        upd(con, " update aeItemLessonInstructor set ili_create_usr_id = '" + admin_usr_id + "' where ili_create_usr_id = ?", usr_id);
        upd(con, " update aeItemMessage set img_create_usr_id = '" + admin_usr_id + "' where img_create_usr_id = ?", usr_id);
        upd(con, " update aeItemMessage set img_update_usr_id = '" + admin_usr_id + "' where img_update_usr_id = ?", usr_id);
        upd(con, " update aeItemMote set imt_create_usr_id = '" + admin_usr_id + "' where imt_create_usr_id = ?", usr_id);
        upd(con, " update aeItemMote set imt_upd_usr_id = '" + admin_usr_id + "' where imt_upd_usr_id = ?", usr_id);
        upd(con, " update aeItemMoteDefault set imd_create_usr_id = '" + admin_usr_id + "' where imd_create_usr_id = ?", usr_id);
        upd(con, " update aeItemMoteDefault set imd_upd_usr_id = '" + admin_usr_id + "' where imd_upd_usr_id = ?", usr_id);
        upd(con, " update aeItemRating set irt_create_usr_id = '" + admin_usr_id + "' where irt_create_usr_id = ?", usr_id);
        upd(con, " update aeItemRating set irt_update_usr_id = '" + admin_usr_id + "' where irt_update_usr_id = ?", usr_id);
        upd(con, " update aeItemRatingDefination set ird_update_usr_id = '" + admin_usr_id + "' where ird_update_usr_id = ?", usr_id);
        upd(con, " update aeItemRelation set ire_create_usr_id = '" + admin_usr_id + "' where ire_create_usr_id = ?", usr_id);
        upd(con, " update aeItemRequirement set itr_create_usr_id = '" + admin_usr_id + "' where itr_create_usr_id = ?", usr_id);
        upd(con, " update aeItemRequirement set itr_update_usr_id = '" + admin_usr_id + "' where itr_update_usr_id = ?", usr_id);
        upd(con, " update aeItemTargetRule set itr_create_usr_id = '" + admin_usr_id + "' where itr_create_usr_id = ?", usr_id);
        upd(con, " update aeItemTargetRule set itr_update_usr_id = '" + admin_usr_id + "' where itr_update_usr_id = ?", usr_id);
        upd(con, " update aeItemTargetRuleDetail set ird_create_usr_id = '" + admin_usr_id + "' where ird_create_usr_id = ?", usr_id);
        upd(con, " update aeItemTargetRuleDetail set ird_update_usr_id = '" + admin_usr_id + "' where ird_update_usr_id = ?", usr_id);
        upd(con, " update aeItemTemplate set itp_create_usr_id = '" + admin_usr_id + "' where itp_create_usr_id = ?", usr_id);
        upd(con, " update aeItemType set ity_create_usr_id = '" + admin_usr_id + "' where ity_create_usr_id = ?", usr_id);
        upd(con, " update aeItemTypeTemplate set itt_create_usr_id = '" + admin_usr_id + "' where itt_create_usr_id = ?", usr_id);
        upd(con, " update aeLearningSoln set lsn_create_usr_id = '" + admin_usr_id + "' where lsn_create_usr_id = ?", usr_id);
        upd(con, " update aeLearningSoln set lsn_upd_usr_id = '" + admin_usr_id + "' where lsn_upd_usr_id = ?", usr_id);
        upd(con, " update aeLearningSolnTemplate set snt_create_usr_id = '" + admin_usr_id + "' where snt_create_usr_id = ?", usr_id);
        upd(con, " update aeLearningSolnTemplate set snt_upd_usr_id = '" + admin_usr_id + "' where snt_upd_usr_id = ?", usr_id);
        upd(con, " update aeNotifyHistory set nhs_create_usr_id = '" + admin_usr_id + "' where nhs_create_usr_id = ?", usr_id);
        upd(con, " update aeOpenItem set oim_create_usr_id = '" + admin_usr_id + "' where oim_create_usr_id = ?", usr_id);
        upd(con, " update aeOpenItem set oim_upd_usr_id = '" + admin_usr_id + "' where oim_upd_usr_id = ?", usr_id);
        upd(con, " update aeProgramDetails set pdt_create_usr_id = '" + admin_usr_id + "' where pdt_create_usr_id = ?", usr_id);
        upd(con, " update aeProgramDetails set pdt_upd_usr_id = '" + admin_usr_id + "' where pdt_upd_usr_id = ?", usr_id);
        upd(con, " update aeTemplate set tpl_create_usr_id = '" + admin_usr_id + "' where tpl_create_usr_id = ?", usr_id);
        upd(con, " update aeTemplate set tpl_upd_usr_id = '" + admin_usr_id + "' where tpl_upd_usr_id = ?", usr_id);
        upd(con, " update aeTemplateView set tvw_create_usr_id = '" + admin_usr_id + "' where tvw_create_usr_id = ?", usr_id);
        upd(con, " update aeTreeNode set tnd_create_usr_id = '" + admin_usr_id + "' where tnd_create_usr_id = ?", usr_id);
        upd(con, " update aeTreeNode set tnd_upd_usr_id = '" + admin_usr_id + "' where tnd_upd_usr_id = ?", usr_id);
        upd(con, " update aeTreeNodeRelation set tnr_create_usr_id = '" + admin_usr_id + "' where tnr_create_usr_id = ?", usr_id);
        upd(con, " update aeTreeNodeSubscribe set tsb_create_usr_id = '" + admin_usr_id + "' where tsb_create_usr_id = ?", usr_id);
        upd(con, " update bpBlueprint set blp_create_usr_id = '" + admin_usr_id + "' where blp_create_usr_id = ?", usr_id);
        upd(con, " update bpBlueprint set blp_update_usr_id = '" + admin_usr_id + "' where blp_update_usr_id = ?", usr_id);
        upd(con, " update certificate set cfc_create_user_id = '" + admin_usr_id + "' where cfc_create_user_id = ?", usr_id);
        upd(con, " update certificate set cfc_update_user_id = '" + admin_usr_id + "' where cfc_update_user_id = ?", usr_id);
        upd(con, " update certificate set cfc_delete_user_id = '" + admin_usr_id + "' where cfc_delete_user_id = ?", usr_id);
        upd(con, " update cfCertificate set ctf_create_usr_id = '" + admin_usr_id + "' where ctf_create_usr_id = ?", usr_id);
        upd(con, " update cfCertificate set ctf_upd_usr_id = '" + admin_usr_id + "' where ctf_upd_usr_id = ?", usr_id);
        upd(con, " update cfCertification set cfn_create_usr_id = '" + admin_usr_id + "' where cfn_create_usr_id = ?", usr_id);
        upd(con, " update cfCertification set cfn_upd_usr_id = '" + admin_usr_id + "' where cfn_upd_usr_id = ?", usr_id);
        upd(con, " update cmAssessment set asm_create_usr_id = '" + admin_usr_id + "' where asm_create_usr_id = ?", usr_id);
        upd(con, " update cmAssessment set asm_update_usr_id = '" + admin_usr_id + "' where asm_update_usr_id = ?", usr_id);
        upd(con, " update cmSkillBase set skb_create_usr_id = '" + admin_usr_id + "' where skb_create_usr_id = ?", usr_id);
        upd(con, " update cmSkillBase set skb_update_usr_id = '" + admin_usr_id + "' where skb_update_usr_id = ?", usr_id);
        upd(con, " update cmSkillBase set skb_delete_usr_id = '" + admin_usr_id + "' where skb_delete_usr_id = ?", usr_id);
        upd(con, " update cmSkillScale set ssl_create_usr_id = '" + admin_usr_id + "' where ssl_create_usr_id = ?", usr_id);
        upd(con, " update cmSkillScale set ssl_update_usr_id = '" + admin_usr_id + "' where ssl_update_usr_id = ?", usr_id);
        upd(con, " update cmSkillScale set ssl_delete_usr_id = '" + admin_usr_id + "' where ssl_delete_usr_id = ?", usr_id);
        upd(con, " update cmSkillSet set sks_create_usr_id = '" + admin_usr_id + "' where sks_create_usr_id = ?", usr_id);
        upd(con, " update cmSkillSet set sks_update_usr_id = '" + admin_usr_id + "' where sks_update_usr_id = ?", usr_id);
        upd(con, " update CodeTable set ctb_create_usr_id = '" + admin_usr_id + "' where ctb_create_usr_id = ?", usr_id);
        upd(con, " update CodeTable set ctb_upd_usr_id = '" + admin_usr_id + "' where ctb_upd_usr_id = ?", usr_id);
        upd(con, " update CodeType set ctp_create_usr_id = '" + admin_usr_id + "' where ctp_create_usr_id = ?", usr_id);
        upd(con, " update CodeType set ctp_update_usr_id = '" + admin_usr_id + "' where ctp_update_usr_id = ?", usr_id);
        upd(con, " update CourseCriteria set ccr_create_usr_id = '" + admin_usr_id + "' where ccr_create_usr_id = ?", usr_id);
        upd(con, " update CourseCriteria set ccr_upd_usr_id = '" + admin_usr_id + "' where ccr_upd_usr_id = ?", usr_id);
        upd(con, " update CourseMeasurement set cmt_create_usr_id = '" + admin_usr_id + "' where cmt_create_usr_id = ?", usr_id);
        upd(con, " update CourseMeasurement set cmt_update_usr_id = '" + admin_usr_id + "' where cmt_update_usr_id = ?", usr_id);
        upd(con, " update CourseModuleCriteria set cmr_create_usr_id = '" + admin_usr_id + "' where cmr_create_usr_id = ?", usr_id);
        upd(con, " update CourseModuleCriteria set cmr_upd_usr_id = '" + admin_usr_id + "' where cmr_upd_usr_id = ?", usr_id);
        upd(con, " update creditsType set cty_create_usr_id = '" + admin_usr_id + "' where cty_create_usr_id = ?", usr_id);
        upd(con, " update creditsType set cty_update_usr_id = '" + admin_usr_id + "' where cty_update_usr_id = ?", usr_id);
        upd(con, " update ctGlossary set glo_create_usr_id = '" + admin_usr_id + "' where glo_create_usr_id = ?", usr_id);
        upd(con, " update ctGlossary set glo_update_usr_id = '" + admin_usr_id + "' where glo_update_usr_id = ?", usr_id);
        upd(con, " update ctReference set ref_create_usr_id = '" + admin_usr_id + "' where ref_create_usr_id = ?", usr_id);
        upd(con, " update ctReference set ref_update_usr_id = '" + admin_usr_id + "' where ref_update_usr_id = ?", usr_id);
        upd(con, " update Enrolment set enr_create_usr_id = '" + admin_usr_id + "' where enr_create_usr_id = ?", usr_id);
        upd(con, " update Entity set ent_delete_usr_id = '" + admin_usr_id + "' where ent_delete_usr_id = ?", usr_id);
        upd(con, " update EntityRelation set ern_create_usr_id = '" + admin_usr_id + "' where ern_create_usr_id = ?", usr_id);
        upd(con, " update EntityRelationHistory set erh_create_usr_id = '" + admin_usr_id + "' where erh_create_usr_id = ?", usr_id);
        upd(con, " update EvalAccess set eac_create_usr_id = '" + admin_usr_id + "' where eac_create_usr_id = ?", usr_id);
        upd(con, " update fmFacility set fac_create_usr_id = '" + admin_usr_id + "' where fac_create_usr_id = ?", usr_id);
        upd(con, " update fmFacility set fac_upd_usr_id = '" + admin_usr_id + "' where fac_upd_usr_id = ?", usr_id);
        upd(con, " update fmFacilitySchedule set fsh_cancel_usr_id = '" + admin_usr_id + "' where fsh_cancel_usr_id = ?", usr_id);
        upd(con, " update fmFacilitySchedule set fsh_create_usr_id = '" + admin_usr_id + "' where fsh_create_usr_id = ?", usr_id);
        upd(con, " update fmFacilitySchedule set fsh_upd_usr_id = '" + admin_usr_id + "' where fsh_upd_usr_id = ?", usr_id);
        upd(con, " update fmLocation set loc_create_usr_id = '" + admin_usr_id + "' where loc_create_usr_id = ?", usr_id);
        upd(con, " update fmLocation set loc_upd_usr_id = '" + admin_usr_id + "' where loc_upd_usr_id = ?", usr_id);
        upd(con, " update fmReservation set rsv_cancel_usr_id = '" + admin_usr_id + "' where rsv_cancel_usr_id = ?", usr_id);
        upd(con, " update fmReservation set rsv_create_usr_id = '" + admin_usr_id + "' where rsv_create_usr_id = ?", usr_id);
        upd(con, " update fmReservation set rsv_upd_usr_id = '" + admin_usr_id + "' where rsv_upd_usr_id = ?", usr_id);
        upd(con, " update ForumMarkMsg set fmm_usr_id = '" + admin_usr_id + "' where fmm_usr_id = ?", usr_id);
        upd(con, " update ForumMessage set fmg_usr_id = '" + admin_usr_id + "' where fmg_usr_id = ?", usr_id);
        upd(con, " update ForumTopic set fto_usr_id = '" + admin_usr_id + "' where fto_usr_id = ?", usr_id);
        upd(con, " update FriendshipLink set fsl_create_usr_id = '" + admin_usr_id + "' where fsl_create_usr_id = ?", usr_id);
        upd(con, " update FriendshipLink set fsl_update_usr_id = '" + admin_usr_id + "' where fsl_update_usr_id = ?", usr_id);
        upd(con, " update IMSLog set ilg_create_usr_id = '" + admin_usr_id + "' where ilg_create_usr_id = ?", usr_id);
        upd(con, " update IntegCompleteCondition set icd_create_usr_id = '" + admin_usr_id + "' where icd_create_usr_id = ?", usr_id);
        upd(con, " update IntegCompleteCondition set icd_update_usr_id = '" + admin_usr_id + "' where icd_update_usr_id = ?", usr_id);
        upd(con, " update IntegCourseCriteria set icc_create_usr_id = '" + admin_usr_id + "' where icc_create_usr_id = ?", usr_id);
        upd(con, " update IntegCourseCriteria set icc_update_usr_id = '" + admin_usr_id + "' where icc_update_usr_id = ?", usr_id);
        upd(con, " update kmBaseObject set bob_delete_usr_id = '" + admin_usr_id + "' where bob_delete_usr_id = ?", usr_id);
        upd(con, " update kmFolder set fld_update_usr_id = '" + admin_usr_id + "' where fld_update_usr_id = ?", usr_id);
        upd(con, " update kmLibraryObjectBorrow set lob_create_usr_id = '" + admin_usr_id + "' where lob_create_usr_id = ?", usr_id);
        upd(con, " update kmLibraryObjectBorrow set lob_update_usr_id = '" + admin_usr_id + "' where lob_update_usr_id = ?", usr_id);
        upd(con, " update kmLibraryObjectCopy set loc_create_usr_id = '" + admin_usr_id + "' where loc_create_usr_id = ?", usr_id);
        upd(con, " update kmLibraryObjectCopy set loc_update_usr_id = '" + admin_usr_id + "' where loc_update_usr_id = ?", usr_id);
        upd(con, " update kmLibraryObjectCopy set loc_delete_usr_id = '" + admin_usr_id + "' where loc_delete_usr_id = ?", usr_id);
        upd(con, " update kmNode set nod_create_usr_id = '" + admin_usr_id + "' where nod_create_usr_id = ?", usr_id);
        upd(con, " update kmNodeAssignment set nam_create_usr_id = '" + admin_usr_id + "' where nam_create_usr_id = ?", usr_id);
        upd(con, " update kmObject set obj_update_usr_id = '" + admin_usr_id + "' where obj_update_usr_id = ?", usr_id);
        upd(con, " update kmObjectHistory set ojh_update_usr_id = '" + admin_usr_id + "' where ojh_update_usr_id = ?", usr_id);
        upd(con, " update kmObjectType set oty_create_usr_id = '" + admin_usr_id + "' where oty_create_usr_id = ?", usr_id);
        upd(con, " update knowCatalog set kca_create_usr_id = '" + admin_usr_id + "' where kca_create_usr_id = ?", usr_id);
        upd(con, " update knowCatalog set kca_update_usr_id = '" + admin_usr_id + "' where kca_update_usr_id = ?", usr_id);
        upd(con, " update knowCatalogRelation set kcr_create_usr_id = '" + admin_usr_id + "' where kcr_create_usr_id = ?", usr_id);
        upd(con, " update knowVoteDetail set kvd_create_usr_id = '" + admin_usr_id + "' where kvd_create_usr_id = ?", usr_id);
        upd(con, " update MeasurementEvaluation set mtv_create_usr_id = '" + admin_usr_id + "' where mtv_create_usr_id = ?", usr_id);
        upd(con, " update MeasurementEvaluation set mtv_update_usr_id = '" + admin_usr_id + "' where mtv_update_usr_id = ?", usr_id);
        upd(con, " update Message set msg_usr_id = '" + admin_usr_id + "' where msg_usr_id = ?", usr_id);
        upd(con, " update emailMessage set emsg_send_ent_id = '" + admin_usr_ent_id + "' where emsg_send_ent_id = ?", usr_ent_id);
        upd(con, " update emailMessage set emsg_create_ent_id = '" + admin_usr_ent_id + "' where emsg_create_ent_id = ?", usr_ent_id);
        upd(con, " update Module set mod_usr_id_instructor = '" + admin_usr_id + "' where mod_usr_id_instructor = ?", usr_id);
		/*upd(con, " update ModuleEvaluation set mov_create_usr_id = '" + admin_usr_id + "' where mov_create_usr_id = ?", usr_id);
		upd(con, " update ModuleEvaluation set mov_update_usr_id = '" + admin_usr_id + "' where mov_update_usr_id = ?", usr_id);
		upd(con, " update ModuleEvaluationHistory set mvh_create_usr_id = '" + admin_usr_id + "' where mvh_create_usr_id = ?", usr_id);*/
        upd(con, " update moduleTrainingCenter set mtc_create_usr_id = '" + admin_usr_id + "' where mtc_create_usr_id = ?", usr_id);
        upd(con, " update ObjectView set ojv_create_usr_id = '" + admin_usr_id + "' where ojv_create_usr_id = ?", usr_id);
        upd(con, " update ObjectView set ojv_update_usr_id = '" + admin_usr_id + "' where ojv_update_usr_id = ?", usr_id);
		/*upd(con, " update ProgressAttemptSave set pas_create_usr_id = '" + admin_usr_id + "' where pas_create_usr_id = ?", usr_id);
		upd(con, " update ProgressAttemptSave set pas_update_usr_id = '" + admin_usr_id + "' where pas_update_usr_id = ?", usr_id);
		upd(con, " update ProgressAttemptSaveAnswer set psa_create_usr_id = '" + admin_usr_id + "' where psa_create_usr_id = ?", usr_id);
		upd(con, " update ProgressAttemptSaveAnswer set psa_update_usr_id = '" + admin_usr_id + "' where psa_update_usr_id = ?", usr_id);*/
        upd(con, " update psnBiography set pbg_create_usr_id = '" + admin_usr_id + "' where pbg_create_usr_id = ?", usr_id);
        upd(con, " update psnBiography set pbg_update_usr_id = '" + admin_usr_id + "' where pbg_update_usr_id = ?", usr_id);
        upd(con, " update psnPreference set pfr_create_usr_id = '" + admin_usr_id + "' where pfr_create_usr_id = ?", usr_id);
        upd(con, " update psnPreference set pfr_update_usr_id = '" + admin_usr_id + "' where pfr_update_usr_id = ?", usr_id);
        upd(con, " update QueContainerSpec set qcs_create_usr_id = '" + admin_usr_id + "' where qcs_create_usr_id = ?", usr_id);
        upd(con, " update QueContainerSpec set qcs_update_usr_id = '" + admin_usr_id + "' where qcs_update_usr_id = ?", usr_id);
        upd(con, " update RawQuestion set raq_usr_id_owner = '" + admin_usr_id + "' where raq_usr_id_owner = ?", usr_id);
        upd(con, " update RegUser set usr_ste_usr_id = '" + admin_usr_id + "' where usr_ste_usr_id = ?", usr_id);
        upd(con, " update RegUser set usr_approve_usr_id = '" + admin_usr_id + "' where usr_approve_usr_id = ?", usr_id);
        upd(con, " update ReportSpec set rsp_create_usr_id = '" + admin_usr_id + "' where rsp_create_usr_id = ?", usr_id);
        upd(con, " update ReportSpec set rsp_upd_usr_id = '" + admin_usr_id + "' where rsp_upd_usr_id = ?", usr_id);
        upd(con, " update ReportTemplate set rte_create_usr_id = '" + admin_usr_id + "' where rte_create_usr_id = ?", usr_id);
        upd(con, " update ReportTemplate set rte_upd_usr_id = '" + admin_usr_id + "' where rte_upd_usr_id = ?", usr_id);
        upd(con, " update ResourceRequirement set rrq_create_usr_id = '" + admin_usr_id + "' where rrq_create_usr_id = ?", usr_id);
        upd(con, " update Resources set res_usr_id_owner = '" + admin_usr_id + "' where res_usr_id_owner = ?", usr_id);
        upd(con, " update Resources set res_upd_user = '" + admin_usr_id + "' where res_upd_user = ?", usr_id);
        upd(con, " update studyGroup set sgp_create_usr_id = '" + admin_usr_id + "' where sgp_create_usr_id = ?", usr_id);
        upd(con, " update studyGroupMember set sgm_create_usr_id = '" + admin_usr_id + "' where sgm_create_usr_id = ?", usr_id);
        upd(con, " update studyGroupMember set sgm_upd_usr_id = '" + admin_usr_id + "' where sgm_upd_usr_id = ?", usr_id);
        upd(con, " update studyGroupRelation set sgr_create_usr_id = '" + admin_usr_id + "' where sgr_create_usr_id = ?", usr_id);
        upd(con, " update studyGroupResources set sgs_create_usr_id = '" + admin_usr_id + "' where sgs_create_usr_id = ?", usr_id);
        upd(con, " update studyGroupResources set sgs_upd_usr_id = '" + admin_usr_id + "' where sgs_upd_usr_id = ?", usr_id);
        upd(con, " update SuperviseTargetEntity set spt_create_usr_id = '" + admin_usr_id + "' where spt_create_usr_id = ?", usr_id);
        upd(con, " update SystemSetting set sys_cfg_create_usr_id = '" + admin_usr_id + "' where sys_cfg_create_usr_id = ?", usr_id);
        upd(con, " update SystemSetting set sys_cft_update_usr_id = '" + admin_usr_id + "' where sys_cft_update_usr_id = ?", usr_id);
        upd(con, " update tcRelation set tcn_create_usr_id = '" + admin_usr_id + "' where tcn_create_usr_id = ?", usr_id);
        upd(con, " update tcTrainingCenter set tcr_create_usr_id = '" + admin_usr_id + "' where tcr_create_usr_id = ?", usr_id);
        upd(con, " update tcTrainingCenter set tcr_update_usr_id = '" + admin_usr_id + "' where tcr_update_usr_id = ?", usr_id);
        upd(con, " update tcTrainingCenterOfficer set tco_create_usr_id = '" + admin_usr_id + "' where tco_create_usr_id = ?", usr_id);
        upd(con, " update tcTrainingCenterOfficer set tco_update_usr_id = '" + admin_usr_id + "' where tco_update_usr_id = ?", usr_id);
        upd(con, " update tcTrainingCenterTargetEntity set tce_create_usr_id = '" + admin_usr_id + "' where tce_create_usr_id = ?", usr_id);
        upd(con, " update tpTrainingPlan set tpn_approve_usr_id = '" + admin_usr_id + "' where tpn_approve_usr_id = ?", usr_id);
        upd(con, " update tpTrainingPlan set tpn_create_usr_id = '" + admin_usr_id + "' where tpn_create_usr_id = ?", usr_id);
        upd(con, " update tpTrainingPlan set tpn_update_usr_id = '" + admin_usr_id + "' where tpn_update_usr_id = ?", usr_id);
        upd(con, " update tpTrainingPlan set tpn_submit_usr_id = '" + admin_usr_id + "' where tpn_submit_usr_id = ?", usr_id);
        upd(con, " update tpYearPlan set ypn_approve_usr_id = '" + admin_usr_id + "' where ypn_approve_usr_id = ?", usr_id);
        upd(con, " update tpYearPlan set ypn_submit_usr_id = '" + admin_usr_id + "' where ypn_submit_usr_id = ?", usr_id);
        upd(con, " update tpYearPlan set ypn_create_usr_id = '" + admin_usr_id + "' where ypn_create_usr_id = ?", usr_id);
        upd(con, " update tpYearPlan set ypn_update_usr_id = '" + admin_usr_id + "' where ypn_update_usr_id = ?", usr_id);
        upd(con, " update tpYearSetting set ysg_create_usr_id = '" + admin_usr_id + "' where ysg_create_usr_id = ?", usr_id);
        upd(con, " update tpYearSetting set ysg_update_usr_id = '" + admin_usr_id + "' where ysg_update_usr_id = ?", usr_id);
        upd(con, " update UploadLog set ulg_usr_id_owner = '" + admin_usr_id + "' where ulg_usr_id_owner = ?", usr_id);
        upd(con, " update userCreditsDetail set ucd_create_usr_id = '" + admin_usr_id + "' where ucd_create_usr_id = ?", usr_id);
        upd(con, " update userCreditsDetail set ucd_update_usr_id = '" + admin_usr_id + "' where ucd_update_usr_id = ?", usr_id);
        upd(con, " update userCreditsDetailLog set ucl_create_usr_id = '" + admin_usr_id + "' where ucl_create_usr_id = ?", usr_id);
        upd(con, " update UserGroup set usg_usr_id_admin = '" + admin_usr_id + "' where usg_usr_id_admin = ?", usr_id);
        upd(con, " update usrRoleTargetEntity set rte_create_usr_id = '" + admin_usr_id + "' where rte_create_usr_id = ?", usr_id);
        upd(con, " update wbTransactionLog set tlg_create_usr_id = '" + admin_usr_id + "' where tlg_create_usr_id = ?", usr_id);

        // 知识中心
        upd(con, " update kb_attachment set kba_create_user_id = '" + admin_usr_id + "' where kba_create_user_id = ?", usr_id);
        upd(con, " update kb_attachment set kba_update_user_id = '" + admin_usr_id + "' where kba_update_user_id = ?", usr_id);
        upd(con, " update kb_catalog set kbc_create_user_id = '" + admin_usr_id + "' where kbc_create_user_id = ?", usr_id);
        upd(con, " update kb_catalog set kbc_update_user_id = '" + admin_usr_id + "' where kbc_update_user_id = ?", usr_id);
        upd(con, " update kb_item set kbi_approve_user_id = '" + admin_usr_id + "' where kbi_approve_user_id = ?", usr_id);
        upd(con, " update kb_item set kbi_publish_user_id = '" + admin_usr_id + "' where kbi_publish_user_id = ?", usr_id);
        upd(con, " update kb_item set kbi_create_user_id = '" + admin_usr_id + "' where kbi_create_user_id = ?", usr_id);
        upd(con, " update kb_item set kbi_update_user_id = '" + admin_usr_id + "' where kbi_update_user_id = ?", usr_id);
        upd(con, " update kb_item_attachment set kia_create_user_id = '" + admin_usr_id + "' where kia_create_user_id = ?", usr_id);
        upd(con, " update kb_item_cat set kic_create_user_id = '" + admin_usr_id + "' where kic_create_user_id = ?", usr_id);
        upd(con, " update kb_item_tag set kit_create_user_id = '" + admin_usr_id + "' where kit_create_user_id = ?", usr_id);
        upd(con, " update tag set tag_create_user_id = '" + admin_usr_id + "' where tag_create_user_id = ?", usr_id);
        upd(con, " update tag set tag_update_user_id = '" + admin_usr_id + "' where tag_update_user_id = ?", usr_id);
    }

    public static void upd(Connection con, boolean ignoreException, String SQL, long p1) throws SQLException {
        if (ignoreException) {
            try {
                upd(con, SQL, p1);
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            upd(con, SQL, p1);
        }
    }

    public static void upd(Connection con, boolean ignoreException, String SQL, String p1) throws SQLException {
        if (ignoreException) {
            try {
                upd(con, SQL, p1);
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            upd(con, SQL, p1);
        }
    }

    public static void upd(Connection con, boolean ignoreException, String SQL, long p1, String p2) throws SQLException {
        if (ignoreException) {
            try {
                upd(con, SQL, p1, p2);
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            upd(con, SQL, p1, p2);
        }
    }

    public static void upd(Connection con, boolean ignoreException, String SQL, long p1, long p2) throws SQLException {
        if (ignoreException) {
            try {
                upd(con, SQL, p1, p2);
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            upd(con, SQL, p1, p2);
        }
    }

    public static void upd(Connection con, boolean ignoreException, String SQL, long p1, long p2, long p3) throws SQLException {
        if (ignoreException) {
            try {
                upd(con, SQL, p1, p2, p3);
                if (!con.getAutoCommit()) {
                    con.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            upd(con, SQL, p1, p2, p3);
        }
    }

    public static void upd(Connection con, String SQL, long p1) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, p1);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void upd(Connection con, String SQL, long p1, long p2) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, p1);
        stmt.setLong(2, p2);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void upd(Connection con, String SQL, long p1, long p2, long p3) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, p1);
        stmt.setLong(2, p2);
        stmt.setLong(3, p3);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void upd(Connection con, String SQL, String p1) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, p1);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void upd(Connection con, String SQL, long p1, String p2) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, p1);
        stmt.setString(2, p2);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void upd(Connection con, String SQL, String p1, long p2) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, p1);
        stmt.setLong(2, p2);
        stmt.executeUpdate();
        stmt.close();
    }
}