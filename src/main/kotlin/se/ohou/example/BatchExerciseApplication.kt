package se.ohou.example

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class BatchExerciseApplication

fun main(args: Array<String>) {
    runApplication<BatchExerciseApplication>(*args)
}
