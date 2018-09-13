package com.ztdx.eams.domain.archives.application;

import com.ztdx.eams.basic.UserCredential;
import com.ztdx.eams.basic.exception.InvalidArgumentException;
import com.ztdx.eams.basic.utils.DateUtils;
import com.ztdx.eams.domain.archives.model.DescriptionItem;
import com.ztdx.eams.domain.archives.model.DescriptionItemDataType;
import com.ztdx.eams.domain.archives.model.Entry;
import com.ztdx.eams.domain.archives.model.PropertyType;
import com.ztdx.eams.domain.archives.repository.DescriptionItemRepository;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.objects.Global.Infinity;

@Service
public class DescriptionItemService {

    private final DescriptionItemRepository descriptionItemRepository;

    private final ElasticsearchOperations elasticsearchOperations;

    @Autowired
    public DescriptionItemService(DescriptionItemRepository descriptionItemRepository, ElasticsearchOperations elasticsearchOperations) {
        this.descriptionItemRepository = descriptionItemRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public <R> Map<String, R> list(int catalogueId, Function<DescriptionItem, R> map) {
        return descriptionItemRepository.findByCatalogueId(catalogueId).stream()
                .collect(
                        Collectors.toMap(
                                DescriptionItem::getMetadataName
                                , map
                                , (d1, d2) -> d2));
    }

    public List<DescriptionItem> findAllById(Collection<Integer> ids) {
        return descriptionItemRepository.findAllById(ids);
    }

    public List<DescriptionItem> findAllByCatalogueIdIn(Collection<Integer> ids) {
        return descriptionItemRepository.findByCatalogueIdIn(ids);
    }

    public List<DescriptionItem> findByCatalogueId(int catalogueId) {
        return descriptionItemRepository.findByCatalogueId(catalogueId);
    }

//    //新增条目数据验证
//    public Entry addVerification(Entry entry, UserCredential userCredential) {
//        //获取目录著录项
//        List<DescriptionItem> descriptionItemList = descriptionItemRepository.findByCatalogueId(entry.getCatalogueId());
//        //著录项数据
//        Map<String, Object> dataMap = entry.getItems();
//        //著录项验证
//        for (DescriptionItem descriptionItem : descriptionItemList) {
//            //获取著录项名称
//            String metadataName = descriptionItem.getMetadataName();
//            //获取数据
//            Object value = entry.getItems().get(metadataName);
//            //是否自增
//            if (descriptionItem.getIsIncrement() == 1) {
//                SearchRequestBuilder srBuilder = elasticsearchOperations.getClient().prepareSearch("archive_record_" + entry.getCatalogueId());
//                srBuilder.setTypes("record");
//                srBuilder.addAggregation(AggregationBuilders.max("max").field(metadataName));
//                double max = ((Max) srBuilder.get().getAggregations().getAsMap().get("max")).getValue();
//                if (max == (-Infinity)) {
//                    dataMap.put(metadataName, 1);
//                } else {
//                    dataMap.put(metadataName, (int) max + descriptionItem.getIncrement());
//                }
//            } else {
//                //是否可空
//                //如果著录项不能为空，并且数据也为空
//                if ((descriptionItem.getIsNull() == 0 && "" == value) || (descriptionItem.getIsNull() == 0 && null == value)) {
//                    throw new InvalidArgumentException(descriptionItem.getDisplayName() + "不能为空");
//                    //如果著录项可为空，并且数据也为空且有默认值
//                } else if ((descriptionItem.getIsNull() == 1 && "" == value) || (descriptionItem.getIsNull() == 1 && null == value)) {
//
//                    switch (descriptionItem.getDefaultValue()) {
//                        //当前登录人姓名
//                        case LoginUserName: {
//                            dataMap.put(metadataName, userCredential.getName());
//                            break;
//                        }
//                        //当前系统年度
//                        case SystemYear: {
//                            dataMap.put(metadataName, DateUtils.getCurrentYear());
//                            break;
//                        }
//                        case SystemDate_yyyy_MM_dd: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("yyyy-MM-dd"));
//                            break;
//                        }
//                        case SystemDate_yyyyMMdd: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("yyyyMMdd"));
//                            break;
//                        }
//                        case SystemDateTime: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//                            break;
//                        }
//                        case SystemTime: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("HH:mm:ss"));
//                            break;
//                        }
//                    }
//                    //数据不为空时
//                } else if ("" != value && null != value) {
//                    //著录项类型为数值型
//                    if (descriptionItem.getDataType() == DescriptionItemDataType.Integer) {
//                        try {
//                            Integer.parseInt(String.valueOf(value));
//                        } catch (NumberFormatException e) {
//                            throw new InvalidArgumentException(descriptionItem.getDisplayName() + "必须为数值");
//                        }
//                    }
//                    //著录项类型为日期型
//                    if (descriptionItem.getDataType() == DescriptionItemDataType.Date) {
//                        if (!checkString("((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$", (String) value)) {
//                            throw new InvalidArgumentException(descriptionItem.getDisplayName() + "必须为yyyy-MM-dd日期格式");
//                        }
//                    }
//                    //著录项类型为数值型
//                    if (descriptionItem.getDataType() == DescriptionItemDataType.Double) {
//                        try {
//                            Double.parseDouble(String.valueOf(value));
//                        } catch (NumberFormatException e) {
//                            throw new InvalidArgumentException(descriptionItem.getDisplayName() + "必须为浮点数值");
//                        }
//                    }
//                }
//            }
//        }
//        entry.setItems(dataMap);
//        return entry;
//    }
//
//    //修改条目数据验证
//    public Entry updateVerification(Entry entry, UserCredential userCredential) {
//        //获取目录著录项
//        List<DescriptionItem> descriptionItemList = descriptionItemRepository.findByCatalogueId(entry.getCatalogueId());
//        //著录项数据
//        Map<String, Object> dataMap = entry.getItems();
//        //著录项验证
//        for (DescriptionItem descriptionItem : descriptionItemList) {
//            //获取著录项名称
//            String metadataName = descriptionItem.getMetadataName();
//            //获取值
//            Object value = dataMap.get(metadataName);
//            //是否只读
//            if (descriptionItem.getIsRead() == 0) {
//                dataMap.put(metadataName, null);
//            } else {
//                //如果著录项不能为空，并且数据也为空
//                if ((descriptionItem.getIsNull() == 1 && "" == value) || (descriptionItem.getIsNull() == 1 && null == value)) {
//                    throw new InvalidArgumentException(descriptionItem.getDisplayName() + "不能为空");
//                    //如果著录项可为空，并且数据也为空且有默认值
//                } else if ((descriptionItem.getIsNull() == 0 && "" == value) || (descriptionItem.getIsNull() == 0 && null == value)) {
//
//                    switch (descriptionItem.getDefaultValue()) {
//                        //当前登录人姓名
//                        case LoginUserName: {
//                            dataMap.put(metadataName, userCredential.getName());
//                            break;
//                        }
//                        //当前系统年度
//                        case SystemYear: {
//                            dataMap.put(metadataName, DateUtils.getCurrentYear());
//                            break;
//                        }
//                        case SystemDate_yyyy_MM_dd: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("yyyy-MM-dd"));
//                            break;
//                        }
//                        case SystemDate_yyyyMMdd: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("yyyyMMdd"));
//                            break;
//                        }
//                        case SystemDateTime: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//                            break;
//                        }
//                        case SystemTime: {
//                            dataMap.put(metadataName, DateUtils.getCurrentDateTime("HH:mm:ss"));
//                            break;
//                        }
//                    }
//                    //数据不为空时
//                } else if ("" != value && null != value) {
//                    //著录项类型为数值型
//                    if (descriptionItem.getDataType() == DescriptionItemDataType.Integer) {
//                        try {
//                            Integer.parseInt(String.valueOf(value));
//                        } catch (NumberFormatException e) {
//                            throw new InvalidArgumentException(descriptionItem.getDisplayName() + "必须为数值");
//                        }
//                    }
//                    //著录项类型为日期型
//                    if (descriptionItem.getDataType() == DescriptionItemDataType.Date) {
//                        if (!checkString("((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$", (String) value)) {
//                            throw new InvalidArgumentException(descriptionItem.getDisplayName() + "必须为yyyy-MM-dd日期格式");
//                        }
//                    }
//                    //著录项类型为数值型
//                    if (descriptionItem.getDataType() == DescriptionItemDataType.Double) {
//                        try {
//                            Double.parseDouble(String.valueOf(value));
//                        } catch (NumberFormatException e) {
//                            throw new InvalidArgumentException(descriptionItem.getDisplayName() + "必须为浮点数值");
//                        }
//                    }
//                }
//            }
//
//        }
//        entry.setItems(dataMap);
//        return entry;
//    }

//    public boolean checkString(String pattern, String string) {
//        return string.matches(pattern);
//    }

    public DescriptionItem findByCatalogueIdAndPropertyType(int catalogueId, PropertyType boxNumber) {
        return descriptionItemRepository.findByCatalogueIdAndPropertyType(catalogueId, boxNumber);
    }
}
