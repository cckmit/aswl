package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ContentMalfunction;
import com.aswl.as.ibrs.mapper.ContentMalfunctionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Slf4j
@Service
public class ContentMalfunctionService extends CrudService<ContentMalfunctionMapper, ContentMalfunction> {
    private final ContentMalfunctionMapper contentMalfunctionMapper;

    //最小添加数
    private static final int minHeatAddCount=2;

    //最大添加数
    private static final int maxHeatAddCount=12;

    /**
     * 新增常见故障表
     *
     * @param contentMalfunction
     * @return int
     */
    @Transactional
    @Override
    public int insert(ContentMalfunction contentMalfunction) {
        return super.insert(contentMalfunction);
    }

    /**
     * 删除常见故障表
     *
     * @param contentMalfunction
     * @return int
     */
    @Transactional
    @Override
    public int delete(ContentMalfunction contentMalfunction) {
        return super.delete(contentMalfunction);
    }

    /**
     * 常见故障表查看数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addClicks(String id) {
        return contentMalfunctionMapper.addClicks(id);
    }

    /**
     * 常见故障表 点赞数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addLikes(String id,Integer addCount) {
        return contentMalfunctionMapper.addLikes(id,addCount);
    }

    // 校验更新数量 TODO
//    @Transactional
    public int addHeatCount(List<ContentMalfunction> list)
    {
        int hCount=3600000;

        Date now=new Date();
        long n=now.getTime();

        for(ContentMalfunction temp:list)
        {
            if(
                    (temp.getHeatCount()!=null && temp.getHeatCount()>=temp.getHeatMaxCount())
                    ||
                    (temp.getLastHeatTime()!=null && now.getTime()-temp.getLastHeatTime().getTime()<hCount)
            )
            {
                continue;
            }

            //如果距离最后时间相差一小时，就要按小时加上去 //TODO 遍历加上去
            if(temp.getLastHeatTime()!=null && temp.getHeatCount()!=null )
            {
                //保存现在的时间（根据相差的时间数添加数量）
                for(long i=temp.getLastHeatTime().getTime();i<n;i+=hCount)
                {
                    if(temp.getHeatCount()<temp.getHeatMaxCount())
                    {
                        temp.setHeatCount(temp.getHeatCount()+getNextAddCount());
                    }
                }
            }
            else
            {
                temp.setHeatCount(getNextAddCount());
            }
            temp.setLastHeatTime(new Date());
            update(temp);
        }

        return 1;
    }

    private int getNextAddCount()
    {
        return minHeatAddCount+(new Random().nextInt(maxHeatAddCount-minHeatAddCount));
    }


}