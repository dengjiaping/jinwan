package com.bangqu.yinwan.shop.util;

import java.util.List;
import java.util.ArrayList;

/**
 * 名字处理类
 *
 * @author yangyao
 * @version 1.0
 */
public class NameUtil {

    /**
     * 获取姓氏
     *
     * @param name 需要取出姓氏的名字
     * @return String类型的匹配姓氏（例：'王刚',返回'王'）
     */
    public static String getFamiliyName(String name) {
        for (String str : getFamilyNames()) {
            if (name.substring(0, 2).equals(str))
                return name.substring(0, 2).toString();
        }
        return name.substring(0, 1).toString();
    }

    /**
     * 获取尊称
     *
     * @param name 需要获得尊称的名字
     * @param male 需要获得尊称的性别 支持Boolean(true男，false女),Integer(0男,1女),String(男,女)
     * @return String类型的匹配尊称（例：'王刚',返回'王先生';'王美丽',返回'王小姐'）
     */
    public static String getPrefixName(String name, Object male) {
        String prefixName = null;
        for (String str : getFamilyNames()) {
            if (name.substring(0, 2).equals(str)) {
                prefixName = name.substring(0, 2);
                break;
            }
        }
        if (prefixName == null)
            prefixName = name.substring(0, 1);
        return prefixName += (
                male instanceof Boolean ? (Boolean) male ? "男" : "女" :
                        male instanceof Integer ? (Integer) male == 0 ? "男" : "女" :
                                male instanceof String ? (String) male : null).equals("男") ? "先生" : "女士";
    }

    /**
     * 获取复姓姓氏集合
     *
     * @return 返回List<String> 对象
     */
    private static List<String> getFamilyNames() {
        List<String> familyNames = new ArrayList<String>();
        String names = "欧阳、太史、端木、上官、司马、东方、独孤、南宫、万俟、闻人、夏侯、" +
                "诸葛、尉迟、公羊、赫连、澹台、皇甫、宗政、濮阳、公冶、太叔、申屠、公孙、慕容、" +
                "仲孙、钟离、长孙、宇文、司徒、鲜于、司空、闾丘、子车、亓官、司寇、巫马、公西、颛孙、壤驷、" +
                "公良、漆雕、乐正、宰父、谷梁、拓跋、夹谷、轩辕、令狐、段干、百里、呼延、东郭、南门、羊舌、" +
                "微生、公户、公玉、公仪、梁丘、公仲、公上、公门、公山、公坚、左丘、公伯、西门、公祖、第五、" +
                "公乘、贯丘、公皙、南荣、东里、东宫、仲长、子书、子桑、即墨、达奚、褚师";
        String[] list = names.split("、");
        for (int i = 0; i < list.length; i++) {
            familyNames.add(list[i]);
        }
        return familyNames;
    }
}
