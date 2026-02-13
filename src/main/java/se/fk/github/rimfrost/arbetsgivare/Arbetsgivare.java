package se.fk.github.rimfrost.arbetsgivare;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.ArbetsgivareControllerApi;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.Anstallning;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.GetArbetsgivare200Response;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.Organisation;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.SpecificeradLon;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.Lonerad;
import se.fk.rimfrost.api.arbetsgivare.jaxrsspec.controllers.generatedsource.model.LoneradTyp;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller that handles requests for the Arbetsgivare API.
 */
@SuppressWarnings("SpellCheckingInspection")
@ApplicationScoped
@Path("/arbetsgivare")
public class Arbetsgivare implements ArbetsgivareControllerApi
{
   private static final Logger log = LoggerFactory.getLogger(Arbetsgivare.class);

   /**
    * Default constructor for ApplicationScoped resource.
    */
   public Arbetsgivare()
   {
      // Default constructor required to suppress warning
   }

   @Override
   public GetArbetsgivare200Response getArbetsgivare(String personnummer)
   {
      log.info("Arbetsgivare received request: personnummer={}", personnummer);
      var response = new GetArbetsgivare200Response();

      var anstallning = new Anstallning();
      var org = new Organisation();
      org.setNamn("Cool Arbetsgivare AB");
      org.setNummer("123456-7890");

      anstallning.setArbetstid(100);
      anstallning.setOrganisation(org);
      anstallning.setStartdag(LocalDate.now().minusYears(4));
      anstallning.setSlutdag(null);

      response.addAnstallningarItem(anstallning);
      log.info("Arbetsgivare sending response: {}", response);
      return response;
   }

   @Override
   public SpecificeradLon getSpecificeradLon(String personnummer, LocalDate fromDatum, LocalDate tomDatum)
   {
      log.info("SpecificeradLon received request: personnummer={}, period={} - {}",
              personnummer, fromDatum, tomDatum);

      var response = new SpecificeradLon();
      response.setPersonnummer(personnummer);
      response.setPeriodFrom(fromDatum);
      response.setPeriodTom(tomDatum);

      // Skapa organisation
      var org = new Organisation();
      org.setNamn("Cool Arbetsgivare AB");
      org.setNummer("123456-7890");
      response.setOrganisation(org);

      // Skapa lonerader
      // Lon: +42 000 kr
      var grundlon = new Lonerad();
      grundlon.setTyp(LoneradTyp.GRUNDLON);
      grundlon.setBeskrivning("Manadslon");
      grundlon.setBelopp(42000.0);
      response.addLoneraderItem(grundlon);

      // OB-tillagg: +2 000 kr
      var obTillagg = new Lonerad();
      obTillagg.setTyp(LoneradTyp.TILLAGG);
      obTillagg.setBeskrivning("OB-tillagg");
      obTillagg.setBelopp(2000.0);
      response.addLoneraderItem(obTillagg);

      // Franvaro sjukdom: -2 000 kr
      var franvaroSjuk = new Lonerad();
      franvaroSjuk.setTyp(LoneradTyp.AVDRAG);
      franvaroSjuk.setBeskrivning("Franvaro sjukdom");
      franvaroSjuk.setBelopp(-2000.0);
      response.addLoneraderItem(franvaroSjuk);

      // Franvaro VAB: -2 000 kr
      var franvaroVab = new Lonerad();
      franvaroVab.setTyp(LoneradTyp.AVDRAG);
      franvaroVab.setBeskrivning("Franvaro VAB");
      franvaroVab.setBelopp(-2000.0);
      response.addLoneraderItem(franvaroVab);

      // Berakna summa: 42000 + 2000 - 2000 - 2000 = 40000
      double summa = 42000.0 + 2000.0 - 2000.0 - 2000.0;
      response.setLonesumma(summa);
      response.setAntalLonerader(4);

      log.info("SpecificeradLon sending response: lonesumma={} kr, {} lonerader",
              response.getLonesumma(), response.getAntalLonerader());
      return response;
   }
}