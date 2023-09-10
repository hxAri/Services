package org.hxari.payload.response;

import java.util.List;

import org.hxari.model.TaskModel;

public record TaskItemsResponse<Tasks>( 
	String next, 
	int length, 
	List<TaskModel> tasks 
) {}
