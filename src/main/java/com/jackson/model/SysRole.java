package com.jackson.model;

import lombok.Data;

@Data
public class SysRole extends BaseEntity {
	private static final long serialVersionUID = -6525908145032868837L;

	private Integer id;
	private String name;
	private String description;

	@Override
	public String toString() {
		return "SysRole{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
