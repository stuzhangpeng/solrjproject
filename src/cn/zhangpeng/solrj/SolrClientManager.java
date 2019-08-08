package cn.zhangpeng.solrj;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**solr客户端，solrj的增删查改
 * @Auther:zhangpeng
 * @Date:2019/8/8
 * @Description:cn.zhangpeng.solrj
 * @Version:1.0
 */
public class SolrClientManager {
/**
*功能描述：获取solrJ客户端
*@param:
*@return:SolrClient
*@author:
*/
    public SolrClient getSolrClient(){
        //设置solr的连接地址并指定核
        String baseUrl="http://localhost:8080/solr/new_core";
        SolrClient solrClient =new HttpSolrClient.Builder(baseUrl).build();
        return solrClient;
    }
    /**
    *功能描述：通过solrclient增加index
    *@param:null
    *@return:void
    *@author:zhangpeng
    */
    @Test
    public  void addIndexBySolrClient() throws IOException, SolrServerException {
        //获得solr客户端
        SolrClient solrClient = getSolrClient();
        //创建solrinputdocument对象
        SolrInputDocument document =new SolrInputDocument();
        //设置field域
        document.setField("id", "ccc");
        document.setField("title","通过客户端添加的内容");
        document.setField("image","http://img12.360buyimg.com/n1/s450x450_jfs/t3034/299/2060854617/119711/577e85cb/57d11b6cN1fd1194d.jpg");
        solrClient.add(document);
        //提交
        solrClient.commit();
    }
    /**
    *功能描述：通过solrclient删除index
    *@param:null
    *@return:void
    *@author:zhangpeng
    */
    @Test
    public  void deleteIndexBySolrClient() throws IOException, SolrServerException {
        //获得solr客户端
        SolrClient solrClient = getSolrClient();
       // 删除所有(域名：域值)
         //  solrClient.deleteByQuery("*:*");solrClient.deleteById(通过id查询)
        solrClient.deleteByQuery("id:aaa");
        solrClient.commit();
    }
    /**
    *功能描述：通过solrclient更新index
    *@param:null
    *@return:void
    *@author:zhangpeng
    */
    @Test
    public  void updateIndexBySolrClient() throws IOException, SolrServerException {
        //获得solr客户端
        SolrClient solrClient = getSolrClient();
        //通过add方法添加，id相同即为更新
        SolrInputDocument document =new SolrInputDocument();
        //设置field域
        document.setField("id", "bbb");
        document.setField("title","通过更新客户端添加的内容");
        document.setField("image","http://img12.360buyimg.com/n1/s450x450_jfs/t3034/299/2060854617/119711/577e85cb/57d11b6cN1fd1194d.jpg");
        solrClient.add(document);
        solrClient.commit();
    }
    /**
    *功能描述：通过solrclient查找index
    *@param:null
    *@return:void
    *@author:zhangpeng
    */
    @Test
    public  void findIndexBySolrClient() throws IOException, SolrServerException {
        //获得solr客户端
        SolrClient solrClient = getSolrClient();
        //设置查询参数
        SolrQuery params =new SolrQuery();
//      params.set("q", "id:bbb"); 设置查询参数
        params.setQuery("有货");
        //设置过滤条件
        params.setFilterQueries("title:联通");
        //排序
        params.setSort("price", SolrQuery.ORDER.asc);
        //设置分页,开始的文档
        params.setStart(0);
        //设置显示的条数
        params.setRows(10);
        //设置高亮开关
       // params.setHighlight(true);
        //添加高亮的field
        params.addHighlightField("sell_pointm                                                                           v vvvv ");
        //设置返回结果包含的域名
        params.set("fl", "id","title","image","sell_point");
        //设置默认查询域
        params.set("df","sell_point");
        //指定高亮的前后缀
       params.setHighlightSimplePre("<span style='color:red'>");
       params.setHighlightSimplePost("</span >");
        //执行查询
        QueryResponse response = solrClient.query(params);
        //获得常规结果集
        SolrDocumentList results = response.getResults();
        //获得结果条数
        long numFound = results.getNumFound();
        System.out.println("num:"+numFound);
        //获得高亮的结果集,大map的key为id,小map的key为field value为list
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        //遍历doucument集合
        for(SolrDocument solrDocument:results){
            Object id = solrDocument.get("id");
            Object image = solrDocument.get("image");
            Object title= solrDocument.get("title");
            Object sell_point= solrDocument.get("sell_point");
            //Object price = solrDocument.get("price");
            //通过key获取高亮的数据
            Map<String, List<String>> stringListMap = highlighting.get(id);
            //通过field获取list
            List<String> titles = stringListMap.get("sell_point");
            System.out.println("highlighting:"+titles.get(0));
            System.out.println("..............................");
            System.out.println("id:"+id);
            System.out.println("image:"+image);
            System.out.println("title:"+title);
            System.out.println("sell_point:"+sell_point);
            // System.out.println(price);
            System.out.println(">>>>>>>>>>>>>>>>>>>>");
        }
    }
}
