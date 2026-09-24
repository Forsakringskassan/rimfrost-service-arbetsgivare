package se.fk.github.rimfrost.arbetsgivare;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ArbetsgivareTest
{

   @Test
   void testArbetsgivareTrue()
   {
      String actualResponse = given()
            .when().get("/arbetsgivare/19900101-9999")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

      var expectedStartDate = LocalDate.now().minusYears(4).toString();

      assertThat(actualResponse).isEqualToIgnoringWhitespace("""
                        {
              "anstallningar": [
                {
                  "startdag": "%s",
                  "slutdag": null,
                  "arbetstid": 100,
                  "organisation": {
                    "nummer": "123456-7890",
                    "namn": "Region Dalarna"
                  }
                }
              ]
            }
            """.formatted(expectedStartDate));
   }

   @Test
   void testArbetsgivareMalformedPnr()
   {
      given()
            .when().get("/arbetsgivare/1234")
            .then()
            .statusCode(400);
   }
}
