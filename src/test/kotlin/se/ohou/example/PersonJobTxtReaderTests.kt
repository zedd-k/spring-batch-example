package se.ohou.example

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.core.io.ClassPathResource

class PersonJobTxtReaderTests {

    @Test
    fun `txtReader should read data correctly from sample-data txt`() {
        val reader = FlatFileItemReaderBuilder<Person>()
            .name("txtPersonReaderTest")
            .resource(ClassPathResource("sample-data.txt")) // Reads from src/test/resources
            .linesToSkip(1) // Skip header
            .delimited().delimiter("|")
            .names("id", "name", "age", "region")
            .fieldSetMapper(BeanWrapperFieldSetMapper<Person>().apply {
                setTargetType(Person::class.java)
            })
            .build()

        val persons = mutableListOf<Person>()
        try {
            reader.open(ExecutionContext())
            var person: Person?
            while (reader.read().also { person = it } != null) {
                persons.add(person!!)
            }
        } finally {
            reader.close()
        }

        // Assertions
        assertEquals(6, persons.size, "Should read 6 persons from the file")

        assertEquals("John", persons[0].name, "First person's name should be John")
        assertEquals(20, persons[0].age, "First person's age should be 20")

        assertEquals("Alice", persons[5].name, "Last person's name should be Alice")
        assertEquals(25, persons[5].age, "Last person's age should be 25")
    }
}
