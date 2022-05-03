package ru.bevz.freeter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.bevz.freeter.controller.MainController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@WithUserDetails("admin")
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("normalize-space(//div[@id='navbarSupportedContent']/div)").string("admin"));
    }

    @Test
    public void messageListTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(4));
    }

    @Test
    public void filterWhenTagIsTag1MessageTest() throws Exception {
        this.mockMvc.perform(get("/main?filter=tag1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(2));

    }

    @Test
    public void filterWhenTagIsTag2MessageTest() throws Exception {
        this.mockMvc.perform(get("/main").param("filter", "tag2"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(1))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='2']").exists());

    }

    @Test
    public void filterWhenTagIsTag0MessageTest() throws Exception {
        this.mockMvc.perform(get("/main").param("filter", "tag0"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(0))
                .andExpect(xpath("normalize-space(//div[@id='messageList']/h3)").string("No messages"));

    }

    @Test
    public void addMessageWithFileToListTest() throws Exception {
        MockHttpServletRequestBuilder multipart =
                multipart("/main")
                        .file("file", "123".getBytes())
                        .param("text", "fifth")
                        .param("tag", "tag4")
                        .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']").exists())
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/div/p").string("fifth"))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/div/a[@class='card-link']").string("#tag4"))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/img").exists());
    }

    @Test
    public void addMessageWithoutFileToListTest() throws Exception {
        MockHttpServletRequestBuilder multipart =
                multipart("/main")
                        .file("file", new byte[0])
                        .param("text", "fifth")
                        .param("tag", "tag4")
                        .with(csrf());

        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']").exists())
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/div/p").string("fifth"))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/div/a[@class='card-link']").string("#tag4"))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/img").doesNotExist());
    }
}
