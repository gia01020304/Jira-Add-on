package API.bul;

import java.util.Collection;

import org.ofbiz.core.entity.GenericEntityException;
import org.ofbiz.core.entity.GenericValue;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.opensymphony.workflow.loader.ActionDescriptor;

import API.model.WorkFlowModel;

public class WorkFlowBUL {
	public WorkFlowModel getWorkFlowOfProjectAndIssueType(Long projectId, String issueTypeId) throws Exception {
		try {
			JiraWorkflow jiraWorkflow = ComponentAccessor.getWorkflowManager().getWorkflow(projectId, issueTypeId);
			WorkFlowModel workFlowModel=new WorkFlowModel();
			if (jiraWorkflow != null) {
				Collection<ActionDescriptor> collection = jiraWorkflow.getAllActions();
				if (collection != null && collection.size() > 0) {
					
					for (ActionDescriptor actionDescriptor : collection) {
						workFlowModel.getListIDTransition().add(actionDescriptor.getId());
					}
				}
			}
			return workFlowModel;
		} catch (Exception e) {
			throw new Exception("\n" + this.getClass() + " getWorkFlowOfProjectAndIssueType fail:" + e.getMessage());
		}
	}

}
