package feature.uk.gov.ida.verifymatchingservicetesttool;

import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.listeners.TestExecutionSummary.Failure;
import uk.gov.ida.verifymatchingservicetesttool.configurations.ApplicationConfiguration;
import uk.gov.ida.verifymatchingservicetesttool.scenarios.OptionalAddressFieldsExcludedScenario;

import java.util.Arrays;

import static common.uk.gov.ida.verifymatchingservicetesttool.builders.ApplicationConfigurationBuilder.aApplicationConfiguration;
import static common.uk.gov.ida.verifymatchingservicetesttool.services.LocalMatchingServiceStub.RELATIVE_MATCH_URL;
import static javax.ws.rs.core.MediaType.APPLICATION_XHTML_XML_TYPE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class WrongContentTypeFeatureTest extends FeatureTestBase {

    @Test
    public void shouldFailWhenWrongContentTypeIsPassed() {
        localMatchingService.ensureResponseHeaderFor(RELATIVE_MATCH_URL, APPLICATION_XHTML_XML_TYPE);

        ApplicationConfiguration applicationConfiguration = aApplicationConfiguration()
            .withLocalMatchingServiceMatchUrl(localMatchingService.getMatchingUrl())
            .build();

        application.execute(
            listener,
            Arrays.asList(selectClass(OptionalAddressFieldsExcludedScenario.class)),
            applicationConfiguration
        );

        Failure firstFailure = listener.getSummary().getFailures().get(0);

        assertThat(
            firstFailure.getException().getMessage(),
            allOf(
                containsString("Expected: is \"application/json\""),
                containsString("but: was \"application/xhtml+xml\"")
            )
        );
    }
}
