package com.cupshe.data.access.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionControl {

	private Permission permission;
	
	private CreateIdColumn createIdColumn;
	
	private List<User> userList;
	
}
