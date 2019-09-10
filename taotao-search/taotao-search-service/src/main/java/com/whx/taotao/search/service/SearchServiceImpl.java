package com.whx.taotao.search.service;

import com.whx.taotao.commons.PicUrlUtil;
import com.whx.taotao.pojo.TbItem;
import com.whx.taotao.search.inter.SearchService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Resource
    private HttpSolrServer httpSolrServer;
    @Value("${nginx_server}")
    private String nginxServer;
    @Override
    public List<TbItem> searchGoodsByKeyWord(String keyword) {
        SolrQuery solrQuery=new SolrQuery();
        if ("all".equals(keyword)) {
            solrQuery.setQuery("*:*");
        }else{
            solrQuery.setQuery("product_search:\""+keyword+"\"");
            solrQuery.setHighlight(true);
            solrQuery.setHighlightSimplePre("<strong style='color:red'>");
            solrQuery.setHighlightSimplePost("</strong>");
            solrQuery.addHighlightField("title");
        }

        List<TbItem> tbItems=new ArrayList<>();
        try {
            QueryResponse queryResponse = httpSolrServer.query(solrQuery);
            Map<String, Map<String, List<String>>> highlightings = queryResponse.getHighlighting();
            SolrDocumentList results = queryResponse.getResults();
//            results.getNumFound();
            for (SolrDocument result : results) {
                TbItem tbItem=new TbItem();
                tbItems.add(tbItem);

                Collection<String> fieldNames = result.getFieldNames();

                for (String fieldName : fieldNames) {
                    Object fieldValue = result.getFieldValue(fieldName);
                    switch (fieldName) {
                        case "title":
                            String hlTitle=null;
                            if (highlightings != null) {
                                hlTitle=  highlightings.get(result.getFieldValue("id")).get("title")==null?fieldValue.toString():highlightings.get(result.getFieldValue("id")).get("title").toString();
                            }else {
                                tbItem.setTitle(fieldValue.toString());
                                hlTitle=tbItem.getTitle();
                            }
                            tbItem.setTitle(hlTitle);
                            break;
                        case "sell_point":
                            tbItem.setSellPoint(fieldValue.toString());
                            break;
                        case "barcode":
                            tbItem.setBarcode(fieldValue.toString());
                            break;
                        case "price":
                            tbItem.setPrice(NumberUtils.createLong(fieldValue.toString()));
                            break;
                        case "num":
                            tbItem.setNum(NumberUtils.createInteger(fieldValue.toString()));
                            break;
                        case "image":

                            String firstImage=fieldValue.toString().split(",")[0];
                            tbItem.setImage(PicUrlUtil.genPullPaths(firstImage,nginxServer));
                            break;
                        case "cid":
                            tbItem.setCid(NumberUtils.createLong(fieldValue.toString()));
                            break;
                        case "id":
                            tbItem.setId(NumberUtils.createLong(fieldValue.toString()));
                            break;
                    }


                }
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return tbItems;
    }

    @Override
    public void addSearchItem(TbItem tbItem) {
        SolrInputDocument solrInputDocumen=new SolrInputDocument();
        solrInputDocumen.addField("id",tbItem.getId() );
        solrInputDocumen.addField("title",tbItem.getTitle() );
        solrInputDocumen.addField("sell_point",tbItem.getSellPoint() );
        solrInputDocumen.addField("barcode",tbItem.getBarcode() );
        solrInputDocumen.addField("price",tbItem.getPrice() );
        solrInputDocumen.addField("num",tbItem.getNum() );
        solrInputDocumen.addField("image",tbItem.getImage() );
        solrInputDocumen.addField("cid",tbItem.getCid() );

        try {
            httpSolrServer.add(solrInputDocumen);
            httpSolrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
