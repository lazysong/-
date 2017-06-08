package com.lazysong.gojob.module;

import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.controler.UrlConstructor;

import java.util.Map;

/**
 * Created by lazysong on 2017/4/4.
 */
public class BaseRequestData implements UrlConstructor{
    private int requestCode;

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    private Map<String, Object> data;

    public BaseRequestData() {

    }
    public BaseRequestData(int requestCode) {
        this.requestCode = requestCode;
    }

    public BaseRequestData(int requestCode, Map<String, Object> data) {
        this.requestCode = requestCode;
        this.data = data;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public String constructUrlString(String baseUrlString) {
        StringBuilder builder = new StringBuilder(baseUrlString);
        String userid;
        String keyword;
        switch (requestCode) {
            case RequestCode.USER_EXISTS: // 判断用户是否存在
            case RequestCode.CAT_USER: // 查看用户的信息
            case RequestCode.GET_RECOMAND: // 获取系统推荐的招聘信息
            case RequestCode.CAT_RESUME: // 查看个人简历
            case RequestCode.CAT_MARK_INFO: // 查看个人收藏的招聘信息
            case RequestCode.CAT_MARK_COM: // 查看关注的公司信息
            case RequestCode.CAT_WILLING: // 查看个人求职意向
                userid = (String) data.get("user_id");
                builder.append("/Test/a.scaction?requestCode=" + requestCode)
                        .append("&user_id=" + userid);
                return builder.toString();
            case RequestCode.EDIT_USER:
                builder.append("/Test/a.scaction?requestCode=" + requestCode);
                return builder.toString();
            /*
            * 按照分类查看招聘信息		user_id,分类标准（如地点，行业类别），分类关键字	招聘信息集合（Gson格式）
            * */
            case RequestCode.CAT_BY_CATEGRY:
                userid = (String) data.get("user_id");
                String categry = (String) data.get("categry");
                keyword = (String) data.get("keyword");
                builder.append("/Test/a.scaction?requestCode=" + requestCode)
                        .append("&user_id=" + userid)
                        .append("&categry=" + categry)
                        .append("&keyword=" + keyword);
                return builder.toString();
            /*
            * 搜索关键字		user_id,关键字	招聘信息集合（Gson格式）
            * */
            case RequestCode.SEARCH:
                userid = (String) data.get("user_id");
                keyword = (String) data.get("keyword");
                builder.append("/Test/a.scaction?requestCode=" + requestCode)
                        .append("&user_id=" + userid)
                        .append("&keyword=" + keyword);
                return builder.toString();
            /*
            *   获取地点表			地点集合（Gson格式）
                获取行业类别表			行业集合（Gson格式）
                获取公司表			公司集合（Gson格式）
            * */
            case RequestCode.GET_PLACES:
            case RequestCode.GET_INDUSTRY:
            case RequestCode.GET_COMPANY:
                builder.append("/Test/a.scaction?requestCode=" + requestCode);
                return builder.toString();
            /*
            * 提交个人求职意向		求职意向对象（Gson格式）	是否成功
            * */
            case RequestCode.EDIT_WILLING:
            /*
            * 提交个人简历		简历对象（Gson格式）	是否成功	编辑个人简历
            * */
            case RequestCode.EDIT_RESUME:
                builder.append("/Test/a.scaction?requestCode=" + requestCode);
                return builder.toString();
            /*
            *   收藏招聘信息		user_id,招聘信息编号	是否成功
                取消收藏招聘信息		user_id,招聘信息编号	是否成功
                隐藏招聘信息		user_id,招聘信息编号	是否成功

            * */
            case RequestCode.MARK_POST:
            case RequestCode.UNMARK_POST:
            case RequestCode.HIDE_POST:
                userid = (String) data.get("user_id");
                String postNo = (String) data.get("postNo");
                builder.append("/Test/a.scaction?requestCode=" + requestCode)
                        .append("&user_id=" + userid)
                        .append("&postNo=" + postNo);
                return builder.toString();
            /*
            *   关注某公司		user_id,公司编号	是否成功
                取消关注某公司		user_id,公司编号	是否成功
            * */
            case RequestCode.WATCH_CMP:
            case RequestCode.UNWATCH_CMP:
                userid = (String) data.get("user_id");
                String cmpNo = (String) data.get("cmpNo");
                builder.append("/Test/a.scaction?requestCode=" + requestCode)
                        .append("&user_id=" + userid)
                        .append("&postNo=" + cmpNo);
                return builder.toString();
        }
        return null;
    }
}