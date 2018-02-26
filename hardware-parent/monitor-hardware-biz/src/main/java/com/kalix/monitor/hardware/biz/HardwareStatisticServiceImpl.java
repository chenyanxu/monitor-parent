package com.kalix.monitor.hardware.biz;


import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.kalix.framework.core.api.persistence.JpaStatistic;
import com.kalix.framework.core.api.persistence.JsonData;
import com.kalix.framework.core.api.persistence.JsonStatus;
import com.kalix.framework.core.api.web.model.QueryDTO;
import com.kalix.framework.core.impl.biz.ShiroGenericBizServiceImpl;
import com.kalix.framework.core.impl.dao.CommonMethod;
import com.kalix.framework.core.util.SerializeUtil;
import com.kalix.monitor.hardware.api.biz.IHardwareStatisticService;
import com.kalix.monitor.hardware.api.dao.IHardwareStatisticDao;
import com.kalix.monitor.hardware.entities.HardwareLogBean;

import javax.persistence.Tuple;
import java.util.*;


/**
 * Created by fj on 2017-8-17.
 */
public class HardwareStatisticServiceImpl extends ShiroGenericBizServiceImpl<IHardwareStatisticDao, HardwareLogBean> implements IHardwareStatisticService {
    private JsonStatus jsonStatus = new JsonStatus();


    @Override
    public JsonData getHardwareStatistic(String jsonStr) {
        JsonData d1 = new JsonData();
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> barMap = new HashMap<>();
        Map<String, Object> jsonMap = null;
        String chartTitle="";
        int arraySize=0;
        String groupSelectValue="";
        String NoSelect="";
        List list_yc=null;
        String sql =null;
        String sql_yc=null;
        String sort="";
        if (jsonStr != null && !jsonStr.isEmpty()) {
            jsonMap = SerializeUtil.jsonToMap(jsonStr);

            String chartSelectValue=(String)jsonMap.get("chartSelectValue");
            String hardwareSelectValue=(String)jsonMap.get("hardwareSelectValue");
            String beginDate=(String)jsonMap.get("creationDate:begin:gt");
            String endDate=(String)jsonMap.get("creationDate:end:lt");

            if ("1".equals(chartSelectValue)) {
                chartTitle="异常比例统计";
                sql="select t.* from (SELECT to_number(to_char(d.creationdate, 'mm'),'99G999D9S') as month,count(id)  from monitor_hardware_log d where d.creationDate>='"+beginDate+"' and d.creationDate<= '"+endDate+"' GROUP BY month order by month) t";
                NoSelect="chartSelectValue,hardwareSelectValue,creationDate:begin:gt,creationDate:end:lt";
                sql += CommonMethod.createWhereConditionSelect(jsonStr,sort,NoSelect);
            } else if ("2".equals(chartSelectValue)) {
                if("2".equals(hardwareSelectValue))
                {
                    chartTitle="内存信息统计";
                    sql="select t.* from (select memory,count(id) from monitor_hardware_info d where d.creationDate>='"+beginDate+"' and d.creationDate<= '"+endDate+"' GROUP BY d.memory) t ";
                    NoSelect="chartSelectValue,hardwareSelectValue,creationDate:begin:gt,creationDate:end:lt";
                    sql += CommonMethod.createWhereConditionSelect(jsonStr,sort,NoSelect);
                }
                else if("1".equals(hardwareSelectValue)){
                    chartTitle="操作系统类型统计";
                    NoSelect="chartSelectValue,hardwareSelectValue,creationDate:begin:gt,creationDate:end:lt";
                    sql="select t.* from (select split_part(split_part(d.computer,'；',2),'：',2) as yb ,count(id) from monitor_hardware_info d where d.creationDate>='"+beginDate+"' and d.creationDate<= '"+endDate+"' GROUP BY yb) t";
                    sql += CommonMethod.createWhereConditionSelect(jsonStr,sort,NoSelect);
                }
                else
                {
                    chartTitle="硬盘容量统计";
                    NoSelect="chartSelectValue,hardwareSelectValue,creationDate:begin:gt,creationDate:end:lt";
                    sql="select t.* from (select split_part(split_part(d.networkadapter,'；',2),'：',2) as yb ,count(id) from monitor_hardware_info d where d.creationDate>='"+beginDate+"' and d.creationDate<= '"+endDate+"' GROUP BY yb) t";
                    sql += CommonMethod.createWhereConditionSelect(jsonStr,sort,NoSelect);
                }

            } else {
                chartTitle="异常趋势统计";
                sql_yc="select t.* from (select '异常数量' ,count(mac) as status from (SELECT mac from monitor_hardware_log d where d.creationDate>='"+beginDate+"' and d.creationDate<= '"+endDate+"' GROUP BY d.mac) as yc) t";
                sql="select t.* from (select '装机数量' as zj,count(mac) from monitor_hardware_info) t";
                NoSelect="chartSelectValue,hardwareSelectValue,creationDate:begin:gt,creationDate:end:lt";
                sql_yc += CommonMethod.createWhereConditionSelect(jsonStr,sort,NoSelect);
                list_yc=dao.findArrayByNativeSql(sql_yc);
            }



            List list=dao.findArrayByNativeSql(sql);
            if(list!=null&&list.size()!=0)
            {
                if(list_yc!=null&&list_yc.size()!=0)
                {
                    arraySize=list.size()+list_yc.size();
                }
                else
                {
                    arraySize=list.size();
                }

                String[] types = new String[arraySize];
                int[] datas = new int[arraySize];
                for (int i=0; i<list.size(); i++) {
                    Object[] cells = (Object[]) list.get(i);
                    if ("1".equals(chartSelectValue))
                    {
                        types[i] = String.valueOf(cells[0])+"月";
                        datas[i] = Integer.parseInt(cells[1].toString());

                    }
                    else if("2".equals(chartSelectValue)) {
                        types[i] = String.valueOf(cells[0]);
                        datas[i] = Integer.parseInt(cells[1].toString());
                    }
                    else
                    {
                        types[i] = String.valueOf("正常数量");
                        int ycsl=0;
                        if(list_yc!=null&&list_yc.size()!=0)
                        {

                            for (int j=0; j<list_yc.size(); j++) {
                                Object[] cells_yc = (Object[]) list_yc.get(j);
                                types[i+list.size()] = String.valueOf(cells_yc[0]);
                                ycsl=Integer.parseInt(cells_yc[1].toString());
                                datas[i+list.size()] = ycsl;

                            }
                        }
                        datas[i] = Integer.parseInt(cells[1].toString())-ycsl;
                    }


                }

                String chartData = "";
                if ("2".equals(chartSelectValue)) {
                    chartData = pieChart(types, datas, chartTitle);
                } else if ("1".equals(chartSelectValue)) {
                    chartData = lineChart(types                                                            , datas, chartTitle);
                } else {
                    chartData = pieChart(types, datas, "异常比例统计");
                }
                barMap.put("option", chartData);
                dataList.add(barMap);
                d1.setData(dataList);
            }

        }
        return d1;
    }

