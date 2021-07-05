import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import com.zydx.note.entity.${entityName};
import com.zydx.note.service.${entityName}Service;
import com.zydx.note.dto.response.${entityName}DTO;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "")
@RestController
@RequestMapping("${entityName}")
@Slf4j
public class ${entityName}Controller extends BaseController<${entityName}Service,${entityName},${entityName}DTO>{

}