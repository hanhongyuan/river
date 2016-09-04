package com.chen.river.web.rest;

import com.chen.river.RiverApp;
import com.chen.river.domain.WorkRoad;
import com.chen.river.repository.WorkRoadRepository;

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
 * Test class for the WorkRoadResource REST controller.
 *
 * @see WorkRoadResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RiverApp.class)
public class WorkRoadResourceIntTest {
    private static final String DEFAULT_ROAD_NAME = "AAAAA";
    private static final String UPDATED_ROAD_NAME = "BBBBB";
    private static final String DEFAULT_POINTS = "AAAAA";
    private static final String UPDATED_POINTS = "BBBBB";

    @Inject
    private WorkRoadRepository workRoadRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWorkRoadMockMvc;

    private WorkRoad workRoad;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkRoadResource workRoadResource = new WorkRoadResource();
        ReflectionTestUtils.setField(workRoadResource, "workRoadRepository", workRoadRepository);
        this.restWorkRoadMockMvc = MockMvcBuilders.standaloneSetup(workRoadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkRoad createEntity(EntityManager em) {
        WorkRoad workRoad = new WorkRoad();
        workRoad = new WorkRoad();
        workRoad.setRoadName(DEFAULT_ROAD_NAME);
        workRoad.setPoints(DEFAULT_POINTS);
        return workRoad;
    }

    @Before
    public void initTest() {
        workRoad = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkRoad() throws Exception {
        int databaseSizeBeforeCreate = workRoadRepository.findAll().size();

        // Create the WorkRoad

        restWorkRoadMockMvc.perform(post("/api/work-roads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workRoad)))
                .andExpect(status().isCreated());

        // Validate the WorkRoad in the database
        List<WorkRoad> workRoads = workRoadRepository.findAll();
        assertThat(workRoads).hasSize(databaseSizeBeforeCreate + 1);
        WorkRoad testWorkRoad = workRoads.get(workRoads.size() - 1);
        assertThat(testWorkRoad.getRoadName()).isEqualTo(DEFAULT_ROAD_NAME);
        assertThat(testWorkRoad.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void getAllWorkRoads() throws Exception {
        // Initialize the database
        workRoadRepository.saveAndFlush(workRoad);

        // Get all the workRoads
        restWorkRoadMockMvc.perform(get("/api/work-roads?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workRoad.getId().intValue())))
                .andExpect(jsonPath("$.[*].roadName").value(hasItem(DEFAULT_ROAD_NAME.toString())))
                .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.toString())));
    }

    @Test
    @Transactional
    public void getWorkRoad() throws Exception {
        // Initialize the database
        workRoadRepository.saveAndFlush(workRoad);

        // Get the workRoad
        restWorkRoadMockMvc.perform(get("/api/work-roads/{id}", workRoad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workRoad.getId().intValue()))
            .andExpect(jsonPath("$.roadName").value(DEFAULT_ROAD_NAME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkRoad() throws Exception {
        // Get the workRoad
        restWorkRoadMockMvc.perform(get("/api/work-roads/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkRoad() throws Exception {
        // Initialize the database
        workRoadRepository.saveAndFlush(workRoad);
        int databaseSizeBeforeUpdate = workRoadRepository.findAll().size();

        // Update the workRoad
        WorkRoad updatedWorkRoad = workRoadRepository.findOne(workRoad.getId());
        updatedWorkRoad.setRoadName(UPDATED_ROAD_NAME);
        updatedWorkRoad.setPoints(UPDATED_POINTS);

        restWorkRoadMockMvc.perform(put("/api/work-roads")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkRoad)))
                .andExpect(status().isOk());

        // Validate the WorkRoad in the database
        List<WorkRoad> workRoads = workRoadRepository.findAll();
        assertThat(workRoads).hasSize(databaseSizeBeforeUpdate);
        WorkRoad testWorkRoad = workRoads.get(workRoads.size() - 1);
        assertThat(testWorkRoad.getRoadName()).isEqualTo(UPDATED_ROAD_NAME);
        assertThat(testWorkRoad.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void deleteWorkRoad() throws Exception {
        // Initialize the database
        workRoadRepository.saveAndFlush(workRoad);
        int databaseSizeBeforeDelete = workRoadRepository.findAll().size();

        // Get the workRoad
        restWorkRoadMockMvc.perform(delete("/api/work-roads/{id}", workRoad.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkRoad> workRoads = workRoadRepository.findAll();
        assertThat(workRoads).hasSize(databaseSizeBeforeDelete - 1);
    }
}
