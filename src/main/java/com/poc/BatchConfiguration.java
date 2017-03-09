package com.poc;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job(Step step){
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step sep(){
        return stepBuilderFactory.get("step")
                .chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

    }

    @Bean
    public ItemWriter writer() {
        ItemWriter itemWriter = new ItemWriter() {
            @Override
            public void write(List list) throws Exception {
                for(Object o : list) {
                    System.out.println(o);
                }
                System.out.println("passou");
            }
        };
        return itemWriter;
    }

    @Bean
    public ItemProcessor<? super Object, ?> processor() {
        ItemProcessor itemProcessor = new ItemProcessor() {
            @Override
            public Object process(Object o) throws Exception {
                return o.toString() + " de Tal";
            }
        };
        return itemProcessor;
    }

    @Bean
    public ItemReader<String> reader() {
        List<String> list = new ArrayList<>();
        list.add("Fulano");
        list.add("Bertano");
        list.add("Ciclano");



        ListItemReader listItemReader = new ListItemReader(list);
        return listItemReader;
    }
}
