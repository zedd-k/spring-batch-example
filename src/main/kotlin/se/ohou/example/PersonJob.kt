package se.ohou.example

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource

@Configuration
class PersonJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun personProcessingJob(): Job = jobBuilderFactory.get("personProcessingJob")
        .flow(personProcessingStep())
        .end()
        .build()

    @Bean
    fun personProcessingStep(): Step {
        // reader, processor, writer를 가진 tasklet step을 생성
        return stepBuilderFactory.get("personProcessingStep")
            .chunk<Person, Person>(1)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean
    fun reader(): ItemReader<Person> {
        // sample-data.csv 파일을 읽어서 Person 도메인 객체로 컨버팅
        val flatFileItemReader = FlatFileItemReader<Person>()
        flatFileItemReader.setResource(ClassPathResource("sample-data.csv"))

        val delimitedLineTokenizer = DelimitedLineTokenizer() // Default: Comma
        delimitedLineTokenizer.setNames("name", "age")

        val defaultLineMapper = DefaultLineMapper<Person>()

        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer)

        val beanWrapperFieldSetMapper = BeanWrapperFieldSetMapper<Person>()
        beanWrapperFieldSetMapper.setTargetType(Person::class.java)

        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper)

        flatFileItemReader.setLineMapper(defaultLineMapper)

        return flatFileItemReader
    }

    @Bean
    fun processor(): ItemProcessor<Person, Person> {
        // Person 객체안에 name을 모두 소문자로 변환
        return ItemProcessor<Person, Person> {
            Person(it.name.lowercase(), it.age)
        }
    }


    @Bean
    fun writer(): ItemWriter<Person> {
        // Person 객체를 json 파일로 write (파일 위치는 상관없음)
        return JsonFileItemWriterBuilder<Person>()
            .name("personJsonWriter")
            .jsonObjectMarshaller(JacksonJsonObjectMarshaller())
            .resource(FileSystemResource("sample-data.json"))
            .build()
    }
}