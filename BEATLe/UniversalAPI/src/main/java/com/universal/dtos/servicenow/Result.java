package com.universal.dtos.servicenow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Result {

	@JsonProperty("upon_approval")
	private String uponApproval;
	@JsonProperty("location")
	private String location;
	@JsonProperty("expected_start")
	private String expectedStart;
	@JsonProperty("close_notes")
	private String closeNotes;
	@JsonProperty("impact")
	private String impact;
	@JsonProperty("urgency")
	private String urgency;
	@JsonProperty("correlation_id")
	private String correlationId;
	@JsonProperty("sys_tags")
	private String sysTags;
	@JsonProperty("description")
	private String description;
	@JsonProperty("group_list")
	private String groupList;
	@JsonProperty("priority")
	private String priority;
	@JsonProperty("sys_domain")
	private SysDomain sysDomain;
	@JsonProperty("delivery_plan")
	private String deliveryPlan;
	@JsonProperty("sys_mod_count")
	private String sysModCount;
	@JsonProperty("related_incidents")
	private String relatedIncidents;
	@JsonProperty("work_notes_list")
	private String workNotesList;
	@JsonProperty("follow_up")
	private String followUp;
	@JsonProperty("closed_at")
	private String closedAt;
	@JsonProperty("sla_due")
	private String slaDue;
	@JsonProperty("delivery_task")
	private String deliveryTask;
	@JsonProperty("sys_updated_on")
	private String sysUpdatedOn;
	@JsonProperty("parent")
	private String parent;
	@JsonProperty("work_end")
	private String workEnd;
	@JsonProperty("number")
	private String number;
	@JsonProperty("closed_by")
	private ClosedBy closedBy;
	@JsonProperty("work_start")
	private String workStart;
	@JsonProperty("business_duration")
	private String businessDuration;
	@JsonProperty("activity_due")
	private String activityDue;
	@JsonProperty("correlation_display")
	private String correlationDisplay;
	@JsonProperty("company")
	private String company;
	@JsonProperty("due_date")
	private String dueDate;
	@JsonProperty("active")
	private String active;
	@JsonProperty("known_error")
	private String knownError;
	@JsonProperty("assignment_group")
	private String assignmentGroup;
	@JsonProperty("knowledge")
	private String knowledge;
	@JsonProperty("made_sla")
	private String madeSla;
	@JsonProperty("comments_and_work_notes")
	private String commentsAndWorkNotes;
	@JsonProperty("problem_state")
	private String problemState;
	@JsonProperty("state")
	private String state;
	@JsonProperty("user_input")
	private String userInput;
	@JsonProperty("approval_set")
	private String approvalSet;
	@JsonProperty("sys_created_on")
	private String sysCreatedOn;
	@JsonProperty("reassignment_count")
	private String reassignmentCount;
	@JsonProperty("rfc")
	private String rfc;
	@JsonProperty("opened_at")
	private String openedAt;
	@JsonProperty("order")
	private String order;
	@JsonProperty("short_description")
	private String shortDescription;
	@JsonProperty("sys_updated_by")
	private String sysUpdatedBy;
	@JsonProperty("upon_reject")
	private String uponReject;
	@JsonProperty("approval_history")
	private String approvalHistory;
	@JsonProperty("work_notes")
	private String workNotes;
	@JsonProperty("calendar_duration")
	private String calendarDuration;
	@JsonProperty("sys_id")
	private String sysId;
	@JsonProperty("approval")
	private String approval;
	@JsonProperty("work_around")
	private String workAround;
	@JsonProperty("sys_created_by")
	private String sysCreatedBy;
	@JsonProperty("assigned_to")
	private String assignedTo;
	@JsonProperty("sys_domain_path")
	private String sysDomainPath;
	@JsonProperty("cmdb_ci")
	private String cmdbCi;
	@JsonProperty("opened_by")
	private OpenedBy openedBy;
	@JsonProperty("sys_class_name")
	private String sysClassName;
	@JsonProperty("watch_list")
	private String watchList;
	@JsonProperty("time_worked")
	private String timeWorked;
	@JsonProperty("escalation")
	private String escalation;
	@JsonProperty("contact_type")
	private String contactType;
	@JsonProperty("comments")
	private String comments;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The uponApproval
	 */
	@JsonProperty("upon_approval")
	public String getUponApproval() {
		return uponApproval;
	}

	/**
	 * 
	 * @param uponApproval
	 * The upon_approval
	 */
	@JsonProperty("upon_approval")
	public void setUponApproval(String uponApproval) {
		this.uponApproval = uponApproval;
	}

	/**
	 * 
	 * @return
	 * The location
	 */
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 * The location
	 */
	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 * @return
	 * The expectedStart
	 */
	@JsonProperty("expected_start")
	public String getExpectedStart() {
		return expectedStart;
	}

	/**
	 * 
	 * @param expectedStart
	 * The expected_start
	 */
	@JsonProperty("expected_start")
	public void setExpectedStart(String expectedStart) {
		this.expectedStart = expectedStart;
	}

	/**
	 * 
	 * @return
	 * The closeNotes
	 */
	@JsonProperty("close_notes")
	public String getCloseNotes() {
		return closeNotes;
	}

	/**
	 * 
	 * @param closeNotes
	 * The close_notes
	 */
	@JsonProperty("close_notes")
	public void setCloseNotes(String closeNotes) {
		this.closeNotes = closeNotes;
	}

	/**
	 * 
	 * @return
	 * The impact
	 */
	@JsonProperty("impact")
	public String getImpact() {
		return impact;
	}

	/**
	 * 
	 * @param impact
	 * The impact
	 */
	@JsonProperty("impact")
	public void setImpact(String impact) {
		this.impact = impact;
	}

	/**
	 * 
	 * @return
	 * The urgency
	 */
	@JsonProperty("urgency")
	public String getUrgency() {
		return urgency;
	}

	/**
	 * 
	 * @param urgency
	 * The urgency
	 */
	@JsonProperty("urgency")
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	/**
	 * 
	 * @return
	 * The correlationId
	 */
	@JsonProperty("correlation_id")
	public String getCorrelationId() {
		return correlationId;
	}

	/**
	 * 
	 * @param correlationId
	 * The correlation_id
	 */
	@JsonProperty("correlation_id")
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	/**
	 * 
	 * @return
	 * The sysTags
	 */
	@JsonProperty("sys_tags")
	public String getSysTags() {
		return sysTags;
	}

	/**
	 * 
	 * @param sysTags
	 * The sys_tags
	 */
	@JsonProperty("sys_tags")
	public void setSysTags(String sysTags) {
		this.sysTags = sysTags;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The groupList
	 */
	@JsonProperty("group_list")
	public String getGroupList() {
		return groupList;
	}

	/**
	 * 
	 * @param groupList
	 * The group_list
	 */
	@JsonProperty("group_list")
	public void setGroupList(String groupList) {
		this.groupList = groupList;
	}

	/**
	 * 
	 * @return
	 * The priority
	 */
	@JsonProperty("priority")
	public String getPriority() {
		return priority;
	}

	/**
	 * 
	 * @param priority
	 * The priority
	 */
	@JsonProperty("priority")
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * 
	 * @return
	 * The sysDomain
	 */
	@JsonProperty("sys_domain")
	public SysDomain getSysDomain() {
		return sysDomain;
	}

	/**
	 * 
	 * @param sysDomain
	 * The sys_domain
	 */
	@JsonProperty("sys_domain")
	public void setSysDomain(SysDomain sysDomain) {
		this.sysDomain = sysDomain;
	}

	/**
	 * 
	 * @return
	 * The deliveryPlan
	 */
	@JsonProperty("delivery_plan")
	public String getDeliveryPlan() {
		return deliveryPlan;
	}

	/**
	 * 
	 * @param deliveryPlan
	 * The delivery_plan
	 */
	@JsonProperty("delivery_plan")
	public void setDeliveryPlan(String deliveryPlan) {
		this.deliveryPlan = deliveryPlan;
	}

	/**
	 * 
	 * @return
	 * The sysModCount
	 */
	@JsonProperty("sys_mod_count")
	public String getSysModCount() {
		return sysModCount;
	}

	/**
	 * 
	 * @param sysModCount
	 * The sys_mod_count
	 */
	@JsonProperty("sys_mod_count")
	public void setSysModCount(String sysModCount) {
		this.sysModCount = sysModCount;
	}

	/**
	 * 
	 * @return
	 * The relatedIncidents
	 */
	@JsonProperty("related_incidents")
	public String getRelatedIncidents() {
		return relatedIncidents;
	}

	/**
	 * 
	 * @param relatedIncidents
	 * The related_incidents
	 */
	@JsonProperty("related_incidents")
	public void setRelatedIncidents(String relatedIncidents) {
		this.relatedIncidents = relatedIncidents;
	}

	/**
	 * 
	 * @return
	 * The workNotesList
	 */
	@JsonProperty("work_notes_list")
	public String getWorkNotesList() {
		return workNotesList;
	}

	/**
	 * 
	 * @param workNotesList
	 * The work_notes_list
	 */
	@JsonProperty("work_notes_list")
	public void setWorkNotesList(String workNotesList) {
		this.workNotesList = workNotesList;
	}

	/**
	 * 
	 * @return
	 * The followUp
	 */
	@JsonProperty("follow_up")
	public String getFollowUp() {
		return followUp;
	}

	/**
	 * 
	 * @param followUp
	 * The follow_up
	 */
	@JsonProperty("follow_up")
	public void setFollowUp(String followUp) {
		this.followUp = followUp;
	}

	/**
	 * 
	 * @return
	 * The closedAt
	 */
	@JsonProperty("closed_at")
	public String getClosedAt() {
		return closedAt;
	}

	/**
	 * 
	 * @param closedAt
	 * The closed_at
	 */
	@JsonProperty("closed_at")
	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}

	/**
	 * 
	 * @return
	 * The slaDue
	 */
	@JsonProperty("sla_due")
	public String getSlaDue() {
		return slaDue;
	}

	/**
	 * 
	 * @param slaDue
	 * The sla_due
	 */
	@JsonProperty("sla_due")
	public void setSlaDue(String slaDue) {
		this.slaDue = slaDue;
	}

	/**
	 * 
	 * @return
	 * The deliveryTask
	 */
	@JsonProperty("delivery_task")
	public String getDeliveryTask() {
		return deliveryTask;
	}

	/**
	 * 
	 * @param deliveryTask
	 * The delivery_task
	 */
	@JsonProperty("delivery_task")
	public void setDeliveryTask(String deliveryTask) {
		this.deliveryTask = deliveryTask;
	}

	/**
	 * 
	 * @return
	 * The sysUpdatedOn
	 */
	@JsonProperty("sys_updated_on")
	public String getSysUpdatedOn() {
		return sysUpdatedOn;
	}

	/**
	 * 
	 * @param sysUpdatedOn
	 * The sys_updated_on
	 */
	@JsonProperty("sys_updated_on")
	public void setSysUpdatedOn(String sysUpdatedOn) {
		this.sysUpdatedOn = sysUpdatedOn;
	}

	/**
	 * 
	 * @return
	 * The parent
	 */
	@JsonProperty("parent")
	public String getParent() {
		return parent;
	}

	/**
	 * 
	 * @param parent
	 * The parent
	 */
	@JsonProperty("parent")
	public void setParent(String parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return
	 * The workEnd
	 */
	@JsonProperty("work_end")
	public String getWorkEnd() {
		return workEnd;
	}

	/**
	 * 
	 * @param workEnd
	 * The work_end
	 */
	@JsonProperty("work_end")
	public void setWorkEnd(String workEnd) {
		this.workEnd = workEnd;
	}

	/**
	 * 
	 * @return
	 * The number
	 */
	@JsonProperty("number")
	public String getNumber() {
		return number;
	}

	/**
	 * 
	 * @param number
	 * The number
	 */
	@JsonProperty("number")
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * 
	 * @return
	 * The closedBy
	 */
	@JsonProperty("closed_by")
	public ClosedBy getClosedBy() {
		return closedBy;
	}

	/**
	 * 
	 * @param closedBy
	 * The closed_by
	 */
	@JsonProperty("closed_by")
	public void setClosedBy(ClosedBy closedBy) {
		this.closedBy = closedBy;
	}

	/**
	 * 
	 * @return
	 * The workStart
	 */
	@JsonProperty("work_start")
	public String getWorkStart() {
		return workStart;
	}

	/**
	 * 
	 * @param workStart
	 * The work_start
	 */
	@JsonProperty("work_start")
	public void setWorkStart(String workStart) {
		this.workStart = workStart;
	}

	/**
	 * 
	 * @return
	 * The businessDuration
	 */
	@JsonProperty("business_duration")
	public String getBusinessDuration() {
		return businessDuration;
	}

	/**
	 * 
	 * @param businessDuration
	 * The business_duration
	 */
	@JsonProperty("business_duration")
	public void setBusinessDuration(String businessDuration) {
		this.businessDuration = businessDuration;
	}

	/**
	 * 
	 * @return
	 * The activityDue
	 */
	@JsonProperty("activity_due")
	public String getActivityDue() {
		return activityDue;
	}

	/**
	 * 
	 * @param activityDue
	 * The activity_due
	 */
	@JsonProperty("activity_due")
	public void setActivityDue(String activityDue) {
		this.activityDue = activityDue;
	}

	/**
	 * 
	 * @return
	 * The correlationDisplay
	 */
	@JsonProperty("correlation_display")
	public String getCorrelationDisplay() {
		return correlationDisplay;
	}

	/**
	 * 
	 * @param correlationDisplay
	 * The correlation_display
	 */
	@JsonProperty("correlation_display")
	public void setCorrelationDisplay(String correlationDisplay) {
		this.correlationDisplay = correlationDisplay;
	}

	/**
	 * 
	 * @return
	 * The company
	 */
	@JsonProperty("company")
	public String getCompany() {
		return company;
	}

	/**
	 * 
	 * @param company
	 * The company
	 */
	@JsonProperty("company")
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * 
	 * @return
	 * The dueDate
	 */
	@JsonProperty("due_date")
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * 
	 * @param dueDate
	 * The due_date
	 */
	@JsonProperty("due_date")
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * 
	 * @return
	 * The active
	 */
	@JsonProperty("active")
	public String getActive() {
		return active;
	}

	/**
	 * 
	 * @param active
	 * The active
	 */
	@JsonProperty("active")
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * 
	 * @return
	 * The knownError
	 */
	@JsonProperty("known_error")
	public String getKnownError() {
		return knownError;
	}

	/**
	 * 
	 * @param knownError
	 * The known_error
	 */
	@JsonProperty("known_error")
	public void setKnownError(String knownError) {
		this.knownError = knownError;
	}

	/**
	 * 
	 * @return
	 * The assignmentGroup
	 */
	@JsonProperty("assignment_group")
	public String getAssignmentGroup() {
		return assignmentGroup;
	}

	/**
	 * 
	 * @param assignmentGroup
	 * The assignment_group
	 */
	@JsonProperty("assignment_group")
	public void setAssignmentGroup(String assignmentGroup) {
		this.assignmentGroup = assignmentGroup;
	}

	/**
	 * 
	 * @return
	 * The knowledge
	 */
	@JsonProperty("knowledge")
	public String getKnowledge() {
		return knowledge;
	}

	/**
	 * 
	 * @param knowledge
	 * The knowledge
	 */
	@JsonProperty("knowledge")
	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	/**
	 * 
	 * @return
	 * The madeSla
	 */
	@JsonProperty("made_sla")
	public String getMadeSla() {
		return madeSla;
	}

	/**
	 * 
	 * @param madeSla
	 * The made_sla
	 */
	@JsonProperty("made_sla")
	public void setMadeSla(String madeSla) {
		this.madeSla = madeSla;
	}

	/**
	 * 
	 * @return
	 * The commentsAndWorkNotes
	 */
	@JsonProperty("comments_and_work_notes")
	public String getCommentsAndWorkNotes() {
		return commentsAndWorkNotes;
	}

	/**
	 * 
	 * @param commentsAndWorkNotes
	 * The comments_and_work_notes
	 */
	@JsonProperty("comments_and_work_notes")
	public void setCommentsAndWorkNotes(String commentsAndWorkNotes) {
		this.commentsAndWorkNotes = commentsAndWorkNotes;
	}

	/**
	 * 
	 * @return
	 * The problemState
	 */
	@JsonProperty("problem_state")
	public String getProblemState() {
		return problemState;
	}

	/**
	 * 
	 * @param problemState
	 * The problem_state
	 */
	@JsonProperty("problem_state")
	public void setProblemState(String problemState) {
		this.problemState = problemState;
	}

	/**
	 * 
	 * @return
	 * The state
	 */
	@JsonProperty("state")
	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state
	 * The state
	 */
	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @return
	 * The userInput
	 */
	@JsonProperty("user_input")
	public String getUserInput() {
		return userInput;
	}

	/**
	 * 
	 * @param userInput
	 * The user_input
	 */
	@JsonProperty("user_input")
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	/**
	 * 
	 * @return
	 * The approvalSet
	 */
	@JsonProperty("approval_set")
	public String getApprovalSet() {
		return approvalSet;
	}

	/**
	 * 
	 * @param approvalSet
	 * The approval_set
	 */
	@JsonProperty("approval_set")
	public void setApprovalSet(String approvalSet) {
		this.approvalSet = approvalSet;
	}

	/**
	 * 
	 * @return
	 * The sysCreatedOn
	 */
	@JsonProperty("sys_created_on")
	public String getSysCreatedOn() {
		return sysCreatedOn;
	}

	/**
	 * 
	 * @param sysCreatedOn
	 * The sys_created_on
	 */
	@JsonProperty("sys_created_on")
	public void setSysCreatedOn(String sysCreatedOn) {
		this.sysCreatedOn = sysCreatedOn;
	}

	/**
	 * 
	 * @return
	 * The reassignmentCount
	 */
	@JsonProperty("reassignment_count")
	public String getReassignmentCount() {
		return reassignmentCount;
	}

	/**
	 * 
	 * @param reassignmentCount
	 * The reassignment_count
	 */
	@JsonProperty("reassignment_count")
	public void setReassignmentCount(String reassignmentCount) {
		this.reassignmentCount = reassignmentCount;
	}

	/**
	 * 
	 * @return
	 * The rfc
	 */
	@JsonProperty("rfc")
	public String getRfc() {
		return rfc;
	}

	/**
	 * 
	 * @param rfc
	 * The rfc
	 */
	@JsonProperty("rfc")
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	/**
	 * 
	 * @return
	 * The openedAt
	 */
	@JsonProperty("opened_at")
	public String getOpenedAt() {
		return openedAt;
	}

	/**
	 * 
	 * @param openedAt
	 * The opened_at
	 */
	@JsonProperty("opened_at")
	public void setOpenedAt(String openedAt) {
		this.openedAt = openedAt;
	}

	/**
	 * 
	 * @return
	 * The order
	 */
	@JsonProperty("order")
	public String getOrder() {
		return order;
	}

	/**
	 * 
	 * @param order
	 * The order
	 */
	@JsonProperty("order")
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * 
	 * @return
	 * The shortDescription
	 */
	@JsonProperty("short_description")
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * 
	 * @param shortDescription
	 * The short_description
	 */
	@JsonProperty("short_description")
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * 
	 * @return
	 * The sysUpdatedBy
	 */
	@JsonProperty("sys_updated_by")
	public String getSysUpdatedBy() {
		return sysUpdatedBy;
	}

	/**
	 * 
	 * @param sysUpdatedBy
	 * The sys_updated_by
	 */
	@JsonProperty("sys_updated_by")
	public void setSysUpdatedBy(String sysUpdatedBy) {
		this.sysUpdatedBy = sysUpdatedBy;
	}

	/**
	 * 
	 * @return
	 * The uponReject
	 */
	@JsonProperty("upon_reject")
	public String getUponReject() {
		return uponReject;
	}

	/**
	 * 
	 * @param uponReject
	 * The upon_reject
	 */
	@JsonProperty("upon_reject")
	public void setUponReject(String uponReject) {
		this.uponReject = uponReject;
	}

	/**
	 * 
	 * @return
	 * The approvalHistory
	 */
	@JsonProperty("approval_history")
	public String getApprovalHistory() {
		return approvalHistory;
	}

	/**
	 * 
	 * @param approvalHistory
	 * The approval_history
	 */
	@JsonProperty("approval_history")
	public void setApprovalHistory(String approvalHistory) {
		this.approvalHistory = approvalHistory;
	}

	/**
	 * 
	 * @return
	 * The workNotes
	 */
	@JsonProperty("work_notes")
	public String getWorkNotes() {
		return workNotes;
	}

	/**
	 * 
	 * @param workNotes
	 * The work_notes
	 */
	@JsonProperty("work_notes")
	public void setWorkNotes(String workNotes) {
		this.workNotes = workNotes;
	}

	/**
	 * 
	 * @return
	 * The calendarDuration
	 */
	@JsonProperty("calendar_duration")
	public String getCalendarDuration() {
		return calendarDuration;
	}

	/**
	 * 
	 * @param calendarDuration
	 * The calendar_duration
	 */
	@JsonProperty("calendar_duration")
	public void setCalendarDuration(String calendarDuration) {
		this.calendarDuration = calendarDuration;
	}

	/**
	 * 
	 * @return
	 * The sysId
	 */
	@JsonProperty("sys_id")
	public String getSysId() {
		return sysId;
	}

	/**
	 * 
	 * @param sysId
	 * The sys_id
	 */
	@JsonProperty("sys_id")
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	/**
	 * 
	 * @return
	 * The approval
	 */
	@JsonProperty("approval")
	public String getApproval() {
		return approval;
	}

	/**
	 * 
	 * @param approval
	 * The approval
	 */
	@JsonProperty("approval")
	public void setApproval(String approval) {
		this.approval = approval;
	}

	/**
	 * 
	 * @return
	 * The workAround
	 */
	@JsonProperty("work_around")
	public String getWorkAround() {
		return workAround;
	}

	/**
	 * 
	 * @param workAround
	 * The work_around
	 */
	@JsonProperty("work_around")
	public void setWorkAround(String workAround) {
		this.workAround = workAround;
	}

	/**
	 * 
	 * @return
	 * The sysCreatedBy
	 */
	@JsonProperty("sys_created_by")
	public String getSysCreatedBy() {
		return sysCreatedBy;
	}

	/**
	 * 
	 * @param sysCreatedBy
	 * The sys_created_by
	 */
	@JsonProperty("sys_created_by")
	public void setSysCreatedBy(String sysCreatedBy) {
		this.sysCreatedBy = sysCreatedBy;
	}

	/**
	 * 
	 * @return
	 * The assignedTo
	 */
	@JsonProperty("assigned_to")
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * 
	 * @param assignedTo
	 * The assigned_to
	 */
	@JsonProperty("assigned_to")
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * 
	 * @return
	 * The sysDomainPath
	 */
	@JsonProperty("sys_domain_path")
	public String getSysDomainPath() {
		return sysDomainPath;
	}

	/**
	 * 
	 * @param sysDomainPath
	 * The sys_domain_path
	 */
	@JsonProperty("sys_domain_path")
	public void setSysDomainPath(String sysDomainPath) {
		this.sysDomainPath = sysDomainPath;
	}

	/**
	 * 
	 * @return
	 * The cmdbCi
	 */
	@JsonProperty("cmdb_ci")
	public String getCmdbCi() {
		return cmdbCi;
	}

	/**
	 * 
	 * @param cmdbCi
	 * The cmdb_ci
	 */
	@JsonProperty("cmdb_ci")
	public void setCmdbCi(String cmdbCi) {
		this.cmdbCi = cmdbCi;
	}

	/**
	 * 
	 * @return
	 * The openedBy
	 */
	@JsonProperty("opened_by")
	public OpenedBy getOpenedBy() {
		return openedBy;
	}

	/**
	 * 
	 * @param openedBy
	 * The opened_by
	 */
	@JsonProperty("opened_by")
	public void setOpenedBy(OpenedBy openedBy) {
		this.openedBy = openedBy;
	}

	/**
	 * 
	 * @return
	 * The sysClassName
	 */
	@JsonProperty("sys_class_name")
	public String getSysClassName() {
		return sysClassName;
	}

	/**
	 * 
	 * @param sysClassName
	 * The sys_class_name
	 */
	@JsonProperty("sys_class_name")
	public void setSysClassName(String sysClassName) {
		this.sysClassName = sysClassName;
	}

	/**
	 * 
	 * @return
	 * The watchList
	 */
	@JsonProperty("watch_list")
	public String getWatchList() {
		return watchList;
	}

	/**
	 * 
	 * @param watchList
	 * The watch_list
	 */
	@JsonProperty("watch_list")
	public void setWatchList(String watchList) {
		this.watchList = watchList;
	}

	/**
	 * 
	 * @return
	 * The timeWorked
	 */
	@JsonProperty("time_worked")
	public String getTimeWorked() {
		return timeWorked;
	}

	/**
	 * 
	 * @param timeWorked
	 * The time_worked
	 */
	@JsonProperty("time_worked")
	public void setTimeWorked(String timeWorked) {
		this.timeWorked = timeWorked;
	}

	/**
	 * 
	 * @return
	 * The escalation
	 */
	@JsonProperty("escalation")
	public String getEscalation() {
		return escalation;
	}

	/**
	 * 
	 * @param escalation
	 * The escalation
	 */
	@JsonProperty("escalation")
	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}

	/**
	 * 
	 * @return
	 * The contactType
	 */
	@JsonProperty("contact_type")
	public String getContactType() {
		return contactType;
	}

	/**
	 * 
	 * @param contactType
	 * The contact_type
	 */
	@JsonProperty("contact_type")
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	/**
	 * 
	 * @return
	 * The comments
	 */
	@JsonProperty("comments")
	public String getComments() {
		return comments;
	}

	/**
	 * 
	 * @param comments
	 * The comments
	 */
	@JsonProperty("comments")
	public void setComments(String comments) {
		this.comments = comments;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
