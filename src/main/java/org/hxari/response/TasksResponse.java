package org.hxari.response;

import java.util.List;

import org.hxari.model.Task;

public record TasksResponse<Tasks>( String next, int length, List<Task> tasks )
{}
