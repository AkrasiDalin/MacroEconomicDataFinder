package modelTests;

import app.model.DataFetcher;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

public class DataFetcherTest {

	@Test
	public void overViewForUKShouldReturnUnitedKingdom() throws IOException {
		assertEquals("GBR Should give country name: United Kingdom", "United Kingdom",
				DataFetcher.getOverview("GBR").get(0));
	}

	@Test
	public void GDPForMaldivesShouldBeAccurate() throws IOException {
		assertEquals("GDP of Maldives in 2015 should be: ", BigDecimal.valueOf(3142812004.19099),
				DataFetcher.getGDP("MDV").get(9).getR());
	}

	@Test
	public void GDPperCapitaForUSAShouldBeAccurate() throws IOException {
		assertEquals("GDP per capita of USA in 2015 should be: ", BigDecimal.valueOf(55836.7926308733),
				DataFetcher.getGDPcapita("USA").get(9).getR());
	}

	@Test
	public void CPIForBrazilShouldBeAccurate() throws IOException {
		assertEquals("CPI of Brazil in 2015 should be: ", BigDecimal.valueOf(9.02723977356148),
				DataFetcher.getCPI("BRA").get(9).getR());
	}

	@Test
	public void unemploymentForHongKongShouldBeAccurate() throws IOException {
		assertEquals("Unemployment for Hong Kong in 2015 should be: ", BigDecimal.valueOf(3.20000004768372),
				DataFetcher.getUnemployment("HKG").get(9).getR());
	}

	@Test
	public void accountBalanceForSingaporeShouldBeAccurate() throws IOException {
		assertEquals("Account Balance for Singapore in 2015 should be: ", BigDecimal.valueOf(57921917334.9335),
				DataFetcher.getAccountBalance("SG").get(9).getR());
	}

	@Test
	public void GDPComparisonForUKAndUSAShouldBeAccurate() throws IOException {
		String[] countries = { "GBR", "USA" };
		assertEquals("The GDP for the UK in 2013 should be: ", BigDecimal.valueOf(2712296271989.99),
				DataFetcher.compareGDP(countries, "2013").get(0).getR());
		assertEquals("The GDP for the USA in 2013 should be: ", BigDecimal.valueOf(16663160000000L),
				DataFetcher.compareGDP(countries, "2013").get(1).getR());

	}

	@Test
	public void GDPCapitaForGermanyFranceAndSpainShouldBeAccurate() throws IOException {
		String[] countries = { "FRA", "DEU", "ESP" };
		assertEquals("The GDPCapita for Germany in 2014 should be: ", BigDecimal.valueOf(47767.0019056435),
				DataFetcher.compareGDPcapita(countries, "2014").get(0).getR());
		assertEquals("The GDPCapita for France in 2014 should be: ", BigDecimal.valueOf(42546.8387870273),
				DataFetcher.compareGDPcapita(countries, "2014").get(2).getR());
		assertEquals("The GDPCapita for Spain in 2014 should be: ", BigDecimal.valueOf(29718.5002155441),
				DataFetcher.compareGDPcapita(countries, "2014").get(1).getR());
	}

	@Test
	public void CPIForIndiaSingaporeMalaysiaAndThailandShouldBeAccurate() throws IOException {
		String[] countries = { "IND", "SG", "MYS", "THA" };
		assertEquals("The CPI for India in 2015 should  be: ", BigDecimal.valueOf(5.87242659466756),
				DataFetcher.compareCPI(countries, "2015").get(0).getR());
		assertEquals("The CPI for Malaysia in 2015 should  be: ", BigDecimal.valueOf(2.10438980238348),
				DataFetcher.compareCPI(countries, "2015").get(1).getR());
		assertEquals("The CPI for Singapore in 2015 should  be: ", BigDecimal.valueOf(-0.541666666666702),
				DataFetcher.compareCPI(countries, "2015").get(2).getR());
		assertEquals("The CPI for Thailand in 2015 should  be: ", BigDecimal.valueOf(-0.895021443222092),
				DataFetcher.compareCPI(countries, "2015").get(3).getR());

	}

	@Test
	public void unemploymentForPolandRussiaAndTurkeyHouldBeaccurate() throws IOException {
		String[] countries = { "POL", "RUS" };
		assertEquals("The unemployment rate of Poland in 2015 was: ", BigDecimal.valueOf(9.19999980926514),
				DataFetcher.compareUnemployment(countries, "2015").get(0).getR());
		assertEquals("The unemployment rate of Russia in 2015 was: ", BigDecimal.valueOf(5.09999990463257),
				DataFetcher.compareUnemployment(countries, "2015").get(1).getR());
	}

	@Test
	public void accountBalanceForChinaAndJapanShouldBeAccurate() throws IOException {
		String[] countries = { "CHN", "JPN" };
		assertEquals("The Account Balance of China in 2005 should be :", BigDecimal.valueOf(132378493766.399),
				DataFetcher.compareAccountBalance(countries, "2005").get(0).getR());
		assertEquals("The Account Balance of Japan in 2005 should be :", BigDecimal.valueOf(170122750083.948),
				DataFetcher.compareAccountBalance(countries, "2005").get(1).getR());
	}

	@Test
	public void clearCacheShouldDeleteAllFiles() throws IOException {
		DataFetcher.clearCache();
		assertEquals("The directory should be deleted", false, new File("data").exists());
	}
}
