package com.chen.river.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chen.river.domain.Inspector;

import com.chen.river.repository.InspectorRepository;
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
 * REST controller for managing Inspector.
 */
@RestController
@RequestMapping("/api")
public class InspectorResource {

    private final Logger log = LoggerFactory.getLogger(InspectorResource.class);
        
    @Inject
    private InspectorRepository inspectorRepository;

    /**
     * POST  /inspectors : Create a new inspector.
     *
     * @param inspector the inspector to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inspector, or with status 400 (Bad Request) if the inspector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inspectors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inspector> createInspector(@RequestBody Inspector inspector) throws URISyntaxException {
        log.debug("REST request to save Inspector : {}", inspector);
        if (inspector.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inspector", "idexists", "A new inspector cannot already have an ID")).body(null);
        }
        Inspector result = inspectorRepository.save(inspector);
        return ResponseEntity.created(new URI("/api/inspectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inspector", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inspectors : Updates an existing inspector.
     *
     * @param inspector the inspector to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inspector,
     * or with status 400 (Bad Request) if the inspector is not valid,
     * or with status 500 (Internal Server Error) if the inspector couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inspectors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inspector> updateInspector(@RequestBody Inspector inspector) throws URISyntaxException {
        log.debug("REST request to update Inspector : {}", inspector);
        if (inspector.getId() == null) {
            return createInspector(inspector);
        }
        Inspector result = inspectorRepository.save(inspector);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inspector", inspector.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inspectors : get all the inspectors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inspectors in body
     */
    @RequestMapping(value = "/inspectors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Inspector> getAllInspectors() {
        log.debug("REST request to get all Inspectors");
        List<Inspector> inspectors = inspectorRepository.findAll();
        return inspectors;
    }

    /**
     * GET  /inspectors/:id : get the "id" inspector.
     *
     * @param id the id of the inspector to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inspector, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inspectors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inspector> getInspector(@PathVariable Long id) {
        log.debug("REST request to get Inspector : {}", id);
        Inspector inspector = inspectorRepository.findOne(id);
        return Optional.ofNullable(inspector)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inspectors/:id : delete the "id" inspector.
     *
     * @param id the id of the inspector to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inspectors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInspector(@PathVariable Long id) {
        log.debug("REST request to delete Inspector : {}", id);
        inspectorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inspector", id.toString())).build();
    }

}