    /**
     * 饼图Options
     * @param types
     * @param datas
     * @param chartTitle
     * @return
     */
    private String pieChart(String[] types, int[] datas, String chartTitle) {
        GsonOption option = new GsonOption();
        option.title().text(chartTitle).x("center");// 大标题、标题位置
        // 提示工具 鼠标在每一个数据项上，触发显示提示数据
        option.tooltip().trigger(Trigger.item).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.legend().orient(Orient.horizontal).x("left").data(types);// 图例及位置
        option.calculable(true);// 拖动进行计算
        Pie pie = new Pie();
        // 标题、半径、位置
        pie.name(chartTitle).radius("80%").center("50%", "60%");
        // 循环数据
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("value", datas[i]);
            map.put("name", types[i]);
            pie.data(map);
        }
        option.series(pie);
        return option.toString();
    }

    /**
     * 折线图Options
     * @param types
     * @param datas
     * @param chartTitle
     * @return
     */
    private String lineChart(String[] types, int[] datas, String chartTitle) {
        GsonOption option = new GsonOption();
        option.title(chartTitle); // 标题
        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式
        option.legend(chartTitle);// 图例
        Line line = new Line();
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(types);// 轴数据类别
        // 循环数据
        for (int i = 0; i < datas.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", datas[i]);
            line.data(map);
        }
        option.xAxis(category);// x轴
        option.yAxis(new com.github.abel533.echarts.axis.ValueAxis());// y轴
        option.series(line);
        return option.toString();
    }

    /**
     * 柱状图Options
     * @param types
     * @param datas
     * @param chartTitle
     * @return
     */
    private String barChart(String[] types, int[] datas, String chartTitle){
        String title = chartTitle;
        GsonOption option = new GsonOption();
        option.title(title); // 标题
        // 工具栏
//        option.toolbox().show(true).feature(Tool.mark, // 辅助线
//                Tool.dataView, // 数据视图
//                new MagicType(Magic.line, Magic.bar),// 线图、柱状图切换
//                Tool.restore,// 还原
//                Tool.saveAsImage);// 保存为图片
        option.tooltip().show(true).formatter("{a} <br/>{b} : {c}");//显示工具提示,设置提示格式
        option.legend(title);// 图例
        Bar bar = new Bar(title);// 图类别(柱状图)
        CategoryAxis category = new CategoryAxis();// 轴分类
        category.data(types);// 轴数据类别
        // 循环数据
        for (int i = 0; i < datas.length; i++) {
            int data = datas[i];
            Map<String, Object> map = new HashMap<>();
            map.put("value", data);
            bar.data(map);
        }
        option.xAxis(category);// x轴
        option.yAxis(new com.github.abel533.echarts.axis.ValueAxis());// y轴
        option.series(bar);
        return option.toString();
    }
}
