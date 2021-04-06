package hu.psprog.leaflet.tlql.it.suites;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.tlql.config.TLQLProcessorConfig;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;
import hu.psprog.leaflet.tlql.it.config.ITContextConfig;
import hu.psprog.leaflet.tlql.it.config.TLQLIntegrationTestsArgumentProvider;
import hu.psprog.leaflet.tlql.processor.TLQLProcessorService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration tests for TLQL processor.
 *
 * @author Peter Smith
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {TLQLProcessorConfig.class, ITContextConfig.class})
@ActiveProfiles("it")
public class TLQLProcessingIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(TLQLProcessingIT.class);

    @Autowired
    private TLQLProcessorService tlqlProcessorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${it.show-generated-query-model:false}")
    private boolean showGeneratedQueryModel;

    @ParameterizedTest
    @ArgumentsSource(TLQLIntegrationTestsArgumentProvider.class)
    public void shouldParseGivenQuery(String query, DSLQueryModel expectedQueryModel) {

        // when
        DSLQueryModel result = tlqlProcessorService.parse(query);

        // then
        logGeneratedQueryModel(result);
        assertThat(result, equalTo(expectedQueryModel));
    }

    private void logGeneratedQueryModel(DSLQueryModel result) {
        if (showGeneratedQueryModel) {
            try {
                String resultAsJson = objectMapper.writeValueAsString(result);
                LOGGER.info("Generated query model: {}", resultAsJson);
            } catch (JsonProcessingException e) {
                LOGGER.error("Failed to log generated query model", e);
            }
        }
    }
}
