
import com.zydx.note.entity.${entityName};
import com.zydx.note.mapper.${entityName}Mapper;
import com.zydx.note.service.${entityName}Service;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ${entityName}ServiceImpl  extends ServiceImpl<${entityName}Mapper, ${entityName}> implements ${entityName}Service  {
	
}