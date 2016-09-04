package com.chen.river.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chen.river.domain.WorkRoad;

import com.chen.river.repository.WorkRoadRepository;
import com.chen.river.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkRoad.
 */
@RestController
@RequestMapping("/api")
public class WorkRoadResource {

    private final Logger log = LoggerFactory.getLogger(WorkRoadResource.class);
        
    @Inject
    private WorkRoadRepository workRoadRepository;

    /**
     * POST  /work-roads : Create a new workRoad.
     *
     * @param workRoad the workRoad to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workRoad, or with status 400 (Bad Request) if the workRoad has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-roads",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkRoad> createWorkRoad(@RequestBody WorkRoad workRoad) throws URISyntaxException {
        log.debug("REST request to save WorkRoad : {}", workRoad);
        if (workRoad.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workRoad", "idexists", "A new workRoad cannot already have an ID")).body(null);
        }
        WorkRoad result = workRoadRepository.save(workRoad);
        return ResponseEntity.created(new URI("/api/work-roads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workRoad", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-roads : Updates an existing workRoad.
     *
     * @param workRoad the workRoad to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workRoad,
     * or with status 400 (Bad Request) if the workRoad is not valid,
     * or with status 500 (Internal Server Error) if the workRoad couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-roads",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkRoad> updateWorkRoad(@RequestBody WorkRoad workRoad) throws URISyntaxException {
        log.debug("REST request to update WorkRoad : {}", workRoad);
        if (workRoad.getId() == null) {
            return createWorkRoad(workRoad);
        }
        WorkRoad result = workRoadRepository.save(workRoad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workRoad", workRoad.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-roads : get all the workRoads.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workRoads in body
     */
    @RequestMapping(value = "/work-roads",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkRoad> getAllWorkRoads() {
        log.debug("REST request to get all WorkRoads");
        List<WorkRoad> workRoads = workRoadRepository.findAll();
        return workRoads;
    }

    /**
     * GET  /work-roads/:id : get the "id" workRoad.
     *
     * @param id the id of the workRoad to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workRoad, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-roads/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkRoad> getWorkRoad(@PathVariable Long id) {
        log.debug("REST request to get WorkRoad : {}", id);
        WorkRoad workRoad = workRoadRepository.findOne(id);
        return Optional.ofNullable(workRoad)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-roads/:id : delete the "id" workRoad.
     *
     * @param id the id of the workRoad to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-roads/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkRoad(@PathVariable Long id) {
        log.debug("REST request to delete WorkRoad : {}", id);
        workRoadRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workRoad", id.toString())).build();
    }

}
