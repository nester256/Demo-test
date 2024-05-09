package com.example.demo_test

import com.example.demo.DemoApplication
import com.example.demo.model.dto.ServicesDto
import com.example.demo.model.filter.ServiceFilter
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

import java.nio.charset.StandardCharsets

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc
class ServiceControllerTest extends Specification {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(
                    cmd -> cmd.withHostConfig(
                            HostConfig.newHostConfig().withPortBindings(
                                    new PortBinding(
                                            Ports.Binding.bindPort(5432), new ExposedPort(5432)
                                    )
                            )
                    )
            )

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.port", postgres::getMappedPort);
    }

    def setupSpec() {
        postgres.start()
    }

    def cleanupSpec() {
        postgres.stop()
    }


    def "findAll method should successfully find data by search filter"() {
        given:
        def filter = ServiceFilter.builder()
                .search("Angular")
                .build()
        def expected = ServicesDto.builder()
                .id(2)
                .name("Frontend")
                .language("Angular").build()
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/services/find-all")
                        .content(objectMapper.writeValueAsString(filter))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        then:
        def actual = objectMapper.readValue(
                result.getContentAsByteArray(), new TypeReference<CustomPageImpl<ServicesDto>>() {})
        actual.content[0] == expected
    }

    def "findAll method should successfully find all data with empty search filter"() {
        given:
        def filter = ServiceFilter.builder().search("").build()
        def expected = [
                ServicesDto.builder().id(1).name("Backend").language("Java").build(),
                ServicesDto.builder().id(2).name("Frontend").language("Angular").build(),
                ServicesDto.builder().id(3).name("PaymentAPI").language(".NET").build(),
                ServicesDto.builder().id(4).name("CustomerCabinet").language("Java").build(),
                ServicesDto.builder().id(5).name("Scheduler").language("Java").build()
        ]

        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/services/find-all")
                        .content(objectMapper.writeValueAsString(filter))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        then:
        def actual = objectMapper.readValue(
                result.getContentAsByteArray(), new TypeReference<CustomPageImpl<ServicesDto>>() {})
        actual.content == expected
    }

    def "createService method should successfully create new Service"() {
        given:
        def req = ServicesDto.builder()
                .name("TestService")
                .language("Java").build()

        def expected = ServicesDto.builder()
                .id(6)
                .name("TestService")
                .language("Java").build()

        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/services")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        then:
        def actual = objectMapper.readValue(result.getContentAsByteArray(), new TypeReference<ServicesDto>() {})
        actual == expected
    }

    def "The createService method should not create a new Service because no data was passed"() {
        given:
        def req = ServicesDto.builder().build()

        String expected = "Service name cannot be null or empty"

        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/services")
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        then:
        def actual = result.getContentAsString()
        actual == expected
    }

    def "getService method should successfully find data by id"() {
        given:
        def expected = ServicesDto.builder()
                .id(6)
                .name("TestService")
                .language("Java").build()
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/services/{id}", 6)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        then:
        def actual = objectMapper.readValue(result.getContentAsByteArray(), new TypeReference<ServicesDto>() {})
        actual == expected
    }

    def "The getService method should not find data by ID because the service does not exist"() {
        given:
        String expected = "Service with id: 7 not found"
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/services/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse();


        then:
        def actual = result.getContentAsString()
        actual == expected
    }


    void "update method should successfully updated service by id"() {
        given:
        def req = ServicesDto.builder()
                .id(6)
                .name("UpdatedService")
                .language("Python").build()

        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/services/{id}", 6)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        )

        then:
        result.andExpect(status().isOk())
    }

    void "the update method should not update the service because no data is passed"() {
        given:
        def req = ServicesDto.builder().name("Test12").build()
        String expected = "Service language cannot be null or empty"

        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/services/{id}", 5)
                        .content(objectMapper.writeValueAsString(req))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        then:
        def actual = result.getContentAsString()
        actual == expected
    }

    void "test delete service"() {
        when:
        def result = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/services/{id}", 6)
        )

        then:
        result.andExpect(status().isNoContent())
    }
}

