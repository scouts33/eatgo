package co.kr.fastcampus.eatgo.interfaces;

import co.kr.fastcampus.eatgo.application.RestaurantService;
import co.kr.fastcampus.eatgo.domain.MenuItem;
import co.kr.fastcampus.eatgo.domain.Restaurant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    public void list() throws Exception {
        List<Restaurant> restaurantLists = new ArrayList<>();
        restaurantLists.add(new Restaurant(1004L, "ZOKER House", "Seoul"));

        given(restaurantService.getRestaurants()).willReturn(restaurantLists);

        mvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1004")))
                .andExpect(content().string(containsString("\"name\":\"ZOKER House\"")));
    }

    @Test
    public void detail() throws Exception {
        Restaurant restaurant = new Restaurant(1004L, "ZOKER House", "Seoul");
        Restaurant restaurant2 = new Restaurant(2020L, "Cyber Food", "Seoul");

        restaurant.addMenuItem(new MenuItem("Kimchi"));
        restaurant2.addMenuItem(new MenuItem("Kimchi"));

        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant);
        given(restaurantService.getRestaurant(2020L)).willReturn(restaurant2);

        mvc.perform(get("/restaurant/1004"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1004")))
                .andExpect(content().string(containsString("\"name\":\"ZOKER House\"")))
                .andExpect(content().string(containsString("Kimchi")));

        mvc.perform(get("/restaurant/2020"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":2020")))
                .andExpect(content().string(containsString("\"name\":\"Cyber Food\"")));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Beryong\", \"address\":\"Busan\"}"))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/restaurants/1234"))
            .andExpect(content().string("{}"));

//        restaurantService.addRestaurant(restaurant);

        verify(restaurantService).addRestaurant(any());
    }
}