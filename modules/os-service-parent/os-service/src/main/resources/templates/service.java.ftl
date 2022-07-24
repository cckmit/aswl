package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(${entity} entity);

    public boolean updateEntityById(${entity} entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public ${entity} getEntityById(String id);

    public List<${entity}> findList(${entity} entity);

}
</#if>
