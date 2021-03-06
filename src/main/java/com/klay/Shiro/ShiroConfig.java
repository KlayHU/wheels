package com.klay.Shiro;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * 创建ShiroFilterFactorBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactorBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //1.设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //添加shior内置过滤器
        /**
         * Shiro内置过滤器，可以实现权限相关的拦截器
         *     常用的过滤器
         *         anon: 无需认证（登录）可以访问
         *         authc: 必须认证才可以访问
         *         user: 如果使用rememberMe的功能可以直接访问
         *         perms: 该资源必须授予资源权限才可以访问
         *         role: 该资源必须得到角色权限才可以访问
         *
         */

        Map<String,String> filterMap = new LinkedHashMap<String, String>();

      /*  filterMap.put("/add","authc");

        filterMap.put("/update","authc");*/
        filterMap.put("user/Thymeleaf","anon");

        filterMap.put("user/login","anon");

        //授权过滤器
        //注意：当授权拦截后，shiro会自动跳转到未授权的页面
        filterMap.put("/add","perms[user:add]");

        filterMap.put("/*","authc");

        //修改登录跳转页面
        shiroFilterFactoryBean.setLoginUrl("user/toLogin");
        //设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);




        return shiroFilterFactoryBean;
    }


    /**
     * 创建DefaultWebSecurityManager
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);

        return securityManager;
    }


    /**
     * 创建Realm对象
     */
    //方法返回的对象放回到Spring环境中
    @Bean(name = "userRealm")
    public UserRealm getRealm(){
        return new UserRealm();
    }
}
