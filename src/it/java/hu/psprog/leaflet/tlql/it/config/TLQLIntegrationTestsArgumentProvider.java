package hu.psprog.leaflet.tlql.it.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import hu.psprog.leaflet.tlql.ir.DSLQueryModel;
import hu.psprog.leaflet.tlql.ir.DSLTimestampValue;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * TLQL processing integration test scenario argument provider implementation.
 * Place query files under testdata/queries and expectation files under testdata/expectation folder.
 * Filenames should look like this:
 *  - scenario_name.tlql for queries;
 *  - scenario_name.json for expectation files (scenario_name must be the same as for the query file for a single scenario).
 *
 * @author Peter Smith
 */
public class TLQLIntegrationTestsArgumentProvider implements ArgumentsProvider {

    private static final ClassPathResource SCENARIO_QUERY_FILES_PATH = new ClassPathResource("testdata/queries");
    private final ObjectMapper objectMapper;

    TLQLIntegrationTestsArgumentProvider() {

        SimpleModule dslTimestampDeserializationModule = new SimpleModule();
        dslTimestampDeserializationModule.addDeserializer(DSLTimestampValue.class,
                new DSLTimestampValueDeserializer(DSLTimestampValue.class));

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(dslTimestampDeserializationModule);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {

        return findScenarioQueryFiles()
                .map(this::prepareArgumentsForScenario);
    }

    private Arguments prepareArgumentsForScenario(Path path) {
        try {

            String query = Files.readString(path);
            DSLQueryModel expectedQueryModel = objectMapper.readValue(getExpectationFile(path), DSLQueryModel.class);

            return Arguments.of(query, expectedQueryModel);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Stream<Path> findScenarioQueryFiles() throws IOException {
        return Files.find(Paths.get(SCENARIO_QUERY_FILES_PATH.getURI()), 1, this::isScenarioFile);
    }

    private boolean isScenarioFile(Path path, BasicFileAttributes basicFileAttributes) {

        return path.toFile().isFile()
                && path.getFileName().toString().endsWith(".tlql");
    }

    private File getExpectationFile(Path queryFilePath) throws IOException {

        String queryFileName = queryFilePath.getFileName().toString();
        String expectationFileName = queryFileName.replace(".tlql", ".json");

        return new ClassPathResource(String.format("testdata/expectation/%s", expectationFileName))
                .getFile();
    }
}
