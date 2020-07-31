package com.foreign.exchange.service.impl;

import com.foreign.exchange.pojo.FinanceRatePrice;
import com.foreign.exchange.pojo.RmbQuote;
import com.foreign.exchange.service.FinanceRateMarketService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.*;

/**
 * @author
 * @create 2020-07-20-15:43
 *
 */
@Service
public class FinanceRateMarketServiceImpl implements FinanceRateMarketService {

    public  static  final  String RATE_URL_PREFIX = "https://srh.bankofchina.com/search/whpj/searchen.jsp";

    @Override
    public RmbQuote getPrice(String rateCode) {

        return  (RmbQuote) getExchangeRate(rateCode,"").get(0);
    }

    /**
     * 获取当日传入币种汇率信息
     * @param rateCode 币种
     * @param date 日期
     * @return
     */
    private  List getExchangeRate(String rateCode,String date){

        //判断date是否为空，如果为空则返回当前信息
        String isToday = StringUtils.isEmpty(date)? new DateTime().toString("yyyy-MM-dd"):date;
        List list = new ArrayList();
        //中行大概5分钟更新一次数据，设置为15页，基本能覆盖1天的数据内容
        for (int page = 1;page<=15; page++){
            //抓取时间为isToday,币种为sourceCurrency，页数为page的中国银行网页信息
            String serachEnHtml = getSerachEnHtml(isToday,rateCode,String.valueOf(page));

//            System.out.println(serachEnHtml);
            //开始解析html中的汇率列表信息
            Map map = assembleObjByHtml(serachEnHtml);

            String flag = (String) map.get("flag");
            String htmlPage = (String)map.get("page");

            List mapList = (List)map.get("list");
            for (Iterator iterator =mapList.iterator();iterator.hasNext();list.add(iterator.next())){
            }

            //当flag为1执行成功时，或总页数等于循环查到的页数时，则不需要再次进行查询
            if ("1".equals(flag)||Integer.parseInt(htmlPage)<page){
                break;
            }
        }

        return list;

    }
    /**
     *页面取得的页面，解析html中的内容，先不做业务逻辑，全部查询
     * @param html 要解析的html
     * @return
     */
    private  Map assembleObjByHtml(String html){
        //存储数据
        Map map = new HashMap(5);

        //使用Jsoup将html解析为document对象
        Document document = Jsoup.parse(html);

        //获取页面隐藏域中存放的当前页面
        Elements pageItem = document.getElementsByAttributeValue("name","page");
        String pageItemValue = "";
        pageItemValue = pageItem.select("input[name=page]").val();
        map.put("page",pageItemValue);

        //获取页面的整个table信息,这个返回的页面基本上是多个table,下面需要下面处理
        Elements tables = document.getElementsByTag("table");

        //设置存放汇率信息的table下标为-1（默认不存在）
        int tableIndex = -1;

        //从table中循环获取，查找含有Currency Name字段的table
        for(int i=0;i<tables.size();i++){
            Element element = tables.get(i);
            String text = element.text();
            //找到含有汇率信息的table,给tableIndex赋值，跳出循环
            if (text.indexOf("Currency Name") > -1){
                tableIndex = i;
                break;
            }

        }
        List<RmbQuote> list = new ArrayList();
        //如果找到汇率列表信息
        if (tableIndex > -1){
            Element table = tables.get(tableIndex);
            //遍历该表格内的所有<tr></tr>
            Elements trs = table.select("tr");
            for (int i=1;i<trs.size();++i){
                RmbQuote rmbQuote = new RmbQuote();
                Element tr = trs.get(i);
                //将数据放入实体对象中
                Elements tds = tr.select("td");
                rmbQuote.setCurrencyCode(tds.get(0).text());
                rmbQuote.setFbuyPrice(new BigDecimal(tds.get(1).text()));
                rmbQuote.setMbuyPrice(new BigDecimal(tds.get(2).text()));
                rmbQuote.setFsellPrice(new BigDecimal(tds.get(3).text()));
                rmbQuote.setMsellPrice(new BigDecimal(tds.get(4).text()));
                rmbQuote.setBankConversionPrice(new BigDecimal(tds.get(5).text()));
                rmbQuote.setCreatedTime(tds.get(6).text());
                list.add(rmbQuote);
            }
            map.put("list",list);
        }else {
            map.put("flag","1");
        }


        return  map;
    }


    /**
     * 获取整个网页的内容
     * @param isToday 传入当前时间或空
     * @param rateCode 币种
     * @param liPage 当前查询页数
     * @return
     */
    private  String getSerachEnHtml(String isToday,String rateCode,String liPage){
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String Url = RATE_URL_PREFIX+"?erectDate={erectDate}&nothing={nothing}&pjname={pjname}&page={page}";

        Map<String,String> map = new HashMap<>();
        map.put("erectDate",isToday);
        map.put("nothing",isToday);
        map.put("pjname",rateCode);
        map.put("page",liPage);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(Url,String.class,map);
        if (responseEntity.getStatusCodeValue() != 200){
            throw   new  RuntimeException("获取外汇价格失败！");
        }
        return  responseEntity.getBody();

    }


    //单元测试
//    public static void main(String[] args) {
//        List list = new ArrayList();
//        //list = FinanceRateMarketServiceImpl.getExchangeRate("USD","");
//        //String result = FinanceRateMarketServiceImpl.getSerachEnHtml("20200727","USD","1");
//        FinanceRateMarketService financeRateMarketService = new FinanceRateMarketServiceImpl();
//        System.out.println( financeRateMarketService.getPrice("USD").toString());
//      System.out.println("总数："+list.size());
//      System.out.println("汇总："+list.get(0).toString());
//    }

}
