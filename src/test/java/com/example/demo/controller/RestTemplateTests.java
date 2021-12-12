package com.example.demo.controller;

import com.example.demo.entity.Workspace;
import com.example.demo.model.*;
import com.example.demo.utility.HeaderRequestInterceptor;
import com.example.demo.utility.MapperUtility;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Slf4j
@SpringBootTest
public class RestTemplateTests {

    private RestTemplate restTemplate = new RestTemplate();

    public RestTemplateTests() throws URISyntaxException {
    }

    @Test
    public void createUserAndAfterThatCallAllMethodsThatRequireJWTAuthentication() throws URISyntaxException {
        final String baseUrl = "http://localhost:8080/api/login";
        URI uri = new URI(baseUrl);

        createUserSuccess();

        LoginRequestDto requestDto = new LoginRequestDto("first.user@gmail.com", "123456123456");

        ResponseEntity<EntityResponseDto> result = restTemplate.postForEntity(uri, requestDto, EntityResponseDto.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        log.debug("JWT TOKEN: " + result.getBody().getData());
        restTemplate.setInterceptors(Collections.singletonList(HeaderRequestInterceptor.getAuthorisationHeader((String) result.getBody().getData())));
        getUserSuccess();
        addUserDetailsSuccess();

        createTwoWorkspacesForOneUserSuccess();
        getAllWorkspacesByUserIdSuccess();
        updateWorkspaceSuccess();
        getWorkspaceByWorkspaceIdSuccess();
        deleteWorkspaceSuccess();

        deleteUserSuccess();
    }

    public void getUserSuccess() throws URISyntaxException {
        final String baseUrl = "http://localhost:8080/api/user";
        URI uri = new URI(baseUrl);

        ResponseEntity<EntityResponseDto> result = restTemplate.getForEntity(uri, EntityResponseDto.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBody());
        Assert.assertEquals(200, result.getBody().getCode());
        Assert.assertEquals("Success", result.getBody().getMessage());

        UserResponseDto userResponseDto = MapperUtility.map(result.getBody().getData(), UserResponseDto.class);
        log.debug(userResponseDto.toString());
    }


    public void createUserSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/user";
        URI uri = new URI(baseUrl);

        UserRequestDto request = new UserRequestDto("First", "User", "first.user@gmail.com", "123456123456", "123456123456", true);

        Assert.assertNotNull(request);

        ResponseEntity<EntityResponseDto> result = this.restTemplate.postForEntity(uri, request, EntityResponseDto.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBody());
        Assert.assertEquals(201, result.getBody().getCode());
        Assert.assertEquals("Created", result.getBody().getMessage());

        UserResponseDto userResponseDto = MapperUtility.map(result.getBody().getData(), UserResponseDto.class);
        log.debug(userResponseDto.toString());

        Assert.assertNotEquals(400, result.getBody().getCode());
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    public void deleteUserSuccess() throws URISyntaxException {

        String baseUrl = "http://localhost:8080/api/user";
        URI uri = new URI(baseUrl);
        this.restTemplate.delete(uri);
    }

    public void addUserDetailsSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/userDetails";
        URI uri = new URI(baseUrl);

        UserDetailsDto userDetailsDto = new UserDetailsDto(1, "medina.tr@gmail.com", "Tuzla", "Bosnia", "75272");

        ResponseEntity<EntityResponseDto> result = restTemplate.postForEntity(uri, userDetailsDto, EntityResponseDto.class);

        Assert.assertEquals(200, result.getStatusCodeValue());

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBody());
        Assert.assertEquals(200, result.getBody().getCode());
        Assert.assertEquals("Success", result.getBody().getMessage());
        Assert.assertEquals("User details added for user with id: 1", result.getBody().getData());

