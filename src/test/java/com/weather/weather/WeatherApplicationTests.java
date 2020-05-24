package com.weather.weather;

import com.weather.weather.model.CityMg;
import com.weather.weather.model.CityMySQL;
import com.weather.weather.model.State;
import com.weather.weather.rest.RestAPIControl;
import com.weather.weather.rest.WebControl;
import com.weather.weather.rest.modelJSON.CityData;
import com.weather.weather.view.MainPage;
import net.aksingh.owmjapis.model.param.City;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static java.util.EnumSet.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class WeatherApplicationTests {

    @Autowired
    RestAPIControl restAPIControl;
    @Autowired
    MainPage mainPage;

    List<CityMySQL> listCityDay = new ArrayList<>();
    List<CityMySQL> listCityWeek = new ArrayList<>();
    List<CityMySQL> listCityWeek2 = new ArrayList<>();
    long date;

    @BeforeEach
    public void init(){
        CityMg c;
        CityMySQL cityMySQL;
        List<CityMg> list = new ArrayList<>();
        date = Instant.now().getEpochSecond();

        // příprava dat
        Main.getMySQLService().deleteAll();
        Main.getMongoDBService().deleteAll();

        Main.getMySQLService().ChangeState("Czech Republic");
        Main.getMySQLService().AddCity("Prague");

        // příprava day test
        c = new CityMg("Prague", date, 23.5f, 48f, 3.6f, 30f);
        Main.getMongoDBService().SaveData(c);
        list.add(c);

        c = new CityMg("Prague", date, 22.9f, 48f, 3.6f, 30f);
        Main.getMongoDBService().SaveData(c);
        list.add(c);

        cityMySQL = new CityMySQL("Prague", list, 23.2f);
        listCityDay.add(cityMySQL);
        mainPage.UnixTimeToDate(listCityDay);

        // příprava week test
        date -= 259200;

        c = new CityMg("Prague", date, 23.5f, 48f, 3.6f, 30f);
        Main.getMongoDBService().SaveData(c);
        list.add(c);

        c = new CityMg("Prague", date, 22.9f, 48f, 3.6f, 30f);
        Main.getMongoDBService().SaveData(c);
        list.add(c);

        cityMySQL = new CityMySQL("Prague", list, 23.2f);
        listCityWeek.add(cityMySQL);
        mainPage.UnixTimeToDate(listCityWeek);

        // příprava week2 test
        date -= 604800;

        c = new CityMg("Prague", date, 23.5f, 48f, 3.6f, 30f);
        Main.getMongoDBService().SaveData(c);
        list.add(c);

        c = new CityMg("Prague", date, 22.9f, 48f, 3.6f, 30f);
        Main.getMongoDBService().SaveData(c);
        list.add(c);

        cityMySQL = new CityMySQL("Prague", list, 23.2f);
        listCityWeek2.add(cityMySQL);
        mainPage.UnixTimeToDate(listCityWeek2);

    }

    @Test
    @Order(1)
    public void dayTest() throws Exception {

        List<CityMySQL> res = restAPIControl.getWeekInfo(null);

        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(0), listCityDay.get(0).getCities().get(0)));
        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(1), listCityDay.get(0).getCities().get(1)));
        assertEquals(listCityDay.get(0).getCityName(), res.get(0).getCityName());
        assertEquals(listCityDay.get(0).getAverageTemp(), res.get(0).getAverageTemp());

    }

    @Test
    @Order(2)
    public void weekTest() throws Exception {

        // TEST
        List<CityMySQL> res = restAPIControl.getWeekInfo(null);

        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(0), listCityWeek.get(0).getCities().get(0)));
        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(1), listCityWeek.get(0).getCities().get(1)));
        assertEquals(listCityWeek.get(0).getCityName(), res.get(0).getCityName());
        assertEquals(listCityWeek.get(0).getAverageTemp(), res.get(0).getAverageTemp());
    }

    @Test
    @Order(3)
    public void week2Test() throws Exception {

        // TEST
        List<CityMySQL> res = restAPIControl.getWeek2Info(null);

        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(0), listCityWeek2.get(0).getCities().get(0)));
        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(1), listCityWeek2.get(0).getCities().get(1)));
        assertEquals(listCityWeek2.get(0).getCityName(), res.get(0).getCityName());
        assertEquals(listCityWeek2.get(0).getAverageTemp(), res.get(0).getAverageTemp());
    }

    @Test
    @Order(4)
    public void updateStateTest() throws Exception{
        int status = restAPIControl.UpdateState("Germany");
        Assert.assertEquals(202, status);
    }

    @Test
    @Order(5)
    public void addDeleteTest() throws Exception{
        Assert.assertEquals(202, restAPIControl.AddCity("Varnsdorf"));
        Assert.assertEquals(202, restAPIControl.DeleteCity("Varnsdorf"));
    }

    @Test
    @Order(6)
    public void deleteTest() throws Exception{
        CityData cityData = new CityData("Prague", String.valueOf(date));
        Assert.assertEquals(202, restAPIControl.DeleteTemp(cityData));
        List<CityMySQL> res = restAPIControl.getWeek2Info(null);

        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(0), listCityWeek.get(0).getCities().get(0)));
        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(1), listCityWeek.get(0).getCities().get(1)));
        assertEquals(listCityWeek.get(0).getCityName(), res.get(0).getCityName());
        assertEquals(listCityWeek.get(0).getAverageTemp(), res.get(0).getAverageTemp());
        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(0), listCityDay.get(0).getCities().get(0)));
        assertEquals(true, haveSamePropertyValues(CityMg.class, res.get(0).getCities().get(1), listCityDay.get(0).getCities().get(1)));
        assertEquals(listCityDay.get(0).getCityName(), res.get(0).getCityName());
        assertEquals(listCityDay.get(0).getAverageTemp(), res.get(0).getAverageTemp());

    }

    public static <T> boolean haveSamePropertyValues (Class<T> type, T t1, T t2) throws Exception {

        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
            Method m = pd.getReadMethod();
            Object o1 = m.invoke(t1);
            Object o2 = m.invoke(t2);
            if (!Objects.equals(o1, o2)) {
                return false;
            }
        }
        return true;
    }

}
