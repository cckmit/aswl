package com.aswl.as.user.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.user.api.module.Post;
import com.aswl.as.user.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/17 14:43
 */
@AllArgsConstructor
@Service
public class PostService extends CrudService<PostMapper, Post> {
    private final PostMapper postMapper;
}
