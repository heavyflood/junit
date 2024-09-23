package com.example.demo;

import com.example.demo.Controller.MemberController;
import com.example.demo.Entity.Member;
import com.example.demo.Repository.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@EnabledIfSystemProperty(named = "env", matches = "QA")
public class MemberTest {

    @InjectMocks
    MemberController memberController;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Mock
    private MemberService memberService;

    @Mock
    MemberRepository memberRepository;
    protected MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) { // mockMvc 초기화, 각메서드가 실행되기전에 초기화 되게 함
        // mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("멤버 생성 테스트")
    @Transactional
    @Rollback(true)
    void memberCreationTest() throws Exception{

        //given
        Member member = Member.builder().name("YUN").age(20).build();
        RequestBuilder req = MockMvcRequestBuilders.post("/member/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(mapper.writeValueAsString(member));

        //when
        MockHttpServletResponse res = mockMvc.perform(req)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/member/YUN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.age").value(member.getAge()))
                .andDo(print())
                .andReturn();

        Member res2String = mapper.readValue(res.getContentAsString()
                , new TypeReference<Member>() {});

        System.out.println("Post Result:::"+res2String);

    }

    @Test
    @DisplayName("멤버 목록 조회 테스트")
    void memberListSelectTest() throws Exception{

        //given

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/member/list")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        //then
        List<Member> after = mapper.readValue(
                mvcResult.getResponse().getContentAsString()
                , new TypeReference<List<Member>>() {});
        System.out.println("mvcResult :: " + mapper.writeValueAsString(after));

    }

    @Test
    @DisplayName("멤버 조회 테스트")
    void memberSelectTest() throws Exception{

        //given

        //when
        MvcResult mvcResult = mockMvc.perform(RestDocumentationRequestBuilders.get("/member/id/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(document("get-member",
                        pathParameters(
                                parameterWithName("id").description("회원 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("age").description("회원 나이")
                        )
                ))
                .andDo(print())
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(jsonPath("$.age", notNullValue()))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

    }
}
