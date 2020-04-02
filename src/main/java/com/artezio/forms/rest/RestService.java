package com.artezio.forms.rest;

import com.artezio.forms.FormClient;
import com.artezio.forms.resources.ClassPathResourceLoader;
import com.artezio.forms.resources.ResourceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Service
@Path("/")
public class RestService {

    private final FormClient formClient;
    private final TaskService taskService;
    private final FormService formService;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    public RestService(TaskService taskService, FormService formService, FormClient formClient) {
        this.formClient = formClient;
        this.taskService = taskService;
        this.formService = formService;
    }

    @GET
    @Path("/task/{id}/form")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getTaskForm(@PathParam("id") String taskId) throws IOException {
        String taskVariablesJson = OBJECT_MAPPER.writeValueAsString(taskService.getVariablesTyped(taskId));
        String formKey = getTaskFormKey(taskId);
        ObjectNode data = (ObjectNode) OBJECT_MAPPER.readTree(taskVariablesJson);
        ResourceLoader resourceLoader = new ClassPathResourceLoader();
        return formClient.getFormWithData(formKey, data, resourceLoader);
    }

    @POST
    @Path("/task/{id}/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    public void completeTask(@PathParam("id") String taskId, ObjectNode submittedVariables) throws IOException {
        String formKey = getTaskFormKey(taskId);
        Collection<String> formVariableNames = formClient.getRootFormFieldNames(formKey);
        String taskVariablesJson = OBJECT_MAPPER.writeValueAsString(taskService.getVariables(taskId, formVariableNames));
        String cleanData = formClient.dryValidationAndCleanup(formKey, submittedVariables, (ObjectNode) OBJECT_MAPPER.readTree(taskVariablesJson));
        taskService.complete(taskId, OBJECT_MAPPER.readValue(cleanData, Map.class));
    }

    private String getTaskFormKey(String taskId) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        return formService.getTaskFormKey(processDefinitionId, task.getTaskDefinitionKey());
    }

}
