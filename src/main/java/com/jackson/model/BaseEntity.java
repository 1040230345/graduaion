package com.jackson.model;

import lombok.Data;
import java.io.Serializable;

@Data
public abstract class BaseEntity implements Serializable {


//	@JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
	private Long createTime ;
//	@JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
	private Long updateTime ;

}
