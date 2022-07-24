package com.aswl.as.user.service;

import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.NumberUtil;
import com.aswl.as.user.api.module.Dept;
import com.aswl.as.user.api.module.User;
import com.aswl.as.user.mapper.DeptMapper;
import com.aswl.as.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门service
 *
 * @author aswl.com
 * @date 2018/8/26 22:46
 */
@AllArgsConstructor
@Slf4j
@Service
public class DeptService extends CrudService<DeptMapper, Dept> {
    private final DeptMapper deptMapper;
    private  final UserMapper userMapper;

    /**
     * 新增部门
     *
     * @param dept dept
     * @return int
     */
    @Transactional
    @Override
    public int insert(Dept dept) {
        //自动编码
        this.generateDeptCode(dept);
        return super.insert(dept);
    }
    @Transactional
    public int update(Dept dept){
        //自动编码
        this.generateDeptCode(dept);
        return super.update(dept);

    }



    /**
     * 删除部门
     *
     * @param dept dept
     * @return int
     */
    @Transactional
    @Override
    public int delete(Dept dept) {
        // 查询部门下是否有人员
        User user = new User();
        user.setDeptId(dept.getId());
        List<User> list = userMapper.findList(user);
        if (list!= null && list.size() >0){
            throw new CommonException("该部门还有人员无法删除，请联系管理员");
        }
        // 删除部门
        return super.delete(dept);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return int
     */
    @Transactional
    public int deleteAll(String[] ids) {
        for (int i = 0; i <ids.length ; i++) {
            User user = new User();
            user.setId(ids[i]);
            List<User> list = userMapper.findList(user);
            if (list!= null && list.size() >0){
                throw new CommonException("该部门还有人员无法删除，请联系管理员");
            }
        }
        return this.dao.deleteAll(ids);
    }

    /**
     * 根据用户批量查询
     *
     * @param userList userList
     * @return List
     * @author aswl.com
     * @date 2019/07/03 22:06:50
     */
    public List<Dept> getListByUsers(List<User> userList) {
        Dept dept = new Dept();
        dept.setIds(userList.stream().filter(tempUser -> tempUser.getDeptId() != null).map(User::getDeptId).distinct().toArray(String[]::new));
        return this.findListById(dept);
    }

    /**
     * 自动生成部门编码
     * @param dept 部门对象
     */
    public void generateDeptCode(Dept dept) {
      //查询父级节点是否存在
       Dept d1 = deptMapper.findDeptByParentId("-1");
       if ("-1".equals(dept.getParentId())) {
           if (null == d1) {
               dept.setParentId("-1");
               dept.setDeptCode("A1");
           } else {
               dept.setParentId("-1");
               dept.setDeptCode(NumberUtil.addOne(d1.getDeptCode()));
           }
       }
        else{
            Dept d2 = deptMapper.findDeptByParentId(dept.getParentId());
            if (null == d2) {
                if (dept.getDeptCode().length() == 2) {
                    dept.setDeptCode(dept.getDeptCode()+"B1");
                } else {
                    dept.setDeptCode(dept.getDeptCode() + "C1");
                }
            } else {
                dept.setDeptCode(NumberUtil.addOne(d2.getDeptCode()));
            }
        }
    }

}
