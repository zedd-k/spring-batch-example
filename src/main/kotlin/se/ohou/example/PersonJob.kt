package se.ohou.example

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource

@Configuration
class PersonJob(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory
) {

    private val RESOURCE_PATH = "${System.getProperty("user.dir")}/src/main/resources"

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
        return FlatFileItemReaderBuilder<Person>()
            .name("csvReader")
            .resource(FileSystemResource("${RESOURCE_PATH}/sample-data.csv"))
            .lineMapper { line, _ ->
                val splitLine = line.split(",")
                Person(splitLine[0], splitLine[1].toInt())
            }.build()
    }

    @Bean
    fun processor(): ItemProcessor<Person, Person> {
        return ItemProcessor {
            Person(it.name.lowercase(), it.age)
        }
    }

    @Bean
    fun writer(): ItemWriter<Person> {
        // Person 객체를 json 파일로 write (파일 위치는 상관없음)
        return JsonFileItemWriterBuilder<Person>()
            .name("jsonWriter")
            .jsonObjectMarshaller(JacksonJsonObjectMarshaller<Person>())
            .resource(FileSystemResource("${RESOURCE_PATH}/sample-data.json"))
            .build()
    }
}