import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
public class ${entityName}DTO implements Serializable {


<#list cis as ci>
 <#if ci.javaType=="Date">
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
 </#if>
	@ApiModelProperty(name = "${ci.property}" , value = "${ci.comment}")
	private ${ci.javaType} ${ci.property};
    
</#list>

}
	