package com.qfedu.studyblog1805.quartz;


import com.qfedu.studyblog1805.spider.CnBlogsPipeline;
import com.qfedu.studyblog1805.spider.CnblogsProcess;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import us.codecraft.webmagic.Spider;

public class BlogJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        CnBlogsPipeline pipeline = (CnBlogsPipeline) dataMap.get("pipeline");
        CnblogsProcess process = (CnblogsProcess) dataMap.get("process");
        new Spider(process).addUrl("https://www.cnblogs.com/").addPipeline(pipeline).thread(6).run();
    }
}
