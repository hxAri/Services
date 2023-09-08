package org.hxari.payload.response;

import java.util.List;

import org.hxari.model.TaskModel;

public record TaskItemsResponse<Tasks>( 
	int length, 
	String next, 
	List<TaskModel> tasks 
) {}