        log.debug(String.valueOf("code: " + result.getBody().getCode()) + " message: " + result.getBody().getMessage() + " data: " + result.getBody().getData());
    }

    public void createTwoWorkspacesForOneUserSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/workspace";
        URI uri = new URI(baseUrl);

        WorkspaceDto firstWorkspace = new WorkspaceDto(1L, "workspace1", 4785, 4);
        WorkspaceDto secondWorkspace = new WorkspaceDto(2L, "workspace2", 4455, 1);

        ResponseEntity<EntityResponseDto> result1 = this.restTemplate.postForEntity(uri, firstWorkspace, EntityResponseDto.class);
        ResponseEntity<EntityResponseDto> result2 = this.restTemplate.postForEntity(uri, secondWorkspace, EntityResponseDto.class);

        Assert.assertEquals(200, result1.getStatusCodeValue());
        Assert.assertNotNull(result1);
        Assert.assertNotNull(result1.getBody());
        Assert.assertEquals(200, result1.getBody().getCode());
        //Assert.assertEquals("Successfully added workspace for user with id 1", result1.getBody().getMessage());

        Assert.assertEquals(200, result2.getStatusCodeValue());
        Assert.assertNotNull(result2);
        Assert.assertNotNull(result2.getBody());
        Assert.assertEquals(200, result2.getBody().getCode());
        Assert.assertEquals("Successfully added workspace for user with id 1", result2.getBody().getMessage());

        WorkspaceDto workspaceDto1 = MapperUtility.map(result1.getBody().getData(), WorkspaceDto.class);
        log.debug(workspaceDto1.toString());

        WorkspaceDto workspaceDto2 = MapperUtility.map(result2.getBody().getData(), WorkspaceDto.class);
        log.debug(workspaceDto2.toString());
    }

    public void getWorkspaceByWorkspaceIdSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/workspace/1";
        URI uri = new URI(baseUrl);

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("id", 1L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        log.debug("builder.buildAndExpand(urlParams).toUri(): " + builder.buildAndExpand(urlParams).toUri());

        ResponseEntity<EntityResponseDto> result = restTemplate.getForEntity
                (uri, EntityResponseDto.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBody());
        Assert.assertEquals(200, result.getBody().getCode());
        Assert.assertEquals("Success", result.getBody().getMessage());

        WorkspaceDto workspaceDto2 = MapperUtility.map(result.getBody().getData(), WorkspaceDto.class);
        log.debug(workspaceDto2.toString());
    }

    public void getAllWorkspacesByUserIdSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/workspace";
        URI uri = new URI(baseUrl);

        ResponseEntity<EntityResponseDto> result = restTemplate.getForEntity(uri, EntityResponseDto.class);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getBody());
        Assert.assertEquals(200, result.getBody().getCode());
        Assert.assertEquals("Success", result.getBody().getMessage());

        Set<Workspace> response = MapperUtility.mapStrict(result.getBody().getData(), Set.class);
        log.debug(response.toString());
    }

    public void updateWorkspaceSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/workspace";
        URI uri = new URI(baseUrl);

        WorkspaceDto request = new WorkspaceDto(1L, "updatedWorkspace", 587468, 6);

        ResponseEntity<EntityResponseDto> getResult = this.restTemplate.getForEntity(uri, EntityResponseDto.class);
        log.debug("OLD Workspace(code: " + getResult.getBody().getCode() + " message: " + getResult.getBody().getMessage() + " user: "
                + getResult.getBody().getData() + ")");

        this.restTemplate.put(String.valueOf(uri), request, EntityResponseDto.class);

    }

    public void deleteWorkspaceSuccess() throws URISyntaxException {

        final String baseUrl = "http://localhost:8080/api/workspace/1";
        URI uri = new URI(baseUrl);

        Map<String, Long> urlParams = new HashMap<>();
        urlParams.put("id", 1L);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        log.debug("builder.buildAndExpand(urlParams).toUri(): " + builder.buildAndExpand(urlParams).toUri());

        this.restTemplate.delete(String.valueOf(uri), urlParams);

    }
}
