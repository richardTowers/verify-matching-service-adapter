package uk.gov.ida.matchingserviceadapter.rest.matchingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import uk.gov.ida.matchingserviceadapter.builders.AddressDtoBuilder;
import uk.gov.ida.matchingserviceadapter.builders.SimpleMdsValueDtoBuilder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.ida.matchingserviceadapter.builders.VerifyMatchingDatasetDtoBuilder.aVerifyMatchingDatasetDto;
import static uk.gov.ida.matchingserviceadapter.rest.JsonTestUtil.jsonFixture;

public class VerifyMatchingDatasetDtoTest {

    private ObjectMapper objectMapper;
    private static final DateTime date = DateTime.parse("2014-02-01T01:02:03.567Z");


    @Before
    public void setUp() {
        objectMapper = Jackson.newObjectMapper().setDateFormat(ISO8601DateFormat.getDateInstance());
    }

    @Test
    public void shouldSerializeToJson() throws IOException {

        MatchingDatasetDto verifyMatchingDatasetDto = createVerifyMatchingDatasetDto(date);

        String serializedJson = objectMapper.writeValueAsString(verifyMatchingDatasetDto);
        String expectedJson = jsonFixture(objectMapper, "verify-matching-dataset.json");

        assertThat(serializedJson).isEqualTo(expectedJson);
    }

    @Test
    public void shouldDeserializeFromJson() throws Exception {
        VerifyMatchingDatasetDto deserializedValue =
                objectMapper.readValue(jsonFixture(objectMapper, "verify-matching-dataset.json"), VerifyMatchingDatasetDto.class);

        MatchingDatasetDto expectedValue = createVerifyMatchingDatasetDto(date);
        assertThat(deserializedValue).isEqualTo(expectedValue);
    }

    private MatchingDatasetDto createVerifyMatchingDatasetDto(DateTime dateTime) {
        return aVerifyMatchingDatasetDto()
                .addSurname(getSimpleMdsValue("walker", dateTime))
                .withAddressHistory(ImmutableList.of(getAddressDto("EC2", dateTime), getAddressDto("WC1", dateTime)))
                .withDateOfBirth(getSimpleMdsValue(LocalDate.fromDateFields(dateTime.toDate()), dateTime))
                .withFirstname(getSimpleMdsValue("walker", dateTime))
                .withGender(getSimpleMdsValue(GenderDto.FEMALE, dateTime))
                .withMiddleNames(getSimpleMdsValue("walker", dateTime))
                .withSurnameHistory(
                        ImmutableList.of(
                                getSimpleMdsValue("smith", dateTime),
                                getSimpleMdsValue("walker", dateTime)
                        ))
                .build();
    }

    private VerifyAddressDto getAddressDto(String postcode, DateTime dateTime) {
        return new AddressDtoBuilder()
                .withFromDate(dateTime)
                .withInternationalPostCode("123")
                .withLines(ImmutableList.of("a", "b")).withPostCode(postcode)
                .withToDate(dateTime)
                .withUPRN("urpn")
                .withVerified(true)
                .buildVerifyAddressDto();
    }

    private <T> SimpleMdsValueDto<T> getSimpleMdsValue(T value, DateTime dateTime) {
        return new SimpleMdsValueDtoBuilder<T>()
                .withFrom(dateTime)
                .withTo(dateTime)
                .withValue(value)
                .withVerifiedStatus(true)
                .build();
    }

}
