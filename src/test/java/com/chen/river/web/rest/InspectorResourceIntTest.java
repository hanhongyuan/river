package com.chen.river.web.rest;

import com.chen.river.RiverApp;
import com.chen.river.domain.Inspector;
import com.chen.river.repository.InspectorRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InspectorResource REST controller.
 *
 * @see InspectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RiverApp.class)
public class InspectorResourceIntTest {
    private static final String DEFAULT_USER_NAME = "AAAAA";
    private static final String UPDATED_USER_NAME = "BBBBB";
    private static final String DEFAULT_DEVICE_SEQ = "AAAAA";
    private static final String UPDATED_DEVICE_SEQ = "BBBBB";
    private static final String DEFAULT_WORK_DAY = "AAAAA";
    private static final String UPDATED_WORK_DAY = "BBBBB";
    private static final String DEFAULT_WORK_TIME = "AAAAA";
    private static final String UPDATED_WORK_TIME = "BBBBB";
    private static final String DEFAULT_OFF_WORK_TIME = "AAAAA";
    private static final String UPDATED_OFF_WORK_TIME = "BBBBB";

    @Inject
    private InspectorRepository inspectorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInspectorMockMvc;

    private Inspector inspector;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InspectorResource inspectorResource = new InspectorResource();
        ReflectionTestUtils.setField(inspectorResource, "inspectorRepository", inspectorRepository);
        this.restInspectorMockMvc = MockMvcBuilders.standaloneSetup(inspectorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspector createEntity(EntityManager em) {
        Inspector inspector = new Inspector();
        inspector = new Inspector();
        inspector.setUserName(DEFAULT_USER_NAME);
        inspector.setDeviceSeq(DEFAULT_DEVICE_SEQ);
        inspector.setWorkDay(DEFAULT_WORK_DAY);
        inspector.setWorkTime(DEFAULT_WORK_TIME);
        inspector.setOffWorkTime(DEFAULT_OFF_WORK_TIME);
        return inspector;
    }

    @Before
    public void initTest() {
        inspector = createEntity(em);
    }

    @Test
    @Transactional
    public void createInspector() throws Exception {
        int databaseSizeBeforeCreate = inspectorRepository.findAll().size();

        // Create the Inspector

        restInspectorMockMvc.perform(post("/api/inspectors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inspector)))
                .andExpect(status().isCreated());

        // Validate the Inspector in the database
        List<Inspector> inspectors = inspectorRepository.findAll();
        assertThat(inspectors).hasSize(databaseSizeBeforeCreate + 1);
        Inspector testInspector = inspectors.get(inspectors.size() - 1);
        assertThat(testInspector.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testInspector.getDeviceSeq()).isEqualTo(DEFAULT_DEVICE_SEQ);
        assertThat(testInspector.getWorkDay()).isEqualTo(DEFAULT_WORK_DAY);
        assertThat(testInspector.getWorkTime()).isEqualTo(DEFAULT_WORK_TIME);
        assertThat(testInspector.getOffWorkTime()).isEqualTo(DEFAULT_OFF_WORK_TIME);
    }

    @Test
    @Transactional
    public void getAllInspectors() throws Exception {
        // Initialize the database
        inspectorRepository.saveAndFlush(inspector);

        // Get all the inspectors
        restInspectorMockMvc.perform(get("/api/inspectors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inspector.getId().intValue())))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].deviceSeq").value(hasItem(DEFAULT_DEVICE_SEQ.toString())))
                .andExpect(jsonPath("$.[*].workDay").value(hasItem(DEFAULT_WORK_DAY.toString())))
                .andExpect(jsonPath("$.[*].workTime").value(hasItem(DEFAULT_WORK_TIME.toString())))
                .andExpect(jsonPath("$.[*].offWorkTime").value(hasItem(DEFAULT_OFF_WORK_TIME.toString())));
    }

    @Test
    @Transactional
    public void getInspector() throws Exception {
        // Initialize the database
        inspectorRepository.saveAndFlush(inspector);

        // Get the inspector
        restInspectorMockMvc.perform(get("/api/inspectors/{id}", inspector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inspector.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.deviceSeq").value(DEFAULT_DEVICE_SEQ.toString()))
            .andExpect(jsonPath("$.workDay").value(DEFAULT_WORK_DAY.toString()))
            .andExpect(jsonPath("$.workTime").value(DEFAULT_WORK_TIME.toString()))
            .andExpect(jsonPath("$.offWorkTime").value(DEFAULT_OFF_WORK_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInspector() throws Exception {
        // Get the inspector
        restInspectorMockMvc.perform(get("/api/inspectors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInspector() throws Exception {
        // Initialize the database
        inspectorRepository.saveAndFlush(inspector);
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();

        // Update the inspector
        Inspector updatedInspector = inspectorRepository.findOne(inspector.getId());
        updatedInspector.setUserName(UPDATED_USER_NAME);
        updatedInspector.setDeviceSeq(UPDATED_DEVICE_SEQ);
        updatedInspector.setWorkDay(UPDATED_WORK_DAY);
        updatedInspector.setWorkTime(UPDATED_WORK_TIME);
        updatedInspector.setOffWorkTime(UPDATED_OFF_WORK_TIME);

        restInspectorMockMvc.perform(put("/api/inspectors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInspector)))
                .andExpect(status().isOk());

        // Validate the Inspector in the database
        List<Inspector> inspectors = inspectorRepository.findAll();
        assertThat(inspectors).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectors.get(inspectors.size() - 1);
        assertThat(testInspector.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testInspector.getDeviceSeq()).isEqualTo(UPDATED_DEVICE_SEQ);
        assertThat(testInspector.getWorkDay()).isEqualTo(UPDATED_WORK_DAY);
        assertThat(testInspector.getWorkTime()).isEqualTo(UPDATED_WORK_TIME);
        assertThat(testInspector.getOffWorkTime()).isEqualTo(UPDATED_OFF_WORK_TIME);
    }

    @Test
    @Transactional
    public void deleteInspector() throws Exception {
        // Initialize the database
        inspectorRepository.saveAndFlush(inspector);
        int databaseSizeBeforeDelete = inspectorRepository.findAll().size();

        // Get the inspector
        restInspectorMockMvc.perform(delete("/api/inspectors/{id}", inspector.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Inspector> inspectors = inspectorRepository.findAll();
        assertThat(inspectors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
