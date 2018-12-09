package com.qfedu.studyblog1805.app;

import com.qfedu.studyblog1805.quartz.BlogJob;
import com.qfedu.studyblog1805.spider.CnBlogsPipeline;
import com.qfedu.studyblog1805.spider.CnblogsProcess;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

/**
 *@Author feri
 *@Date Created in 2018/11/22 16:37
 */
@RestController
public class BlogSpiderController {
    @Autowired
    private CnBlogsPipeline pipeline;
    @Autowired
    private CnblogsProcess process;


    //这是没有定时任务的爬取
    @GetMapping("blogspider")
    public String startSpider()
    {
        System.out.println("开始爬取……");
        new Spider(process).addUrl("https://www.cnblogs.com/").addPipeline(pipeline).thread(6).run();
        return "爬虫结束……";
    }

    //这是定时任务爬取
    @GetMapping("quartzspider")
    public String quartzSpider() throws SchedulerException {
        System.out.println("开始爬取……");
        //创建执行器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        //创建触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("blog","blog").withSchedule(CronScheduleBuilder.cronSchedule("* 0/5 * * * ?")).build();
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(BlogJob.class).build();
        //创建map
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        //进行爬虫，并且处理数据
        jobDataMap.put("process",process);
        jobDataMap.put("pipeline",pipeline);

        //添加执行工作
        scheduler.scheduleJob(jobDetail,trigger);
        //开始执行
        scheduler.start();
        return "定时任务已添加，等待触发";
    }



}
