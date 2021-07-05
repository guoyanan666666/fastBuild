import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("${dbTableName}")
public class ${entityName} extends Model<${entityName}> {


<#list cis as ci>
 <#if ci.javaType=="Date">
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
 </#if>
 <#if ci.columnName == "id">
	@TableId(value = "id", type = IdType.AUTO)
 </#if>
	@ApiModelProperty(name = "${ci.property}" , value = "${ci.comment}")
	private ${ci.javaType} ${ci.property};

</#list>
	@Override
		protected Serializable pkVal() {
		return this.id;
	}

}
	